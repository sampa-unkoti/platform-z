package com.kalam.analytics.platformz.services;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalam.analytics.platformz.repository.UiDataRepository;

@Service
public class UiDataService {

	@Autowired
	UiDataRepository uiDataRepository;   
 
    @Transactional
    public Object addDomain(Object domain) {
    	return uiDataRepository.addDomain(domain);
    }
    
    public List<Map<String, Object>> getDomainList(Object domain) {
    	return uiDataRepository.getDomainList(domain);
    }
}
