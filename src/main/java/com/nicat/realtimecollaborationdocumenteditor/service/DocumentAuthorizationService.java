package com.nicat.realtimecollaborationdocumenteditor.service;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Doc;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.Permission;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.User;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.PermissionRepository;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.UserRepository;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.UserPermissions;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.NotFoundException;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.UnauthorizedException;
import com.nicat.realtimecollaborationdocumenteditor.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentAuthorizationService {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    boolean fullAccess(String username, Doc doc) {
        return doc.getOwnerUsername().equals(username);
    }

    boolean hasReadAccess(String docId) {
        String username = SecurityUtil.getCurrentUsername();
        return permissionRepository.countByToUserName(username) > 0;
    }

    boolean hasEditAccess(String docId) {
        String username = SecurityUtil.getCurrentUsername();
        return permissionRepository.countByToUserNameAndUserPermissions(username, UserPermissions.EDIT) > 0;
    }
}