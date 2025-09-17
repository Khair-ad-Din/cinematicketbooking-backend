package com.WisdomMonkey.CinemaTicketBooking_Backend.controller;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins="http://localhost:4200")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // === Basic CRUD Operations ===

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<User> users = userService.findAll();
            return ResponseEntity.ok(new ApiResponse(true, "Obtained users:", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "User found", user.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "User found", user.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "User found", user.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    // === Searched By State ===

    @GetMapping("/active/{status}")
    public ResponseEntity<?> getUsersByActiveStatus(@PathVariable Boolean status) {
        try {
            List<User> users = userService.findByActive(status);
            String message = status ? "Obtained Active Users" : "Obtained Inactive Users";
            return ResponseEntity.ok(new ApiResponse(true, message, users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    // === Counters ===

    @GetMapping("/count")
    public ResponseEntity<?> getUserCount() {
        try {
            long count = userService.countAll();
            return ResponseEntity.ok(new ApiResponse(true, "Total Users Count:", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/count/active")
    public ResponseEntity<?> getActiveUserCount() {
        try {
            long count = userService.countByActive(true);
            return ResponseEntity.ok(new ApiResponse(true, "Active Users Count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/count/inactive")
    public ResponseEntity<?> getInactiveUserCount() {
        try {
            long count = userService.countByActive(false);
            return ResponseEntity.ok(new ApiResponse(true, "Inactive Users Count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    // === Availability Checkers ===

    @GetMapping("/check/username/{username}")
    public ResponseEntity<?> checkUsernameAvailability(@PathVariable String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            String message = exists ? "Available Username" : "Not Available Username";
            return ResponseEntity.ok(new ApiResponse(true, message, !exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/check/email/{email}")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            String message = exists ? "Available Email" : "Not Available Email";
            return ResponseEntity.ok(new ApiResponse(true, message, !exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    // === Create/Update Operations ===

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            // Verificar duplicados
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Username Already Exists", null));
            }
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Email Already Exists", null));
            }

            User savedUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User Created Successfully", savedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            Optional<User> existingUser = userService.findById(id);
            if (existingUser.isPresent()) {
                user.setId(id); // Asegurar que el ID coincida
                User updatedUser = userService.save(user);
                return ResponseEntity.ok(new ApiResponse(true, "User Updated Successfully", updatedUser));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Optional<User> existingUser = userService.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();

                // Actualizar solo los campos enviados
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "username" -> user.setUsername((String) value);
                        case "email" -> user.setEmail((String) value);
                        case "firstname" -> user.setFirstname((String) value);
                        case "lastname" -> user.setLastname((String) value);
                        case "active" -> user.setActive((Boolean) value);
                        case "password" -> user.setPassword((String) value); // TODO: Implement hash
                    }
                });

                User updatedUser = userService.save(user);
                return ResponseEntity.ok(new ApiResponse(true, "User Partially Updated", updatedUser));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    // === Special Operations ===

    @PutMapping("/{id}/last-access")
    public ResponseEntity<?> updateLastAccessDate(@PathVariable Long id) {
        try {
            Optional<User> userToUpdate = userService.findById(id);
            if (userToUpdate.isPresent()) {
                String username = userToUpdate.get().getUsername();
                userService.updateLastAccess(username);

                User updatedUser = userService.findById(id).orElse(null);
                return ResponseEntity.ok(new ApiResponse(true, "Last Access Updated Successfully", updatedUser));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        try {
            Optional<User> userToToggle = userService.findById(id);
            if (userToToggle.isPresent()) {
                User user = userToToggle.get();
                user.setActive(!user.isActive());
                User updatedUser = userService.save(user);
                String status = updatedUser.isActive() ? "Active" : "Inactive";
                return ResponseEntity.ok(new ApiResponse(true, "User " + status, updatedUser));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    // === Delete Operations ===

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            Optional<User> existingUser = userService.findById(id);
            if (existingUser.isPresent()) {
                boolean deleted = userService.deleteById(id);
                if (deleted) {
                    return ResponseEntity.ok(new ApiResponse(true, "User Deleted Successfully", null));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponse(false, "User Cannot Be Deleted", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal Server Error", null));
        }
    }

    /**
     * Class for standardized API responses
     */
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters/Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}