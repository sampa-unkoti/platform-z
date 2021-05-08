package com.kalam.analytics.platformz.config;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.IgnitionEx;
import org.apache.ignite.internal.processors.resource.GridSpringResourceContextImpl;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Slf4j
@Configuration
public class IgniteDbConfiguration implements WebMvcConfigurer {
	private static final Logger log = LoggerFactory.getLogger(IgniteConfiguration.class);

	@Bean
	public Ignite igniteInstance() {
		TcpDiscoverySpi spi = new TcpDiscoverySpi().setLocalPort(47505);;
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500", "127.0.0.1:47501","127.0.0.1:47505"));
		spi.setIpFinder(ipFinder);
		TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
		commSpi.setLocalPort(47105);
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);
		DataStorageConfiguration storageCfg = new DataStorageConfiguration();
//		// Enabling the persistence.
		storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
		storageCfg.getDefaultDataRegionConfiguration().setMaxSize(1L * 1024 * 1024 * 1024);
		cfg.setDataStorageConfiguration(storageCfg);
		
		CacheConfiguration[] cacheConfigs = new CacheConfiguration[29];
//		CacheConfiguration<String, StockQuoteOhlc> cacheCfg = null;
//		
//		CacheConfiguration<String, User> userCacheConfig = new CacheConfiguration<>("UserCache");
//		userCacheConfig.setIndexedTypes(String.class, User.class);
//		userCacheConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
//		userCacheConfig.setBackups(1);
//		userCacheConfig.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
//
//		CacheConfiguration<String, BrokerSett> brokerSettCacheConfig = new CacheConfiguration<>("BrokerSettCache");
//		brokerSettCacheConfig.setIndexedTypes(String.class, BrokerSett.class);
//		brokerSettCacheConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
//		brokerSettCacheConfig.setBackups(1);
//		brokerSettCacheConfig.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
//
//		CacheConfiguration<String, TradeSett> tradeSettCacheConfig = new CacheConfiguration<>("TradeSettCache");
//		tradeSettCacheConfig.setIndexedTypes(String.class, TradeSett.class);
//		tradeSettCacheConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
//		tradeSettCacheConfig.setBackups(1);
//		tradeSettCacheConfig.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
//
//		cacheConfigs[0] = userCacheConfig;
//		cacheConfigs[1] = brokerSettCacheConfig;
//		cacheConfigs[2] = tradeSettCacheConfig;

		
		cfg.setDiscoverySpi(spi);
		cfg.setCommunicationSpi(commSpi);
		
		cfg.setCacheConfiguration(cacheConfigs);
		Ignite ignite = null;
		try {
			log.info("Ignite instance starting...");
			ignite = Ignition.start(cfg);
			ignite.active(true);
			log.info("Ignite instance started");
		} catch (IgniteException e) {
			log.error("Ignite instance not started",e);
		} 
		
		return ignite;
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

}