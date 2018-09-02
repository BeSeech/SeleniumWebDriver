package com.beseech.page;


import com.beseech.context.Context;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home extends BasicPage {
    public WebElement sideBarLogo;
    public WebElement logoText;

    public Home(Context context)
    {
        super(context);
        this.pageUrl = "";
        System.out.println("Create Home Page");
    }

    public boolean isLoaded() {
        return  getContext().getWebDriver().getTitle().startsWith("AirSlate");
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("sidebar__logo")));

        sideBarLogo = getWebDriver().findElement(By.className("sidebar__logo"));
        logoText = sideBarLogo.findElement(By.className("logo__text"));
    }

    public String getLogoText() {
        return logoText.getText();
    }

}

