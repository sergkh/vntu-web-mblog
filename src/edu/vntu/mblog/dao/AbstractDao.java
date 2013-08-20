package edu.vntu.mblog.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.vntu.mblog.jdbc.ConnectionFactory;

/**
 * @author sergey
 */
public class AbstractDao {
	
	protected Connection getConnection() {
		return ConnectionFactory.createConnection();
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
}
