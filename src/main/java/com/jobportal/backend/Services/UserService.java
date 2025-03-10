package com.jobportal.backend.Services;

import com.jobportal.backend.DTOs.UserDTO;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.UserRole;
import com.jobportal.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void updateUserRoles(String username) {
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<UserDTO> getUsers(int page, int size, String username, String email, String role) {
        Pageable pageable = PageRequest.of(page, size);

        UserRole userRole = null;
        if (role != null) {
            try {
                userRole = UserRole.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                userRole = null;
            }
        }

        Page<User> userPage;
        if (username != null && email != null && userRole != null) {
            userPage = userRepository.findByUsernameContainingOrEmailContainingOrRole(pageable, username, email, userRole);
        } else if (username != null && email != null) {
            userPage = userRepository.findByUsernameContainingOrEmailContaining(pageable, username, email);
        } else if (userRole != null) {
            userPage = userRepository.findByRole(pageable, userRole);
        } else if (username != null) {
            userPage = userRepository.findByUsernameContaining(pageable, username);
        } else if (email != null) {
            userPage = userRepository.findByEmailContaining(pageable, email);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        Page<UserDTO> userDTOPage = userPage.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole());
            userDTO.setCreatedAt(user.getCreatedAt().toString());
            userDTO.setUpdatedAt(user.getUpdatedAt().toString());
            return userDTO;
        });

        return userDTOPage;
    }


}


