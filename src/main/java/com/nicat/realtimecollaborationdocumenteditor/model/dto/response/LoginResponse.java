package com.nicat.realtimecollaborationdocumenteditor.model.dto.response;

import ch.qos.logback.classic.spi.LoggingEventVO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String refreshToken;
}
