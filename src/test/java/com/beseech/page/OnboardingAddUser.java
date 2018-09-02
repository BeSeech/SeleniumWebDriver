package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnboardingAddUser extends BasicPage {
    public WebElement submitButton;

    public OnboardingAddUser(Context context)
    {
        super(context);
        this.pageUrl = "/onboarding/add-User";
        System.out.println("Create Onboarding Add User Page");
    }

    public boolean isLoaded() {
        return  getContext().getWebDriver().getTitle().startsWith("AirSlate - Onboarding");
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("registration__actions")));

        WebElement footer = getContext().getWebDriver().findElement(By.className("registration__actions"));
        submitButton = footer.findElement(By.className("button--primary"));
    }


    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
        submitButton.click();
    }
}

