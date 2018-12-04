import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sample.intercls.ICalculator;
import utils.Calculator;

/**
 * Created by duongnapham on 1/13/16.
 */
public class FactoryImplTestSubtract {

    private int op1;
    private int op2;
    private int expResult;

    private static ICalculator calculator;

    @BeforeClass
    public static void initCalculator() {
        calculator = new Calculator();
    }

    public FactoryImplTestSubtract(int op1, int op2, int expResult) {
        this.op1=op1;
        this.op2=op2;
        this.expResult=expResult;
    }

    @Test
    public final void testSubtract() {
        Assert.assertEquals(calculator.subtraction(op1, op2), expResult);
    }
}
