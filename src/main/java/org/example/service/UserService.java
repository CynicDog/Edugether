package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.vertx.core.json.JsonObject;
import org.example.entity.socials.Follow;
import org.example.entity.socials.FollowRequest;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.entity.users.User;
import org.example.projection.UserProjection;
import org.example.repository.FollowRepository;
import org.example.repository.FollowRequestRepository;
import org.example.repository.UserRepository;
import org.example.util.enums.FOLLOW_REQUEST_STATUS;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final Logger logger = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final FollowRequestRepository followRequestRepository;

    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, FollowRequestRepository followRequestRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRequestRepository = followRequestRepository;
        this.followRepository = followRepository;
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

    public List<UserProjection> getRequestsPaginatedByRecipientIdAndByStatus(Long recipientId, Integer page, Integer limit, String option) {

        if (option.equals("pending")) {
            return followRequestRepository.getPendingRequestsByRecipientId(recipientId, page, limit);
        } else if (option.equals("accepted")) {
            return followRequestRepository.getAcceptedRequestsByRecipientId(recipientId, page, limit);
        } else if (option.equals("declined")) {
            return followRequestRepository.getDeclinedRequestsByRecipientId(recipientId, page, limit);
        } else {
            return followRequestRepository.getSentRequestsByRecipientId(recipientId, page, limit)
                    .stream()
                    .map(request -> {
                        request.setRequestStatus(FOLLOW_REQUEST_STATUS.SENT);
                        return request;
                    })
                    .collect(Collectors.toList());
        }
    }

    public String updateRequestStatus(Long requestId, UserProjection authentication) {

        FollowRequest followRequest = followRequestRepository.getRequestById(requestId);

        if (followRequest.getRecipientId() == authentication.getId()) {

            Follow follow = followRepository.getFollowByFollowerAndFollowed(
                    followRequest.getSenderId(), followRequest.getRecipientId());

            if (followRequest.getFollowRequestStatus().equals(FOLLOW_REQUEST_STATUS.PENDING)) {
                if (follow == null) {
                    follow = new Follow(followRequest.getSenderId(), followRequest.getRecipientId());
                    followRepository.insertFollow(follow);
                }
                followRequest.setFollowRequestStatus(FOLLOW_REQUEST_STATUS.ACCEPTED);
            } else if (followRequest.getFollowRequestStatus().equals(FOLLOW_REQUEST_STATUS.ACCEPTED)) {
                if (follow != null) {
                    followRepository.deleteFollow(follow);
                }
                followRequest.setFollowRequestStatus(FOLLOW_REQUEST_STATUS.DECLINED);
            } else {
                if (follow == null) {
                    follow = new Follow(followRequest.getSenderId(), followRequest.getRecipientId());
                    followRepository.insertFollow(follow);
                }
                followRequest.setFollowRequestStatus(FOLLOW_REQUEST_STATUS.ACCEPTED);
            }
        }
        followRequestRepository.updateFollowRequest(followRequest);

        return followRequest.getFollowRequestStatus().toString();
    }

    public long getFollowingsCountByUserId(Long userId) {

        return followRepository.countFollowByFollowerId(userId);
    }

    public long getFollowersCountByUserId(Long userId) {

        return followRepository.countFollowByFollowedId(userId);
    }

    public List<User> getFollowersPaginatedByUserIdOrderByCreateDateAsc(Long userId, Integer page, Integer limit) {

        return followRepository.getFollowersPaginatedByUserIdOrderByCreateDateAsc(userId, page, limit);
    }

    public List<User> getFollowersPaginatedByUserIdOrderByCreateDateDesc(Long userId, Integer page, Integer limit) {

        return followRepository.getFollowersPaginatedByUserIdOrderByCreateDateDesc(userId, page, limit);
    }

    public List<User> getFollowingsPaginatedByUserIdOrderByCreateDateDesc(Long userId, Integer page, Integer limit) {

        return followRepository.getFollowingsPaginatedByUserIdOrderByCreateDateDesc(userId, page, limit);
    }

    public List<User> getFollowingsPaginatedByUserIdOrderByCreateDateAsc(Long userId, Integer page, Integer limit) {

        return followRepository.getFollowingsPaginatedByUserIdOrderByCreateDateAsc(userId, page, limit);
    }
}
