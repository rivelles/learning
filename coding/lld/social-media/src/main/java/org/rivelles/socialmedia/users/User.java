package org.rivelles.socialmedia.users;

import org.rivelles.socialmedia.reactions.Comment;
import org.rivelles.socialmedia.reactions.Like;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class User {
    private final int id;
    private String name;
    private String email;
    private final UserRelationships userRelationships;
    private final Set<Post> posts;
    private final List<NotificationsObserver> observers;

    public User(int id, String name, String email, List<NotificationsObserver> observers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.observers = observers;
        this.userRelationships = new UserRelationships();
        this.posts = new TreeSet<>();
    }

    public void follow(User user) {
        if (user == this) throw new IllegalArgumentException("Can't follow the same user");

        userRelationships.follow(user);
    }

    public void sendFriendRequest(User user) {
        if (user == this) throw new IllegalArgumentException("Can't receive request from the same user");

        user.userRelationships.receiveFriendRequest(this);
    }

    public void acceptFriendRequest(User user) {
        if (user == this) throw new IllegalArgumentException("Can't receive request from the same user");

        userRelationships.acceptFriendRequest(user);
        user.userRelationships.addFriend(this);
    }

    public void publish(String content) {
        if (content.isBlank()) throw new IllegalArgumentException("Content can't be empty");

        var post = new Post(this, content);
        this.posts.add(post);
    }

    public List<Post> loadFeed() {
        return userRelationships.getFollowing()
            .stream()
            .map(User::getPosts)
            .flatMap(Collection::stream)
            .sorted()
            .toList();
    }

    public void like(Post post) {
        var like = new Like(this, post);
        post.addLike(like);
        post.getAuthor().sendNotification();
    }

    public void comment(Post post, String content) {
        var comment = new Comment(this, post, content);
        post.addComment(comment);
        post.getAuthor().sendNotification();
    }

    private Set<Post> getPosts() {
        return posts;
    }

    private void sendNotification() {
        observers.forEach(notificationsObserver -> notificationsObserver.notify(this));
    }
}
