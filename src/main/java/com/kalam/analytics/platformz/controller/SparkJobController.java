package com.kalam.analytics.platformz.controller;

import java.io.File;
import java.net.URI;
import java.util.Map;

import org.apache.livy.LivyClient;
import org.apache.livy.LivyClientBuilder;
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
import com.kalam.analytics.platformz.sparkjobs.PiJob;

@RestController
@CrossOrigin
@RequestMapping("/v1")
public class SparkJobController {

	@RequestMapping(value = "/job", method = RequestMethod.POST)
	public UnkotiApiResponse<?> submitJob(@RequestBody JsonNode domain) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(domain, new TypeReference<Map<String, Object>>(){});
		
		LivyClient client = new LivyClientBuilder().setURI(new URI("livyUrl")).build();
		final int samples = 200;
		try {
		  System.err.printf("Uploading %s to the Spark context...\n", "piJar");
		  client.uploadJar(new File("piJar")).get();

		  System.err.printf("Running PiJob with %d samples...\n", samples);
		  double pi = client.submit(new PiJob(samples)).get();

		  System.out.println("Pi is roughly: " + pi);
		} finally {
		  client.stop(true);
		}
		
		return UnkotiResponseFactory.createSuccessResponse("success");

	}
}
