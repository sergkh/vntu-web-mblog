/**
 *
 */
package edu.vntu.mblog.services;

import static edu.vntu.mblog.util.ValidationUtils.validateEmail;
import static edu.vntu.mblog.util.ValidationUtils.validateLen;

import java.util.EnumSet;
import java.util.List;

import edu.vntu.mblog.dao.PostsDao;
import edu.vntu.mblog.dao.UserSubscribersDao;
import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.domain.UserStatistics;
import edu.vntu.mblog.errors.AuthenticationExeption;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.jdbc.ConnectionManager;
import edu.vntu.mblog.util.SecurityUtils;

/**
 *
 * @author sergey
 */
public class UsersService {

    private static final UsersService instance = new UsersService();

    private UsersDao usersDao;
    private UserSubscribersDao subscribersDao;
    private PostsDao postsDao;

    private ConnectionManager connectionManager;

    public User register(String login, String email, String password) throws ValidationException {
        validateLen("login", login, 3, 128);
        validateLen("email", email, 3, 128);
        validateEmail("email", email);
        validateLen("password", password, 6, Integer.MAX_VALUE);

        String passHash = SecurityUtils.digest(password);

        User u = new User(login, email, passHash);
        u.setPermissions(EnumSet.of(User.Permission.USER));

        try {
            usersDao.create(u);
            usersDao.addPermission(u.getId(), User.Permission.USER);
            connectionManager.commitTransaction();
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }

        return u;
    }

    public User login(String loginOrEmail, String password) throws AuthenticationExeption {
        String passHash = SecurityUtils.digest(password);

        try {
            User user = usersDao.getByLoginOrEmail(loginOrEmail);

            if(user == null || !user.getPassHash().equals(passHash)) {
                throw new AuthenticationExeption("Wrong login or password");
            }

            connectionManager.commitTransaction();

            return user;
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public List<User> getUsersList(int offset, int limit) throws ValidationException {
        if(offset < 0)
            throw new ValidationException("offset", "Offset can't be negative");

        if(limit < 0)
            throw new ValidationException("limit", "Limit can't be negative");

        try {

            List<User> users = usersDao.getAllUsers(offset, limit);
            connectionManager.commitTransaction();

            return users;
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }


    public UserStatistics getStatistics(String loginOrEmail) throws UserNotFoundException {
        try {

            User u = usersDao.getByLoginOrEmail(loginOrEmail);

            if(u == null) {
                throw new UserNotFoundException(loginOrEmail, "User not found");
            }

            long userId = u.getId();

            UserStatistics stat = new UserStatistics(
                    postsDao.getCountForUser(userId),
                    subscribersDao.getFollowersCount(userId),
                    subscribersDao.getFollowingCount(userId));

            connectionManager.commitTransaction();

            return stat;
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public boolean isSubscribed(String followedLogin, String subscriberLogin) throws UserNotFoundException {
        try {

            User followed = usersDao.getByLoginOrEmail(followedLogin);
            User subscriber = usersDao.getByLoginOrEmail(subscriberLogin);

            if(followed == null) {
                throw new UserNotFoundException(followedLogin, "User not found");
            }

            if(subscriber == null) {
                throw new UserNotFoundException(subscriberLogin, "User not found");
            }

            boolean result = subscribersDao.isSubscribed(followed.getId(), subscriber.getId());

            connectionManager.commitTransaction();

            return result;
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public void toggleSubscription(String followedLogin, String subscriberLogin, boolean subscribe)
            throws UserNotFoundException {
        try {

            User followed = usersDao.getByLoginOrEmail(followedLogin);
            User subscriber = usersDao.getByLoginOrEmail(subscriberLogin);

            if(followed == null) {
                throw new UserNotFoundException(followedLogin, "User not found");
            }

            if(subscriber == null) {
                throw new UserNotFoundException(subscriberLogin, "User not found");
            }

            if(subscribe) {
                subscribersDao.subscribe(followed.getId(), subscriber.getId());
            } else {
                subscribersDao.unsubscribe(followed.getId(), subscriber.getId());
            }

            connectionManager.commitTransaction();
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }


    public void togglePermission(long userId, User.Permission permission, boolean addPerm) {
        try {
            if(addPerm) {
                usersDao.addPermission(userId, permission);
            } else {
                usersDao.clearPermission(userId, permission);
            }

            connectionManager.commitTransaction();
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public void toggleUser(long userId, boolean block) {
        try {
            usersDao.toggleUserBlock(userId, block);
            connectionManager.commitTransaction();
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public User getUser(String login) {
        try {
            User u = usersDao.getByLoginOrEmail(login);
            connectionManager.commitTransaction();
            return u;
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public void setAvatar(long userId, String fileName) {
        try {
            usersDao.setAvatar(userId, fileName);
            connectionManager.commitTransaction();
        } catch (Exception e) {
            connectionManager.rollbackTransaction();
            throw e;
        }
    }

    public void setUsersDao(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public void setSubscribersDao(UserSubscribersDao subscribersDao) {
        this.subscribersDao = subscribersDao;
    }

    public void setPostsDao(PostsDao postsDao) {
        this.postsDao = postsDao;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
