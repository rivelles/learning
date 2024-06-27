## Social Media Feed

Design a social media feed that displays posts from users' friends and followed accounts. 
The feed should be sorted by recency and allow users to interact with posts by liking and commenting.

### Requirements
Friendship and Following: Users can follow other users and accept friend requests.
Posts: Users can create text-based posts.
Feed: Display a feed of posts from friends and followed users, sorted by the most recent posts first.
Likes: Users can like posts.
Comments: Users can comment on posts.
Notifications: Users receive notifications when they are followed, when their posts are liked, 
or when comments are made on their posts.

____________________________________________________________

Entities:
User
- ID
- Name
- Following
- Friends
- UserFeed

Post
- Content
- Author
- Likes
- Comments

UserFeed
- User
- Posts

Like
- User
- Post

Comment
- User
- Post
- Content

Notification
- User
- Type