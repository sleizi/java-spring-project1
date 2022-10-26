package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {
    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#buttonSubmit")
    private WebElement submitButton;

    public void login(String username, String password) {
        setValue(usernameField, username);
        setValue(passwordField, password);
        clickOnButton(submitButton);
    }
}
