package com.nicat.realtimecollaborationdocumenteditor.service;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Doc;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.websocket.DocumentUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final DocService docService;
    private final CacheService cacheService;
    private final UserService userService;

    public void handleDocumentLoad(DocumentUpdateMessage message) {
        String documentId = message.getDocumentId();
        String username = message.getUserId();
        String userId = userService.findByUsername(username).getId();

        log.info("User {} requested to load document {}", userId, documentId);

        try {
            String content = cacheService.getCachedDocumentContent(documentId);

            if (content == null) {
                Doc document = docService.getDoc(documentId);
                content = document.getContent();
                cacheService.cacheDocumentContent(documentId, content);
            }

            DocumentUpdateMessage contentMessage = new DocumentUpdateMessage(
                    documentId,
                    content,
                    "system",
                    System.currentTimeMillis());

            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/document." + documentId + ".content",
                    contentMessage);

            log.debug("Sent document content to user {} for document {}", userId, documentId);

        } catch (Exception e) {
            log.error("Error processing document load request", e);
        }
    }
}
