package org.example.entity.contents;

import org.example.entity.users.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ElementCollection
    @CollectionTable(
            name = "PostComment",
            joinColumns = @JoinColumn(name = "userId")
    )
    @Column(name = "content")
    private List<String> comments;


    @ManyToMany
    @JoinTable(
            name = "PostLikedUsers",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> likedUsers;

    private String title;
    private String content;
    private Integer likedCount;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Integer likedCount) {
        this.likedCount = likedCount;
    }
}
