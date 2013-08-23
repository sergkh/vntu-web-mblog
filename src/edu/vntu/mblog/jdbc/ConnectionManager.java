package edu.vntu.mblog.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Helper class allowing to get database connection from JNDI.
 * To create datasource one need to add following into tomcats conf/context.xml file:
 *
 * <Resource name="jdbc/dataSource" auth="Container" type="javax.sql.DataSource" driverClassName="org.h2.Driver"
 *      url="jdbc:h2:mem:test" username="dbuser" password="dbpass" maxActive="20" maxIdle="10" maxWait="-1"/>
 *
 * Date: 8/13/13, 5:37 PM
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
public class ConnectionManager {
	
	private final ThreadLocal<Connection> threadLocalConnections = new ThreadLocal<Connection>(){
		@Override
		protected Connection initialValue() {
	        try {
	            return dataSource.getConnection();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		}
	};
	
	private static final ConnectionManager instance = new ConnectionManager(); 
	
	private DataSource dataSource = null;

	private ConnectionManager() {
		try {
	    	InitialContext ic = new InitialContext();
	    	Context xmlContext = (Context) ic.lookup("java:comp/env");
	        dataSource = (DataSource) xmlContext.lookup("jdbc/dataSource");
	        initDatabase(dataSource.getConnection());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

	}
	
	public static ConnectionManager getInstance() {
		return instance;
	}
	
    public Connection currentConnection() {
    	return threadLocalConnections.get();
    }
    
    public void startTransaction() {
    	Connection c = currentConnection();
    	try {
    		c.setAutoCommit(false);
    	} catch (Exception e){
    		throw new RuntimeException(e);
    	}
    }
    
    public void commitTransaction() {
    	try {
    		Connection c = currentConnection();
    		c.commit();
    		c.close();
    		threadLocalConnections.remove();
    	} catch (Exception e){
    		throw new RuntimeException(e);
    	}
    }

    public void rollbackTransaction() {
    	try {
    		Connection c = currentConnection(); 
    		c.rollback();
    		c.close();
    		threadLocalConnections.remove();
    	} catch (Exception e){
    		throw new RuntimeException(e);
    	}
    }

    
    private static void initDatabase(Connection con) throws IOException, SQLException {
    	String script = readScript("/sql/schema.sql");
    	
        Statement st = con.createStatement();

        for(String sql : script.split(";")) {
            st.addBatch(sql);
        }

        st.executeBatch();
        st.close();

        con.commit();
        con.close();
    }
    
    private static String readScript(String location) throws IOException {
    	InputStream is = ConnectionManager.class.getResourceAsStream(location);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    	
	    String         line = null;
	    StringBuilder  sb = new StringBuilder();

	    while((line=reader.readLine()) != null) {
	    	sb.append(line);
	    }

	    return sb.toString();
    }
}

