import org.testng.annotations.Test;
import utils.DataProviderClass;

/**
 * Created by duongnapham on 1/13/16.
 */
public class DataProviderTest {

    @Test(dataProvider = "data-provider", dataProviderClass = DataProviderClass.class)
    public void testDataProviderMethod(String data)
    {
        System.out.println("Data is: " + data);
    }
}
