package org.example.service;

import org.example.repository.UserRepository;
import org.jboss.logging.Logger;

public class UserService {

    private final Logger logger = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
