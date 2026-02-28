package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups = {"Sanity","Regression","Master","DataDriver"})
    @Parameters({"os","browser"})
    public void setup(String os, String br) throws IOException {

        // Load config.properties
        p = new Properties();
        FileReader file = new FileReader("src/test/resources/config.properties");
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        String executionEnv = p.getProperty("execution_env");

        // ================== REMOTE EXECUTION ==================
        if (executionEnv.equalsIgnoreCase("remote")) {

            if (br.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();

                if (os.equalsIgnoreCase("windows")) {
                    options.setPlatformName("Windows 11");
                } else if (os.equalsIgnoreCase("mac")) {
                    options.setPlatformName("macOS");
                }

                driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            }

            else if (br.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();
                driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            }

            else if (br.equalsIgnoreCase("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            }

            else {
                throw new IllegalArgumentException("Invalid browser for remote execution: " + br);
            }
        }

        // ================== LOCAL EXECUTION ==================
        else if (executionEnv.equalsIgnoreCase("local")) {

            switch (br.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;

                case "edge":
                    driver = new EdgeDriver();
                    break;

                case "firefox":
                    driver = new FirefoxDriver();
                    break;

                default:
                    throw new IllegalArgumentException("Invalid browser: " + br);
            }
        }

        else {
            throw new IllegalArgumentException("Invalid execution_env in config.properties");
        }

        logger.info("Browser launched successfully");

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(p.getProperty("appURL"));

        logger.info("Application launched successfully.");
    }

    @AfterClass(groups = {"Sanity","Regression","Master","DataDriver"})
    public void tearDown() {

        if (driver != null) {
            logger.info("Closing browser...");
            driver.quit();
            logger.info("Browser closed.");
        }
    }

    // Random Data Methods
    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    // Screenshot Method
    public String captureScreen(String tname) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());

        TakesScreenshot ts = (TakesScreenshot) driver;
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        String targetFilePath = System.getProperty("user.dir")
                + File.separator + "screenshots" 
                + File.separator + tname + "_" + timeStamp + ".png";

        File targetFile = new File(targetFilePath);
        targetFile.getParentFile().mkdirs();

        Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return targetFilePath;
    }
}