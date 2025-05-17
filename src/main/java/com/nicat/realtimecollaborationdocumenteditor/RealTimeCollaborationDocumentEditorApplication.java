package com.nicat.realtimecollaborationdocumenteditor;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Permission;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.PermissionRepository;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.UserPermissions;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.NotFoundException;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class RealTimeCollaborationDocumentEditorApplication {


    public static void main(String[] args) {
        SpringApplication.run(RealTimeCollaborationDocumentEditorApplication.class, args);
    }


}