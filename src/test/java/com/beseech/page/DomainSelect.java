package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class DomainSelect extends BasicPage {
    public List<WebElement> organizations;

    public DomainSelect(Context context)
    {
        super(context);
        this.pageUrl = "/domain-select";
        System.out.println("Create Domain Select Page");
    }

    public void launchOrganization(int index) {
        System.out.println("Launch Organization: " + organizations.get(index).findElement(By.tagName("h5")).getText());
        organizations.get(index).findElement(By.className("button--secondary")).click();
    }

    public boolean isLoaded() {
        return  getContext().getWebDriver().getTitle().startsWith("AirSlate Domain select");
    }

    public void printOrganizations() {
        System.out.println(Tools.getSpace(1) + "Organizations: ");
        for (int i = 0; i < organizations.size(); i++) {
            System.out.println(Tools.getSpace(2) + organizations.get(i).findElement(By.tagName("h5")).getText());
        }
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("button--primary")));

        organizations = getWebDriver().findElements(By.className("workspace-item"));
   }

    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
    }
}

