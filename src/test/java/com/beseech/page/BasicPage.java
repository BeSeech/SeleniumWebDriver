package com.beseech.page;

import com.beseech.context.Context;
import org.openqa.selenium.WebDriver;

public class BasicPage implements BasicPageInterface {
    private Context _context;
    public String pageUrl = "";
    public final static int WAIT_FOR_ELEMENT_SECONDS = 60;


    BasicPage(Context context) {
        this._context = context;
    }

    public String getUrl() {
        return _context.getBaseUrl() + pageUrl;
    }

    public Context getContext() {
        return _context;
    }

    public WebDriver getWebDriver() {
        return _context.getWebDriver();
    }

    public void load() {
        getWebDriver().navigate().to(getUrl());
    }

    public boolean isLoaded()
    {
        return false;
    }

    public void initElements() {

    }

    public boolean loadWithInitializationAndCheck() {
        load();

        initElements();
        return isLoaded();
    }

}

