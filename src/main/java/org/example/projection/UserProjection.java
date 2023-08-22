package org.example.projection;

import org.example.util.enums.TYPE;

public class UserProjection {

    private Long id;
    private String username;
    private String email;
    private TYPE type;


    public UserProjection(Long id, String username, TYPE type) {
        this.id = id;
        this.username = username;
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public TYPE getType() {
        return type;
    }
}
