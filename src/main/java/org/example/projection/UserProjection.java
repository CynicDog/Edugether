package org.example.projection;

public class UserProjection {

    private Long id;
    private String username;

    public UserProjection(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
