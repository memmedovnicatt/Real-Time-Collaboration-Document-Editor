package com.nicat.realtimecollaborationdocumenteditor.model.dto.websocket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Message for real-time document updates via WebSocket")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUpdateMessage {
    @Schema(description = "Unique identifier of the document", example = "60c72b2f5e7c2a1b3c9d8e7f")
    String documentId;

    @Schema(description = "Current content of the document", example = "This is the updated document content.")
    String content;

    @Schema(description = "ID of the user who made the update", example = "60c72b2f5e7c2a1b3c9d8e7a")
    String userId;

    @Schema(description = "Timestamp of the update in milliseconds since epoch", example = "1623456789000")
    long timestamp;
}
