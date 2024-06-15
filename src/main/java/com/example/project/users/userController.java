package com.example.project.users;

import com.example.project.users.Services.userService;
import com.example.project.users.data.DTO.*;
import com.example.project.users.data.help.resetPassRequestStatus;
import com.example.project.users.data.help.userRole;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class userController {
    private final userService userService;
    private final ModelMapper modelMapper;


    public userController(ModelMapper modelMapper,userService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/getById")
    public ResponseEntity<userDTO> getUserById(@RequestParam("userId") Long userId){
        try {
            return ResponseEntity.ok(modelMapper.map(userService.getUserById(userId), userDTO.class));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/register")
    private ResponseEntity<userRegisterStatusDTO> registerNewUser(@RequestBody userRegisterDTO userRegisterDTO){
        try {
            return ResponseEntity.ok(userService.register(userRegisterDTO));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<loginResponseDTO> login(@RequestBody userLogin userLogin){
        try {
            return ResponseEntity.ok(userService.login(userLogin));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/password-reset/request")
    public ResponseEntity<resetPassRequestStatus> passwordResetRequest(@RequestParam("userEmail")String userEmail){
        try {
            return ResponseEntity.ok(userService.requestResetPassword(userEmail));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/check/valid")
    public ResponseEntity checkUserTokenValidity(Authentication authentication){
        return ResponseEntity.ok(userService.userByEmail(authentication.getPrincipal().toString()));
    }

    @PostMapping("/google/login")
    public ResponseEntity<loginResponseDTO> loginGoogleUser(@RequestBody googleLoginDTO googleLoginDTO){
        try {
            return ResponseEntity.ok(userService.googleLogin(googleLoginDTO));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/users/cleaner")
    public ResponseEntity<List<userDTO>> getAllCleaners(){
        try {
            return ResponseEntity.ok(userService.findAllCleaners());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin/users")
    public List<userDTO> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/delete")
    public void deleteUser(Authentication authentication) {
         userService.deleteAccount(authentication.getPrincipal().toString());
    }

    @PutMapping("/{userId}/updateRole")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long userId,
            @RequestParam userRole newRole) {

        userService.updateUserRole(userId, newRole);
        return ResponseEntity.ok("User role updated successfully");
    }
}
