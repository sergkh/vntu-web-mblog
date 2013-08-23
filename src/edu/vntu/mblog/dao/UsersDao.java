package edu.vntu.mblog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.domain.User.Permission;

public class UsersDao extends AbstractDao {
	public UsersDao() {}
	
	 public void create(User user) {
		String sql = "INSERT into USERS (login, email, passhash) VALUES (?,?,?)";
		Connection con = getConnection();
		
		try (PreparedStatement createSt = con.prepareStatement(sql)) {
			
			createSt.setString(1, user.getLogin());
			createSt.setString(2, user.getEmail());
			createSt.setString(3, user.getPassHash());
			createSt.executeUpdate();
			
			try(PreparedStatement getIdSt = con.prepareStatement("CALL IDENTITY()")) {
				ResultSet rs = getIdSt.executeQuery();
				if(rs.next()) {
					user.setId(rs.getLong(1));
				} else throw new RuntimeException("Call identity is not supported by your database");
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	 }
	 
	public void addPermission(long userId, User.Permission role) {
		String sql = "INSERT INTO user_permissions VALUES (?, SELECT id FROM permissions WHERE name=?)";
		
		Connection con = getConnection();
		
		try (PreparedStatement createSt = con.prepareStatement(sql)) {
			createSt.setLong(1, userId);
			createSt.setString(2, role.name());
			createSt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	 
	 public User getByLoginOrEmail(String identifier) {
		String sql = "SELECT * FROM users WHERE login=? OR email=?";
		Connection con = getConnection();
		
		try (PreparedStatement getSt = con.prepareStatement(sql)) {
			
			getSt.setString(1, identifier);
			getSt.setString(2, identifier);

			ResultSet results = getSt.executeQuery();

			if (!results.next()) 
				return null;
			
			return loadRoles(convert(results));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	 }
	 
	 public List<User> getAllUsers(int offset, int limit) {
		String sql = "SELECT * FROM users ORDER BY created DESC LIMIT ? OFFSET ?";
		
		Connection con = getConnection();
		
		 try (PreparedStatement getSt = con.prepareStatement(sql)) {
			 
			getSt.setInt(1, limit);
			getSt.setInt(2, offset);
			
			ResultSet results = getSt.executeQuery();
			
			List<User> users = new ArrayList<>();
			
			while (results.next()) {
				users.add(loadRoles(convert(results))) ;	
			}
			
			return users;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	 }
	 
	 private User loadRoles(User u) {
		 String sql = 
		 	"SELECT name FROM permissions WHERE id IN (SELECT perm_id FROM user_permissions WHERE user_id=?)";

		 Connection con = getConnection();
		 
		 try (PreparedStatement getSt = con.prepareStatement(sql)) {
			 
			getSt.setLong(1, u.getId());

			ResultSet results = getSt.executeQuery();

			List<User.Permission> perm = new ArrayList<>(); 
			
			while (results.next()) {
				perm.add(
					Permission.valueOf(results.getString("name"))
				);
			}
			
			u.setPermissions(EnumSet.copyOf(perm));
			
			return u;
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
