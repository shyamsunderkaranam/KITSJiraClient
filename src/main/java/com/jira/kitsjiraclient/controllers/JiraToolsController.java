package com.jira.kitsjiraclient.controllers;

import com.jira.kitsjiraclient.services.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@RestController
public class JiraToolsController {

	@Autowired
	JiraSearchService jiraSearchService;

	@CrossOrigin(allowedHeaders = "Access-Control-Allow-Origin")
	@RequestMapping("/jaiganesh")
	public ResponseEntity<String> testMethod(){

		return ResponseEntity.ok("JAI GANESH");
	}

	@CrossOrigin(allowedHeaders = "Access-Control-Allow-Origin")
	@RequestMapping(value = {"/ticketsProjectWise"}, method = RequestMethod.GET)
	public ResponseEntity<List<JSONObject>> getATGEnvAvailability() {
		
		List<JSONObject> tempList=  jiraSearchService.getProjectJqlData();
		
		return ResponseEntity.ok(tempList);
	}

	

}
