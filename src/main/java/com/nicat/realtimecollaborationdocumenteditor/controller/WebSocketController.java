package com.nicat.realtimecollaborationdocumenteditor.controller;

import com.nicat.realtimecollaborationdocumenteditor.model.dto.websocket.DocumentUpdateMessage;
import com.nicat.realtimecollaborationdocumenteditor.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/websockets")
public class WebSocketController {

    private final WebSocketService webSocketService;

    @Operation(
            summary = "Load document",
            description = "Handles document load requests. Client sends this message to load a document's content."
    )
    @PostMapping    ("/document.load")
    public void handleDocumentLoad(@Payload DocumentUpdateMessage message) {
        webSocketService.handleDocumentLoad(message);
    }

//    @Operation(
//            summary = "Update document",
//            description = "Handles document update messages from clients. This is sent when a user makes changes to a document."
//    )
//    @MessageMapping("/document.update")
//    public void handleDocumentUpdate(@Payload DocumentUpdateMessage message) {
//        webSocketService.handleDocumentUpdate(message);
//    }
//
//    @Operation(
//            summary = "Join document session",
//            description = "Handles user join notifications. Sent when a user starts editing a document."
//    )
//    @MessageMapping("/document.join")
//    public void handleUserJoin(@Payload DocumentUpdateMessage message) {
//        webSocketService.handleUserJoin(message);
//    }
//
//    @Operation(
//            summary = "Leave document session",
//            description = "Handles user leave notifications. Sent when a user stops editing a document."
//    )
//    @MessageMapping("/document.leave")
//    public void handleUserLeave(@Payload DocumentUpdateMessage message) {
//        webSocketService.handleUserLeave(message);
//    }
}
