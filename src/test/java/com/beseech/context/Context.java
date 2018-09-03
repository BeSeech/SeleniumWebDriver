package com.beseech.context;

import com.beseech.model.TestConfig;
import com.beseech.tools.Tools;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class Context {
    private WebDriver _webDriver;
    private String _qaToken;
    private String _protocol;
    private String _urlSubdomainPart;
    private String _urlPostfix;
    private String _apiBasicUrl;

    public String uid;

    public String getUrlSubdomainPart() {
        return _urlSubdomainPart;
    }

    public void setUrlSubdomainPart(String urlSubdomainPart) {
        this._urlSubdomainPart = urlSubdomainPart;
    }

    public Context(WebDriver webDriver, TestConfig testConfig) {
        _protocol = testConfig.protocol;
        _urlSubdomainPart = testConfig.urlSubdomainPart;
        _qaToken = testConfig.qaToken;
        _apiBasicUrl = testConfig.apiBasicUrl;
        _webDriver = webDriver;
        _urlPostfix = testConfig.urlPostfix;
        uid = UUID.randomUUID().toString();
        System.out.println("Create test context ");
        System.out.println(Tools.getSpace(1) + "web url: " + getBaseUrl());
        System.out.println(Tools.getSpace(1) + "Api url: " + getApiBaseUrl());
    }

    public String getQaToken() {
        return _qaToken;
    }

    public WebDriver getWebDriver() {
        return _webDriver;
    }

    public String getBaseUrl() {
        return _protocol + "://" + _urlSubdomainPart + "." + _urlPostfix;
    }

    public String getBaseInviteUrl(String orgSubdomainToJoin) {
        return _protocol + "://" + orgSubdomainToJoin + "." + _urlPostfix;
    }

    public String getApiBaseUrl() {
        return _protocol + "://" + _apiBasicUrl;
    }

    public String getUrlPostfix() {
        return _urlPostfix;
    }

    public void сlose() {
        _webDriver.close();
        _webDriver.quit();
    }
}
