package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.vertx.core.json.JsonObject;
import org.example.entity.socials.FollowRequest;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.entity.users.User;
import org.example.projection.UserProjection;
import org.example.repository.FollowRequestRepository;
import org.example.repository.UserRepository;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

import java.util.List;

public class UserService {

    private final Logger logger = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final FollowRequestRepository followRequestRepository;

    public UserService(UserRepository userRepository, FollowRequestRepository followRequestRepository) {
        this.userRepository = userRepository;
        this.followRequestRepository = followRequestRepository;
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

    public User getUserByUsername(String name) {

        return userRepository.loadUserByUsername(name);
    }

    public void registerQualification(String username, String qualification) {

        Teacher teacher = userRepository.loadTeacherByUsername(username);

        teacher.addQualification(qualification);

        userRepository.updateUser((User) teacher);
    }

    public void registerInterest(String username, String interest) {

        Student student = userRepository.loadStudentByUsername(username);

        student.addInterest(interest);

        userRepository.updateUser((User) student);
    }

    public void registerFollowRequest(Long recipientId, String senderUsername) {

        User sender = userRepository.loadUserByUsername(senderUsername);

        try {
            followRequestRepository.insertFollowRequest(new FollowRequest(sender.getId(), recipientId));
        } catch (Exception e) {
            throw e;
        }
    }
}
