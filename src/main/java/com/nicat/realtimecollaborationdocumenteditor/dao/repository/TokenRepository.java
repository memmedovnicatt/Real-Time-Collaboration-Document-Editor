package com.nicat.realtimecollaborationdocumenteditor.dao.repository;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Token;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    List<Token> findByUserAndIsLoggedOut(User user, Boolean isLoggedOut);

    Optional<Token> findByAccessToken(String accessToken);

}