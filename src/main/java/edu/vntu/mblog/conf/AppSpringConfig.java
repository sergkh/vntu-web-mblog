package edu.vntu.mblog.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Spring java configuration file.
 *
 * Date: 11/28/13, 9:54 PM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@Configuration
@ComponentScan({"edu.vntu.mblog.dao", "edu.vntu.mblog.services"})
@Import(TransactionsConfig.class)
public class AppSpringConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws NamingException {
        return (DataSource) new JndiTemplate().lookup("java:comp/env/jdbc/dataSource");
    }
}
