package com.nicat.realtimecollaborationdocumenteditor.dao.document;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "documents")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doc {
    @Id
    String id;

    String title;
    String content;

    String ownerUsername;
    private List<DocumentVersion> versions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DocumentVersion {
        String contentSnapshot;
        String editedByUserId;
        LocalDateTime timestamp;
    }
}