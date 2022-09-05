package com.my.exhibitions.services;

import com.my.exhibitions.entities.Exhibition;
import com.my.exhibitions.entities.User;
import com.my.exhibitions.repositories.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExhibitionService {

    private static final int DEFAULT_PAGE_SIZE = 5;

    private final ExhibitionRepository exhibitionRepository;
    private final HallService hallService;
    private final UserService userService;

    @Autowired
    public ExhibitionService(ExhibitionRepository exhibitionRepository, HallService hallService, UserService userService) {
        this.exhibitionRepository = exhibitionRepository;
        this.hallService = hallService;
        this.userService = userService;
    }

    public void save(Exhibition exhibition, List<String> halls) {
        exhibition.setHalls(halls.stream().map(Long::parseLong).map(hallService::findById).collect(Collectors.toSet()));
        save(exhibition);
    }

    public void save(Exhibition exhibition) {
        exhibitionRepository.save(exhibition);
    }


    public Exhibition findById(long id) {
        return exhibitionRepository.findById(id);
    }

    public Page<Exhibition> getPage(int pageNum, String sortType) {
        Pageable paging = PageRequest.of(pageNum, DEFAULT_PAGE_SIZE, Sort.by(sortType));
        return exhibitionRepository.findAll(paging);
    }

    public Page<Exhibition> getPage(int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DEFAULT_PAGE_SIZE);
        return exhibitionRepository.findAll(paging);
    }

    public void cancelExhibition(long id) {
        Exhibition exhibition = exhibitionRepository.findById(id);
        exhibition.removeHalls();
        exhibition.removeUsers();
        exhibitionRepository.deleteById(id);
    }

    public void addCustomer(long exhibitionId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();


        Exhibition exhibition = exhibitionRepository.findById(exhibitionId);
        User currentUser = userService.findByUsername(username);

        List<Exhibition> exhibitions = currentUser.getExhibitions();
        exhibitions.add(exhibition);

        List<User> users = exhibition.getUsers();
        users.add(currentUser);

        save(exhibition);
        userService.update(currentUser);
    }

    public Map<Exhibition, Integer> getStats() {
        Map<Exhibition, Integer> stats = new LinkedHashMap<>();
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        exhibitions.forEach(exhibition -> {
            int ticketsSold = exhibitionRepository.getNumberOfTicketsSoldByID(exhibition.getId());
            stats.put(exhibition, ticketsSold);
        });
        return stats;
    }

    public int getNumberOfTicketsBoughtForUserAtExhibition(User user, Exhibition exhibition) {
        return exhibitionRepository.getNumberOfTicketsBoughtBySingleUser(exhibition.getId(), user.getId());
    }

    public Map<String, Integer> getDetailedStats(long exhibitionId) {
        Map<String, Integer> stats = new LinkedHashMap<>();
        List<User> users = userService.findAll();
        users.forEach(user -> stats.put(
                user.getUsername(),
                exhibitionRepository.getNumberOfTicketsBoughtBySingleUser(exhibitionId, user.getId())
        ));
        return stats.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public boolean existsByTheme(String theme) {
        return exhibitionRepository.existsByTheme(theme);
    }

    public Exhibition findByTheme(String theme) {
        return exhibitionRepository.findByTheme(theme);
    }
}
