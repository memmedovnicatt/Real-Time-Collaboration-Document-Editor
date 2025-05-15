package com.nicat.realtimecollaborationdocumenteditor.dao.repository;

import com.nicat.realtimecollaborationdocumenteditor.dao.entity.Token;
import com.nicat.realtimecollaborationdocumenteditor.dao.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    List<Token> findByUserAndIsLoggedOut(User user, Boolean isLoggedOut);

    Optional<Token> findByAccessToken(String accessToken);

}