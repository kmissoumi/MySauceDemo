package web.tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import web.pages.DemoCartWebPage;
import web.pages.DemoLoginWebPage;
import web.pages.DemoShopWebPage;

public class SauceDemoWebSimTest implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {

	/**************************************************************************************************/

    public String seleniumURI = "@ondemand.us-west-1.saucelabs.com:443";
    public String buildTag = System.getenv("BUILD_TAG");
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    public String app = "storage:filename=Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";

    /**************************************************************************************************/

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**************************************************************************************************/

	public IOSDriver<MobileElement> driver;
//	public AndroidDriver<MobileElement> driver;
	
	public String sessionId;
	
    /**************************************************************************************************/
	
	@BeforeSuite
	public void setupAppium() throws MalformedURLException {
		
        MutableCapabilities capabilities = new MutableCapabilities();
        
        capabilities.setCapability("appiumVersion", "1.20.1");
        capabilities.setCapability("idleTimeout", "90");
        capabilities.setCapability("noReset", "true");
        capabilities.setCapability("newCommandTimeout", "90");
        capabilities.setCapability("language", "en");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "14.3");
        capabilities.setCapability("deviceName", "iPhone Simulator");
        capabilities.setCapability("name", "SauceDemoAppSimTest");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "safari");

        driver = new IOSDriver<MobileElement>(new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub"), capabilities);
	    sessionId = driver.getSessionId().toString();
	    
//	    driver = new AndroidDriver<MobileElement>(new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub"), capabilities);
//	    sessionId = driver.getSessionId().toString();
	    
	}
	
	@AfterSuite
	public void uninstallApp() throws InterruptedException {
		driver.quit();
	}
	


	/**************************************************************************************************/

	@Test (enabled=true) 
	public void loginTest() throws InterruptedException {
		
        DemoLoginWebPage loginPage = DemoLoginWebPage.visitPage(driver, false);
        DemoShopWebPage shopPage = loginPage.login();
        DemoCartWebPage cart = shopPage.selectItemAndGotToCart();

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