package com.nicat.realtimecollaborationdocumenteditor.controller;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Doc;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.Permission;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.DocCreateRequestDto;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.DocResponseDto;
import com.nicat.realtimecollaborationdocumenteditor.service.DocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/docs")
@RestController
@RequiredArgsConstructor
public class DocController {
    private final DocService docService;

    @PostMapping("/create")
    public ResponseEntity<DocResponseDto> create(@RequestBody @Valid DocCreateRequestDto docCreateRequestDto) {
        DocResponseDto docResponseDto = docService.create(docCreateRequestDto);
        return ResponseEntity.ok(docResponseDto);
    }

    @PostMapping("/share")
    public ResponseEntity<Void> addUser(@RequestParam String docId, @RequestParam String toGaveUsername) {
        docService.addUser(docId, toGaveUsername);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        id = docService.delete(id);
        return ResponseEntity.ok("Document was deleted with this id : " + id);
    }

    @GetMapping()
    public ResponseEntity<List<Doc>> getAll() {
        List<Doc> docList = docService.getAll();
        return ResponseEntity.ok(docList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doc> getDoc(@PathVariable String id) {
        Doc doc = docService.getDoc(id);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/shared")
    @ResponseStatus(HttpStatus.OK)
    public List<Permission> getSharedDoc() {
        return docService.getSharedDoc();
    }
}