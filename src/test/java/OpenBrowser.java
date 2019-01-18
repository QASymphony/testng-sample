import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import utils.CommonUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

/**
 * Created by duongnapham on 2/12/15.
 */
public class OpenBrowser {

    private final Logger logger = Logger.getLogger(OpenBrowser.class);
    private CommonUtils commonUtils = new CommonUtils();
    private WebDriver driver;

    @Test
    public void openBrowserWithUrl(){
        try{
            driver = commonUtils.openChromeBrowser("https://www.qasymphony.com");
            WebElement searchIconElement = driver.findElement(By.xpath("/html/body/div[2]/header/div/div[2]/div/nav/ul/li[9]/a"));
            searchIconElement.click();
            WebElement searchInputElement = driver.findElement(By.id("input-search"));

            searchInputElement.sendKeys("qTest Launch");
            searchInputElement.submit();
            System.out.println("Closing browser now .........");
            logger.info("Quit ....");
            driver.quit();
        }
        catch (Exception e){
            logger.error("testURLs: " + e.getMessage());
        }
    }

}
