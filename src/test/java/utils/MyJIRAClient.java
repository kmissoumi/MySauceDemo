package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJIRAClient {
	
	static final String PROJECT_ID = "10000", ISSUE_TYPE_ID = "10001", SUMMARY = "Summary", DESCRIPTION = "Description";

    /******************************************************************************************************/
    
    public static void main(String[] args) throws Exception {
    	
    	if(args.length > 0) {
            processCommand(args[0], args.length == 1 ? null : args[1]);
    	}

    }
    
	/******************************************************************************************************/
    
    private static void processCommand(String command, String params) {

    	String url = "https://xxxxx.atlassian.net/rest/api/latest";
    	JSONObject payload = null;
    	
    	if(command.equals("GetIssue")) {
    		url += "/issue/" + params;
    	} else if(command.equals("CreateIssue")) {
    		url += "/issue";
    		payload = getCreateIssuePayload(PROJECT_ID, ISSUE_TYPE_ID, SUMMARY, DESCRIPTION);
    	} else if(command.equals("GetProject")) {
    		url += "/project/" + params;
    	} else if(command.equals("GetAllProjects")) {
    		url += "/project/search";
    	} else if(command.equals("GetAllPermissions")) {
    		url += "/permissions";
    	} else if(command.equals("GetMyPermissions")) {
    		url += "/mypermissions?permissions=CREATE_ISSUES%2CCREATE_PROJECT";
    	} else {
    		url = null;
    	}

    	if(url != null) {
            System.out.println(url);
            if(payload == null) {
                sendHttpGet(url);
            } else {
                sendHttpPost(url, payload);
            }
    		
    	}
	
    }
    
	/******************************************************************************************************/
    
    public static void createIssue(String projectId, String issueTypeId, String summary, String description) {

    	String url = "https://etiennetest.atlassian.net/rest/api/latest/issue";
    	JSONObject payload = getCreateIssuePayload(projectId, issueTypeId, summary, description);
        sendHttpPost(url, payload);
	
    }
    
	/******************************************************************************************************/
    
    private static JSONObject getCreateIssuePayload(String projectId, String issueTypeId, String summary, String description) {

    	JSONObject ret = new JSONObject();

        try {

        	JSONObject fieldsData = new JSONObject();

        	JSONObject projectData = new JSONObject();
            projectData.put("id", projectId);
	    	fieldsData.put("project", projectData);
	        
	    	fieldsData.put("summary", summary);
	    	fieldsData.put("description", description);

	    	JSONObject issueTypeData = new JSONObject();
        	issueTypeData.put("id", issueTypeId);
	    	fieldsData.put("issuetype", issueTypeData);
	        
	        ret.put("fields", fieldsData);
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(ret.toString());
    	
		return ret;
		
	}

	/******************************************************************************************************/
    
	public static String sendHttpPost(String url, JSONObject payload) {
		
    	System.out.println("sendHttpPost(): url=" + url);

		String ret = "";
		
		
    	HttpClient httpclient = HttpClients.createDefault();
    	
    	HttpPost post = new HttpPost(url);
    	post.addHeader("Accept" , "application/json");
    	post.addHeader("Content-Type" , "application/json");
    	post.addHeader("Authorization" , "Basic xxxxxxxxxxxxxxx");

	    try {
    		
	    	StringEntity payloadEntity = new StringEntity(payload.toString());
		    post.setEntity(payloadEntity);
		    
	    	HttpResponse response = httpclient.execute(post);

       	 	int code = response.getStatusLine().getStatusCode();
        	System.out.println("status=" + code);

	    	HttpEntity entity = response.getEntity();

	    	if (entity != null) {
	    	    InputStream instream = entity.getContent();
    	    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(instream), 1);
	    	    try {
	                String line;
	                while ((line = bufferedReader.readLine()) != null) {
	                	ret += line;
	                }
	                instream.close();
	                bufferedReader.close();	    	    
	            } finally {
	    	        instream.close();
	                bufferedReader.close();	    	    
	    	    }
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	if(ret.length() == 0) {
    		ret = "I'm sorry, there was an error processing your request";
    	}

    	System.out.println("sendHttpPost(): ret=" + ret);
    	
    	return ret;
    	
	}
	
	/******************************************************************************************************/
    
	public static String sendHttpGet(String url) {
		
    	System.out.println("sendHttpGet(): url=" + url);

		String ret = "";
		
    	HttpClient httpclient = HttpClients.createDefault();
    	
    	HttpGet httpGet = new HttpGet(url);
    	httpGet.addHeader("Accept" , "application/json");
    	httpGet.addHeader("Content-Type" , "application/json");

        try {
    		
       	 	UsernamePasswordCredentials creds = new UsernamePasswordCredentials("email", "key");
       	 	httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet));

       	 	HttpResponse response = httpclient.execute(httpGet);
       	 	
       	 	int code = response.getStatusLine().getStatusCode();
        	System.out.println("status=" + code);
       	 	
	    	HttpEntity entity = response.getEntity();

	    	if (entity != null) {
	    	    InputStream instream = entity.getContent();
    	    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(instream), 1);
	    	    try {
	                String line;
	                while ((line = bufferedReader.readLine()) != null) {
	                	ret += line;
	                }
	                instream.close();
	                bufferedReader.close();	    	    
	            } finally {
	    	        instream.close();
	                bufferedReader.close();	    	    
	    	    }
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	if(ret.length() == 0) {
    		ret = "I'm sorry, there was an error processing your request";
    	}

    	System.out.println("sendHttpGet(): ret=" + ret);
    	
    	return ret;
    	
	}
	
    /******************************************************************************************************/
    
    private void parseResponse(HttpResponse response) throws IOException {
    	
        Scanner s = new Scanner(((HttpEntity) response).getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println(jsonObj.toString(2));
        } catch (Exception e) {
            System.out.println(result);
        }
        
    }
    
}
