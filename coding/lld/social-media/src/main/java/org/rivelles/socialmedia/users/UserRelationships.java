package org.rivelles.socialmedia.users;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class UserRelationships {
    private final Set<User> followers;
    private final Set<User> following;
    private final Set<User> friends;
    private final Set<User> friendRequests;

    public UserRelationships() {
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.friends = new HashSet<>();
        this.friendRequests = new LinkedHashSet<>();
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void follow(User user) {
        if (following.contains(user)) throw new IllegalArgumentException("Already following");

        following.add(user);
    }

    public void receiveFriendRequest(User user) {
        if (friends.contains(user) || friendRequests.contains(user))
            throw new IllegalArgumentException("Users are already friends or request has been already sent");

        friendRequests.add(user);
    }

    public void acceptFriendRequest(User user) {
        if (!friendRequests.contains(user)) throw new IllegalArgumentException("Request not found");
        if (friends.contains(user)) throw new IllegalArgumentException("Already friends");

        friendRequests.remove(user);
        friends.add(user);
    }

    public void addFriend(User user) {
        if (friends.contains(user)) throw new IllegalArgumentException("Already friends");
        friends.add(user);
    }
}
