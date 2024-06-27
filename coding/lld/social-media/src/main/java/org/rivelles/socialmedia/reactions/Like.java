package org.rivelles.socialmedia.reactions;

import org.rivelles.socialmedia.users.Post;
import org.rivelles.socialmedia.users.User;

public class Like {
    private final User user;
    private final Post post;

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
