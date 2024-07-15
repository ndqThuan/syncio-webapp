package online.syncio.backend.user;

import jakarta.validation.Valid;
import online.syncio.backend.auth.responses.LoginResponse;
import online.syncio.backend.auth.responses.ResponseObject;
import online.syncio.backend.exception.AppException;
import online.syncio.backend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController (final UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }

//    @GetMapping
//    public ResponseEntity<List<UserDTO>> getAllUsers () {
//        return ResponseEntity.ok(userService.findAll());
//    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> searchUsers (@RequestParam Optional<String> username) {
        return ResponseEntity.ok(userService.findAll(username));
    }

    @GetMapping("/search-by-username")
    public ResponseEntity<List<UserProfile>> searchUsersByUsername (@RequestParam Optional<String> username) {
        return ResponseEntity.ok(userService.searchUsers(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser (@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping("/{id}/username")
    public ResponseEntity<Map<String, String>> getUsername(@PathVariable(name = "id") final UUID id) {
        final String username = userService.getUsernameById(id);
        return ResponseEntity.ok(Collections.singletonMap("username", username));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers (@RequestParam(name = "username", required = false) final String username,
                                                      @RequestParam(name = "email", required = false) final String email) {
        return ResponseEntity.ok(userService.findTop20ByUsernameContainingOrEmailContaining(username, email));
    }

    /**
     * Get all users with at least one story created in the last 24 hours
     * @return a list of users
     */
    @GetMapping("/stories")
    public ResponseEntity<List<UserStoryDTO>> getUsersWithStories() {
        return ResponseEntity.ok(userService.findAllUsersWithAtLeastOneStoryAfterCreatedDate(LocalDateTime.now().minusDays(1)));
    }

    @PostMapping
    public ResponseEntity<UUID> createUser (@RequestBody @Valid final UserDTO userDTO) {
        if (userService.existsByUsername(userDTO.getUsername())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username already exists!", null);
        }

        if (userService.existsByEmail(userDTO.getEmail())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Email already exists!", null);
        }

        userService.create(userDTO);
        return ResponseEntity.ok(userDTO.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateUser (@PathVariable(name = "id") final UUID id,
                                            @RequestBody final UserDTO userDTO) {

        List<UserDTO> users = userService.findAll(Optional.empty());

        // index of id in users
        int currentIndex = users.indexOf(users.stream().filter(u -> u.getId().equals(id)).findFirst().get());
        System.out.println(currentIndex);

        if (users.stream().anyMatch(u -> u.getUsername().equals(userDTO.getUsername()) && users.indexOf(u) != currentIndex)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username already exists!", null);
        }
        
        if (users.stream().anyMatch(u -> u.getEmail().equals(userDTO.getEmail()) && users.indexOf(u) != currentIndex)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Email already exists!", null);
        }

        userService.update(id, userDTO);
        return ResponseEntity.ok(id);
    }


    @GetMapping("/profile/{id}")
    @ResponseBody
    public ResponseEntity<UserProfile> getUserProfile (@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    /**
     * Get the profile of a user by their id.
     * Use for case when the user already logged in.
     * @param id
     * @return
     */
    @PostMapping("/profile/{id}")
    @ResponseBody
    public ResponseEntity<UserProfile> getUserProfile2 (@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable(name = "id") final UUID id, @RequestBody UpdateProfileDTO user) {
        try {
            User userDetail = userService.updateProfile( id,user);
            LoginResponse loginResponse = LoginResponse
                    .builder()
                    .message("Login successfully")
                    .bio(userDetail.getBio())
                    .email(userDetail.getEmail())
                    .username(userDetail.getUsername())
                    .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .id(userDetail.getId())
                    .build();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Update successfully")
                    .data(loginResponse)
                    .status(HttpStatus.OK)
                    .build());

        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body( ex.getMessage());
        }
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<Void> updateAvatar(@RequestParam("file") MultipartFile file) {
        userService.updateAvatar(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/last/{days}")
    public Map<String, Long> getNewUsersLastNDays(@PathVariable(name = "days") final int days) {
        return userService.getNewUsersLastNDays(days);
    }

    @GetMapping("/outstanding")
    public ResponseEntity<List<UserDTO>> getOutstandingUsers() {
        return ResponseEntity.ok(userService.getOutstandingUsers());
    }

}
