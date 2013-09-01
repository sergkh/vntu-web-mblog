package edu.vntu.mblog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSubscribersDao extends AbstractDao {

	public UserSubscribersDao() {}

	public void subscribe(long followedId, long subscriberId) {
		String sql = "insert into user_followers (followed_id, subscriber_id) VALUES (?,?)";
		Connection con = getConnection();

		try (PreparedStatement createSt = con.prepareStatement(sql)) {
			createSt.setLong(1, followedId);
			createSt.setLong(2, subscriberId);
			createSt.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void unsubscribe(long followedId, long subscriberId) {
		String sql = "DELETE FROM user_followers WHERE followed_id=? AND subscriber_id=?";
		Connection con = getConnection();

		try (PreparedStatement createSt = con.prepareStatement(sql)) {
			createSt.setLong(1, followedId);
			createSt.setLong(2, subscriberId);
			createSt.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isSubscribed(long followedId, long subscriberId) {
		String sql = "SELECT COUNT(*) FROM user_followers WHERE followed_id=? AND subscriber_id=?";
		
		Connection con = getConnection();

		try (PreparedStatement getSt = con.prepareStatement(sql)) {
			getSt.setLong(1, followedId);
			getSt.setLong(2, subscriberId);
			
			ResultSet results = getSt.executeQuery();

			if(!results.next()) return false; 
			
			return results.getInt(1) > 0;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public int getFollowersCount(long userId) {
	 	String sql = "SELECT COUNT(*) FROM user_followers WHERE followed_id=?"; 
		
		Connection con = getConnection();

		try (PreparedStatement getSt = con.prepareStatement(sql)) {
			getSt.setLong(1, userId);
			
			ResultSet results = getSt.executeQuery();

			if(!results.next()) return 0; 
			
			return results.getInt(1);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getFollowingCount(long userId) {
	 	String sql = "SELECT COUNT(*) FROM user_followers WHERE subscriber_id=?";
		
		Connection con = getConnection();

		try (PreparedStatement getSt = con.prepareStatement(sql)) {
			getSt.setLong(1, userId);
			
			ResultSet results = getSt.executeQuery();

			if(!results.next()) return 0; 
			
			return results.getInt(1);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
