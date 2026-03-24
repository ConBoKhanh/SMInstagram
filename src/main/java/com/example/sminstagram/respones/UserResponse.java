package com.example.sminstagram.respones;

import com.example.sminstagram.entities.User;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String loginType;
    private String username;
    private String fullName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
    private String coverUrl;
    private String bio;
    private String website;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postsCount;
    private String role;
    private Boolean isActive;
    private Boolean isVerified;
    private Boolean isPrivate;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .loginType(user.getLoginType())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .coverUrl(user.getCoverUrl())
                .bio(user.getBio())
                .website(user.getWebsite())
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingCount())
                .postsCount(user.getPostsCount())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .isVerified(user.getIsVerified())
                .isPrivate(user.getIsPrivate())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
