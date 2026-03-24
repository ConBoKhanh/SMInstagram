package com.example.sminstagram.services;

import com.example.sminstagram.entities.RefreshToken;
import com.example.sminstagram.entities.User;
import com.example.sminstagram.repos.UserRepo;
import com.example.sminstagram.respones.RefreshTokenRepo;
import com.example.sminstagram.respones.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    // ===== LOGIN =====
    public Map<String, Object> login(String username, String password, HttpServletResponse response) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username hoặc password không đúng"));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Username hoặc password không đúng");

        if (!user.getIsActive())
            throw new RuntimeException("Tài khoản đã bị khóa");

        // Xóa token cũ nếu có
        refreshTokenRepo.deleteByUserId(user.getId());

        // Tạo tokens
        String accessToken  = jwtService.generateAccessToken(
                user.getId().toString(), user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId().toString());

        // Lưu refresh token vào DB
        refreshTokenRepo.save(RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build());

        // Set refreshToken vào HttpOnly Cookie
        setRefreshTokenCookie(response, refreshToken);

        // Trả về accessToken + user info
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("user", UserResponse.fromEntity(user));
        return data;
    }

    // ===== REFRESH =====
    public Map<String, Object> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (!jwtService.isTokenValid(refreshToken))
            throw new RuntimeException("Refresh token không hợp lệ");

        RefreshToken stored = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không tồn tại"));

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepo.delete(stored);
            throw new RuntimeException("Refresh token đã hết hạn, vui lòng đăng nhập lại");
        }

        User user = userRepo.findById(stored.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Chỉ extend khi còn ít hơn 2 ngày
        long daysLeft = ChronoUnit.DAYS.between(
                LocalDateTime.now(), stored.getExpiresAt());

        if (daysLeft < 2) {
            stored.setExpiresAt(LocalDateTime.now().plusDays(7));
            refreshTokenRepo.save(stored);
        }

        String newAccessToken = jwtService.generateAccessToken(
                user.getId().toString(), user.getEmail(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", newAccessToken);
        return data;
    }

    // ===== LOGOUT =====
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        refreshTokenRepo.findByToken(refreshToken)
                .ifPresent(refreshTokenRepo::delete);
        clearRefreshTokenCookie(response);
    }

    // ===== HELPERS =====
    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            throw new RuntimeException("Không tìm thấy cookie");
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) return cookie.getValue();
        }
        throw new RuntimeException("Refresh token không tồn tại");
    }
}
