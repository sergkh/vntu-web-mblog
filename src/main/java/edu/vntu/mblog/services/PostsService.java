package edu.vntu.mblog.services;

import edu.vntu.mblog.dao.jdbc.PostsJdbcDao;
import edu.vntu.mblog.dao.jdbc.UsersJdbcDao;
import edu.vntu.mblog.domain.Post;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;

import java.util.List;

/**
 *
 * User: sergey
 * Date: 11/28/13, 10:19 PM
 */
public interface PostsService {

    void createPost(String userLogin, String post) throws UserNotFoundException, ValidationException;

    List<Post> getUsersFeed(String userLoginOrEmail) throws UserNotFoundException;

    List<Post> getAllPosts(int offset, int limit) throws ValidationException;

    void moderatePost(long id, Post.State state);

    void setPostsDao(PostsJdbcDao postsDao);

    void setUsersDao(UsersJdbcDao usersDao);
}
