package org.example.repository;

import org.example.entity.users.User;

public interface UserRepository {

    public User loadUserByUsername(String username);
}
