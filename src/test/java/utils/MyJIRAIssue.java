package utils;

import org.testng.ITestResult;

public class MyJIRAIssue {
	
	public String browser;
	public String version;
	public String os;
	public String method;
	public String sessionId;
	ITestResult result;
	
    /******************************************************************************************************/
    
    public MyJIRAIssue(String browser, String version, String os, String method, String sessionId) {
    	
    	this.browser = browser;
    	this.version = version;
    	this.os = os;
    	this.method = method;
    	this.sessionId = sessionId;
    	
    }
    
	/******************************************************************************************************/
    
    public void setTestResult(ITestResult result) {
    	this.result = result;
    }

    /******************************************************************************************************/
    
    public String getIssueSummary() {
    	return "Failed in " + method + " running on " + browser + " " + version + " on " + os + ".\n";
    }
	
	/******************************************************************************************************/
    
    public String getIssueDescription() {
    	
    	String ret = "Details:\n" + result.getThrowable().getMessage();
    	ret += "\nhttps://app.saucelabs.com/tests/" + sessionId + "\n";
    	
    	return ret;
    }
	
}
