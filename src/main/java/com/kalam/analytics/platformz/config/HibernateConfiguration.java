package com.kalam.analytics.platformz.config;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ignite.Ignite;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;
 
@Configuration
@RefreshScope
@EnableTransactionManagement
public class HibernateConfiguration {
	
	@Autowired
	Ignite igniteInstance;
	
    @Value("${db.driver}")
    private String DRIVER;
 
    @Value("${db.password}")
    private String PASSWORD;
 
    @Value("${db.url}")
    private String URL;
 
    @Value("${db.username}")
    private String USERNAME;
 
    @Value("${hibernate.dialect}")
    private String DIALECT;
 
    @Value("${hibernate.show_sql}")
    private String SHOW_SQL;
 
    @Value("${hibernate.hbm2ddl.auto}")
    private String HBM2DDL_AUTO;
 
    @Value("${entitymanager.packagesToScan}")
    private String PACKAGES_TO_SCAN;
    
    public static final String XML_MAPPING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE hibernate-mapping PUBLIC\n" +
            "        \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n" +
            "        \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n" +
            "<hibernate-mapping>\n" +
            "    <class entity-name=\"entityid-invoice\" table=\"INV_TXN\">\n" +
            "        <id name=\"id\" type=\"java.lang.Long\" length=\"64\" unsaved-value=\"null\">\n" +
            "            <generator class=\"identity\" />\n" +
            "        </id>" +
            "        <property type=\"java.lang.String\" name=\"username\" column=\"username\"/>\n" +
            "        <property name=\"password\" type=\"java.lang.String\" column=\"password\"/>\n" +
            "        <property name=\"sex\" type=\"java.lang.String\" column=\"sex\"/>\n" +
            "        <property name=\"age\" type=\"java.lang.Integer\" column=\"age\"/>\n" +
            "        <property name=\"birthday\" type=\"java.util.Date\" column=\"birthday\"/>\n" +
            "    </class>" +
            "</hibernate-mapping>";
 
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(DRIVER);
//        dataSource.setUrl(URL);
//        dataSource.setUsername(USERNAME);
//        dataSource.setPassword(PASSWORD);
//        return dataSource;
//    }
 
    @Bean
    @RefreshScope
    public DataSource dataSource() {
    	//get the db details from igniteInstance
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(100);
        ds.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        ds.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/test");
        ds.addDataSourceProperty("user", "root");
        ds.addDataSourceProperty("password", "password");
        ds.addDataSourceProperty("cachePrepStmts", true);
        ds.addDataSourceProperty("prepStmtCacheSize", 250);
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        ds.addDataSourceProperty("useServerPrepStmts", true);
        return ds;
    }
    
    @Bean
    @RefreshScope
    public SessionFactory sessionFactory() {
    	//get the hibernate configs from igniteInstance
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", DIALECT);
        hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
        hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
        hibernateProperties.put("hibernate.default_entity_mode", "dynamic-map");
        sessionFactory.setHibernateProperties(hibernateProperties);
 
        SessionFactory sessionFactory1 = sessionFactory.getObject();
        StandardServiceRegistry serviceRegistry = sessionFactory1.getSessionFactoryOptions().getServiceRegistry();
        //Merge configuration
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
        cfg.addInputStream(new ByteArrayInputStream(XML_MAPPING.getBytes()));
        SessionFactory newSessionFactory = cfg.buildSessionFactory(serviceRegistry);
        return newSessionFactory;
    }
    
//    @Bean(name = "entityManagerFactory")
//    public EntityManagerFactory entityManagerFactory() {
//    	LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//    	emf.setDataSource(dataSource());
////    	emf.setJpaVendorAdapter(jpaVendorAdapter);
//    	emf.setPackagesToScan("com.mysource.model");
//    	emf.setPersistenceUnitName("default");
//    	emf.afterPropertiesSet();
//    	return emf.getObject();
//    }
// 
//    @Bean
//	public SessionFactory getSessionFactory() {
//	    if (entityManagerFactory().unwrap(SessionFactory.class) == null) {
//	        throw new NullPointerException("factory is not a hibernate factory");
//	    }
//	    return entityManagerFactory().unwrap(SessionFactory.class);
//	}
    
    @Bean
    @RefreshScope
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory());
        return transactionManager;
    }   
    

}