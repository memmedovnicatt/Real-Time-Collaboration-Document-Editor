package com.nicat.realtimecollaborationdocumenteditor.dao.document;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    String id;

    String accessToken;
    String refreshToken;

    @DBRef
    User user;

    Boolean isLoggedOut = false;
}