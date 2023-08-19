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

    private String title;
    private String content;
    private Integer likedCount;
}
