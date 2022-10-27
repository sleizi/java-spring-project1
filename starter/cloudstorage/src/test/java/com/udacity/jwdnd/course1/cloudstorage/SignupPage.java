package com.udacity.jwdnd.course1.cloudstorage;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPage extends BasePage {
    public SignupPage(WebDriver webDriver) {
        super(webDriver);
    }

    @FindBy(css = "#inputFirstname")
    private WebElement firstnameField;

    @FindBy(css = "#inputLastname")
    private WebElement lastnameField;

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#buttonSubmit")
    private WebElement submitButton;

    public void signup(String firstname, String lastname, String username, String password) {
        setValue(firstnameField, firstname);
        setValue(lastnameField, lastname);
        setValue(usernameField, username);
        setValue(passwordField, password);
        clickOnButton(submitButton);
    }
}
