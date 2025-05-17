package com.nicat.realtimecollaborationdocumenteditor.dao.repository;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Permission;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.UserPermissions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByWhoGaveUsername(String whoGaveUsername);

    List<Permission> findAllByToUserName(String toUserName);

    Integer countByToUserName(String toUserName);

    Integer countByToUserNameAndUserPermissions(String toUserName, UserPermissions permission);
}