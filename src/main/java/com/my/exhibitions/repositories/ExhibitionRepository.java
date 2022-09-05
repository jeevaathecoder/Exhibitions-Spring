package com.my.exhibitions.repositories;

import com.my.exhibitions.entities.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Exhibition findById(long id);
    void deleteById(long id);

    boolean existsByTheme(String theme);

    @Query(
            value = "SELECT COUNT(*) as number FROM orders WHERE exhibition_id = ?1",
            nativeQuery = true)
    int getNumberOfTicketsSoldByID(long id);

    @Query(
            value = "SELECT COUNT(*) as number_of_bought_tickets " +
                    "FROM orders " +
                    "WHERE exhibition_id = ?1 and user_id = ?2",
            nativeQuery = true
    )
    int getNumberOfTicketsBoughtBySingleUser(long exhibitionId, long userId);

    Exhibition findByTheme(String theme);
}
