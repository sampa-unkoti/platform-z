package com.kalam.analytics.platformz.controller;

import java.io.ByteArrayInputStream;
import java.util.EnumSet;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalam.analytics.platformz.common.UnkotiApiResponse;
import com.kalam.analytics.platformz.common.UnkotiResponseFactory;

@RestController
@CrossOrigin
@RequestMapping("/v1")
public class DbSchemaController implements ApplicationContextAware {

	ApplicationContext context;
	HibernateTransactionManager transactionManager;

	@Autowired
    private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.context = ctx;
		this.transactionManager = (HibernateTransactionManager) this.context.getBean("transactionManager");
	}
	
	@RequestMapping(value = "/schema", method = RequestMethod.POST)
	public UnkotiApiResponse<?> createDomain(@RequestBody JsonNode schema) throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, Object> result = mapper.convertValue(domain, new TypeReference<Map<String, Object>>(){});
//		Object newdomain = this.uiDataService.addDomain(domain);
		String XML_MAPPING = schema.asText();
		StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        sessionFactory.getSessionFactoryOptions();
        //Read the mapping file
        metadataSources.addInputStream(new ByteArrayInputStream(XML_MAPPING.getBytes()));
        Metadata metadata = metadataSources.buildMetadata();
        //Update the database schema, create the table if it does not exist, update the field if it exists, and will not affect the existing data
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaUpdate.execute(EnumSet.of(TargetType.DATABASE), metadata, serviceRegistry);
        
		return UnkotiResponseFactory.createSuccessResponse("");

	}
}
