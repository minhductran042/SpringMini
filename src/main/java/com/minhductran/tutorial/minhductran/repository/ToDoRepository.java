package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    boolean existsByTitle(String title); //Spring JPA tu generate query kiem tra su ton tai
}
