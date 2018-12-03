package tests;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.CastAndCrewPageObject;
import common.ApplicationProperties;
import pages.MainPageObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class executes tests to ensure the 'cast and crew' section of the page works as expected.
 */
public class CastAndCrewPageTest {
    private CastAndCrewPageObject castAndCrewPageObject;
    private MainPageObject mainPageObject;
    private ChromeOptions chromeOptions;
    private FirefoxOptions firefoxOptions;
    private static final ThreadLocal<RemoteWebDriver> drivers = new ThreadLocal<RemoteWebDriver>();

    /**
     * This methods runs before the class runs and instantiates driver as chrome or firefox driver depending on the argument given.
     * Also the method sets different attributes for driver again depending on the arguments.
     */
    @Parameters({"os", "browser", "browserVersion"})
    @BeforeClass(alwaysRun = true)
    public void setupTestBeforeClass(String os, String browser, String browserVersion) {
        Platform platform = Platform.fromString(os.toUpperCase());
        if (browser.equalsIgnoreCase(ApplicationProperties.CHROME_STRING)) {
            this.chromeOptions = new ChromeOptions();
            chromeOptions.setCapability(ApplicationProperties.PLATFORM_STRING, platform);
            chromeOptions.setCapability(ApplicationProperties.BROWSER_VERSION_STRING, browserVersion);
        } else if (browser.equalsIgnoreCase(ApplicationProperties.FIREFOX_STRING)) {
            this.firefoxOptions = new FirefoxOptions();
            firefoxOptions.setCapability(ApplicationProperties.PLATFORM_STRING, platform);
            firefoxOptions.setCapability(ApplicationProperties.BROWSER_VERSION_STRING, browserVersion);
        }
    }

    /**
     * This method runs before every method and creates a new driver (chrome or firefox) and navigates to the URL of website.
     */
    @BeforeMethod(alwaysRun = true)
    public void setupTestBeforeMethod(ITestResult testResult) throws MalformedURLException {
        RemoteWebDriver driver = null;
        if (chromeOptions != null) {
            driver = new RemoteWebDriver(new URL(ApplicationProperties.LOCALHOST_URL), chromeOptions);
        } else if (firefoxOptions != null) {
            driver = new RemoteWebDriver(new URL(ApplicationProperties.LOCALHOST_URL), firefoxOptions);
        }
        drivers.set(driver);
        castAndCrewPageObject = new CastAndCrewPageObject(driver());
        mainPageObject = new MainPageObject(driver());
        driver().navigate().to(ApplicationProperties.webPageURL);
    }

    /**
     * This method runs after each test and quites browser window and sets driver to 'null'.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDownTestAfterMethod() {
        RemoteWebDriver driver = driver();
        driver.quit();
        driver = null;
    }

    /**
     * This method returns driver instance if it's value is not null.
     */
    private RemoteWebDriver driver() {
        RemoteWebDriver driver = drivers.get();
        if (driver == null) {
            throw new IllegalStateException("Driver should have not been null.");
        }
        return driver;
    }


    /**
     * Verify that actor names are displayed in the 'cst and crew' section of web-page.
     */
    @Test(groups = {"smoke", "MainPageTest"})
    public void verifyActorNames() throws InterruptedException {
        System.out.println("==========================");
        System.out.println("verifyActorNames : current thread id : " + Thread.currentThread().getId());
        System.out.println("==========================");
        castAndCrewPageObject.navigateToCastAndCrewSection();

        boolean isSubcategoriesDisplayed = castAndCrewPageObject.isActorNamesDisplayed();
        Assert.assertTrue(isSubcategoriesDisplayed, ApplicationProperties.ACTORS_NAMES_ARE_NOT_DISPLAYED_PROPERLY);
    }

    /**
     * Verify that header is displayed in the 'cst and crew' section of web-page.
     */
    @Test(groups = {"MainPageTest"})
    public void verifyHeaderIsDisplayed() throws InterruptedException {
        System.out.println("==========================");
        System.out.println("verifyHeaderIsDisplayed : current thread id : " + Thread.currentThread().getId());
        System.out.println("==========================");
        castAndCrewPageObject.navigateToCastAndCrewSection();

        boolean isPopularDressesDisplayed = mainPageObject.isHeaderItemsDisplayed();
        Assert.assertTrue(isPopularDressesDisplayed, ApplicationProperties.HEADER_IS_NOT_DISPLAYED_PROPERLY);
    }
}
