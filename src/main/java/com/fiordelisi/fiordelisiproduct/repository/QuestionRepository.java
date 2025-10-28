package com.fiordelisi.fiordelisiproduct.repository;

import com.fiordelisi.fiordelisiproduct.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface QuestionRepository extends MongoRepository<Question,String> {
    Optional<Question> findById(String id);
    List<Question> findAll();
    @Query("""
    {
      '$or': [
        { 'title': { '$elemMatch': { 'language': ?1, 'value': { '$regex': ?0, '$options': 'i' } } } },
        {
          '$and': [
            { 'title': { '$not': { '$elemMatch': { 'language': ?1 } } } },
            { 'title': { '$elemMatch': { 'language': 'vi', 'value': { '$regex': ?0, '$options': 'i' } } } }
          ]
        }
      ]
    }
    """)
    Page<Question> findByNameContainingIgnoreCaseAndLanguageWithFallback(
            String query, String languageCode, Pageable pageable);
}
