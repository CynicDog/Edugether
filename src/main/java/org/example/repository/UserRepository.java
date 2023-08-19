package org.example.repository;

import org.example.entity.users.User;

public interface UserRepository {

    User loadUserByUsername(String username);

    void insertUser(User user);
}
