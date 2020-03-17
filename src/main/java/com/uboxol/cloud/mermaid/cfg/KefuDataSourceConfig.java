package com.uboxol.cloud.mermaid.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.Map;

import static com.uboxol.cloud.mermaid.cfg.KefuDataSourceConfig.JPA_PACKAGE;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/18 17:22
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = JPA_PACKAGE)
public class KefuDataSourceConfig {

    private static final String ENTITY_LOCATION = "com.uboxol.cloud.mermaid.db.entity.kefu";

    static final String JPA_PACKAGE = "com.uboxol.cloud.mermaid.db.repository.kefu";

    @Primary
    @Bean(name = {"kefuDataSource", "datasource"})
    @ConfigurationProperties(prefix = "spring.datasource.kf")
    public DataSource oracleDataSource() {
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = {"kefuEntityManagerFactory", "entityManagerFactory"})
    public EntityManagerFactory kefuEntityManagerFactory(JpaProperties jpaProperties,
                                                         @Qualifier("kefuDataSource") DataSource kefuDataSource) {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(kefuDataSource);
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Map<String, String> properties = jpaProperties.getProperties();
        properties.put("hibernate.default_catalog","ubox_service");
        factory.setJpaPropertyMap(properties);
        adapter.setShowSql(jpaProperties.isShowSql());
        factory.setJpaVendorAdapter(adapter);
        factory.setPackagesToScan(ENTITY_LOCATION);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public EntityManager entityManager(@Qualifier("entityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }

    @Primary
    @Bean(name = {"kefuTransactionManager", "transactionManager"})
    public PlatformTransactionManager transactionManager(
        @Qualifier("kefuEntityManagerFactory") EntityManagerFactory kefuEntityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(kefuEntityManagerFactory);
        return manager;
    }
}
