package com.jira.kitsjiraclient.services;



import java.net.URI;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jira.kitsjiraclient.config.ConfigDAO;
import com.atlassian.httpclient.api.Request.Builder;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JiraSearchService {
	
	@Autowired
	ConfigDAO configdao;
	private final String CONFIG_FILE_PATH="configs/configs.json";

	Logger logger = LoggerFactory.getLogger(JiraSearchService.class);

	public JiraSearchService() {
		// TODO Auto-generated constructor stub
	}

	//public static void main(String[] args) {
	public List<JSONObject> getProjectJqlData() {
		// TODO Auto-generated method stub

		URI jiraServerUri = null;
		JiraRestClient client = null;
		String jiraUrl = "https://demositetemporary.atlassian.net" ;
		String username="Testatlassian1@mailinator.com";
		JSONObject resultOfQuery = new JSONObject();
		List<JSONObject> finalResult = new ArrayList<>();
		//String username="testatlassian1";
		//String pwd="Test12345";
		String pwd="K3DnHn970X94P4hBNZ3NCBA3";
		String jql="project=\"FIRST\"";
		try{
			jiraServerUri = new URI(jiraUrl);
			JSONObject allConfigs = configdao.getJSONData(CONFIG_FILE_PATH);

			logger.info("Fetching configuration details");
	        if(allConfigs != null){

	            //logger.info("Found configurations");
	            if(allConfigs.get("jira_url")!=null) {
	                //logger.info("products Configures are "+allConfigs.get("SITBQUKProductsToFix"));
	            	jiraUrl = allConfigs.get("jira_url").toString();
	                
	            }
	            if(allConfigs.get("jira_user")!=null) {
	                //logger.info("products Configures are "+allConfigs.get("SITBQUKProductsToFix"));
	            	username = allConfigs.get("jira_user").toString();
	                
	            }
	            if(allConfigs.get("dont_tell")!=null) {
	                //logger.info("products Configures are "+allConfigs.get("SITBQUKProductsToFix"));
	            	pwd = allConfigs.get("dont_tell").toString();
	                
	            }
	            if(allConfigs.get("jqlForProjects")!=null) {
	                //logger.info("products Configures are "+allConfigs.get("SITBQUKProductsToFix"));
	            	jql = allConfigs.get("jqlForProjects").toString();
	            	//jql = jql.replace("doublequote", "\"");
	            	logger.info("JQL is: "+jql);
	                
	            }
	        }
			//AuthenticationHandler auth = new BasicHttpAuthenticationHandler(username,pwd);
			logger.info("Connection to Jira");
			try{
				BearerHttpAuthenticationHandler auth = new BearerHttpAuthenticationHandler(pwd);
				JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
				//client = factory.createWithBasicHttpAuthentication(jiraServerUri, username,pwd);
				client = factory.create(jiraServerUri,auth );
				logger.info("Client connection obtained");
			}
			catch(Exception e){
				logger.error("Error connecting to Jira");
				e.printStackTrace();
			}
			/*IssueRestClient issueClient = client.getIssueClient();
			for (int i=0; i< 1004; i++) {
			IssueInput newIssue = new IssueInputBuilder(
				      "FIRST",(long)10001, "This is issue no "+i).build();
			issueClient.createIssue(newIssue).claim();
			}*/
			SearchRestClient searchClient = null;
			try{
				searchClient = client.getSearchClient();
				logger.info("Search client obtained");
			}
			catch(Exception e){
				logger.error("Error fetching jira search client");
				e.printStackTrace();
			}
			
			
			SearchResult result=null;
			try{
				result = searchClient.searchJql(jql).claim();
				//result = searchClient.searchJql(jql, 2000, 0, null).claim();
				logger.info("Max result: "+result.getMaxResults() + " And Total is "+result.getTotal());
			}

			catch(Exception e){
				logger.error("Error fetching jira data or executing JQL");
				e.printStackTrace();
			}
			
			
			try{
				
				if(result != null){
					logger.info("Mapping results");
					Iterable<Issue> results = result.getIssues();
					for(Issue issue : results){
						System.out.println(issue.getKey() +" : " +issue.getSummary());
						resultOfQuery.put("key", issue.getKey());
						resultOfQuery.put("summary", issue.getSummary());
						resultOfQuery.put("creationDate", issue.getCreationDate().toDateTime(DateTimeZone.forID("Europe/London")).toString().substring(0, 19).replace("T", " "));
						
						resultOfQuery.put("status", issue.getStatus().getStatusCategory().getKey());
						resultOfQuery.put("updated", issue.getUpdateDate().toDateTime(DateTimeZone.forID("Europe/London")).toString().substring(0, 19).replace("T", " "));
						String projectTeam = issue.getFieldByName("Project Team").getValue()!=null?issue.getFieldByName("Project Team").getValue().toString():"Not provided";
						resultOfQuery.put("ProjectTeam",projectTeam);
						resultOfQuery.put("resolved", issue.getFieldByName("Resolved").getValue()!=null?issue.getFieldByName("Resolved").getValue().toString():"9999-12-31T23:59:59.000+0530");
						String tier = issue.getFieldByName("Tier").getValue()!=null?issue.getFieldByName("Tier").getValue().toString():"Not provided";
						resultOfQuery.put("Tier",tier);
						finalResult.add(resultOfQuery);
					}
				} else{

					logger.error("Error in mapping Jira results as there are no results");

				}
			}
			catch(Exception e){
				logger.error("Error in mapping Jira results");
				e.printStackTrace();
			}
			
			//return result.getIssues();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return finalResult;
		}
	}
//https://community.atlassian.com/t5/Jira-questions/How-do-I-use-a-personal-access-token-PAT-in-jira-rest-java/qaq-p/1859167

class BearerHttpAuthenticationHandler implements AuthenticationHandler {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final String token;

	public BearerHttpAuthenticationHandler(final String token) {
	this.token = token;
	}


	@Override
	public void configure(Builder builder) {
		builder.setHeader(AUTHORIZATION_HEADER, "Bearer " + token);
	}
}

