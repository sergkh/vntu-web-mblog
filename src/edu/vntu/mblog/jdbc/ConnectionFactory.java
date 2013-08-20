package edu.vntu.mblog.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
public class ConnectionFactory {
	private static DataSource dataSource = null;
	
    public static synchronized Connection createConnection() {
        try {
        	if(dataSource == null) {
            	InitialContext ic = new InitialContext();
            	Context xmlContext = (Context) ic.lookup("java:comp/env");
                dataSource = (DataSource) xmlContext.lookup("jdbc/dataSource");
                initDatabase(dataSource.getConnection());
        	}

            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public static void initDatabase(Connection con) throws IOException, SQLException {
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
    	InputStream is = ConnectionFactory.class.getResourceAsStream(location);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    	
	    String         line = null;
	    StringBuilder  sb = new StringBuilder();

	    while((line=reader.readLine()) != null) {
	    	sb.append(line);
	    }

	    return sb.toString();
    }
}

