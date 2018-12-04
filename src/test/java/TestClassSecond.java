import org.testng.Assert;
import org.testng.annotations.*;

public class TestClassSecond {
    @BeforeTest
    public void beforeTestTestClassSecond() {
        System.out.println("TestClassSecond: before test");
        Assert.assertEquals(true,false);
        System.out.println("TestClassSecond: failed in beforeTestTestClassSecond");
    }

    @Test
    public void testUnitClassOne() {
        try{
            System.out.println("TestClassSecond: before sleep - testUnitClassOne");
            Thread.sleep(5000);
            Assert.assertEquals(true,true);
            System.out.println("TestClassSecond: after sleep - testUnitClassOne");
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testUnitClassTwo() {
        try{
            System.out.println("TestClassSecond: before sleep - testUnitClassTwo");
            Thread.sleep(5000);
            Assert.assertEquals(true,false);
            System.out.println("TestClassSecond: after sleep - testUnitClassTwo");
        }catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    @BeforeMethod
    public void beforeMethodTestClassSecond() {
        System.out.println("TestClassSecond: before method");
    }

    @BeforeMethod
    public static void staticBeforeMethodTestClassSecond() {
        System.out.println("TestClassSecond: static before method");
    }

    @BeforeMethod
    public void beforeMethodWithParamTestClassSecond() {
        String p = "I am beforeMethod";
        System.out.println("TestClassSecond: before method with param " + p);
    }

    @AfterMethod
    public void afterMethodTestClassSecond() {
        System.out.println("TestClassSecond: after method");
    }

    @BeforeClass
    public void beforeClassTestClassSecond() {
        System.out.println("TestClassSecond: before class");
    }

    @AfterClass
    public void afterClassTestClassSecond() {
        System.out.println("TestClassSecond: after class");
    }

    @AfterTest
    public void afterTestTestClassSecond() {
        System.out.println("TestClassSecond: after test");
    }
}
