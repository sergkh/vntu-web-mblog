/**
 * 
 */
package edu.vntu.mblog.services;

import static edu.vntu.mblog.util.ValidationUtils.validateLen;

import java.util.List;

import edu.vntu.mblog.dao.PostsDao;
import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.domain.Post;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.jdbc.ConnectionManager;

/**
 * 
 * @author sergey
 */
public class PostsService {
	private static final PostsService instance = new PostsService(); 

	private final PostsDao postsDao = new PostsDao();
	private final UsersDao usersDao = new UsersDao();

	private final ConnectionManager cm = ConnectionManager.getInstance();
	
	private PostsService() {}

	public static PostsService getInstance() {
		return instance;
	}
	
	public void createPost(String userLogin, String post) throws UserNotFoundException, ValidationException {
		validateLen("post", post, 3, 512);
		
		cm.startTransaction();
		try {
			User u = usersDao.getByLoginOrEmail(userLogin);
			
			if(u == null) {
				throw new UserNotFoundException(userLogin, "User not found");
			}
			
			postsDao.create(u.getId(), post); 
			cm.commitTransaction();
		} catch (Exception e) {
			cm.rollbackTransaction();
			throw e;
		}
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



    public List<Post> getAllPosts(int offset, int limit) throws ValidationException {
        if(offset < 0)
            throw new ValidationException("offset", "Offset can't be negative");

        if(limit < 0)
            throw new ValidationException("limit", "Limit can't be negative");

        cm.startTransaction();
        try {

            List<Post> posts = postsDao.getAll(offset, limit);

            cm.commitTransaction();

            return posts;
        } catch (Exception e) {
            cm.rollbackTransaction();
            throw e;
        }
    }

    public void validatePost(long id, int state) {

        cm.startTransaction();
        try {

           postsDao.validate(id, state);
           
            cm.commitTransaction();
        } catch (Exception e) {
            cm.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

}


