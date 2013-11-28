package edu.vntu.mblog.dao.jdbc;

import edu.vntu.mblog.dao.UsersDao;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.domain.User.Permission;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Repository
public class UsersJdbcDao extends AbstractJdbcDao implements UsersDao {
    public UsersJdbcDao() {}

    @Override
    public void create(User user) {
        String sql = "INSERT into USERS (login, email, passhash) VALUES (?,?,?)";

        jdbc().update(sql, user.getLogin(), user.getEmail(), user.getPassHash());

        long id = jdbc().queryForObject("CALL IDENTITY()", Long.class);
        user.setId(id);
    }

    @Override
    public void addPermission(long userId, User.Permission role) {
        String sql = "INSERT INTO user_permissions VALUES (?, SELECT id FROM permissions WHERE name=?)";
        jdbc().update(sql, userId, role.name());
    }

    @Override
    public void clearPermission(long userId, User.Permission role) {
        String sql = "DELETE FROM user_permissions WHERE user_id = ? AND " +
                "perm_id = (SELECT id FROM permissions WHERE name=?)";

        jdbc().update(sql, userId, role.name());
    }

    @Override
    public User getByLoginOrEmail(String identifier) {
        String sql = "SELECT * FROM users WHERE login=? OR email=?";

        User user = jdbc().queryForObject(sql, mapper, identifier, identifier);

        return loadRoles(user);
    }

    @Override
    public List<User> getAllUsers(int offset, int limit) {
        String sql = "SELECT * FROM users ORDER BY created DESC LIMIT ? OFFSET ?";

        List<User> users = jdbc().query(sql, mapper, limit, offset);

        for(User u: users) loadRoles(u);

        return users;
    }

    @Override
    public void setAvatar(long userId, String fileName) {
        String sql = "UPDATE users SET avatar=? WHERE id=?";
        jdbc().update(sql, fileName, userId);
    }

    @Override
    public void toggleUserBlock(long userId, boolean block) {
        String sql = block ? "UPDATE users SET blocked=NOW() WHERE id=?" :
                             "UPDATE users SET blocked=null WHERE id=?";

        jdbc().update(sql, userId);
    }

    private User loadRoles(User u) {
        String sql = "SELECT name FROM permissions WHERE id IN (SELECT perm_id FROM user_permissions WHERE user_id=?)";
        List<String> permString = jdbc().queryForList(sql, String.class, u.getId());

        List<User.Permission> perm = new ArrayList<>();

        for(String p : permString) {
            perm.add(Permission.valueOf(p));
        }

        u.setPermissions(EnumSet.copyOf(perm));

        return u;
    }

    private final RowMapper<User> mapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getTimestamp("created"),
                    rs.getString("login"),
                    rs.getString("email"),
                    rs.getString("passhash"),
                    rs.getTimestamp("blocked"),
                    rs.getString("avatar")
            );
        }
    };

}
