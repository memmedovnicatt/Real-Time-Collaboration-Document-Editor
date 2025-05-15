package com.nicat.realtimecollaborationdocumenteditor.dao.repository;

import com.nicat.realtimecollaborationdocumenteditor.dao.entity.Authority;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthRepository extends MongoRepository<Authority, String> {
    Optional<Authority> findByRole(Roles role);
}