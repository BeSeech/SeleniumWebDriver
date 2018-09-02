package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnboardingPage extends BasicPage {
    public WebElement inputDomain;
    public WebElement submitButton;

    public OnboardingPage(Context context)
    {
        super(context);
        this.pageUrl = "/onboarding";
        System.out.println("Create Onboarding Page");
    }

    public boolean isLoaded() {
        return  getContext().getWebDriver().getTitle().startsWith("AirSlate - Onboarding");
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("domain")));

        inputDomain = getContext().getWebDriver().findElement(By.name("domain"));
        submitButton = getContext().getWebDriver().findElement(By.cssSelector("button[type='submit']"));
    }

    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
        submitButton.click();
    }

    public void setSubdomainText(String text) {
        System.out.println(Tools.getSpace(1) + "Set subdomain text as '" + text + "'");
        inputDomain.clear();
        inputDomain.sendKeys(getContext().uid);
    }
}

