package org.epam.testing;

import org.epam.testing.pageobjects.EpamLoginPage;
import org.epam.testing.components.FailureListener;
import org.epam.testing.testdata.LoginData;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertTrue;

/**
 * Created by AlexSh on 18.10.2016.
 */


@Listeners({ FailureListener.class })
public class LoginTest {

    //it is static & public. It is for all. Love it or leave it.
    public static  WebDriver myPersonalDriver;
    public static EpamLoginPage epamLoginPage;


    /*
    in testng.xml
    f.e. browser: "firefox","chrome","ie", "opera"
    f.e. pathToDriver: "D:\\PersonalDrivers\\geckodriver.exe",
     */

    @BeforeSuite
    @Parameters({"browser", "pathToDriver","loginPageUrl"})
    public void beforeSuite(@Optional("phantom") String browser,
                            @Optional("D:/PersonalDrivers/phantomjs-2.1.1-windows/bin/phantomjs.exe") String pathToDriver,
                            @Optional("https://jdi-framework.github.io/tests/") String loginPageUrl) {



        ArrayList<String> browsersHerd = new ArrayList<String>(Arrays.asList(new String[]{"firefox", "chrome", "ie", "opera","phantom"}));

        //System's properties we have to set to use drivers
        String[] sysProperty = new String[]{
                "webdriver.gecko.driver",
                "webdriver.opera.driver",
                "webdriver.ie.driver",
                "webdriver.chrome.driver",
                "phantomjs.binary.path"
        };
        //Checking whether file is exist
        File f = new File(pathToDriver);
        if (!(f.exists() && !f.isDirectory())) {
            System.out.println(!f.isDirectory());
            System.out.println(f.exists());
            System.out.println(pathToDriver);
            System.out.println("Error! Check your browser's path in testng.xml!");
            Assert.fail("Error! Check your browser's path in testng.xml!");
        }
        //Checking  whether browser of correct type
        if (!browsersHerd.contains(browser)) {
            System.out.println("Error! Check your browser type in testng.xml!");
            System.out.println("firefox,chrome,ie,opera");
            Assert.fail("Error! Check your browser type testng.xml!");
        }


        //Since Java 7 we can use String
        switch (browser.toLowerCase()) {
            case "firefox":
                System.setProperty(sysProperty[0], pathToDriver);
                myPersonalDriver = new FirefoxDriver();
                break;
            case "opera":
                System.setProperty(sysProperty[1], pathToDriver);
                myPersonalDriver = new OperaDriver();
                break;
            case "ie":
                System.setProperty(sysProperty[2], pathToDriver);
                myPersonalDriver = new InternetExplorerDriver();
                break;
            case "chrome":
                System.setProperty(sysProperty[3], pathToDriver);
                myPersonalDriver=new ChromeDriver();



                break;
            case "phantom":
                System.setProperty(sysProperty[4], pathToDriver);
                Capabilities caps = new DesiredCapabilities().phantomjs();
                ((DesiredCapabilities) caps).setJavascriptEnabled(true);
                ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
                myPersonalDriver = new  PhantomJSDriver(caps);

        }



        System.out.println(browser + " " + pathToDriver);


        //At a "very beginning" we open fist page. This is really door to eternity

        epamLoginPage = new EpamLoginPage(myPersonalDriver);


        epamLoginPage.open();

        //new WebDriverWait(myPersonalDriver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(.,'EPAM framework Wishes')]")));
        try {
        Thread.sleep(8000);
        }
        catch ( Exception e){
        }
    }
    @Step("LOGIN")
    @Test(dataProviderClass=LoginData.class, dataProvider="dataforlogin")
    public void tryLogin(boolean testType, String accountName, String accountPwd) {
        System.out.println("logging into the account");



        //if login or logout menu is closed we will open it
        if (!(epamLoginPage.isLoginOrLogoutMenuOpen())) {
              epamLoginPage.openLoginOrLogoutMenu();
        }


        //if we've opened logout menu (look at the code above) we have to log out since we were in "logged in" state
        //and have not input fields for our data to log in
        if  (epamLoginPage.isLogoutMenuOpen()) {
              epamLoginPage.logout();
         }

        epamLoginPage.login(accountName,accountPwd);


        if (! testType)
            //Is login failed? It has to be...
            assertTrue(epamLoginPage.isLoginFailed());
        else {
            //Is login succeed? It has to be...
            assertTrue(epamLoginPage.isLoginSucceed());
        }
    }


    @AfterSuite
    public void afterSuite() {
        //Close the driver
        myPersonalDriver.close();
        myPersonalDriver.quit();

    }


}
