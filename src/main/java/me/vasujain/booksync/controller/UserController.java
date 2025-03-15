package me.vasujain.booksync.controller;

import me.vasujain.booksync.dto.ApiResponse;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.dto.UserCreateDTO;
import me.vasujain.booksync.dto.UserUpdateDTO;
import me.vasujain.booksync.model.User;
import me.vasujain.booksync.service.UserService;
import me.vasujain.booksync.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUsers(@RequestParam(required = false, defaultValue = "false") boolean paginate,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size,
                                                   @RequestParam(required = false, defaultValue = "username") String sortBy,
                                                   @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        Object result = userService.getAllUsers(paginate, page, size, sortBy, sortDirection);
        ApiResponse<?> response;
        if (paginate) {
            response = ResponseUtil.successPaginated((PaginatedResult<?>) result, "Users retrieved successfully");
        } else {
            response = ResponseUtil.success(result, "Users retrieved successfully");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") UUID id) {
        User user = userService.getUserById(id);
        ApiResponse<User> response = ResponseUtil.success(user, "User retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserCreateDTO createDTO) {
        User user = userService.createUser(createDTO);
        ApiResponse<User> response = ResponseUtil.success(user, "User created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable("id") UUID id,
                                                        @RequestBody UserUpdateDTO updateDTO) {
        User user = userService.updateUser(id, updateDTO);
        ApiResponse<User> response = ResponseUtil.success(user, "User updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = ResponseUtil.success(null, "User deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
