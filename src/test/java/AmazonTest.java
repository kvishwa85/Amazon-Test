import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pom.AmazonHomePageSetup;
import reporting.ReportTest;
import utils.BrowserFactory;
import utils.ReusableUtils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.Map;

public class AmazonTest {
    SoftAssert softAssert = new SoftAssert();
    WebDriver driver;
    ExtentReports extentReports;
    ExtentTest test;

    @BeforeClass
    public void setup() {
        ReportTest reportTest = new ReportTest();
        extentReports = new ExtentReports();
        ReportTest.extentReports = extentReports;
    }

    @BeforeMethod
    public void startTest(Method method) {
        test = extentReports.createTest(method.getName());
    }

    @Test
    public void testAmazon(Method method) throws InterruptedException, ParseException, IOException {
        Map<String, Object> testData = ReusableUtils.readData("dataAmazon", "testCaseData", method.getName());
        driver = BrowserFactory.getDriver(testData.get("browserName").toString());
        driver.get("https://www.amazon.in/");
        driver.manage().window().maximize();
        AmazonHomePageSetup amazonHomePageSetup = new AmazonHomePageSetup(driver);
        amazonHomePageSetup.selectSearchMenu(testData.get("selectMenu").toString());
        amazonHomePageSetup.type(testData.get("searchItem1").toString());
        softAssert.assertTrue(amazonHomePageSetup.isSearchProductRelevent(testData.get("releventItem").toString(), driver), "Data is irrelevent");
        amazonHomePageSetup.type(testData.get("searchItem2").toString());
        amazonHomePageSetup.selectResultFromDropdown(testData.get("searchItem2").toString(), driver);
        amazonHomePageSetup.clickOnProduct(testData.get("iphoneType").toString(), driver);
        softAssert.assertTrue(amazonHomePageSetup.validateProductIsOpenedInNewTab(testData.get("iphoneType").toString(), driver), "New tab is not opened");
        amazonHomePageSetup.clickOnElementWithText(testData.get("text1").toString(), driver);
        amazonHomePageSetup.selectAppleCategoryAndProduct(testData.get("appleCategory").toString(), testData.get("appleProduct").toString(), driver);
        softAssert.assertTrue(amazonHomePageSetup.hoverOverWatchesAndVerifyQuickLook(driver));
        softAssert.assertTrue(amazonHomePageSetup.verifyModalProductDetails( driver));
    }
    @AfterMethod
    public void tearDownTest() {
        extentReports.flush();
    }
    @AfterClass
    public void tearDown(){
        BrowserFactory.tearDown();

    }

}
