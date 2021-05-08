package com.kalam.analytics.platformz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalam.analytics.platformz.common.UnkotiApiResponse;
import com.kalam.analytics.platformz.common.UnkotiResponseFactory;
import com.kalam.analytics.platformz.services.UiDataService;

@RestController
@CrossOrigin
@RequestMapping("/v1")
public class UiDataController {

	@Autowired
	UiDataService uiDataService;
	
	@RequestMapping(value = "/domain", method = RequestMethod.POST)
	public UnkotiApiResponse<?> createDomain(@RequestBody JsonNode domain) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(domain, new TypeReference<Map<String, Object>>(){});
		Object newdomain = this.uiDataService.addDomain(domain);
		return UnkotiResponseFactory.createSuccessResponse(newdomain);

	}
	
	@RequestMapping(value = "/domain/query/list", method = RequestMethod.POST)
	public UnkotiApiResponse<?> getDomainList(@RequestBody JsonNode domain) throws Exception {
		List<Map<String, Object>> domainList = this.uiDataService.getDomainList(domain);
		if (domainList != null && !domainList.isEmpty()) {
			return UnkotiResponseFactory.createSuccessResponse(domainList);
		} else {
			return UnkotiResponseFactory.createFailureResponse("The is no conditions for the user");
		}
	}
	
}

{
	name : {
	    value : Biplab,
	    column : NAME,
	    type : string
	    
		}
	location : {
	    value : Pune,
	    column : Location,
	    type : string
	    
		}
	state: {
	    value : Maharashtra,
	    column : state,
	    type : string
	    
		}
}


{
	columns: [name, location, state]
	data:[Biplab,Pune, Maharashtra]
	type: [string, string, string]
	domain: ADDRESS
}