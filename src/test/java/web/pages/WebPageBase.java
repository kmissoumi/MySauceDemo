package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebPageBase {
	
    /***************************************************************/
    
    public WebDriver driver;

	/*********************************************************************/

    private static final int PAUSE = 3;

    /***************************************************************/
    
    public WebPageBase(WebDriver driver) {
        this.driver = driver;
    }

	/*********************************************************************/
	   
	public void clickWhenReady(String locator) {

        WebElement elem = null;
        int tries = 0;
        
        while(elem == null && tries++ < 10) {
    		try {
    	        elem = driver.findElement(By.cssSelector(locator));
    	        if(elem != null) {
    	        	elem.click();
    	        }
    		} catch (Exception ex) {
    			elem = null;
    			pause(1);
    		}
        	
        }

	}
	
	/*********************************************************************/
	   
	public void clickWhenReady(By by) {

        WebElement elem = null;
        int tries = 0;
        
        while(elem == null && tries++ < 10) {
    		try {
    			log("clickWhenReady(): tries = " + tries + " / Find");
    	        elem = driver.findElement(by);
    	        if(elem != null) {
        			log("clickWhenReady(): tries = " + tries + " / click");
    	        	elem.click();
    	        }
    		} catch (Exception ex) {
    			log("clickWhenReady(): tries = " + tries + " / ex = " + ex.getMessage());
    			elem = null;
    			pause(1);
    		}
        	
        }

	}
	
	/*********************************************************************/
	   
	public void click(String locator) {
        clickWhenReady(By.cssSelector(locator));
	}

	/*********************************************************************/
	   
	public void clickX(String xpath) {
		clickWhenReady(By.xpath(xpath));
	}
	
	/*********************************************************************/
	   
	public void clickT(String text) {
		clickWhenReady(By.linkText(text));
	}
	
	/*********************************************************************/
	   
	public void clickId(String id) {
		clickWhenReady(By.id(id));
	}
	
	/*********************************************************************/
	   
	public void click(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement elem = wait.until(ExpectedConditions.elementToBeClickable(by));
		elem.click();
	}
	
	/*********************************************************************/

	public void clear(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		elem.clear();
	}
	
	/*********************************************************************/
	   
	public void type(By by, String text, boolean isClear) {
		if(isClear) {
			clear(by);
		}
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		elem.sendKeys(text);
	}
	
	/*********************************************************************/
	   
	public void typeWhenReady(By by, String text, boolean isClear) {

        WebElement elem = null;
        int tries = 0;
        
        while(elem == null && tries++ < 50) {
    		try {
    			log("typeWhenReady(): tries = " + tries + " / Find");
    	        elem = driver.findElement(by);
    	        if(elem != null) {
        	        if(isClear) {
            			log("typeWhenReady(): tries = " + tries + " / clear");
        	        	elem.clear();
            			pause(3);
        	        }
        			log("typeWhenReady(): tries = " + tries + " / sendkeys");
    	        	elem.sendKeys(text);
    	        }
    		} catch (Exception ex) {
    			log("typeWhenReady(): tries = " + tries + " / excecption: " + ex.getMessage());
    			elem = null;
    			pause(3);
    		}
        	
        }

		log("typeWhenReady(): tries = " + tries + " / all done !");

	}
	
	/*********************************************************************/
	   
	public void type(String locator, String text) {
		typeWhenReady(By.cssSelector(locator), text, false);
	}
	
	/*********************************************************************/
	   
	public void typeClear(String locator, String text) {
		typeWhenReady(By.cssSelector(locator), text, true);
	}
	
	/*********************************************************************/
	   
	public void typeX(String xpath, String text) {
		typeWhenReady(By.xpath(xpath), text, false);
	}
	
	/*********************************************************************/
	   
	public void typeClearX(String xpath, String text) {
		typeWhenReady(By.xpath(xpath), text, true);
	}
	
	/*********************************************************************/
	   
	public void switchWindow() {
		
		String current=driver.getWindowHandle();
		
		for(String handle : driver.getWindowHandles()) {
			if(!handle.equals(current)) {
				driver.switchTo().window(handle);
			}
		}

	}
	
	/*********************************************************************/
	   
	public void hover(String locator) {
	    WebElement hoverElement = driver.findElement(By.cssSelector(locator));
	    Actions actions = new Actions(driver);
	    actions.moveToElement(hoverElement).build().perform();
	}
	
	/*********************************************************************/
	   
	public void hoverX(String xpath) {
	    WebElement hoverElement = driver.findElement(By.xpath(xpath));
	    Actions builder = new Actions(driver);
	    builder.moveToElement(hoverElement).perform();
	}
	
	/*********************************************************************/
	   
	public void hoverT(String text) {
	    WebElement hoverElement = driver.findElement(By.linkText(text));
	    Actions builder = new Actions(driver);
	    builder.moveToElement(hoverElement).perform();
	}
	
	/*********************************************************************/
	   
	public WebElement getReadyElement(By by) {
		WebElement ret = driver.findElement(by);
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.elementToBeClickable(ret)); 
		return ret;
	}
	
	/*********************************************************************/

	public void waitUntilElementClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(element)); 
	}
	
	/*********************************************************************/
	// Misc methods
	/*********************************************************************/
	   
	public void pause() {
		pause(PAUSE);
	}
	
	/*********************************************************************/
	   
	public void pause(int secs) {
	
	    try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/*********************************************************************/
	   
	public void log(String s) {
		System.out.println(s);
	}

	/*********************************************************************/
	   
	public void get(String url) {
		driver.get(url);
	}

	/*********************************************************************/
	   
	public void quit() {
		driver.quit();
	}

	/*********************************************************************/
	   
	public void close() {
		driver.close();
	}
	
}