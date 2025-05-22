package com.ning.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ning.repository",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager"
)
@EnableTransactionManagement
public class JpaDataSourceConfig {
    @Bean(name = "jpaDataSource")
    public DataSource secondaryDataSource() {
        HikariConfig a = new HikariConfig();
        a.setDriverClassName("com.mysql.jdbc.Driver");
        a.setJdbcUrl("jdbc:mysql://localhost:3306/db?zeroDateTimeBehavior=ConvertToNull&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true");
        a.setUsername("root");
        a.setPassword("123456");
        return new HikariDataSource(a);
    }

    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("jpaDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ning.entity")
                .persistenceUnit("secondaryPU")
                .build();
    }

    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("jpaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
