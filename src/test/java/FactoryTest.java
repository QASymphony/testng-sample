import org.testng.annotations.Factory;

/**
 * Created by duongnapham on 1/13/16.
 */
public class FactoryTest {
    @Factory
    public Object[] createTest() {
        Object[] res = new Object[5];
        res[0] = new FactoryImplTestAdd(2,3,5);
        res[1] = new FactoryImplTestAdd(3,3,6);
        res[2] = new FactoryImplTestAdd(1,3,5);
        res[3] = new FactoryImplTestSubtract(4,2,2);
        res[4] = new FactoryImplTestSubtract(4,1,2);
        return res;
    }
}
