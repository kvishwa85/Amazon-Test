package pom;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BrowserFactory;
import utils.ReusableUtils;

import java.time.Duration;
import java.util.List;

public class AmazonHomePageSetup extends BrowserFactory {
    WebDriver driver;
    @FindBy(xpath = "//select[@title = 'Search in']")
    private WebElement searchMenuDropdown;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(xpath = "//div[@class = 'autocomplete-results-container']//div[@class = 's-suggestion s-suggestion-ellipsis-direction']")
    private List<WebElement> searchResultDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Apple iPhone 13 (128GB)')]/../..")
    private List<WebElement> resultProduct;

    @FindBy(xpath = "//div[@class = 'EditorialTile__container__MuV7R']//a[contains(@aria-label, 'Smartwatch')]")
    private List<WebElement> watches;

    String xpath1 = "//div[@class = 'autocomplete-results-container']//div[@class = 's-suggestion s-suggestion-ellipsis-direction']";
    String textXpath = "//*[text() = '%s']";
    String appleCategoryXpath = "//li[@class= 'Navigation__navItem__bakjf Navigation__hasChildren__jSUsH']/a/span[text() ='%s']/..";
    String appleProductXpath = "//li[@class= 'Navigation__navItem__bakjf Navigation__hasChildren__jSUsH']/a/span[text() ='%s']/ancestor::li//div//li/a";

    public AmazonHomePageSetup(WebDriver driver) {
        //this.driver = driver;
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * This method is used to select the search menu
     *
     * @param menu
     */
    public AmazonHomePageSetup selectSearchMenu(String menu) throws InterruptedException {
        searchMenuDropdown.click();
        Select select = new Select(searchMenuDropdown);
        select.selectByVisibleText(menu);
        return this;
    }

    /**
     * This method is used to type the text in the search box
     *
     * @param text
     */
    public AmazonHomePageSetup type(String text) {
        searchBox.clear();
        searchBox.sendKeys(text);
        return this;
    }

    /**
     * Method to verify if the search results are relevant to the product
     *
     * @param product
     * @return
     */
    public boolean isSearchProductRelevent(String product, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(searchResultDropdown));
            return searchResultDropdown.stream().map(ele -> ele.getAttribute("aria-label")).allMatch(ele -> ele.contains(product));
        } catch (StaleElementReferenceException e) {
            return searchResultDropdown.stream().map(ele -> ele.getAttribute("aria-label")).allMatch(ele -> ele.contains(product));
        } finally {
            ReusableUtils.takeScreenshot("screenshots", driver);
        }
    }

    /**
     * Method to selectResultFromDropdown
     *
     * @param product
     * @param driver
     */
    public void selectResultFromDropdown(String product, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(xpath1));
                wait.until(ExpectedConditions.visibilityOfAllElements(elements));
                elements.get(1).click();
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }

    }

    /**
     * \Method to validate new Tab is opened
     *
     * @param product
     * @param driver
     * @return
     */
    public boolean validateProductIsOpenedInNewTab(String product, WebDriver driver) {
        String originalWindow = driver.getWindowHandle();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        // Switch to the new tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }// Validate the product page is opened
        return driver.getTitle().contains(product);

    }

    /**
     * Method to click On Product
     *
     * @param product
     */
    public void clickOnProduct(String product, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfAllElements(resultProduct));
        resultProduct.stream().findAny().get().click();
    }

    /**
     * Method to click on ELement with text
     *
     * @param text
     * @param driver
     */
    public void clickOnElementWithText(String text, WebDriver driver) {
        WebElement ele = driver.findElement(By.xpath(String.format(textXpath, text)));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(ele)).click();
    }

    public void selectAppleCategoryAndProduct(String categoryName, String productName, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement categoryElement = driver.findElement(By.xpath(String.format(appleCategoryXpath, categoryName)));
        wait.until(ExpectedConditions.visibilityOf(categoryElement));
        categoryElement.click();
        List<WebElement> product = driver.findElements(By.xpath(String.format(appleProductXpath, categoryName)));
        wait.until(ExpectedConditions.visibilityOfAllElements(product));
        product.stream().filter(ele -> ele.getText().contentEquals(productName)).findFirst().get().click();
    }

    /**
     * Method to hover over watch images and verify "Quick view" is displayed
     *
     * @param driver
     * @return boolean
     */
    public boolean hoverOverWatchesAndVerifyQuickLook(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Actions actions = new Actions(driver);
        boolean isQuickViewDisplayed = true;

        for (int i = 0; i < watches.size(); i++) {
            actions.scrollToElement(watches.get(i));
            actions.moveToElement(watches.get(i)).perform();
            try {
                WebElement quickView = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class = 'EditorialTile__container__MuV7R']//a[contains(@aria-label, 'Smartwatch')])[" + i + "]/..//div//button[last()]")));
                if (!quickView.isDisplayed()) {
                    actions.scrollToElement(quickView);
                    ReusableUtils.takeScreenshot("screenshot2", driver);
                    isQuickViewDisplayed = false;
                    break;
                }
            } catch (Exception e) {
                isQuickViewDisplayed = false;
                break;
            }
        }
        return isQuickViewDisplayed;
    }

    public boolean verifyModalProductDetails(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            // Wait for the modal to be visible
            WebElement modal = wait.until(ExpectedConditions.visibilityOf(watches.get(1)));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class = 'EditorialTile__container__MuV7R']//a[contains(@aria-label, 'Smartwatch')])[1]/..//div//h3")));
            String modalProductName = element.getText();

            WebElement quickLook = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class = 'EditorialTile__container__MuV7R']//a[contains(@aria-label, 'Smartwatch')])[1]/..//div//button[last()]")));
            quickLook.click();
            WebElement modalProductNameElementOnQuickLook = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class = 'ProductShowcase__title__SBCBw']")));
            String modalProductNameOnQuickLook = modalProductNameElementOnQuickLook.getText();
            ReusableUtils.takeScreenshot("watchscreenshot", driver);
            return modalProductNameOnQuickLook.contains(modalProductName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
