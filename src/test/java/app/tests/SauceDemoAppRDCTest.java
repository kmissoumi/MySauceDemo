package app.tests;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;

import org.openqa.selenium.InvalidElementStateException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class SauceDemoAppRDCTest extends TestBase  {
	
    public String[] APP_NAMES = {
    		"storage:filename=Android.SauceLabs.Mobile.Sample.app.2.7.1.apk",
    		"storage:filename=iOS.RealDevice.SauceLabs.Mobile.Sample.app.2.7.1.ipa"
    };

    /**************************************************************************************************/
	
    @org.testng.annotations.Test(dataProvider = "hardCodedPhones")
    public void loginTest(Integer platformId, String deviceName, Method method) throws MalformedURLException, InvalidElementStateException, UnexpectedException {
    	
        this.createDriver(APP_NAMES, deviceName, platformId, method.getName(), null);
        AppiumDriver<MobileElement> driver = this.getWebDriver();
        
        driver.findElementByAccessibilityId("test-Username").sendKeys("standard_user");
        driver.findElementByAccessibilityId("test-Password").sendKeys("secret_sauce");
        driver.findElementByAccessibilityId("test-LOGIN").click();

        driver.quit();
        
    }

}