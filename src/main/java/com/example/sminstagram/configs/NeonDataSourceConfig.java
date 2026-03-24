package com.example.sminstagram.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.sminstagram.repos.neon",
        entityManagerFactoryRef = "neonEntityManagerFactory",
        transactionManagerRef = "neonTransactionManager"
)
public class NeonDataSourceConfig {

    @Primary
    @Bean(name = "neonDataSource")
    @ConfigurationProperties(prefix = "neon.datasource")
    public DataSource neonDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "neonEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean neonEntityManagerFactory(
            @Qualifier("neonDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.sminstagram.entities.neon");
        em.setPersistenceUnitName("neon");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.show_sql", "true");
        em.setJpaProperties(props);

        return em;
    }

    @Primary
    @Bean(name = "neonTransactionManager")
    public PlatformTransactionManager neonTransactionManager(
            @Qualifier("neonEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}