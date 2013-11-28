package edu.vntu.mblog.dao;

import edu.vntu.mblog.domain.Post;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sergey
 * Date: 11/28/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PostsDao {
    void create(long authorId, String text);

    void moderate(long postId, Post.State state);

    List<Post> getAll(int offset, int limit);

    int getCountForUser(long userId);

    List<Post> getFeed(long userId, int offset, int limit);
}
