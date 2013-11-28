package edu.vntu.mblog.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Jdbc template and transactions management configuration file.
 *
 * Date: 11/28/13, 11:17 PM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class TransactionsConfig {

    @Autowired private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate template() {
        return new JdbcTemplate(dataSource);
    }
}
