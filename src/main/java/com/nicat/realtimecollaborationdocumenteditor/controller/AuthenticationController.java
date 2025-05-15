package com.nicat.realtimecollaborationdocumenteditor.controller;

import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.LoginRequest;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.RefreshTokenDto;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.RegisterRequestDto;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.LoginResponse;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.RefreshTokenResponse;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.RegisterResponseDto;
import com.nicat.realtimecollaborationdocumenteditor.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponseDto registerResponseDto = authenticationService.register(registerRequestDto);
        return ResponseEntity.ok(registerResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(@RequestBody RefreshTokenDto refreshTokenDto){
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshAccessToken(refreshTokenDto);
        return ResponseEntity.ok(refreshTokenResponse);
    }
}