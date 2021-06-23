package web.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class DemoLoginWebPage extends WebPageBase {
	
    /***************************************************************/
    
    @FindBy(how = How.XPATH, using = "//*[@id=\"user-name\"]")
    private WebElement userName;
    
    @FindBy(how = How.XPATH, using = "//*[@id=\"password\"]")
    private WebElement password;
    
    @FindBy(how = How.XPATH, using = "//*[@id=\"login-button\"]") // //*[@id="login-button"]
    private WebElement submitButton;
        
    /***************************************************************/
    
    private static String USERNAME = "standard_user";
    private static String PASSWD = "secret_sauce";
    
	/*********************************************************************/

    public static String url = "http://www.saucedemo.com/v1";
    
    public boolean isFail;

    /***************************************************************/
    
    public static DemoLoginWebPage visitPage(WebDriver driver, boolean isFail) {
    	DemoLoginWebPage page = new DemoLoginWebPage(driver, isFail);
        page.visitPage();
        return page;
    }

    /***************************************************************/
    
    public DemoLoginWebPage(WebDriver driver, boolean isFail) {
        super(driver);
    	this.isFail = isFail;
        PageFactory.initElements(driver, this);
    }

    /***************************************************************/
    
    public void visitPage() {
        this.driver.get(url);
    }

    /***************************************************************/
    
    public DemoShopWebPage login() {
    	return login(USERNAME, PASSWD);
    }
    
    /***************************************************************/
    
    public DemoShopWebPage login(String userName, String password) {
    	
    	if(isFail) {
    		password = "failed";
    	}
    	
    	this.userName.sendKeys(userName);
    	this.password.sendKeys(password);
    	
    	submitButton.click();
    	
    	return new DemoShopWebPage(driver);
    	
    }
    
}