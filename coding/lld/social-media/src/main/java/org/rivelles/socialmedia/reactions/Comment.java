package org.rivelles.socialmedia.reactions;

import org.rivelles.socialmedia.users.Post;
import org.rivelles.socialmedia.users.User;

public class Comment {
    private final User user;
    private final Post post;
    private String content;

    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }
}
