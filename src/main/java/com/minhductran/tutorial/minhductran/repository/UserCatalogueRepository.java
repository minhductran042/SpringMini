package com.minhductran.tutorial.minhductran.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCatalogueRepository extends JpaRepository<com.minhductran.tutorial.minhductran.model.UserCatalogue, Integer> {

    // This class can be used to define custom methods for UserCatalogue if needed.
    // Currently, it extends JpaRepository to inherit basic CRUD operations.

    // Example of a custom method (if needed):
    // List<UserCatalogue> findBySomeCriteria(String criteria);

    Optional<com.minhductran.tutorial.minhductran.model.UserCatalogue> findById(Integer id);
}
