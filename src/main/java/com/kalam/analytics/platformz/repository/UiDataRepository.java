package com.kalam.analytics.platformz.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UiDataRepository {

	@Autowired
    private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }
	
	public Object addDomain(Object domain) {
        Session session = this.sessionFactory.getCurrentSession();
        
        for (int i = 0; i < 100; i++) {
            Map<String, Object> invoice = new HashMap<>();
            invoice.put("username", "Zhang San" + i);
            invoice.put("password", "adsfwr" + i);
            invoice.put("sex", i % 2 == 0 ? "male" : "female");
            invoice.put("age", i);
            invoice.put("birthday", new Date());
            session.save("entityid-invoice", invoice);
        }
        
//        session.save(domain);
        return domain;
    }
	
	public List<Map<String, Object>> getDomainList(Object domain) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Query<Map<String, Object>> query = session.createQuery("from Student");
        List<Map<String, Object>> domainList = query.getResultList();
        
        return domainList;
    }
}
