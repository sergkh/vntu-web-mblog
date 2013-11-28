package edu.vntu.mblog.dao.jdbc;

import edu.vntu.mblog.dao.PostsDao;
import edu.vntu.mblog.domain.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PostsJdbcDao extends AbstractJdbcDao implements PostsDao {
    public PostsJdbcDao() {}

    @Override
    public void create(long authorId, String text) {
        jdbc().update("insert into posts (owner_id, text) VALUES (?,?)", authorId, text);
    }

    @Override
    public void moderate(long postId, Post.State state) {
        jdbc().update("UPDATE posts SET post_validation_date=NOW(), state=? where id=?",
                state.ordinal(),
                postId);
    }

    @Override
    public List<Post> getAll(int offset, int limit) {
        String sql = "SELECT users.login AS login, users.avatar AS avatar, posts.* FROM users, posts "
                + "WHERE users.id = posts.owner_id AND posts.state = ? "
                + "ORDER BY posts.stamp DESC LIMIT ? OFFSET ?";

        return jdbc().query(sql, mapper, Post.State.UNVALIDATED.ordinal(), limit, offset);
    }

    @Override
    public int getCountForUser(long userId) {
        String sql = "SELECT COUNT(*) FROM posts WHERE posts.owner_id = ? AND posts.state != ?";
        return jdbc().queryForObject(sql, Integer.class, userId, Post.State.DISABLED.ordinal());
    }

    @Override
    public List<Post> getFeed(long userId, int offset, int limit) {
        String sql = "SELECT users.avatar, users.login AS login, posts.* FROM users, posts "
                + "WHERE users.id = posts.owner_id AND ("
                + "users.id IN (SELECT followed_id FROM user_followers WHERE subscriber_id=?) "
                + "OR users.id = ?)  AND posts.state != ? "
                + "ORDER BY posts.stamp DESC LIMIT ? OFFSET ?";

        return jdbc().query(sql, mapper, userId, userId, Post.State.DISABLED.ordinal(), limit, offset);
    }

    private final RowMapper<Post> mapper = new RowMapper<Post>() {
        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            return new Post(
                    rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("avatar"),
                    rs.getString("text"),
                    rs.getTimestamp("stamp")
            );
        }
    };

}
