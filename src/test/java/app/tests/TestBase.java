package app.tests;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

import com.saucelabs.common.SauceOnDemandAuthentication;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import utils.MyJIRAClient;
import utils.MyTestConfig;

public class TestBase  {
	
	/*********************************************************************/

    public static boolean IS_FAIL = false; 						// Fail IE tests for Failure Analysis 

	/**************************************************************************************************/

    public String seleniumURI = "@ondemand.us-west-1.saucelabs.com:443";
    
    public String buildTag = System.getenv("BUILD_TAG");
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public String app = "storage:filename=Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";

    /**************************************************************************************************/

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**************************************************************************************************/

	public String SAUCE_URL = "https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub";
	
	/*********************************************************************/

    @DataProvider(name = "hardCodedPhones", parallel = true)
    public static Object[][] sauceBrowserDataProvider(Method testMethod) {
    	
        return new Object[][]{
            new Object[]{ANDROID, "Google Pixel .*"},
            new Object[]{IOS, "iPhone .*"}
        };
        
    }

    /**************************************************************************************************/

	public static final int ANDROID=0, IOS=1;
	
	public static final String PLATFORM_NAMES[] = {"android", "ios"};
	
	static final String JIRA_PROJECT_ID = "10000", JIRA_ISSUE_TYPE_ID = "10001";
	
	/*********************************************************************/

	private ThreadLocal<AppiumDriver<MobileElement>> webDriver = new ThreadLocal<AppiumDriver<MobileElement>>();

    private ThreadLocal<String> sessionId = new ThreadLocal<String>();

    private ThreadLocal<MyTestConfig> config = new ThreadLocal<MyTestConfig>();

	private List<MyTestConfig> failedTests = new ArrayList<MyTestConfig>();
	
	public AppiumDriver<MobileElement> driver;

	/*********************************************************************/

    public AppiumDriver<MobileElement> getWebDriver() {
        return webDriver.get();
    }

	/*********************************************************************/
    
    public String getSessionId() {
        return sessionId.get();
    }

	/*********************************************************************/

    protected void createDriver(String[] appNames, String deviceName, int platformId, String name, String build) throws MalformedURLException, UnexpectedException {
    	
        MutableCapabilities capabilities = new MutableCapabilities();
        
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("deviceOrientation", "portrait");
        capabilities.setCapability("newCommandTimeout", "240");
        capabilities.setCapability("app", appNames[platformId]);
        capabilities.setCapability("platformName", PLATFORM_NAMES[platformId]);
        capabilities.setCapability("name", name);
        capabilities.setCapability("build", build);
        
        capabilities.setCapability("browserName", "");
        
        if(platformId == ANDROID) {
        	webDriver.set(new AndroidDriver<MobileElement>(new URL(SAUCE_URL),capabilities));        	
        } else if(platformId == IOS) {
            capabilities.setCapability("app", appNames[1]);
        	webDriver.set(new IOSDriver<MobileElement>(new URL(SAUCE_URL),capabilities));        	
        }
        
        String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
        sessionId.set(id);
        config.set(new MyTestConfig(deviceName, "", PLATFORM_NAMES[platformId], name, id));
        
    }


	/*********************************************************************/

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {
    	
    	WebDriver driver = webDriver.get();
    	if(driver != null) {
    		
        	boolean isSuccess = result.isSuccess();
        	if(!isSuccess) {
        		MyTestConfig conf = config.get();
        		conf.setTestResult(result);
                failedTests.add(conf);
        	}
        	
            ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (isSuccess ? "passed" : "failed"));
            driver.quit();
    	}

    }

	/*********************************************************************/

    @AfterSuite
    public void checkIfIssueNeeded() throws Exception {
    	
    	if(failedTests.size() > 0) {
    		
    		String summary = "", description = "";
    		for(MyTestConfig conf : failedTests) {
    			summary += conf.getIssueSummary();
    			description += conf.getIssueDescription();
    		}
    		
    		MyJIRAClient.createIssue(JIRA_PROJECT_ID, JIRA_ISSUE_TYPE_ID, summary, description);
    		
    	}
    	
    }

	/*********************************************************************/

    protected void annotate(String text) {

    	WebDriver driver = webDriver.get();
    	if(driver != null) {
            ((JavascriptExecutor) driver).executeScript("sauce:context=" + text);
    	}

    }
	
}
