package org.epam.testing.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.concurrent.TimeUnit;

/**
 *
 * Created by AlexSh on 18.10.2016.
 */
public class EpamLoginPage {

    //LoginLogout Menu Open
    private By menuOpen = By.xpath("//li[@class='dropdown uui-profile-menu open']");
    //LoginLogout Menu Closed
    private By menuClosed = By.xpath("//li[@class='dropdown uui-profile-menu']");

    //Login Menu
    private By inputAccountName = By.xpath("//input[@id='Login']");
    private By inputAccountPwd = By.xpath("//input[@id='Password']");
    private By enterButton = By.xpath("//button[@class='uui-button dark-blue btn-login']/span[text()='Enter']");

    //Logout Menu
    private By logoutButton = By.xpath("//div[@class='logout']/button[@class='uui-button dark-blue btn-login'][@type='submit']/span[text()='Logout']");

    //We will use the message to check that login is failed
    private By loginFailed = By.xpath("//span[@class='login-txt'][text()='* Login Faild']");


    //If login is succeed....
    //We will use a logoutButton to check whether we are in "login state"
    //Also we can check an access to resources that successful login gives us but not now :)

    private By buttonToOpenOrCloseMenu = By.xpath("//a[@class='dropdown-toggle'][@href='#']");

    private String epamLoginPageUrl = "https://jdi-framework.github.io/tests/";

    private WebDriver driverHere;

    //Two constructors
    public EpamLoginPage( WebDriver driver ) {
        this.driverHere = driver;
        epamLoginPageUrl = "https://jdi-framework.github.io/tests/";
    }
    public EpamLoginPage ( WebDriver driver, String pageUrl) {
        this.driverHere = driver;
        this.epamLoginPageUrl = pageUrl;
    };



    public void open() {

    driverHere.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);


    driverHere.get(epamLoginPageUrl);
    driverHere.manage().window().maximize();
}

    public void open(String strUrl) {

        driverHere.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driverHere.get(strUrl);
        driverHere.manage().window().maximize();
    }



    public void close () {
        driverHere.close();
    }


public void login( String accountName, String accountPwd) {

    driverHere.findElement(inputAccountName).click();
    driverHere.findElement(inputAccountName).clear();
    driverHere.findElement(inputAccountName).sendKeys(accountName);

    driverHere.findElement(inputAccountPwd).click();
    driverHere.findElement(inputAccountPwd).clear();
    driverHere.findElement(inputAccountPwd).sendKeys(accountPwd);

    driverHere.findElement(enterButton).click();
}

public boolean isLoginOrLogoutMenuOpen() {
    return driverHere.findElements(menuOpen).size() !=0 ;
}

public boolean isLoginOrLogoutMenuClosed() {
        return driverHere.findElements(menuClosed).size() !=0 ;
}

public void  openLoginOrLogoutMenu() {
        driverHere.findElement(buttonToOpenOrCloseMenu).click();
}

public void logout() {
        driverHere.findElement(logoutButton).click();
}

public boolean isLogoutMenuOpen() {
    return driverHere.findElements(menuOpen).size() !=0
            && driverHere.findElements(logoutButton).size()!=0;

}

public boolean isLoginFailed (){
    return (driverHere.findElements(loginFailed)).size() != 0;
}

public boolean isLoginSucceed(){
    return (driverHere.findElements(logoutButton)).size() != 0;
}

public boolean isLoggedIn(){
    return isLoginSucceed();
}


}

