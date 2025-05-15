package com.nicat.realtimecollaborationdocumenteditor;

import com.nicat.realtimecollaborationdocumenteditor.dao.entity.Token;
import com.nicat.realtimecollaborationdocumenteditor.dao.entity.User;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


@SpringBootApplication
public class RealTimeCollaborationDocumentEditorApplication  {

    public static void main(String[] args) {
        SpringApplication.run(RealTimeCollaborationDocumentEditorApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        Token token = tokenRepository.findById("6825bfff67137c494aee55b9").get();
//        User user = token.getUser();
//        System.out.println(user.getEmail());
//    }
}

