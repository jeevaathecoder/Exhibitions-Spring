package com.my.exhibitions.services;

import com.my.exhibitions.entities.Hall;
import com.my.exhibitions.repositories.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService {

    private static final int DEFAULT_PAGE_SIZE = 5;
    private final HallRepository hallRepository;

    @Autowired
    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public void save(Hall hall) {
        hallRepository.save(hall);
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Hall findById(long id) {
        return hallRepository.findById(id);
    }

    public Page<Hall> getPage(int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DEFAULT_PAGE_SIZE);
        return hallRepository.findAll(paging);
    }

    public boolean existsByName(String name) {
        return hallRepository.existsByName(name);
    }
}
