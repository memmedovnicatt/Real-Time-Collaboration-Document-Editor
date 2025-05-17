package com.nicat.realtimecollaborationdocumenteditor.service;

import com.nicat.realtimecollaborationdocumenteditor.configuration.redis.RedisRepository;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.Doc;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.Permission;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.User;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.DocRepository;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.PermissionRepository;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.UserRepository;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.DocCreateRequestDto;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.DocResponseDto;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.UserPermissions;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.NotFoundException;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.UnauthorizedException;
import com.nicat.realtimecollaborationdocumenteditor.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class DocService {

    private final DocRepository docRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final DocumentAuthorizationService documentAuthorizationService;
    private final RedisRepository redisRepository;

    @Transactional
    public DocResponseDto create(DocCreateRequestDto docCreateRequestDto) {
        log.info("Create method was started with this request : {}", docCreateRequestDto);
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        String key = "doc:" + username + " created in :" + LocalDateTime.now();
        //The goal is to prevent the previous
        // key from being lost if the same user creates a document with a different title.

        Doc.DocumentVersion initialVersion = Doc.DocumentVersion.builder().contentSnapshot(docCreateRequestDto.getContent()).editedByUserId(user.getId()).timestamp(LocalDateTime.now()).build();

        Doc doc = new Doc();
        doc.setOwnerUsername(username);
        doc.setVersions(List.of(initialVersion));
        doc.setContent(docCreateRequestDto.getContent());
        doc.setTitle(docCreateRequestDto.getTitle());
        redisRepository.putByKey(key, doc.getContent());// cache in redis
        log.info("Dto set to doc");
        docRepository.save(doc);
        log.info("Doc was successfully saved in db");

        DocResponseDto docResponseDto = new DocResponseDto();
        docResponseDto.setContent(doc.getContent());
        docResponseDto.setTitle(doc.getTitle());

        Permission permission = new Permission();
        permission.setDocId(doc.getId());
        permission.setUserPermissions(UserPermissions.OWNER);
        permission.setWhoGaveUsername(user.getUsername());
        permissionRepository.save(permission);
        log.info("Permission was successfully saved in db");

        return docResponseDto;
    }

    @Transactional
    public String delete(String id) {
        log.info("Delete method was started with this id : {}", id);
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        Doc doc = docRepository.findById(id).orElseThrow(() -> new NotFoundException("This id is not found"));
        log.info("id was found");

        if (!documentAuthorizationService.fullAccess(username, doc)) {
            throw new RuntimeException("Not access for delete document");
            //if you want to delete a document, you must be the owner
        }
        docRepository.deleteById(id);
        log.info("deleted from database");
        return id;
    }

    public List<Doc> getAll() {
        log.info("getAll method was started");
        String username = SecurityUtil.getCurrentUsername();
        log.info("getAll method was started for :{}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        log.info("getAll method was started");
        return docRepository.findByOwnerUsername(username);
    }

    public Doc getDoc(String id) {
        log.info("getDoc method was started");
        String whoGaveUsername = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(whoGaveUsername).orElseThrow(() -> new NotFoundException("User not found"));
        log.info("getDoc method was started with this id : {}", id);
        Doc doc = docRepository.findById(id).orElseThrow(() -> new NotFoundException("Doc not found with this is :" + id));
        log.info("doc was found");

        Permission permission = permissionRepository.findByWhoGaveUsername(whoGaveUsername).orElseThrow(() -> new NotFoundException("NOT FOUND"));
        log.info("{}", permission);

        if (!documentAuthorizationService.hasReadAccess(doc.getId())) {
            throw new UnauthorizedException("No permission");
        }
        log.info("Permission was found");
        return doc;
    }

    public void addUser(String docId, String toGaveUsername) {
        log.info("addUser method was started");
        String username = SecurityUtil.getCurrentUsername();
        log.info("{}", username);

        User user = userRepository.findByUsername(toGaveUsername)
                .orElseThrow(() -> new NotFoundException("This user not found"));
        log.info("User found");

        Permission permission = permissionRepository.findByWhoGaveUsername(username)
                .orElseThrow(() -> new NotFoundException("Not found permission for this user"));
        log.info("Permissions was found for current username : {}", username);

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new NotFoundException("Not found document"));
        log.info("Doc was found with this id : {}", docId);
        permission.setDocId(docId);
        user.setSharedDocIds(Collections.singleton(docId));
        permission.setToUserName(toGaveUsername);
        permission.setWhoGaveUsername(username);
        permission.setUserPermissions(UserPermissions.VIEW);
        log.info("{}", permission);
        permissionRepository.save(permission);
        log.info("Permission was successfully saved");
    }

    public List<String> getSharedDoc() {
        log.info("getSharedDoc method was started");

        String username = SecurityUtil.getCurrentUsername();

        return permissionRepository.findAllByToUserName(username)
                .stream()
                .map(Permission::getDocId)
                .toList();
    }
}