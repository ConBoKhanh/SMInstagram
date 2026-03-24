package com.example.sminstagram.services;

import com.example.sminstagram.bases.BaseService;
import com.example.sminstagram.bases.BaseSpecification;
import com.example.sminstagram.entities.FilterRequest;
import com.example.sminstagram.entities.User;
import com.example.sminstagram.repos.UserRepo;
import com.example.sminstagram.requests.QueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepo userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Page<User> getUsers(QueryRequest request) {
        Sort sort = extractSort(request.getFilters());
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
        Specification<User> spec = BaseSpecification.buildFilter(request.getFilters());
        return userRepository.findAll(spec, pageable);
    }

    private Sort extractSort(List<FilterRequest> filters) {
        if (filters == null) return Sort.by("createdAt").descending();
        return filters.stream()
                .filter(f -> f.getSort() != null && f.getField() != null)
                .findFirst()
                .map(f -> "desc".equalsIgnoreCase(f.getSort())
                        ? Sort.by(f.getField()).descending()
                        : Sort.by(f.getField()).ascending())
                .orElse(Sort.by("createdAt").descending());
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setIsActive(false);
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User userUpdate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        mergeFields(userUpdate, user);
        return userRepository.save(user);
    }

}
