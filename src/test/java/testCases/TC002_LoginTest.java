package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyaccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass {

    @Test(groups={"Sanity","Master"})
    public void verify_login() {

        logger.info("***** Starting TC002_LoginTest *****");

        try {

            // HomePage
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            hp.clickLogin();

            logger.info("Navigated to Login Page");

            // LoginPage
            LoginPage lp = new LoginPage(driver);
            lp.setEmail(p.getProperty("email"));
            lp.setPassword(p.getProperty("password"));   // ✅ fixed
            lp.clickLogin();

            logger.info("Login details submitted");

            // MyAccountPage
            MyaccountPage macc = new MyaccountPage(driver);
            boolean targetPage = macc.isMyAccountPageExists();

            Assert.assertTrue(targetPage, "Login failed!");

            logger.info("Login successful - My Account page displayed");

        } catch (Exception e) {

            logger.error("Test failed due to exception: " + e.getMessage());
            Assert.fail("Test failed due to exception");

        }

        logger.info("***** Finished TC002_LoginTest *****");
    }
}