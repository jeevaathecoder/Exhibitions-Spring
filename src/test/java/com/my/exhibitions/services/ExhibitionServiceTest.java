package com.my.exhibitions.services;

import com.my.exhibitions.entities.Exhibition;
import com.my.exhibitions.entities.User;
import com.my.exhibitions.repositories.ExhibitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ExhibitionServiceTest {
    @MockBean
    ExhibitionRepository exhibitionRepository;
    @InjectMocks
    ExhibitionService exhibitionService;

    Exhibition exhibition;

    @Autowired
    public ExhibitionServiceTest(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @BeforeEach
    public void setUp() {
        exhibition = new Exhibition();
        exhibition.setId(1000L);
        exhibition.setTheme("themE");
    }

    @Test
    public void testSave() {
        exhibitionService.save(exhibition);

        verify(exhibitionRepository).save(exhibition);
    }

    @Test
    public void testSaveWithHalls() {
        List<String> halls = List.of("1", "2");
        when(exhibitionRepository.findById(anyLong())).thenReturn(new Exhibition());

        exhibitionService.save(exhibition, halls);

        verify(exhibitionRepository).save(exhibition);
    }

    @Test
    public void testFindById() {
        when(exhibitionService.findById(1000L)).thenReturn(exhibition);

        Exhibition exhibitionFound = exhibitionService.findById(1000L);

        verify(exhibitionRepository).findById(1000L);
        assertThat(exhibitionFound).isEqualTo(exhibition);
    }

    @Test
    public void testFindByIdNull() {
        when(exhibitionService.findById(1000L)).thenReturn(null);

        Exhibition exhibitionFound = exhibitionService.findById(1000L);

        verify(exhibitionRepository).findById(1000L);
        assertThat(exhibitionFound).isNull();
    }

    @Test
    public void testFindByTheme() {
        when(exhibitionService.findByTheme("sas")).thenReturn(exhibition);

        Exhibition exhibitionFound = exhibitionService.findByTheme("sas");

        verify(exhibitionRepository).findByTheme("sas");
        assertThat(exhibitionFound).isEqualTo(exhibition);
    }

    @Test
    public void testFindByThemeNull() {
        when(exhibitionService.findByTheme("sas")).thenReturn(null);

        Exhibition exhibitionFound = exhibitionService.findByTheme("sas");

        verify(exhibitionRepository).findByTheme("sas");
        assertThat(exhibitionFound).isNull();
    }

    @Test
    public void testExistsByTheme() {
        when(exhibitionRepository.existsByTheme("sasha")).thenReturn(true);

        boolean exists = exhibitionService.existsByTheme("sasha");

        assertThat(exists).isTrue();
    }

    @Test
    public void testGetPage() {
        Pageable paging = PageRequest.of(1, 5);

        exhibitionService.getPage(1);

        verify(exhibitionRepository).findAll(paging);
    }

    @Test
    public void testGetPageWithPar() {
        Pageable paging = PageRequest.of(1, 5, Sort.by("default"));

        exhibitionService.getPage(1, "default");

        verify(exhibitionRepository).findAll(paging);
    }

    @Test
    public void testCancelExhibition() {
        when(exhibitionRepository.findById(1000L)).thenReturn(exhibition);

        exhibitionService.cancelExhibition(1000L);

        verify(exhibitionRepository).deleteById(1000L);
    }

    @Test
    public void testGetStats() {
        when(exhibitionRepository.findAll()).thenReturn(List.of(exhibition));

        exhibitionService.getStats();

        verify(exhibitionRepository).findAll();
        verify(exhibitionRepository).getNumberOfTicketsSoldByID(exhibition.getId());
    }

    @Test
    public void testGettingNumberOfTickets() {
        User user = new User();
        user.setId(1000L);
        exhibitionService.getNumberOfTicketsBoughtForUserAtExhibition(user, exhibition);

        verify(exhibitionRepository).getNumberOfTicketsBoughtBySingleUser(exhibition.getId(), user.getId());
    }

    @Test
    public void testGetDetailedStats() {
       exhibitionService.getDetailedStats(exhibition.getId());

       verify(exhibitionRepository, atLeast(1)).getNumberOfTicketsBoughtBySingleUser(eq(exhibition.getId()), anyLong());
    }
}
