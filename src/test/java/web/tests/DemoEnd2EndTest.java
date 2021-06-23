package web.tests;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;

import web.pages.DemoCartWebPage;
import web.pages.DemoLoginWebPage;
import web.pages.DemoShopWebPage;

public class DemoEnd2EndTest extends TestBase {
	
	/*********************************************************************/

    private static String USERNAME = "standard_user";
    private static String PASSWD = "secret_sauce";
    
	/*********************************************************************/

    @org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
    public void loginTest(String browser, String version, String os, String deviceName, Method method) throws MalformedURLException, InvalidElementStateException, UnexpectedException {
    	
        this.createDriver(browser, version, os, deviceName, method.getName());
        WebDriver driver = this.getWebDriver();
        
        this.annotate("Visiting Demo Login page...");
        DemoLoginWebPage loginPage = DemoLoginWebPage.visitPage(driver, false);

        this.annotate(String.format("Login in with: \"%s\"", USERNAME));
        DemoShopWebPage shopPage = loginPage.login(USERNAME, PASSWD);

        this.annotate(String.format("Add item and go to cart", USERNAME));
        DemoCartWebPage cart = shopPage.selectItemAndGotToCart();
        
        driver.quit();
        
    }

}