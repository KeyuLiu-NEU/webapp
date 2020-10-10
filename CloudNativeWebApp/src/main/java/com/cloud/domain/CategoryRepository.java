package com.cloud.domain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface CategoryRepository extends CrudRepository<CategoryInfo, String> {
    Optional<CategoryInfo> findById(String id);
    Optional<CategoryInfo> findByCategory(String category);
}