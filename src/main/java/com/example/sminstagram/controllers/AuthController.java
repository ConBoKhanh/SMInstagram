package com.example.sminstagram.controllers;

import com.example.sminstagram.bases.BaseResponse;
import com.example.sminstagram.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Xác thực người dùng")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập username + password")
    public ResponseEntity<BaseResponse<Map<String, Object>>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(BaseResponse.success(
                authService.login(request.getUsername(), request.getPassword(), response)));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Lấy access token mới")
    public ResponseEntity<BaseResponse<Map<String, Object>>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(BaseResponse.success(
                authService.refresh(request, response)));
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất")
    public ResponseEntity<BaseResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
