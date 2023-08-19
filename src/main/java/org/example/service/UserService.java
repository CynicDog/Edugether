package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.vertx.core.json.JsonObject;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.entity.users.User;
import org.example.repository.UserRepository;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

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

    public boolean authenticate(JsonObject credentials) {

        String username = credentials.getString("username");
        String password = credentials.getString("password");

        User user = userRepository.loadUserByUsername(username);

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        return result.verified;
    }
}
