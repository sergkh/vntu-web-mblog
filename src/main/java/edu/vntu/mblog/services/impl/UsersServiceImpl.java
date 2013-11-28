/**
 *
 */
package edu.vntu.mblog.services.impl;

import edu.vntu.mblog.dao.PostsDao;
import edu.vntu.mblog.dao.UserSubscribersDao;
import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.dao.jdbc.PostsJdbcDao;
import edu.vntu.mblog.dao.jdbc.UserSubscribersJdbcDao;
import edu.vntu.mblog.dao.jdbc.UsersJdbcDao;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.domain.UserStatistics;
import edu.vntu.mblog.errors.AuthenticationExeption;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.UsersService;
import edu.vntu.mblog.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

import static edu.vntu.mblog.util.ValidationUtils.validateEmail;
import static edu.vntu.mblog.util.ValidationUtils.validateLen;

/**
 *
 * @author sergey
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private UserSubscribersDao subscribersDao;

    @Autowired
    private PostsDao postsDao;


    @Override
    @Transactional(readOnly = false)
    public User register(String login, String email, String password) throws ValidationException {
        validateLen("login", login, 3, 128);
        validateLen("email", email, 3, 128);
        validateEmail("email", email);
        validateLen("password", password, 6, Integer.MAX_VALUE);

        String passHash = SecurityUtils.digest(password);

        User u = new User(login, email, passHash);
        u.setPermissions(EnumSet.of(User.Permission.USER));

        usersDao.create(u);
        usersDao.addPermission(u.getId(), User.Permission.USER);

        return u;
    }

    @Override
    @Transactional(readOnly = false)
    public User login(String loginOrEmail, String password) throws AuthenticationExeption {
        String passHash = SecurityUtils.digest(password);

        User user = usersDao.getByLoginOrEmail(loginOrEmail);

        if(user == null || !user.getPassHash().equals(passHash)) {
            throw new AuthenticationExeption("Wrong login or password");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersList(int offset, int limit) throws ValidationException {
        if(offset < 0)
            throw new ValidationException("offset", "Offset can't be negative");

        if(limit < 0)
            throw new ValidationException("limit", "Limit can't be negative");

        return usersDao.getAllUsers(offset, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatistics getStatistics(String loginOrEmail) throws UserNotFoundException {
        User u = usersDao.getByLoginOrEmail(loginOrEmail);

        if(u == null) {
            throw new UserNotFoundException(loginOrEmail, "User not found");
        }

        long userId = u.getId();

        return new UserStatistics(
                postsDao.getCountForUser(userId),
                subscribersDao.getFollowersCount(userId),
                subscribersDao.getFollowingCount(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubscribed(String followedLogin, String subscriberLogin) throws UserNotFoundException {
        User followed = usersDao.getByLoginOrEmail(followedLogin);
        User subscriber = usersDao.getByLoginOrEmail(subscriberLogin);

        if(followed == null) {
            throw new UserNotFoundException(followedLogin, "User not found");
        }

        if(subscriber == null) {
            throw new UserNotFoundException(subscriberLogin, "User not found");
        }

        return subscribersDao.isSubscribed(followed.getId(), subscriber.getId());
    }

    @Override
    @Transactional(readOnly = false)
    public void toggleSubscription(String followedLogin, String subscriberLogin, boolean subscribe)
            throws UserNotFoundException {

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
    }

    @Override
    @Transactional(readOnly = false)
    public void togglePermission(long userId, User.Permission permission, boolean addPerm) {
        if(addPerm) {
            usersDao.addPermission(userId, permission);
        } else {
            usersDao.clearPermission(userId, permission);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void toggleUser(long userId, boolean block) {
        usersDao.toggleUserBlock(userId, block);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String login) {
        return usersDao.getByLoginOrEmail(login);
    }

    @Override
    @Transactional(readOnly = false)
    public void setAvatar(long userId, String fileName) {
        usersDao.setAvatar(userId, fileName);
    }

    @Override
    public void setUsersDao(UsersJdbcDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public void setSubscribersDao(UserSubscribersJdbcDao subscribersDao) {
        this.subscribersDao = subscribersDao;
    }

    @Override
    public void setPostsDao(PostsJdbcDao postsDao) {
        this.postsDao = postsDao;
    }

}
