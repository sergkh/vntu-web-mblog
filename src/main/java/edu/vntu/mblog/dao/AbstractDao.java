package edu.vntu.mblog.dao;

import java.sql.Connection;

import edu.vntu.mblog.jdbc.ConnectionManager;

/**
 * @author sergey
 */
public class AbstractDao {

    private ConnectionManager connectionManager;

    protected Connection getConnection() {
        return connectionManager.currentConnection();
    }

    protected void close(AutoCloseable... closeables) {
        for(AutoCloseable ac : closeables) {
            close(ac);
        }
    }

    protected void close(AutoCloseable c) {
        if(c == null) return ;
        try {
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
