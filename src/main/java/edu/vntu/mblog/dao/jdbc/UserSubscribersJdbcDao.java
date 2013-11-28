package edu.vntu.mblog.dao.jdbc;

import edu.vntu.mblog.dao.UserSubscribersDao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserSubscribersJdbcDao extends AbstractJdbcDao implements UserSubscribersDao {

    public UserSubscribersJdbcDao() {}

    @Override
    public void subscribe(long followedId, long subscriberId) {
        String sql = "insert into user_followers (followed_id, subscriber_id) VALUES (?,?)";
        jdbc().update(sql, followedId, subscriberId);
    }

    @Override
    public void unsubscribe(long followedId, long subscriberId) {
        String sql = "DELETE FROM user_followers WHERE followed_id=? AND subscriber_id=?";
        jdbc().update(sql, followedId, subscriberId);
    }

    @Override
    public boolean isSubscribed(long followedId, long subscriberId) {
        String sql = "SELECT COUNT(*) FROM user_followers WHERE followed_id=? AND subscriber_id=?";
        return jdbc().queryForObject(sql, Integer.class, followedId, subscriberId) > 0;
    }

    @Override
    public int getFollowersCount(long userId) {
        String sql = "SELECT COUNT(*) FROM user_followers WHERE followed_id=?";
        return jdbc().queryForObject(sql, Integer.class, userId);
    }

    @Override
    public int getFollowingCount(long userId) {
        String sql = "SELECT COUNT(*) FROM user_followers WHERE subscriber_id=?";
        return jdbc().queryForObject(sql, Integer.class, userId);
    }
}
