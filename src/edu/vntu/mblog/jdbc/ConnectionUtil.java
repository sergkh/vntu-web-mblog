package edu.vntu.mblog.jdbc;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

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
public class ConnectionUtil {

    private Connection getConnection() {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/dataSource");
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

