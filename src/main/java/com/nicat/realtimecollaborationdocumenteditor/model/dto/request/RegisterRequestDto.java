package com.nicat.realtimecollaborationdocumenteditor.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequestDto {
    @NotEmpty(message = "Username must not be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username;

    @Size(min = 3, max = 30, message = "Password must be between 3 adn 30 characters")
    @NotEmpty(message = "Password cannot be empty")
    String password;

    @NotEmpty(message = "Email must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid.Example: firstname-lastname@example.com  ")
    @Size(min = 6, max = 70, message = "Email must be between 6 and 70 characters")
    String email;
}