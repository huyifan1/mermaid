package com.uboxol.cloud.mermaid.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

import static com.uboxol.cloud.mermaid.cfg.BingXiangDataSourceConfig.JPA_PACKAGE;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/18 17:22
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = JPA_PACKAGE,
    transactionManagerRef = "bingXiangTransactionManager",
    entityManagerFactoryRef = "bingXiangEntityManagerFactory")
public class BingXiangDataSourceConfig {

    private static final String ENTITY_LOCATION = "com.uboxol.cloud.mermaid.db.entity.bx";

    static final String JPA_PACKAGE = "com.uboxol.cloud.mermaid.db.repository.bx";

    @Bean(name = "bingXiangDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.bx")
    public DataSource oracleDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "bingXiangEntityManagerFactory")
    public EntityManagerFactory bingXiangEntityManagerFactory(
        @Qualifier("bingXiangDataSource") DataSource bingXiangDataSource,JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(bingXiangDataSource);
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Map<String, String> properties = jpaProperties.getProperties();
        properties.put("hibernate.default_catalog","orange");
        factory.setJpaPropertyMap(properties);

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setGenerateDdl(false);
        factory.setJpaVendorAdapter(adapter);

        factory.setPackagesToScan(ENTITY_LOCATION);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "bingXiangTransactionManager")
    public PlatformTransactionManager uboxTransactionManager(
        @Qualifier("bingXiangEntityManagerFactory") EntityManagerFactory bingXiangEntityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(bingXiangEntityManagerFactory);
        return manager;
    }
}
