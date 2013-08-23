/**
 * 
 */
package edu.vntu.mblog.services;

import java.util.List;

import edu.vntu.mblog.dao.UserPostsDao;
import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.domain.Post;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.jdbc.ConnectionManager;

/**
 * 
 * @author sergey
 */
public class PostsService {
	private static final PostsService instance = new PostsService(); 
	
	private final UserPostsDao postsDao = new UserPostsDao();
	private final UsersDao usersDao = new UsersDao();
	
	private final ConnectionManager cm = ConnectionManager.getInstance();
	
	private PostsService() {}

	public static PostsService getInstance() {
		return instance;
	}
	
	public List<Post> getUsersFeed(String userLoginOrEmail) throws UserNotFoundException {
		
		cm.startTransaction();
		try {
			
			User u = usersDao.getByLoginOrEmail(userLoginOrEmail);
			
			if(u == null) {
				throw new UserNotFoundException(userLoginOrEmail, "User not found");
			}
			
			List<Post> posts = postsDao.getFeed(u.getId(), 0, 1000); // TODO: extract offset and limit 
			
			cm.commitTransaction();
			
			return posts;
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
	}
}
