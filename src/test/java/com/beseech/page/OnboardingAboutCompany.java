package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnboardingAboutCompany extends BasicPage {
    public WebElement inputName;
    public WebElement inputIndustry;
    public WebElement inputCompanySize;
    public WebElement submitButton;

    public OnboardingAboutCompany(Context context)
    {
        super(context);
        this.pageUrl = "/onboarding/about-company";
        System.out.println("Create Onboarding About Company Page");
    }

    public boolean isLoaded() {
        return  getContext().getWebDriver().getTitle().startsWith("AirSlate - Onboarding");
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("name")));

        inputName = getContext().getWebDriver().findElement(By.name("name"));
        inputIndustry = getContext().getWebDriver().findElement(By.id("react-select-2--value"));
        inputCompanySize = getContext().getWebDriver().findElement(By.id("react-select-3--value"));
        submitButton = getContext().getWebDriver().findElement(By.cssSelector("button[type='submit']"));
    }

    public void setName(String value) {
        System.out.println(Tools.getSpace(1) + "Set Name text as '" + value + "'");
        inputName.clear();
        inputName.sendKeys(value);
    }

    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
        submitButton.click();
    }
}

