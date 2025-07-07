package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUsername(String username); //Spring JPA tu generate query kiem tra su ton tai

    User findByUsername(String username);

}
