package com.beseech.page;


import com.beseech.context.Context;
import com.beseech.tools.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConfirmEmail extends BasicPage {
    public WebElement[] digits = new WebElement[6] ;
    public WebElement submitButton;
    public WebElement resendButton;

    public ConfirmEmail(Context context)
    {
        super(context);
        this.pageUrl = "/onboarding/account";
        System.out.println("Create Confirm Email Page");
    }

    public boolean isLoaded() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Confirm your email")));

        return getContext().getWebDriver().findElement(By.linkText("Confirm your email")) != null;
    }

    @Override
    public void initElements() {
        WebDriverWait wait = new WebDriverWait(getContext().getWebDriver(), WAIT_FOR_ELEMENT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("0")));

        for (int i = 0; i < 6; i++){
            digits[i] = getContext().getWebDriver().findElement(By.name(Integer.toString(i)));
        }

        WebElement footer = getContext().getWebDriver().findElement(By.cssSelector("footer[class='modal-footer']"));
        submitButton = footer.findElement(By.cssSelector("button[type='submit']"));
        resendButton = footer.findElement(By.cssSelector("button[type='reset']"));
    }

    public void submit() {
        System.out.println(Tools.getSpace(1) + "Submit");
        System.out.println(submitButton.getText());
        submitButton.click();
    }

    public void setCode(String code6Digits) {
        System.out.println(Tools.getSpace(1) + "Set code value as '" + code6Digits + "'");
        for (int i = 0; i < 6; i++){
            digits[i].clear();
            digits[i].sendKeys(String.valueOf(code6Digits.charAt(i)));
        }
    }
}

