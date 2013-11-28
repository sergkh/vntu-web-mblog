package edu.vntu.mblog.services;

import edu.vntu.mblog.dao.jdbc.PostsJdbcDao;
import edu.vntu.mblog.dao.jdbc.UserSubscribersJdbcDao;
import edu.vntu.mblog.dao.jdbc.UsersJdbcDao;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.domain.UserStatistics;
import edu.vntu.mblog.errors.AuthenticationExeption;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;

import java.util.List;

/**
 *
 * User: sergey
 * Date: 11/28/13, 10:20 PM
 */
public interface UsersService {

    User register(String login, String email, String password) throws ValidationException;

    User login(String loginOrEmail, String password) throws AuthenticationExeption;

    List<User> getUsersList(int offset, int limit) throws ValidationException;

    UserStatistics getStatistics(String loginOrEmail) throws UserNotFoundException;

    boolean isSubscribed(String followedLogin, String subscriberLogin) throws UserNotFoundException;

    void toggleSubscription(String followedLogin, String subscriberLogin, boolean subscribe)
            throws UserNotFoundException;

    void togglePermission(long userId, User.Permission permission, boolean addPerm);

    void toggleUser(long userId, boolean block);

    User getUser(String login);

    void setAvatar(long userId, String fileName);

    void setUsersDao(UsersJdbcDao usersDao);

    void setSubscribersDao(UserSubscribersJdbcDao subscribersDao);

    void setPostsDao(PostsJdbcDao postsDao);
}
