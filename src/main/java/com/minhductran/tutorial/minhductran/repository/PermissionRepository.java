package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    // This class can be used to define custom methods for UserCatalogue if needed.
    // Currently, it extends JpaRepository to inherit basic CRUD operations.

    // Example of a custom method (if needed):
    // List<UserCatalogue> findBySomeCriteria(String criteria);

    Optional<Permission> findById(Integer id);
}
