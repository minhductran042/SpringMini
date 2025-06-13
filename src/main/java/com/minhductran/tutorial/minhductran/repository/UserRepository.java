package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
