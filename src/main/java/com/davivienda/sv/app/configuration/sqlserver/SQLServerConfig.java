package com.davivienda.sv.app.configuration.sqlserver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.davivienda.sv.app.repositories.sqlserver",
    entityManagerFactoryRef = "sqlServerEntityManagerFactory",
    transactionManagerRef = "sqlServerTransactionManager"
)
public class SQLServerConfig {

    private final String databasePlatform = "org.hibernate.dialect.SQLServer2012Dialect";

    /**
     * DataSource usando JNDI para WildFly
     */
    @Bean(name = "sqlServerDataSource")
    public DataSource sqlServerDataSource() throws NamingException {
        JndiTemplate jndiTemplate = new JndiTemplate();
        return (DataSource) jndiTemplate.lookup("java:jboss/BEPDataSourceDREF");
    }

    @Bean(name = "sqlServerJdbcTemplate")
    public JdbcTemplate sqlServerJdbcTemplate(
        @Qualifier("sqlServerDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "sqlServerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqlServerEntityManagerFactory(
        @Qualifier("sqlServerDataSource") DataSource dataSource,
        @Qualifier("sqlServerHibernateProperties") Map<String, Object> hibernateProps
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.davivienda.sv.app.entities.sqlserver");
        em.setPersistenceUnitName("sqlserver-pu");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(databasePlatform);
        vendorAdapter.setShowSql(false);

        hibernateProps.put("hibernate.dialect", databasePlatform);
        hibernateProps.put("hibernate.hbm2ddl.auto", "none");
        hibernateProps.put("hibernate.show_sql", "false");
        hibernateProps.put("hibernate.format_sql", "true");

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(hibernateProps);
        return em;
    }

    @Bean(name = "sqlServerTransactionManager")
    public JpaTransactionManager sqlServerTransactionManager(
        @Qualifier("sqlServerEntityManagerFactory") LocalContainerEntityManagerFactoryBean sqlServerEntityManagerFactory
    ) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(sqlServerEntityManagerFactory.getObject());
        return tm;
    }

    @Bean(name = "sqlServerHibernateProperties")
    public Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", databasePlatform);
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        return properties;
    }
}