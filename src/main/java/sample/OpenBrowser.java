package sample;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by duongnapham on 2/12/15.
 */
public class OpenBrowser {

    private final Logger logger = Logger.getLogger(OpenBrowser.class);
    private CommonUtils commonUtils = new CommonUtils();
    private WebDriver driver;

    @Test
    public void openBrowserWithUrl() throws Exception{
        try{
            String browserName = "Chrome";
            System.out.println("Open browser " + browserName);
            ArrayList<String> listURLs = commonUtils.getListURLs();
            for(int i=0; i<listURLs.size(); i++){
                System.out.println("Access url:  " + listURLs.get(i) + " on browser " + browserName);
                driver = commonUtils.openBrowser(browserName, listURLs.get(i));
                System.out.println(driver.toString());
                Thread.sleep(3000L);
                System.out.println("Closing browser now .........");
                logger.info("Quit ....");
                driver.quit();
            }
        }
        catch (Exception e){
            logger.error("testURLs: " + e.getMessage());
        }
    }

}
