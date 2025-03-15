package me.vasujain.booksync.service;

import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.dto.UserCreateDTO;
import me.vasujain.booksync.dto.UserUpdateDTO;
import me.vasujain.booksync.expection.ResourceNotFoundException;
import me.vasujain.booksync.model.User;
import me.vasujain.booksync.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Object getAllUsers(boolean paginate, int page, int size, String sortBy, String sortDir) {
        if (paginate) {
            int offset = page * size;
            List<User> users = userRepository.findAll(offset, size, sortBy, sortDir);
            int totalElements = userRepository.countAll();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            return PaginatedResult.<User>builder()
                    .content(users)
                    .currentPage(page)
                    .pageSize(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        } else {
            return userRepository.findAll(0, Integer.MAX_VALUE, sortBy, sortDir);
        }
    }

    public User getUserById(UUID id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return userOpt.get();
    }

    @Transactional
    public User createUser(UserCreateDTO createDTO) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(createDTO.getUsername())
                .email(createDTO.getEmail())
                .passwordHash(createDTO.getPassword()) // Ensure proper hashing in production
                .role(createDTO.getRole())
                .firstName(createDTO.getFirstName())
                .lastName(createDTO.getLastName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User updateUser(UUID id, UserUpdateDTO updateDTO) {
        User existingUser = getUserById(id);
        if (updateDTO.getUsername() != null) {
            existingUser.setUsername(updateDTO.getUsername());
        }
        if (updateDTO.getEmail() != null) {
            existingUser.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getRole() != null) {
            existingUser.setRole(updateDTO.getRole());
        }
        if (updateDTO.getFirstName() != null) {
            existingUser.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            existingUser.setLastName(updateDTO.getLastName());
        }
        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.update(existingUser);
        return existingUser;
    }

    @Transactional
    public void deleteUser(UUID id) {
        getUserById(id);
        userRepository.deleteById(id);
    }
}
