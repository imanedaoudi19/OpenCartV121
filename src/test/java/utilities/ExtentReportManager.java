package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;

    String repName;

    // 🔹 Before execution starts
    @Override
    public void onStart(ITestContext testContext) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                .format(new Date());

        repName = "Test-Report-" + timeStamp + ".html";

        sparkReporter = new ExtentSparkReporter(
                System.getProperty("user.dir") + "\\reports\\" + repName);

        sparkReporter.config().setDocumentTitle("Opencart Automation Report");
        sparkReporter.config().setReportName("Opencart Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Static Info
        extent.setSystemInfo("Application", "Opencart");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));

        // From testng.xml
        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        List<String> includedGroups =
                testContext.getCurrentXmlTest().getIncludedGroups();

        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    // 🔹 When Test Starts
    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
    }

    // 🔹 When Test Passes
    @Override
    public void onTestSuccess(ITestResult result) {

        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());

        test.log(Status.PASS,
                result.getName() + " got successfully executed");
    }

    // 🔹 When Test Fails
    @Override
    public void onTestFailure(ITestResult result) {

        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());

        test.log(Status.FAIL,
                result.getName() + " got failed");
        test.log(Status.INFO,
                result.getThrowable().getMessage());

        try {
            String imgPath =
                    new BaseClass().captureScreen(result.getName());
            test.addScreenCaptureFromPath(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 When Test Skipped
    @Override
    public void onTestSkipped(ITestResult result) {

        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());

        test.log(Status.SKIP,
                result.getName() + " got skipped");
        test.log(Status.INFO,
                result.getThrowable().getMessage());
    }

    // 🔹 After execution finishes
    @Override
    public void onFinish(ITestContext testContext) {

        extent.flush();

        String pathOfExtentReport =
                System.getProperty("user.dir") + "\\reports\\" + repName;

        File extentReport = new File(pathOfExtentReport);

        // 🔹 Auto open report after execution
        try {
            Desktop.getDesktop().browse(extentReport.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }

      
         // 🔹 Send Email with Report (Optional - Requires Mail Configuration)
/*
         try {
             URL url = new URL("file:///" +
                     System.getProperty("user.dir") +
                     "\\reports\\" + repName);

             ImageHtmlEmail email = new ImageHtmlEmail();
             email.setDataSourceResolver(new DataSourceUrlResolver(url));
             email.setHostName("smtp.googlemail.com");
             email.setSmtpPort(465);
             email.setAuthenticator(
                     new DefaultAuthenticator("your_email@gmail.com", "your_password"));
             email.setSSLOnConnect(true);

             email.setFrom("your_email@gmail.com");
             email.setSubject("Test Results");
             email.setMsg("Please find attached report...");

             email.addTo("receiver@gmail.com");
             email.attach(url, "Extent Report", "Automation Test Report");

             email.send();

         } catch (Exception e) {
             e.printStackTrace();
         }
    */
    }
}