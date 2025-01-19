package reporting;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import java.io.File;
import java.util.List;

import static com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath;

public class ReportTest implements IReporter {
    public static ExtentReports extentReports;
    private static ExtentSparkReporter spark ;
    private static ExtentTest test;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        outputDirectory = "C:\\Users\\karan\\IdeaProjects\\TestAssignment\\testOutput";
        spark  = new ExtentSparkReporter(outputDirectory + File.separator + "ExtentReport.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);

        for (ISuite suite : suites) {
            suite.getResults().values().forEach(result -> {
                result.getTestContext().getPassedTests().getAllResults().forEach(tr -> {
                    test = extentReports.createTest(tr.getMethod().getMethodName());
                    test.pass("Test passed");
                });

                result.getTestContext().getFailedTests().getAllResults().forEach(tr -> {
                    test = extentReports.createTest(tr.getMethod().getMethodName());
                    test.fail(tr.getThrowable());
                });

                result.getTestContext().getSkippedTests().getAllResults().forEach(tr -> {
                    test = extentReports.createTest(tr.getMethod().getMethodName());
                    test.skip(tr.getThrowable());
                });
            });
        }

        extentReports.flush();
    }
}
