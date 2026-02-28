package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {

    @Test(groups="Regression")
    public void verify_account_registration() {
    		logger.info("**** Starting TC001_AccountRegistrationTest ****");
       try
       {
    		HomePage hp = new HomePage(driver); // use driver from BaseClass
        hp.clickMyAccount();
		logger.info("Clicked on MyAccount Link");
 
        hp.clickRegister();
		logger.info("Clicked on Register Link");

        AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
		logger.info("Providing customer stails...");

        regpage.setFirstName(randomString().toUpperCase());
        regpage.setLastName(randomString().toUpperCase());

        String email = randomString() + "@gmail.com";
        regpage.setEmail(email);

        regpage.setTelephone(randomNumber());

        String password = randomAlphaNumeric();
        regpage.setPassword(password);
        regpage.setConfirmPassword(password);

        regpage.setPrivacyPolicy();
        regpage.clickContinue();
		logger.info("Validating expected message");

        String confms = regpage.getConfirmationMessage();
if(confms.equals("Your Account Has Been Created!"))
{  
	Assert.assertTrue(true);
       }
else
{
	   logger.error("Test failed..");
	   logger.debug("Debug logs..");
	Assert.assertTrue(false);	
}
}
       catch(Exception e)
       {
    	   Assert.fail();
       }
		logger.info("**** Finished TC001_AccountRegistrationTest ****");

    }

}