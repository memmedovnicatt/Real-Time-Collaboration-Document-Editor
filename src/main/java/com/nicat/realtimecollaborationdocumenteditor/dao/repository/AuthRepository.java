package com.nicat.realtimecollaborationdocumenteditor.dao.repository;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Authority;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<Authority, String> {
    Optional<Authority> findByRole(Roles role);
}