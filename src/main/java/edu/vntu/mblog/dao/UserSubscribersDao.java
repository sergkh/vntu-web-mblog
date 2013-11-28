package edu.vntu.mblog.dao;

/**
 * Created with IntelliJ IDEA.
 * User: sergey
 * Date: 11/28/13
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserSubscribersDao {
    void subscribe(long followedId, long subscriberId);

    void unsubscribe(long followedId, long subscriberId);

    boolean isSubscribed(long followedId, long subscriberId);

    int getFollowersCount(long userId);

    int getFollowingCount(long userId);
}
