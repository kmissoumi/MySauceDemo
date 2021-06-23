package web.tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import web.pages.DemoCartWebPage;
import web.pages.DemoLoginWebPage;
import web.pages.DemoShopWebPage;

public class SauceDemoWebRDCTest implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {

	/**************************************************************************************************/

    public String seleniumURI = "@ondemand.us-west-1.saucelabs.com:443";
    public String buildTag = System.getenv("BUILD_TAG");
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

    /**************************************************************************************************/

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**************************************************************************************************/

	public AndroidDriver<MobileElement> driver;
	
	public String sessionId;
	
    /**************************************************************************************************/
	
	@BeforeTest
	public void setupAppium() throws MalformedURLException {
		
        MutableCapabilities capabilities = new MutableCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Google Pixel XL");
        capabilities.setCapability("deviceOrientation", "portrait");
        capabilities.setCapability("newCommandTimeout", "240");
        capabilities.setCapability("browserName", "Chrome");

        capabilities.setCapability("name", "SauceDemoWebRDCTest");
        capabilities.setCapability("build", "Java-TestNG-Appium-Android");
        
        
	    driver = new AndroidDriver<MobileElement>(new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() + seleniumURI + "/wd/hub"), capabilities);
	    sessionId = driver.getSessionId().toString();
	    
	}
	
	@AfterTest
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