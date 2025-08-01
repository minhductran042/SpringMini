package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
    Optional<Email> findByTo(String to);
}
