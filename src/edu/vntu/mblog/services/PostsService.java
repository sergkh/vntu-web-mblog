/**
 * 
 */
package edu.vntu.mblog.services;

import edu.vntu.mblog.dao.UserPostsDao;

/**
 * @author sergey
 *
 */
public class PostsService {
	private static final PostsService instance = new PostsService(); 
	
	private final UserPostsDao postsDao = new UserPostsDao();
	
	private PostsService() {}

	public static PostsService getInstance() {
		return instance;
	}
}
