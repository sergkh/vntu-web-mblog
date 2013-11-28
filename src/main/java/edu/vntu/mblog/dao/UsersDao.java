package edu.vntu.mblog.dao;

import edu.vntu.mblog.domain.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sergey
 * Date: 11/28/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UsersDao {
    void create(User user);

    void addPermission(long userId, User.Permission role);

    void clearPermission(long userId, User.Permission role);

    User getByLoginOrEmail(String identifier);

    List<User> getAllUsers(int offset, int limit);

    void setAvatar(long userId, String fileName);

    void toggleUserBlock(long userId, boolean block);
}
