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

    private String getChromeDriverPath() {
        String osName =  System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "/resources/drivers/windows/chromedriver.exe";
        }
        if (osName.contains("mac")){
            return "/resources/drivers/mac/chromedriver";
        }
        return "/resources/drivers/linux/chromedriver";
    }

    private void setExecutableMode(String path) {
        final File file = new File(path);
        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true, false);
    }

    public WebDriver openChromeBrowser (String baseURL) {
        WebDriver driver = null;
        try{
            String chromeDriverPath = System.getProperty("user.dir") + getChromeDriverPath();
            setExecutableMode(chromeDriverPath);
            System.out.println("---- Opening chrome browser");
            DesiredCapabilities capability = new DesiredCapabilities();
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            capability.setJavascriptEnabled(true);
            capability.setCapability("chrome.switches", "--start-maximized");
            capability.setCapability(ChromeOptions.CAPABILITY, new ChromeOptions());
            capability = DesiredCapabilities.chrome();
            capability.setBrowserName("chrome");
            driver = new ChromeDriver(capability);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(baseURL);
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
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
