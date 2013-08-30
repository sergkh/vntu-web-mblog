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
	
	private final UsersDao usersDao = new UsersDao();
	private final UserSubscribersDao subscribersDao = new UserSubscribersDao();
	private final PostsDao postsDao = new PostsDao();
	
	private final ConnectionManager cm = ConnectionManager.getInstance();
	private UsersService() {}

	public static UsersService getInstance() {
		return instance;
	}
	
	public User register(String login, String email, String password) throws ValidationException {
		validateLen("login", login, 3, 128);
		validateLen("email", email, 3, 128);
		validateEmail("email", email);
		validateLen("password", password, 6, Integer.MAX_VALUE);

		String passHash = SecurityUtils.digest(password);
		
		User u = new User(login, email, passHash);
		u.setPermissions(EnumSet.of(User.Permission.USER));
		
		cm.startTransaction();
		try {
			usersDao.create(u);
			usersDao.addPermission(u.getId(), User.Permission.USER);
			cm.commitTransaction();
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}

		return u;
	}
	
	public User login(String loginOrEmail, String password) throws AuthenticationExeption {
		String passHash = SecurityUtils.digest(password);
		
		cm.startTransaction();
		try {
			User user = usersDao.getByLoginOrEmail(loginOrEmail); 
			
			if(user == null || !user.getPassHash().equals(passHash)) {
				throw new AuthenticationExeption("Wrong login or password");
			}
			
			cm.commitTransaction();
			
			return user;
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
	
	public List<User> getUsersList(int offset, int limit) throws ValidationException {
		if(offset < 0) 
			throw new ValidationException("offset", "Offset can't be negative");
		
		if(limit < 0) 
			throw new ValidationException("limit", "Limit can't be negative");
		
		cm.startTransaction();
		try {
			
			List<User> users = usersDao.getAllUsers(offset, limit);
			cm.commitTransaction();
			
			return users;
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
	
	
	public UserStatistics getStatistics(String loginOrEmail) throws UserNotFoundException {
		cm.startTransaction();
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
			
			cm.commitTransaction();
			
			return stat;
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
	
	public boolean isSubscribed(String followedLogin, String subscriberLogin) throws UserNotFoundException {
		cm.startTransaction();
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
			
			cm.commitTransaction();
			
			return result;
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
	
	public void subscribe(String followedLogin, String subscriberLogin) throws UserNotFoundException {
		cm.startTransaction();
		try {
			
			User followed = usersDao.getByLoginOrEmail(followedLogin);
			User subscriber = usersDao.getByLoginOrEmail(subscriberLogin);
			
			if(followed == null) {
				throw new UserNotFoundException(followedLogin, "User not found");
			}
	
			if(subscriber == null) {
				throw new UserNotFoundException(subscriberLogin, "User not found");
			}
			
			subscribersDao.subscribe(followed.getId(), subscriber.getId());
			
			cm.commitTransaction();
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
	
	public void unsubscribe(String followedLogin, String subscriberLogin) throws UserNotFoundException {
		cm.startTransaction();
		try {
			
			User followed = usersDao.getByLoginOrEmail(followedLogin);
			User subscriber = usersDao.getByLoginOrEmail(subscriberLogin);
			
			if(followed == null) {
				throw new UserNotFoundException(followedLogin, "User not found");
			}
	
			if(subscriber == null) {
				throw new UserNotFoundException(subscriberLogin, "User not found");
			}
			
			subscribersDao.unsubscribe(followed.getId(), subscriber.getId());
			
			cm.commitTransaction();
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
	
}
