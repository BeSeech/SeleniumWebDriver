package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasicPage {
    public WebElement getStartedButton;
    public WebElement userNameInput;
    public WebElement passwordInput;
    public WebElement submitButton;

    public LoginPage(Context context) {
        super(context);
        this.pageUrl = "/userNameInput";
        System.out.println("Create Login Page");
    }

    public void doLoginUser(String userName, String password) {
        initElements();
        setUserNameInput(userName);
        setPasswordInput(password);
        submit();
    }

    public boolean isLoaded() {
        return getContext().getWebDriver().getTitle().startsWith("AirSlate Login");
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Get started")));

        getStartedButton = getWebDriver().findElement(By.linkText("Get started"));
        userNameInput = getWebDriver().findElement(By.id("username"));
        passwordInput = getWebDriver().findElement(By.id("password"));
        submitButton = getContext().getWebDriver().findElement(By.cssSelector("button[type='submit']"));
    }

    public void setUserNameInput(String value) {
        System.out.println(Tools.getSpace(1) + "Set User Name text as '" + value + "'");
        userNameInput.clear();
        userNameInput.sendKeys(value);
    }

    public WebElement getUserNameInput() {
        return this.userNameInput;
    }

    public void setPasswordInput(String value) {
        System.out.println(Tools.getSpace(1) + "Set Password text as '" + value + "'");
        passwordInput.clear();
        passwordInput.sendKeys(value);
    }

    public void getStartedClick() {
        System.out.println(Tools.getSpace(1) + "Click on Get Started");
        getStartedButton.click();
    }

    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
        submitButton.click();
    }

}

