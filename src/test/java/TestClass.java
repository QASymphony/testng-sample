import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestClass {

	@BeforeTest
	public void beforeTestTestClass() {
		System.out.println("testClass: before test");
	}

	@Test
	public void unitLevel1TestClass() {
	    try{
	      System.out.println("testClass: before sleep - Unit level1 testing");
	      Thread.sleep(5000);
            Assert.assertEquals(true,true);
	      System.out.println("testClass: after sleep - Unit level1 testing");
	    } catch(InterruptedException ex) {
	      Thread.currentThread().interrupt();
	    }		
	}

	@Test
	public void unitLevel2TestClass() {
		try{
	      System.out.println("testClass: before sleep - Unit level2 testing");
	      Thread.sleep(5000);
            Assert.assertEquals(false,false);
	      System.out.println("testClass: after sleep - Unit level2 testing");
	    }catch(InterruptedException ex) 
    	{
      		Thread.currentThread().interrupt();
	    }
	}

	@BeforeMethod
	public void beforeMethodTestClass() {
		System.out.println("testClass: before method");
	}

	@BeforeMethod
	public static void staticBeforeMethod() {
		System.out.println("testClass: static before method");
	}

	@Parameters({ "param" })
	@BeforeMethod
	public void beforeMethodWithParamTestClass() {
        String p = "I am beforeMethod";
        System.out.println("testClass: before method with param " + p);
	}

	@AfterMethod
	public void afterMethodTestClass() {
		System.out.println("testClass: after method");
	}

	@BeforeClass
	public void beforeClassTestClass() {
		System.out.println("testClass: before class");
	}

	@AfterClass
	public void afterClassTestClass() {
		System.out.println("testClass: after class");
	}

	@AfterTest
	public void afterTestTestClass() {
		System.out.println("testClass: after test");
	}
}