package com.nicat.realtimecollaborationdocumenteditor.model.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocResponseDto {
    String content;
    String title;
}
