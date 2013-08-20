package edu.vntu.mblog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.vntu.mblog.domain.User;

public class UsersDao extends AbstractDao {
	public UsersDao() {}
	
	 public void create(User user) {
		String sql = "inser into users (login, email, passhash) VALUES (?,?,?)";
		Connection con = getConnection();
		PreparedStatement createSt = null;
		try {
			createSt = con.prepareStatement(sql);
			createSt.setString(1, user.getLogin());
			createSt.setString(2, user.getEmail());
			createSt.setString(3, user.getPassHash());
			createSt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(createSt, con);
		}
	 }
	 
	 public User getByLoginOrEmail(String identifier) {
		String sql = "select * from users where login=? or email=?";
		Connection con = getConnection();
		PreparedStatement getSt = null;
		ResultSet results = null;
		try {
			getSt = con.prepareStatement(sql);
			getSt.setString(1, identifier);
			getSt.setString(2, identifier);

			results = getSt.executeQuery();

			if (!results.next()) 
				return null;
			
			return convert(results);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(results, getSt, con);
		}
	 }
	  
	 private User convert(ResultSet rs) throws SQLException {
		 return new User(
				 rs.getLong("id"),
				 rs.getTimestamp("created"), 
				 rs.getString("login"), 
				 rs.getString("email"),
				 rs.getString("passhash"),
				 rs.getTimestamp("blocked")
		 );
	 }
}
