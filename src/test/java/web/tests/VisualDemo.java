package web.tests;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import web.pages.DemoCartWebPage;
import web.pages.DemoLoginWebPage;
import web.pages.DemoShopWebPage;

public class VisualDemo extends TestBase {
	
	/*********************************************************************/

    private static String USERNAME = "standard_user";
    private static String PASSWD = "secret_sauce";
    
	/*********************************************************************/

    @org.testng.annotations.Test(dataProvider = "hardCodedBrowsersVisual")
    public void loginTest(String browser, String version, String os, String deviceName, Method method) throws MalformedURLException, InvalidElementStateException, UnexpectedException {

        MutableCapabilities visualOptions = new MutableCapabilities();
        visualOptions.setCapability("projectName", "MyEndToEndDemo");
        visualOptions.setCapability("viewportSize", "1280x1024");
        
        this.createDesktopDriver(browser, version, os, method.getName(), visualOptions);
        WebDriver driver = this.getWebDriver();
        
  	  	JavascriptExecutor js = (JavascriptExecutor) driver;
  	  	js.executeScript("/*@visual.init*/", "My Visual Test");
        
        this.annotate("Visiting Demo Login page...");
        DemoLoginWebPage loginPage = DemoLoginWebPage.visitPage(driver, false);
  	  	js.executeScript("/*@visual.snapshot*/", "Login");

        this.annotate(String.format("Login in with: \"%s\"", USERNAME));
        DemoShopWebPage shopPage = loginPage.login(USERNAME, PASSWD);
  	  	js.executeScript("/*@visual.snapshot*/", "Shop");

        this.annotate(String.format("Add item and go to cart", USERNAME));
        DemoCartWebPage cart = shopPage.selectItemAndGotToCart();
  	  	js.executeScript("/*@visual.snapshot*/", "Cart");
        
    }

}