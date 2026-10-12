package com.interntrack.service;

import com.interntrack.dto.AuthResponse;
import com.interntrack.dto.LoginRequest;
import com.interntrack.dto.RegisterRequest;
import com.interntrack.dto.UserResponse;
import com.interntrack.exception.ApiException;
import com.interntrack.model.Role;
import com.interntrack.model.User;
import com.interntrack.repository.UserRepository;
import com.interntrack.security.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest req) {
        String email = req.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(409, "An account with this email already exists");
        }
        User user = new User(req.getName().trim(), email,
                passwordEncoder.encode(req.getPassword()), Role.STUDENT);
        user.setPhone(req.getPhone());
        user.setCollege(req.getCollege());
        user.setBranch(req.getBranch());
        user.setGraduationYear(req.getGraduationYear());
        user.setCgpa(req.getCgpa());
        user.setSkills(req.getSkills());
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), Role.STUDENT.name(), UserResponse.from(user));
    }

    public AuthResponse login(LoginRequest req) {
        String email = req.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(401, "Invalid email or password"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ApiException(401, "Invalid email or password");
        }
        return new AuthResponse(jwtService.generateToken(user),
                user.getRole() == null ? Role.STUDENT.name() : user.getRole().name(),
                UserResponse.from(user));
    }

    /** The authenticated user from the current JWT / SecurityContext. */
    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ApiException(401, "Not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(401, "Not authenticated"));
    }

    public Long currentUserId() {
        return currentUser().getUserId();
    }
}
