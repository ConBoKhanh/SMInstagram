package com.example.sminstagram.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.example.sminstagram.repos.supabase",
        entityManagerFactoryRef = "supabaseEntityManagerFactory",
        transactionManagerRef = "supabaseTransactionManager"
)
public class SupabaseDataSourceConfig {

    @Bean(name = "supabaseDataSource")
    @ConfigurationProperties(prefix = "supabase.datasource")
    public DataSource supabaseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "supabaseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean supabaseEntityManagerFactory(
            @Qualifier("supabaseDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.sminstagram.entities.supabase");
        em.setPersistenceUnitName("supabase");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.show_sql", "true");
        em.setJpaProperties(props);

        return em;
    }

    @Bean(name = "supabaseTransactionManager")
    public PlatformTransactionManager supabaseTransactionManager(
            @Qualifier("supabaseEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
