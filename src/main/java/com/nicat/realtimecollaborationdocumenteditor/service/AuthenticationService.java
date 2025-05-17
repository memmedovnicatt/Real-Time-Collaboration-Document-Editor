package com.nicat.realtimecollaborationdocumenteditor.service;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.Authority;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.Token;
import com.nicat.realtimecollaborationdocumenteditor.dao.document.User;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.AuthRepository;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.TokenRepository;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.UserRepository;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.LoginRequest;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.RefreshTokenDto;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.request.RegisterRequestDto;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.LoginResponse;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.RefreshTokenResponse;
import com.nicat.realtimecollaborationdocumenteditor.model.dto.response.RegisterResponseDto;
import com.nicat.realtimecollaborationdocumenteditor.model.enums.Roles;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.AlreadyExistException;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.NotFoundException;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.PasswordMismatchException;
import com.nicat.realtimecollaborationdocumenteditor.model.exception.child.UnauthorizedException;
import com.nicat.realtimecollaborationdocumenteditor.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final AuthRepository authRepository;

    public RegisterResponseDto register(@Valid RegisterRequestDto registerRequestDto) {
        log.info("Register method was started with this request : {}", registerRequestDto);

        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            log.info("Failed to register user: User with email {} already exists", registerRequestDto.getEmail());
            throw new AlreadyExistException("EMAIL_ALREADY_EXISTS");
        }

        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            log.info("Failed to register user: User with email {} already exists", registerRequestDto.getEmail());
            throw new AlreadyExistException("USERNAME_ALREADY_EXISTS");
        }

        User user = User.builder()
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .email(registerRequestDto.getEmail())
                .authorities(List.of(authRepository.findByRole(Roles.USER).orElseGet(() -> {
                    Authority authority = Authority.builder()
                            .role(Roles.USER)
                            .build();
                    return authRepository.save(authority);
                })))
                .build();

        log.info("RegisterRequestDto set to user");
        userRepository.save(user);
        log.info("User saved in database");
        var jwtAccessToken = jwtUtil.generateAccessToken(user);
        var jwtRefreshToken = jwtUtil.generateRefreshToken(user);

        revokeAllTokensOfUser(user);
        saveUserToken(user, jwtAccessToken, jwtRefreshToken);

        RegisterResponseDto registerResponseDto = new RegisterResponseDto();
        registerResponseDto.setAccessToken(jwtAccessToken);
        registerResponseDto.setRefreshToken(jwtRefreshToken);
        log.info("User successfully registered");

        return registerResponseDto;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("{}", loginRequest);
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));


        var accessToken = jwtUtil.generateAccessToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);

        revokeAllTokensOfUser(user);
        saveUserToken(user, accessToken, refreshToken);

        LoginResponse LoginResponse = new LoginResponse();
        LoginResponse.setAccessToken(accessToken);
        LoginResponse.setRefreshToken(refreshToken);

        log.info("User successfully logged in");

        return LoginResponse;
    }

    private void saveUserToken(User user, String accessToken, String refreshToken) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllTokensOfUser(User user) {
        List<Token> tokens = tokenRepository.findByUserAndIsLoggedOut(user, Boolean.FALSE);
        tokens.forEach(token -> token.setIsLoggedOut(Boolean.TRUE));
        tokenRepository.saveAll(tokens);
    }

    public RefreshTokenResponse refreshAccessToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        String userEmail = jwtUtil.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new UnauthorizedException("Invalid refresh token - no user found");
        }

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!jwtUtil.isTokenValid(refreshToken, user)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);
        revokeAllTokensOfUser(user);
        saveUserToken(user, newAccessToken, refreshToken);
        return new RefreshTokenResponse(newAccessToken, refreshToken);
    }
}