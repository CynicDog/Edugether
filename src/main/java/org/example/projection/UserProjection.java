package org.example.projection;

import java.security.Principal;

public class UserProjection implements Principal {

    private Long id;
    private String username;


    public UserProjection(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }
}
