package sample;

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
	public void beforeTest() {
		System.out.println("testClass: before test");
	}

	@Test
	public void unitLevel1() {
    try        
    {
      System.out.println("testClass: before sleep - Unit level1 testing");
      Thread.sleep(5000);
      System.out.println("testClass: after sleep - Unit level1 testing");
    } 
    catch(InterruptedException ex) 
    {
      Thread.currentThread().interrupt();
    }		
	}

	@Test
	public void unitLevel2() {
		{
      System.out.println("testClass: before sleep - Unit level2 testing");
      Thread.sleep(5000);
      System.out.println("testClass: after sleep - Unit level2 testing");
    } 
    catch(InterruptedException ex) 
    {
      Thread.currentThread().interrupt();
    }
	}

	@BeforeMethod
	public void beforeMethod() {
		System.out.println("testClass: before method");
	}

	@BeforeMethod
	public static void staticBeforeMethod() {
		System.out.println("testClass: static before method");
	}

	@Parameters({ "param" })
	@BeforeMethod
	public void beforeMethodWithParam(String p) {
		System.out.println("testClass: before method with param " + p);
	}

	@AfterMethod
	public void afterMethod() {
		System.out.println("testClass: after method");
	}

	@BeforeClass
	public void beforeClass() {
		System.out.println("testClass: before class");
	}

	@AfterClass
	public void afterClass() {
		System.out.println("testClass: after class");
	}

	@AfterTest
	public void afterTest() {
		System.out.println("testClass: after test");
	}
}