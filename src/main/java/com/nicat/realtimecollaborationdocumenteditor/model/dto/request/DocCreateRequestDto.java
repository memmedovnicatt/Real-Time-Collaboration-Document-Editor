package com.nicat.realtimecollaborationdocumenteditor.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocCreateRequestDto {
    String title;
    String content;
}