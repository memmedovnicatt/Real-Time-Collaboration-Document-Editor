package com.nicat.realtimecollaborationdocumenteditor.dao.document;

import com.nicat.realtimecollaborationdocumenteditor.model.enums.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "authority")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority {
    @Id
    String id;

    Roles role;

    @DBRef
    List<User> users;
}