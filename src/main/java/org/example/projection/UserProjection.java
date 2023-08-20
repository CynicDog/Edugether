package org.example.projection;

import org.example.util.enums.TYPE;

import java.security.Principal;

public class UserProjection implements Principal {

    private Long id;
    private String username;
    private String email;
    private TYPE type;


    public UserProjection(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserProjection(Long id, String username, String email, TYPE type) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public TYPE getType() {
        return type;
    }
}
