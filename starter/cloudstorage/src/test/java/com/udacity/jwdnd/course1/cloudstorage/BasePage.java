package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    private WebDriver driver;

    public BasePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.driver = webDriver;
    }

    protected void clickOnButton(WebElement ele) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", ele);
    }

    protected String getValue(WebElement ele) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        return (String) executor.executeScript("return arguments[0].value;", ele);
    }

    protected void setValue(WebElement ele, String newValue) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].value='" + newValue + "';" , ele);
    }
}
