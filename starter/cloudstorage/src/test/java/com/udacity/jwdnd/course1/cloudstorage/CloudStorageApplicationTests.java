package com.udacity.jwdnd.course1.cloudstorage;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "p4ssw0rd";
    private static final String FIRSTNAME = "Tester";
    private static final String LASTNAME = "Udacity";
    private static final String NOTE_TITLE = "TestNote";
    private static final String NOTE_DESCRIPTION = "Note Description";
    private static final String CREDENTIAL_URL = "http://testurl";
    private static final String CREDENTIAL_USERNAME = "TestUser";
    private static final String CREDENTIAL_PASSWORD = "TestPassword";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void beforeEach() {
        baseUrl = "http://localhost:" + port;
        this.driver = new ChromeDriver();
    }

    @AfterEach
    void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup(firstName, lastName, userName, password);
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(userName, password);
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a successful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    @Order(1)
    void testRedirection() {
        // Create a test account
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    void testBadUrl() {
        // Create a test account
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    void testLargeUpload() {
        // Create a test account
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    void getLoginPage() {
        driver.get(baseUrl + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    void getSignupPage() {
        driver.get(baseUrl + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    void signupAndLoginUserSuccess() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        Assertions.assertEquals("Home", driver.getTitle());
    }

    @Test
    void loginFailsIfNotSignedup() {
        final String username = "hacker";
        final String password = "badpassword";

        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        WebElement errorMessage = driver.findElement(By.id("containerLoginError"));

        Assertions.assertEquals("Login", driver.getTitle());
        Assertions.assertEquals("Invalid username or password", errorMessage.getText());
    }

    @Test
    void redirectToLoginIfNotSignedIn() {
        driver.get(baseUrl + "/home");

        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    void createAndReadNewNote() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);

        // Create
        homePage.createNewNote(NOTE_TITLE, NOTE_DESCRIPTION);

        // Read
        List<String> noteTitles = homePage.getNoteTitles();
        List<String> noteDescriptions = homePage.getNoteDescriptions();
        Assertions.assertEquals(NOTE_TITLE, noteTitles.get(0));
        Assertions.assertEquals(NOTE_DESCRIPTION, noteDescriptions.get(0));

        // Cleanup
        homePage.deleteNote(0);
    }

    @Test
    void createAndEditAndReadNewNote() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);

        // Create
        homePage.createNewNote(NOTE_TITLE, NOTE_DESCRIPTION);

        // Edit
        homePage.editNote(0, NOTE_TITLE + "EDIT", NOTE_DESCRIPTION + "EDIT");

        // Read
        List<String> noteTitles = homePage.getNoteTitles();
        List<String> noteDescriptions = homePage.getNoteDescriptions();
        Assertions.assertEquals(NOTE_TITLE + "EDIT", noteTitles.get(0));
        Assertions.assertEquals(NOTE_DESCRIPTION + "EDIT", noteDescriptions.get(0));

        // Cleanup
        homePage.deleteNote(0);
    }

    @Test
    void createAndDeleteNewNote() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);

        // Create
        homePage.createNewNote(NOTE_TITLE, NOTE_DESCRIPTION);
        homePage.createNewNote(NOTE_TITLE + "2", NOTE_DESCRIPTION + "2");
        List<String> noteTitles = homePage.getNoteTitles();
        List<String> noteDescriptions = homePage.getNoteDescriptions();
        Assertions.assertEquals(2, noteTitles.size());
        Assertions.assertEquals(2, noteDescriptions.size());

        // Delete
        homePage.deleteNote(1);
        noteTitles = homePage.getNoteTitles();
        noteDescriptions = homePage.getNoteDescriptions();
        Assertions.assertEquals(1, noteTitles.size());
        Assertions.assertEquals(1, noteDescriptions.size());
        Assertions.assertEquals(NOTE_TITLE, noteTitles.get(0));
        Assertions.assertEquals(NOTE_DESCRIPTION, noteDescriptions.get(0));

        // Cleanup
        homePage.deleteNote(0);
    }

    @Test
    void createAndReadNewCredential() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);

        // Create
        homePage.createNewCredential(CREDENTIAL_URL, CREDENTIAL_USERNAME, CREDENTIAL_PASSWORD);

        // Read
        List<String> credentialUrls = homePage.getCredentialUrls();
        List<String> credentialUsernames = homePage.getCredentialUsernames();
        List<String> credentialPasswords = homePage.getCredentialPasswords();
        Assertions.assertEquals(CREDENTIAL_URL, credentialUrls.get(0));
        Assertions.assertEquals(CREDENTIAL_USERNAME, credentialUsernames.get(0));
        Assertions.assertNotEquals(CREDENTIAL_PASSWORD, credentialPasswords.get(0)); // must not show unencrypted password

        // Cleanup
        homePage.deleteCredential(0);
    }

    @Test
    void createAndEditAndReadNewCredential() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);

        // Create
        homePage.createNewCredential(CREDENTIAL_URL, CREDENTIAL_USERNAME, CREDENTIAL_PASSWORD);

        // Edit
        homePage.editCredential(0, CREDENTIAL_URL + "EDIT", CREDENTIAL_USERNAME + "EDIT", CREDENTIAL_PASSWORD + "EDIT");

        // Read
        List<String> credentialUrls = homePage.getCredentialUrls();
        List<String> credentialUsernames = homePage.getCredentialUsernames();
        List<String> credentialPasswords = homePage.getCredentialPasswords();
        Assertions.assertEquals(CREDENTIAL_URL + "EDIT", credentialUrls.get(0));
        Assertions.assertEquals(CREDENTIAL_USERNAME + "EDIT", credentialUsernames.get(0));
        Assertions.assertNotEquals(CREDENTIAL_PASSWORD + "EDIT", credentialPasswords.get(0)); // must not show unencrypted password

        // Cleanup
        homePage.deleteCredential(0);
    }

    @Test
    void createAndDeleteNewCredential() {
        doMockSignUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        doLogIn(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);
        driver.get(baseUrl + "/home");

        // Create
        homePage.createNewCredential(CREDENTIAL_URL, CREDENTIAL_USERNAME, CREDENTIAL_PASSWORD);
        homePage.createNewCredential(CREDENTIAL_URL + "2", CREDENTIAL_USERNAME + "2", CREDENTIAL_PASSWORD + "2");
        List<String> credentialUrls = homePage.getCredentialUrls();
        List<String> credentialUsernames = homePage.getCredentialUsernames();
        List<String> credentialPasswords = homePage.getCredentialPasswords();
        Assertions.assertEquals(2, credentialUrls.size());
        Assertions.assertEquals(2, credentialUsernames.size());
        Assertions.assertEquals(2, credentialPasswords.size());

        // Delete
        homePage.deleteCredential(1);
        credentialUrls = homePage.getCredentialUrls();
        credentialUsernames = homePage.getCredentialUsernames();
        credentialPasswords = homePage.getCredentialPasswords();
        Assertions.assertEquals(1, credentialUrls.size());
        Assertions.assertEquals(1, credentialUsernames.size());
        Assertions.assertEquals(1, credentialPasswords.size());

        // Cleanup
        homePage.deleteCredential(0);
    }


}
