package edu.vntu.mblog.jdbc;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

/**
 * Helper class allowing to get database connection from JNDI.
 * To create data source one need to add following into tomcats conf/context.xml file:
 *
 * <Resource name="jdbc/dataSource" auth="Container" type="javax.sql.DataSource" driverClassName="org.h2.Driver"
 *      url="jdbc:h2:mem:test" username="dbuser" password="dbpass" maxActive="20" maxIdle="10" maxWait="-1"/>
 *
 * Date: 8/13/13, 5:37 PM
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
public class ConnectionManager {
	private static final ConnectionManager instance = new ConnectionManager(); 
	private final DataSource dataSource;
	
	private final ThreadLocal<Connection> threadLocalConnections = new ThreadLocal<Connection>(){
		@Override
		protected Connection initialValue() {
	        try {
                Connection c = dataSource.getConnection();
                c.setAutoCommit(false);
	            return c;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		}
	};

	private ConnectionManager() {
		try {
	    	InitialContext ic = new InitialContext();
	    	Context xmlContext = (Context) ic.lookup("java:comp/env");
	        dataSource = (DataSource) xmlContext.lookup("jdbc/dataSource");
        } catch (NameNotFoundException nfe) {
            throw new RuntimeException("Can't obtain dataSource from context. " +
                    "Have you added data source to context.xml file? " +
                    "Read javadoc on ConnectionManager.java file.", nfe);
        } catch (Exception e) {
            throw new RuntimeException("Can't obtain dataSource from context", e);
        }

	}
	
	public static ConnectionManager getInstance() {
		return instance;
	}
	
    public Connection currentConnection() {
    	return threadLocalConnections.get();
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
}

