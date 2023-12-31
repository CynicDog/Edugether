package org.example.repository;

import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.entity.users.User;
import org.example.projection.UserProjection;

import java.util.List;

public interface UserRepository {

    User loadUserByUsername(String username);
    Teacher loadTeacherByUsername(String username);
    Student loadStudentByUsername(String username);
    void insertUser(User user);
    User loadUserByEmail(String email);
    boolean isUsernameUnique(String username);
    boolean isEmailUnique(String email);
    List<UserProjection> getUsersRandomlyLimitBy(Integer limit);
    void updateUser(User user);
    Student getStudentById(Long studentId);
    User getUserById(Long userId);
    void deleteUserByUsername(String username);
}
