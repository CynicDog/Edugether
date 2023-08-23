package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.vertx.core.json.JsonObject;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.entity.users.User;
import org.example.projection.UserProjection;
import org.example.repository.UserRepository;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

import java.util.List;

public class UserService {

    private final Logger logger = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerStudent(JsonObject userCommand) {

        Student user = new Student(
                userCommand.getString("username"),
                BCrypt.withDefaults().hashToString(12, userCommand.getString("password").toCharArray()),
                userCommand.getString("email"),
                TYPE.STUDENT
        );

        userRepository.insertUser(user);
    }
    public void registerTeacher(JsonObject userCommand) {

        Teacher user = new Teacher(
                userCommand.getString("username"),
                BCrypt.withDefaults().hashToString(12, userCommand.getString("password").toCharArray()),
                userCommand.getString("email"),
                TYPE.TEACHER
        );

        userRepository.insertUser(user);
    }

    public User authenticate(JsonObject credentials) {

        String username = credentials.getString("username");
        String password = credentials.getString("password");

        User user = userRepository.loadUserByUsername(username);

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        return result.verified ? user : null;
    }

    public boolean isUsernameUnique(String username) {

        return userRepository.isUsernameUnique(username);
    }

    public boolean isEmailUnique(String email) {

        return userRepository.isEmailUnique(email);
    }

    public List<UserProjection> getUsersRandomlyLimitBy(Integer limit) {

        return userRepository.getUsersRandomlyLimitBy(limit);
    }

    public Teacher getUserByUsername(String name) {

        return (Teacher) userRepository.loadUserByUsername(name);
    }

    public void registerQualification(String username, String qualification) {

        Teacher teacher = userRepository.loadTeacherByUsername(username);

        teacher.addQualification(qualification);

        userRepository.updateUser((User) teacher);
    }
}
