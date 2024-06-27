package org.rivelles.socialmedia.users;

import org.rivelles.socialmedia.reactions.Comment;
import org.rivelles.socialmedia.reactions.Like;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

public class Post implements Comparable<Post>{
    private User author;
    private String content;
    private Set<Like> likes;
    private Set<Comment> comments;
    private final Instant timestamp;

    Post(User author, String content) {
        this.author = author;
        this.content = content;
        this.likes = new LinkedHashSet<>();
        this.comments = new LinkedHashSet<>();
        this.timestamp = Instant.now();
    }

    void addLike(Like like) {
        if (likes.contains(like)) throw new IllegalArgumentException("User already liked this post");

        this.likes.add(like);
    }

    void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public int compareTo(Post o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    public User getAuthor() {
        return author;
    }
}
