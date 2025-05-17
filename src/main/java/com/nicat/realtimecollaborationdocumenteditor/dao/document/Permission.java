package com.nicat.realtimecollaborationdocumenteditor.dao.document;

import com.nicat.realtimecollaborationdocumenteditor.model.enums.UserPermissions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Permission {
    @Id
    String id;

    String docId;
    String whoGaveUsername;//ownerUserId
    String toUserName;
    UserPermissions userPermissions;
}