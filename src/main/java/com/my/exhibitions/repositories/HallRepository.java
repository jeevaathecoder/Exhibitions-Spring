package com.my.exhibitions.repositories;

import com.my.exhibitions.entities.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Hall findById(long id);
    boolean existsByName(String name);
}
