/**
 * 
 */
package edu.vntu.mblog.services;

import java.util.EnumSet;
import java.util.List;

import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.util.SecurityUtils;
import static edu.vntu.mblog.util.ValidationUtils.*;

/**
 * 
 * @author sergey
 */
public class UsersService {
	
	private final UsersDao usersDao;
	
	public UsersService() {
		usersDao = new UsersDao();
	}
	
	public User register(String login, String email, String password) {
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
	
	public List<User> getUsersList(int offset, int limit) {
		if(offset < 0) 
			throw new ValidationException("offset", "Offset can't be negative");
		
		if(limit < 0) 
			throw new ValidationException("limit", "Limit can't be negative");
		
		return usersDao.getAllUsers(offset, limit);
	}
}
