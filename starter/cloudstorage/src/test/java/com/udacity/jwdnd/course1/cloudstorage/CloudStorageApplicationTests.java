package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;

	private static final String USERNAME = "seleniumtest";
	private static final String PASSWORD = "$eleniumpa$$word";
	private static final String FIRSTNAME = "Selenium";
	private static final String LASTNAME = "Test";
	private static final String NOTE_TITLE = "TestNote";
	private static final String NOTE_DESCRIPTION = "Test Note Description";
	private static final String CREDENTIAL_URL = "http://testurl";
	private static final String CREDENTIAL_USERNAME = "TestUser";
	private static final String CREDENTIAL_PASSWORD = "TestPassword";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		baseUrl = "http://localhost:" + port;
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseUrl + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void signupAndLoginUserSuccess() {
		signup(driver);
		login(driver);
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void loginFailsIfNotSignedup() {
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
	public void redirectToLoginIfNotSignedIn() {
		driver.get(baseUrl + "/home");

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void createAndReadNewNote() {
		signup(driver);
		login(driver);
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
	public void createAndEditAndReadNewNote() {
		signup(driver);
		login(driver);
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
	public void createAndDeleteNewNote() {
		signup(driver);
		login(driver);
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
	public void createAndReadNewCredential() {
		signup(driver);
		login(driver);
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
	public void createAndEditAndReadNewCredential() {
		signup(driver);
		login(driver);
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
	public void createAndDeleteNewCredential() {
		signup(driver);
		login(driver);
		HomePage homePage = new HomePage(driver);
		driver.get(baseUrl + "/home");

		// Create
		homePage.createNewCredential(CREDENTIAL_URL, CREDENTIAL_USERNAME, CREDENTIAL_PASSWORD);
		homePage.createNewCredential(CREDENTIAL_URL+"2", CREDENTIAL_USERNAME+"2", CREDENTIAL_PASSWORD+"2");
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

	private void signup(WebDriver driver) {
		driver.get(baseUrl + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
	}

	private void login(WebDriver driver) {
		driver.get(baseUrl + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(USERNAME, PASSWORD);
	}
}
