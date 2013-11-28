/**
 *
 */
package edu.vntu.mblog.services.impl;

import static edu.vntu.mblog.util.ValidationUtils.validateLen;

import java.util.List;

import edu.vntu.mblog.dao.PostsDao;
import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.dao.jdbc.PostsJdbcDao;
import edu.vntu.mblog.dao.jdbc.UsersJdbcDao;
import edu.vntu.mblog.domain.Post;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sergey
 */
@Service
public class PostsServiceImpl implements PostsService {

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private UsersDao usersDao;

    @Override
    @Transactional(readOnly =  false)
    public void createPost(String userLogin, String post) throws UserNotFoundException, ValidationException {
        validateLen("post", post, 3, 512);

        User u = usersDao.getByLoginOrEmail(userLogin);

        if(u == null) {
            throw new UserNotFoundException(userLogin, "User not found");
        }

        postsDao.create(u.getId(), post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getUsersFeed(String userLoginOrEmail) throws UserNotFoundException {
        User u = usersDao.getByLoginOrEmail(userLoginOrEmail);

        if(u == null) {
            throw new UserNotFoundException(userLoginOrEmail, "User not found");
        }

        return postsDao.getFeed(u.getId(), 0, 1000); // TODO: extract offset and limit
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(int offset, int limit) throws ValidationException {
        if(offset < 0)
            throw new ValidationException("offset", "Offset can't be negative");

        if(limit < 0)
            throw new ValidationException("limit", "Limit can't be negative");

        return postsDao.getAll(offset, limit);
    }

    @Override
    @Transactional(readOnly = false)
    public void moderatePost(long id, Post.State state) {
        postsDao.moderate(id, state);
    }

    @Override
    public void setPostsDao(PostsJdbcDao postsDao) {
        this.postsDao = postsDao;
    }

    @Override
    public void setUsersDao(UsersJdbcDao usersDao) {
        this.usersDao = usersDao;
    }
}


