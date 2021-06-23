package web.tests;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

import com.saucelabs.common.SauceOnDemandAuthentication;

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
    
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");
    public String screenerApiKey = System.getenv("SCREENER_API_KEY");

    public String buildTag = System.getenv("BUILD_TAG");

    /**************************************************************************************************/

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**************************************************************************************************/

	public String SAUCE_URL = "https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub";
	
	public String SCREENER_URL = "https://hub.screener.io:443/wd/hub";
	
    /**************************************************************************************************/

	public static final String EDGE = "MicrosoftEdge", 
							   IE = "internet explorer",
							   FIREFOX = "firefox",
							   CHROME = "chrome",
							   SAFARI = "safari";
	
	public static final String MACOS = "macOS 10.15",
							   WIN10 = "Windows 10",
							   IOS = "iOS",
							   ANDROID = "Android";
	
	public static final String LATEST = "latest",
							   PERF = "perf";
	
	static final String JIRA_PROJECT_ID = "10000", JIRA_ISSUE_TYPE_ID = "10001";
	
	/*********************************************************************/

	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

    private ThreadLocal<String> sessionId = new ThreadLocal<String>();

    private ThreadLocal<MyTestConfig> config = new ThreadLocal<MyTestConfig>();

	private List<MyTestConfig> failedTests = new ArrayList<MyTestConfig>();
	
	public AndroidDriver<MobileElement> driver;

	/*********************************************************************/

    @DataProvider(name = "hardCodedBrowsers", parallel = true)
    public static Object[][] sauceBrowserDataProvider(Method testMethod) {
    	
        return new Object[][]{
        	
            new Object[]{CHROME, LATEST, ANDROID, "Google Pixel XL"},
            new Object[]{SAFARI, LATEST, IOS, "iPhone 11 Pro Max"},

            new Object[]{CHROME, LATEST, ANDROID, "Android Emulator"},
            new Object[]{SAFARI, LATEST, IOS, "iOS Simulator"},

            new Object[]{PERF, LATEST, MACOS, ""},
            new Object[]{PERF, LATEST, WIN10, ""},	

            new Object[]{EDGE, LATEST, WIN10, ""},
            new Object[]{IE, LATEST, WIN10, ""},
            new Object[]{FIREFOX, LATEST, WIN10, ""},
            new Object[]{CHROME, LATEST, WIN10, ""},

            new Object[]{SAFARI, LATEST, MACOS, ""},
            new Object[]{EDGE, LATEST, MACOS, ""},
            new Object[]{FIREFOX, LATEST, MACOS, ""},
            new Object[]{CHROME, LATEST, MACOS, ""}

        };
        
    }

	/*********************************************************************/

    @DataProvider(name = "hardCodedBrowsersVisual", parallel = true)
    public static Object[][] sauceBrowserDataProviderVisual(Method testMethod) {
    	
        return new Object[][]{
        	
            new Object[]{EDGE, LATEST, WIN10, ""},
            new Object[]{IE, LATEST, WIN10, ""},
            new Object[]{FIREFOX, LATEST, WIN10, ""},
            new Object[]{CHROME, LATEST, WIN10, ""},

            new Object[]{SAFARI, LATEST, MACOS, ""},
            new Object[]{EDGE, LATEST, MACOS, ""},
            new Object[]{FIREFOX, LATEST, MACOS, ""},
            new Object[]{CHROME, LATEST, MACOS, ""}

        };
        
    }

	/*********************************************************************/

    public WebDriver getWebDriver() {
        return webDriver.get();
    }

	/*********************************************************************/
    
    public String getSessionId() {
        return sessionId.get();
    }

	/*********************************************************************/

    protected void createDriver(String browser, String version, String os, String deviceName, String methodName) throws MalformedURLException, UnexpectedException {
    	
        if(os.equals(ANDROID) || os.equals(IOS)) {
        	createMobileDriver(browser, version, os, deviceName, methodName);
        } else {
        	createDesktopDriver(browser, version, os, methodName);
        }

//        String id = driver.getSessionId().toString();

        String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
        sessionId.set(id);
        
        config.set(new MyTestConfig(browser, version, os, methodName, id));
        
    }

	/*********************************************************************/

	private void createMobileDriver(String browser, String version, String os, String deviceName, String methodName) throws MalformedURLException {

        MutableCapabilities capabilities = new MutableCapabilities();
        
        capabilities.setCapability("platformName", os);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("deviceOrientation", "portrait");
        capabilities.setCapability("newCommandTimeout", "240");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);

        capabilities.setCapability("name", methodName);
        
//	    driver = new AndroidDriver<MobileElement>(new URL(SAUCE_URL), capabilities);	    	    
		
        
        if(os.equals(ANDROID)) {
        	webDriver.set(new AndroidDriver<MobileElement>(new URL(SAUCE_URL),capabilities));        	
        } else if(os.equals(IOS)) {
        	webDriver.set(new IOSDriver<MobileElement>(new URL(SAUCE_URL),capabilities));        	
        }

	}

	/*********************************************************************/

    protected void createDesktopDriver(String browser, String version, String os, String methodName, MutableCapabilities visualCaps) throws MalformedURLException, UnexpectedException {
    	
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browser.equals("perf")) {
            capabilities.setCapability("capturePerformance", true);
            capabilities.setCapability("extendedDebugging", true);
            browser = CHROME;
            methodName += " - Performance Test";
        } 

        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability(CapabilityType.PLATFORM, os);
        capabilities.setCapability("name", methodName);

        String url = SAUCE_URL;
        if(visualCaps != null) {
        	visualCaps.setCapability("apiKey", screenerApiKey);
            capabilities.setCapability("sauce:visual", visualCaps);
            url = SCREENER_URL;
        }

        webDriver.set(new RemoteWebDriver(new URL(url),capabilities));

        String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
        sessionId.set(id);
        
        config.set(new MyTestConfig(browser, version, os, methodName, id));
        
    }

	/*********************************************************************/

    protected void createDesktopDriver(String browser, String version, String os, String methodName) throws MalformedURLException, UnexpectedException {
    	createDesktopDriver(browser, version, os, methodName, null);      
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
