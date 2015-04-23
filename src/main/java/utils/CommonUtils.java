package utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by duongnapham on 2/3/15.
 */
public class CommonUtils {

    private final Logger logger = Logger.getLogger(CommonUtils.class);
    public WebDriver driver;

    public WebDriver openBrowser(String browserName, String baseURL) throws Exception {
        try{
            logger.info("Browser: " + browserName + " without remote driver");
            DesiredCapabilities capability = new DesiredCapabilities();
            capability.setPlatform(Platform.ANY);
            capability.setVersion(capability.getVersion());
            capability.setJavascriptEnabled(true);
            capability.setCapability("nativeEvents", true);
            if (browserName.equals("FF")) {
                logger.info("Firefox driver would be used");
                capability = DesiredCapabilities.firefox();
                capability.setBrowserName("firefox");
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.max_chrome_script_run_time", 1000);
                profile.setPreference("dom.max_script_run_time", 1000);
                profile.setPreference("browser.cache.disk.enable", false);
                profile.setPreference("webdriver.firefox.bin","C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
                capability.setCapability(FirefoxDriver.PROFILE, profile);
                driver = new FirefoxDriver(capability);
            }  else if (browserName.equals("IE")) {
                logger.info("IE is selected");
                capability = DesiredCapabilities.internetExplorer();
                capability.setBrowserName("internet explorer");
                capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
                capability.setJavascriptEnabled(true);
                System.setProperty("webdriver.ie.driver", "resources//driver//IEDriverServer.exe");
                driver = new InternetExplorerDriver(capability);
                driver.manage().deleteAllCookies();
            } else if (browserName.equals("CH")){
                logger.info("Google chrome is selected");
                capability.setJavascriptEnabled(true);
                capability.setCapability("chrome.switches", "--start-maximized");
                capability.setCapability(ChromeOptions.CAPABILITY, new ChromeOptions());
                System.setProperty("webdriver.chrome.driver", "resources//driver//chromedriver.exe");
                capability = DesiredCapabilities.chrome();
                capability.setBrowserName("chrome");
                driver = new ChromeDriver(capability);
            }
            driver.manage().window().maximize();
            logger.info("Driver setup is starting ......");
//            driver.get(baseURL);
            driver.navigate().to(baseURL);
            driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
        }
        catch (Exception e){
            logger.error("Error: " + e.getMessage());
        }
        return driver;
    }


    /**
     * get path of file
     * @param fileName
     * */
    public String getAbsolutePath(String fileName){
        String path = "";
        try{
            File f;
            Boolean bool;
            f = new File(fileName);
            bool = f.exists();
            if (bool) {
                path = f.getAbsolutePath();
            }
        }
        catch (Exception ex){
            logger.error("Error: " + ex.getMessage());
        }
        return path;
    }

    public String[][] getTableArray(String xlFilePath, String sheetName, String tableName) {
        String[][] tabArray = null;

        Workbook workbook;
        try {
            logger.info("File path: " + xlFilePath);
            workbook = Workbook.getWorkbook(new File(xlFilePath));
            Sheet sheet = workbook.getSheet(sheetName);
            int startRow, startCol, endRow, endCol, ci, cj;
            Cell tableStart = sheet.findCell(tableName);
            startRow = tableStart.getRow();
            startCol = tableStart.getColumn();

            Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1,
                    100, 64000, false);

            endRow = tableEnd.getRow();
            endCol = tableEnd.getColumn();
            tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
            ci = 0;

            for (int i = startRow + 1; i < endRow; i++, ci++) {
                cj = 0;
                for (int j = startCol + 1; j < endCol; j++, cj++) {
                    tabArray[ci][cj] = sheet.getCell(j, i).getContents();
                }
            }
        } catch (BiffException | IOException ex) {
            logger.error("Error: " + ex.getMessage());
        }

        return (tabArray);
    }


    /**
     * read data in urlinfo sheet
     * */
    public ArrayList<String> getListURLs() throws Exception{
        ArrayList<String> listURLs = new ArrayList<>();
        String excelData = getAbsolutePath("resources//data//data.xls");
        String[][] urlInfo = getTableArray(excelData,"data", "urldata");
        for (int i = 0; i < urlInfo.length; i++) {
            listURLs.add(urlInfo[i][0]);
        }
        return listURLs;
    }
}
