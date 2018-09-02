package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmailJoin extends BasicPage {
    public WebElement inputFirstName;
    public WebElement inputLastName;
    public WebElement inputEmail;
    public WebElement inputPassword;
    public WebElement submitButton;
    public WebElement currentEmailDomain;

    public EmailJoin(Context context)
    {
        super(context);
        this.pageUrl = "/invite";
        System.out.println("Create Email Join Page");

    }

    public void doEmailJoinUser(String firstName, String lastName, String password, String email) {
        initElements();
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setEmail(email.replaceAll(currentEmailDomain.getText(), ""));
        submit();
    }

    public boolean isLoaded() {
        return  getContext().getWebDriver().getTitle().startsWith("AirSlate - Onboarding");
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("firstName")));

        inputFirstName = getWebDriver().findElement(By.name("firstName"));
        inputLastName = getWebDriver().findElement(By.name("lastName"));
        inputEmail = getWebDriver().findElement(By.name("email"));
        inputPassword = getWebDriver().findElement(By.name("password"));
        submitButton = getWebDriver().findElement(By.cssSelector("button[type='submit']"));
        currentEmailDomain = getWebDriver().findElement(By.id("react-select-2--value-item"));

    }

    public void setFirstName(String value) {
        System.out.println(Tools.getSpace(1) + "Set First Name text as '" + value + "'");
        inputFirstName.clear();
        inputFirstName.sendKeys(value);
    }
    public void setLastName(String value) {
        System.out.println(Tools.getSpace(1) + "Set Last Name text as '" + value + "'");
        inputLastName.clear();
        inputLastName.sendKeys(value);
    }
    public void setEmail(String value) {
        System.out.println(Tools.getSpace(1) + "Set Email text as '" + value + "'");
        inputEmail.clear();
        inputEmail.sendKeys(value);
    }
    public void setPassword(String value) {
        System.out.println(Tools.getSpace(1) + "Set Password text as '" + value + "'");
        inputPassword.clear();
        inputPassword.sendKeys(value);
    }

    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
        submitButton.click();
    }
}

