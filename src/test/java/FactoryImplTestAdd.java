import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sample.intercls.ICalculator;
import utils.Calculator;

/**
 * Created by duongnapham on 1/13/16.
 */
public class FactoryImplTestAdd {
    private int op1;
    private int op2;
    int expResult;

    private static ICalculator calculator;

    @BeforeClass
    public static void initCalculator() {
        calculator = new Calculator();
    }

    public FactoryImplTestAdd(int op1, int op2, int expResult) {
        this.op1=op1;
        this.op2=op2;
        this.expResult=expResult;
    }

    @Test
    public final void testAdd() {
        Assert.assertEquals(calculator.sum(op1, op2), expResult);
    }
}
