package com.cloud.domain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface QuestionRepository extends CrudRepository<Question, String> {
    @Query
    Optional<Question> findById(String id);

    Iterable<Question> findAllByUserId(String id);

}