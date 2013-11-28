package edu.vntu.mblog.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author sergey
 */
public class AbstractJdbcDao {

    @Autowired
    private JdbcTemplate jdbcTpl;

    public JdbcTemplate jdbc() {
        return jdbcTpl;
    }

    public void setJdbcTpl(JdbcTemplate jdbcTpl) {
        this.jdbcTpl = jdbcTpl;
    }
}
