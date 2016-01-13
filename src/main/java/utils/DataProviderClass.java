package utils;

import org.testng.annotations.DataProvider;

/**
 * Created by duongnapham on 1/13/16.
 */
public class DataProviderClass {

    @DataProvider(name = "data-provider")
    public static Object[][] dataProviderMethod()
    {
        return new Object[][] { { "data one" }, { "data two" } , { "data three" } };
    }
}
