package com.example.project.users.Services;

import com.example.project.email.EmailController;
import com.example.project.orders.data.DTO.orderDTO;
import com.example.project.orders.data.help.orderStatus;
import com.example.project.token.TokenServices;
import com.example.project.users.data.DTO.*;
import com.example.project.users.data.User;
import com.example.project.users.data.help.loginStatus;
import com.example.project.users.data.help.registerStatus;
import com.example.project.users.data.help.resetPassRequestStatus;
import com.example.project.users.data.help.userRole;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class userService {
    private final userRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenServices tokenServices;
    private final EmailController emailController;

    public userService(com.example.project.users.Services.userRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, TokenServices tokenServices, EmailController emailController) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenServices = tokenServices;
        this.emailController = emailController;
    }

    public User getUserById(Long userId){

        return userRepo.findById(userId).get();
    }
    public List<userDTO> getAll() {
        Iterable<User> users = userRepo.findAll();

        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> modelMapper.map(user, userDTO.class))
                .collect(Collectors.toList());
    }
    public loginResponseDTO login(userLogin loginDTO){
        User loginUser = userRepo.findByUserEmail(loginDTO.getUserEmail());
        if(loginUser==null){
            return new loginResponseDTO(loginStatus.MISSING_USER,"",null);
        }
        else {
            if(passwordEncoder.matches(loginDTO.getUserPassword(),loginUser.getUserPassword()) && loginUser.getActive()){
                return new loginResponseDTO(loginStatus.OK,tokenServices.generateTokenUser(loginUser),modelMapper.map(loginUser, userDTO.class));
            }
            else {
                return new loginResponseDTO(loginStatus.WRONG_PASSWORD,"",null);
            }
        }
    }

    public userRegisterStatusDTO register(userRegisterDTO newUser){
        User registerUser = new User(newUser.getUserName(), newUser.getUserEmail(), newUser.getUserPhoneNumber(), userRole.USER,passwordEncoder.encode(newUser.getUserPassword()),true);
        try {
            userRepo.save(registerUser);
            emailController.sendEmail();
            return new userRegisterStatusDTO(modelMapper.map(registerUser, userDTO.class), registerStatus.OK);
        } catch (Exception e) {
            return new userRegisterStatusDTO(null,registerStatus.USER_EXISTS);
        }
    }
    public resetPassRequestStatus requestResetPassword(String email){
        User requestResetUser = userRepo.findByUserEmail(email);
        if(requestResetUser==null){
            return resetPassRequestStatus.MISSING_USER;
        }
        else {
            return resetPassRequestStatus.SEND;
        }
    }

    public loginStatus resetPassword(String token,resetPasswordDTO resetPasswordDTO){
        try {
            String mail = tokenServices.getMailReset(token);
            User updatePasswordUser = userRepo.findByUserEmail(mail);
            if(updatePasswordUser == null){
                return loginStatus.MISSING_USER;
            }
            else {
                updatePasswordUser.setUserPassword(passwordEncoder.encode(resetPasswordDTO.getNewUserPassword()));
                userRepo.save(updatePasswordUser);
                return loginStatus.OK;
            }
        }
        catch (Exception e){
            return loginStatus.FAILED;
        }
    }

    public loginResponseDTO googleLogin(googleLoginDTO googleLoginDTO) {
        User user = userRepo.findByUserEmail(googleLoginDTO.getEmail());
        if(user==null){
            User newUser=new User(googleLoginDTO.getName(),googleLoginDTO.getEmail(),userRole.USER,true);
            userRepo.save(newUser);
            return new loginResponseDTO(loginStatus.OK,tokenServices.generateTokenUser(newUser),modelMapper.map(newUser, userDTO.class));
        }
        else {
            return new loginResponseDTO(loginStatus.OK,tokenServices.generateTokenUser(user),modelMapper.map(user, userDTO.class));
        }
    }
    public List<userDTO> findAllCleaners(){
        return userRepo.findAllByUserRole(userRole.CLEANER).stream().map(user -> modelMapper.map(user, userDTO.class)).collect(Collectors.toList());
    }
    public void updateUserRole(Long userId, userRole newUserRole) {
        userRepo.updateUserRoleById(userId, newUserRole);
    }

    public userDTO userByEmail(String email){
        return modelMapper.map(userRepo.findByUserEmail(email),userDTO.class);
    }

    public void deleteAccount(String email){
        User user = userRepo.findByUserEmail(email);
        user.setActive(false);
        userRepo.save(user);
    }
}
