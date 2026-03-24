package com.example.sminstagram.controllers;

import com.example.sminstagram.bases.BaseResponse;
import com.example.sminstagram.entities.User;
import com.example.sminstagram.requests.QueryRequest;
import com.example.sminstagram.respones.PageResponse;
import com.example.sminstagram.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Quản lý người dùng")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Lấy user theo id")
    public ResponseEntity<BaseResponse<User>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(userService.getUserById(id)));
    }

    @PostMapping
    @Operation(summary = "Tạo user mới")
    public ResponseEntity<BaseResponse<User>> createUser(@RequestBody User user) {
        return ResponseEntity.status(201).body(BaseResponse.created(userService.createUser(user)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật user theo id")
    public ResponseEntity<BaseResponse<User>> updateUser(@PathVariable UUID id, @RequestBody User user) {
        return ResponseEntity.ok(BaseResponse.success(userService.updateUser(id, user)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa user (isActive = false)")
    public ResponseEntity<BaseResponse<User>> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(userService.deleteUser(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search + filter + phân trang users")
    public ResponseEntity<BaseResponse<PageResponse<User>>> searchUsers(@RequestBody QueryRequest request) {
        return ResponseEntity.ok(BaseResponse.success(PageResponse.of(userService.getUsers(request))));
    }
}
