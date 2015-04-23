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
    @Parameters({ "browserName" })
    public void testURLs(String browserName) throws Exception{
        try{
            System.out.println("Open browser " + browserName);
            logger.info("Open browser " + browserName);
            ArrayList<String> listURLs = commonUtils.getListURLs();
            for(int i=0; i<listURLs.size(); i++){
                System.out.println("Open url:  " + listURLs.get(i) + " on browser " + browserName);
                logger.info("Open url:  " + listURLs.get(i) + " on browser " + browserName);
                driver = commonUtils.openBrowser(browserName, listURLs.get(i));
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
