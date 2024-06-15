package com.example.project.users.Services;

import com.example.project.users.data.User;
import com.example.project.users.data.help.userRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface userRepo extends CrudRepository<User,Long> {
    User findByUserEmail(String email);
    List<User> findAllByUserRole(userRole userRole);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.userRole = ?2 WHERE u.userId = ?1")
    void updateUserRoleById(Long userId, userRole newUserRole);
}
