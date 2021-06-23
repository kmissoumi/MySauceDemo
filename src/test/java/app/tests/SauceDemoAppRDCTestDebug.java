package app.tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class SauceDemoAppRDCTestDebug implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {

	/**************************************************************************************************/

    public String seleniumURI = "@ondemand.us-west-1.saucelabs.com:443";
    public String buildTag = System.getenv("BUILD_TAG");
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public String app = "storage:filename=22042021_app_release(nonsecure).apk";

    /**************************************************************************************************/

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**************************************************************************************************/

	public AndroidDriver<MobileElement> driver;
	
	public String sessionId;
	
    /**************************************************************************************************/
	
	@BeforeSuite
	public void setupAppium() throws MalformedURLException {
		
        MutableCapabilities capabilities = new MutableCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Samsung Galaxy A10");
        capabilities.setCapability("deviceOrientation", "portrait");
        capabilities.setCapability("app", app);
        capabilities.setCapability("newCommandTimeout", "240");

        capabilities.setCapability("name", "SauceDemoAppRDCTestDebug");
        capabilities.setCapability("build", "Java-TestNG-Appium-Android");
        
        capabilities.setCapability("browserName", "");
        
	    driver = new AndroidDriver<MobileElement>(new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub"), capabilities);
	    sessionId = driver.getSessionId().toString();

	}
	
	@AfterSuite
	public void uninstallApp() throws InterruptedException {
		driver.quit();
	}
	


	/**************************************************************************************************/

	@Test (enabled=true) 
	public void loginTest() throws InterruptedException {
		
        driver.findElementByAccessibilityId("test-Username").sendKeys("standard_user");
        driver.findElementByAccessibilityId("test-Password").sendKeys("secret_sauce");
        driver.findElementByAccessibilityId("test-LOGIN").click();

        Assert.assertTrue(driver.findElementByXPath("(//android.view.ViewGroup[@content-desc=\"test-Item\"])[1]/android.view.ViewGroup/android.widget.ImageView").isDisplayed());

	}


    /**************************************************************************************************/

    public SauceOnDemandAuthentication getAuthentication() {
        return authentication;
    }

    /**************************************************************************************************/

	public String getSessionId() {
		return sessionId;
	}
	
}