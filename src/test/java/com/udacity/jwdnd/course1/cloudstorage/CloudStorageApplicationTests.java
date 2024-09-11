package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.io.File;

import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.Random;

//import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() { WebDriverManager.chromedriver().setup(); }

	@BeforeEach
	void setup() {
		driver = WebDriverManager.chromedriver().create();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}


	// Test home page is not accessible without login

	// Test home page is not accessible without login
	@Test
	public void testHomePageNotAccessibleWithoutLogin() {
		// Open the home page URL directly
		driver.get("http://localhost:" + this.port + "/home");

		// Check the current URL to ensure it redirects to the login page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/login"), "User was not redirected to the login page");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);

	}


	// New user signs up, logs in, accesses home page, logs out and is not allowed to access home page
	@Test
	public void testUserSignUpLoginLogout() throws InterruptedException {

		//StringBuilder sb = getStringBuilder(5);
		String sb=getStringBuilder(5);

		// Sign up a new user page
		driver.get("http://localhost:" + this.port + "/signup");

		// Fill out the sign-up form
		WebElement userFirstName = driver.findElement(By.id("inputFirstName"));
		WebElement userLastName = driver.findElement(By.id("inputLastName"));
		WebElement usernameInput = driver.findElement(By.id("inputUsername"));
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		WebElement submitButton = driver.findElement(By.id("buttonSignUp"));

		String firstName = "FirstName" + sb.toString();
		String lastName = "LastName" + sb.toString();
		String username = sb.toString();
		String password = numberToString(5);

		userFirstName.sendKeys(firstName);
		userLastName.sendKeys(lastName);
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		submitButton.click();


		//Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
		WebElement goToLogin = driver.findElement(By.linkText("login"));
		goToLogin.click();

		// Check if signup was successfully redirected to a login page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/login"), "User was not redirected to the login page after sign up");


		// Log in the new user
		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();

		// Check if the user is redirected to the home page
		currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");


		//Log out the user
		WebElement logoutButton = driver.findElement(By.id("logoutButton"));
		logoutButton.click();

		// Verify that the home page is no longer accessible after logout
		driver.get("http://localhost:" + this.port + "/home");
		currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/login"), "User was not redirected to the login page after logout");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);

	}

	private String getStringBuilder(int length) {
		// random string
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		// Loop to create the random string
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}
		return sb.toString();
	}

	// Test for logging, creating a note and verify it is in list of notes
	@Test
	public void testLogCreateNoteAndVerifyInList() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		// inputs string
		String noteName = "Notes_" + numberToString(4);
		String noteDescrip = "Lorem Ipsum simply dummy text";
		String username = "benarrik";
		String password = "123";

		// Log in
		driver.get("http://localhost:" + this.port + "/login");

		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();

		// Check if the user is redirected to the home page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement Notes = driver.findElement(By.linkText("Notes"));
		Notes.click();

		// click "+ Add a New Note" button
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("addNoteForm")));
		element.click();

		// Wait for the list page to load the form to create the note
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteForm")));

		// go to note form elements
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));

		noteTitle.sendKeys(noteName);
		noteDescription.sendKeys(noteDescrip);
		WebElement noteSubmit = driver.findElement(By.id("noteSubmitForm"));
		//WebElement noteSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Save changes")));
		noteSubmit.click();

		//redirect to home
		WebElement resultNoteSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultNoteSuccess")));
		//WebElement resultNoteSuccess=driver.findElement(By.id("resultNoteSuccess"));
		resultNoteSuccess.click();

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement Notes2 = driver.findElement(By.linkText("Notes"));
		Notes2.click();

		// Wait for the list page to load and display the newly added book
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));

		// Verify that the book appears in the list
		WebElement bookList = driver.findElement(By.id("noteTable"));
		String bookListText = bookList.getText();
		assertTrue(bookListText.contains(noteName), "Note title not found in list!");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);
	}


	// test to log in, update a Note and verify the updated note is in the table
	@Test
	public void testLogUpdateNoteAndVerifyInList() throws InterruptedException {

		//StringBuilder sb = getStringBuilder(5);
		String sb=getStringBuilder(5);

		// Log in
		driver.get("http://localhost:" + this.port + "/login");

		// user credentials
		String username = "benarrik";
		String password = "123";

		// note to update
		String newNoteTitle = "The Old Tree " + sb.substring(3);
		String newNoteDescription = "Lorem ipsum dolor sit amet, adipiscing elit " + sb;


		// Log in user
		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();
		// Check if the user is redirected to the home page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement Notes = driver.findElement(By.linkText("Notes"));
		Notes.click();

		//Edit Note
		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement editNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("editNoteButton")));
		editNoteButton.click();

		// go to note form elements
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		System.out.println(noteTitle);
		System.out.println(noteDescription);

		// Wait for the list page to load and display the newly added book
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteForm")));

		// clear notes
		noteTitle.clear();
		noteDescription.clear();

		// update notes
		noteTitle.sendKeys(newNoteTitle);
		noteDescription.sendKeys(newNoteDescription);

		// submit form
		WebElement noteSubmit = driver.findElement(By.id("noteSubmitForm"));
		noteSubmit.click();

		//redirect to home
		WebElement resultNoteSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultNoteSuccess")));
		//WebElement resultNoteSuccess=driver.findElement(By.id("resultNoteSuccess"));
		resultNoteSuccess.click();

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement Notes2 = driver.findElement(By.linkText("Notes"));
		Notes2.click();

		// Wait for the list page to load and display the newly added book
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));

		// Verify that the book appears in the list
		WebElement bookList = driver.findElement(By.id("noteTable"));
		String bookListText = bookList.getText();
		assertTrue(bookListText.contains(newNoteTitle), "Book title not found in list!");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);

	}


	// test to log in, delete a Note and verify the Note is not in the table
	@Test
	public void testLogDeleteNoteAndVerifyNotInList() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		// Log in
		driver.get("http://localhost:" + this.port + "/login");

		// user credentials
		String username = "benarrik";
		String password = "123";

		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();

		// Check if the user is redirected to the home page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement Notes = driver.findElement(By.linkText("Notes"));
		Notes.click();

		// Locate the table of notes by its ID
		WebElement notesTable = wait.until(ExpectedConditions.elementToBeClickable(By.id("noteTable")));

		//Locate delete button of second row of note table
		WebElement deleteLink = notesTable.findElement(By.xpath("//tbody[@id='noteTbody']//tr[2]//a[@class='btn btn-danger']"));

		// Click the delete link to remove the second note
		deleteLink.click();

		// Here, I am checking that the second row is gone from the table
		boolean isDeleted = driver.findElements(By.xpath("//table[@id='noteTable']//tbody/tr[2]")).isEmpty();
		assertTrue(isDeleted, "The second note should be deleted");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);
	}


	// Test log in, create credential and verify the new credential is in the table
	@Test
	public void testLogCreateCredentialsAndVerifyInList() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		// create Inputs
		String str = "cred" +  numberToString(4);
		String credUser = "user_" + str;
		String credUrl = "www.http://" + str + ".com";
		String credPassword = numberToString(5);

		// Log in
		driver.get("http://localhost:" + this.port + "/login");

		String username = "benarrik";
		String password = "123";

		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();

		// Check if the user is redirected to the home page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");

		// Locate the "Credentials" link in the navigation bar using link text and click on the "Credentials" link
		WebElement credentials = driver.findElement(By.linkText("Credentials"));
		credentials.click();

		// Click  "+ Add a New Credential" button
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("addCredentialForm")));
		element.click();

		// Wait for the form to load and display to enter the new credential
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialForm")));

		// go  and enter the new inputs to credential form
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));

		credentialUrl.sendKeys(credUrl);
		credentialUsername.sendKeys(credUser);
		credentialPassword.sendKeys(credPassword);

		// submit the credential form
		WebElement noteSubmit = driver.findElement(By.id("credentialSubmitForm"));
		noteSubmit.click();

		//redirect to home
		WebElement resultNoteSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultCredentialSuccess")));
		//WebElement resultNoteSuccess=driver.findElement(By.id("resultNoteSuccess"));
		resultNoteSuccess.click();

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement credential2 = driver.findElement(By.linkText("Credentials"));
		credential2.click();

		// Wait for the list page to load and display the newly added book
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		// Verify that the book appears in the list
		WebElement credentialList = driver.findElement(By.id("credentialTable"));
		String credentialListText = credentialList.getText();
		assertTrue(credentialListText.contains(credUrl), "Credential URL not found in list!");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);
	}


	// test log in, update Credential and verity the Credential is in the table
	@Test
	public void testLogUpdateCredentialAndVerifyInList() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		// create Inputs
		String str = "newcred" +  numberToString(4);
		String newCredemtialUser = "newuser" +numberToString(2);
		String newCredentialUrl = "www.http://" + str + ".com";
		String newCreddentialPassword = "123" +numberToString(4);

		// 1. Log in
		driver.get("http://localhost:" + this.port + "/login");

		// user credentials
		String username = "benarrik";
		String password = "123";


		// Log in user
		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();
		// Check if the user is redirected to the home page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");

		// Locate the "Credentials" link in the navigation bar using link text and click on the "Credentials" link
		WebElement credentials = driver.findElement(By.linkText("Credentials"));
		credentials.click();

		// Locate the credentials table  by its ID
		WebElement credentialTable = wait.until(ExpectedConditions.elementToBeClickable(By.id("credentialTable")));

		// click edit button
		WebElement editLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody[@id='credentialTbody']//tr[3]//button[@id='editCredentialButton']")));
		editLink.click();

		// go to credential form elements
		WebElement credentialUrl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

		// clear notes form
		credentialUrl.clear();
		credentialUsername.clear();
		credentialPassword.clear();
		credentialPassword.clear();

		// enter updated inputs
		credentialUrl.sendKeys(newCredentialUrl);
		credentialUsername.sendKeys((newCredemtialUser));
		credentialPassword.sendKeys(newCreddentialPassword);

		// submit form
		WebElement credentialSubmit = driver.findElement(By.id("credentialSubmitForm"));
		credentialSubmit.click();


		//redirect to home
		WebElement resultCredentialSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultCredentialSuccess")));
		//WebElement resultNoteSuccess=driver.findElement(By.id("resultNoteSuccess"));
		resultCredentialSuccess.click();

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement credentials2 = driver.findElement(By.linkText("Credentials"));
		credentials2.click();

		// Wait for the list page to load and display the newly added book
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		// Verify that the book appears in the list
		WebElement bookList = driver.findElement(By.id("credentialTable"));
		String bookListText = bookList.getText();

		assertTrue(bookListText.contains(newCredentialUrl), "Credential Url not found in list!");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);
	}

	// Test log in, delete Credential and verify the Credential is deleted
	@Test
	public void testLogDeleteCredentialAndVerifyNotInList() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		// Log in
		driver.get("http://localhost:" + this.port + "/login");

		// user credentials
		String username = "benarrik";
		String password = "123";

		WebElement loginUsernameInput = driver.findElement(By.id("inputUsername"));
		WebElement loginPasswordInput = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("loginButton"));

		loginUsernameInput.sendKeys(username);
		loginPasswordInput.sendKeys(password);
		loginButton.click();

		// Check if the user is redirected to the home page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/home"), "User was not redirected to the home page after login");

		// Locate the "Notes" link in the navigation bar using link text and click on the "Notes" link
		WebElement credentials = driver.findElement(By.linkText("Credentials"));
		credentials.click();

		// Locate the table of notes by its ID
		WebElement credentialsTable = wait.until(ExpectedConditions.elementToBeClickable(By.id("credentialTable")));


		//Locate delete button of second row of note table
		WebElement deleteLink = credentialsTable.findElement(By.xpath("//tbody[@id='credentialTbody']//tr[2]//a[@class='btn btn-danger']"));


		// Click the delete link to remove the second note
		deleteLink.click();

		// Here, I am checking that the second row is gone from the table
		boolean isDeleted = driver.findElements(By.xpath("//table[@id='credentialTable']//tbody/tr[2]")).isEmpty();
		assertTrue(isDeleted, "The second note should be deleted");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);
	}


	/**  helper method **/
	private int RandomIntegerGenerator (int length){
		Random random = new Random();
		// Calculate the range for the number of length L
		int min = (int) Math.pow(10, length - 1); // Smallest number with L digits
		int max = (int) Math.pow(10, length) - 1; // Largest number with L digits

		// Return a random number within this range
		return random.nextInt((max - min) + 1) + min;
	}

	/** Helper Method **/
	private String numberToString(int length){
		int randomNumber = (int) (Math.random() * 1000) + RandomIntegerGenerator(4);
		String numberStr = String.valueOf(randomNumber);
		String newString = "000000" + numberStr;
		return newString.substring(newString.length() -length);
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/

	private void doMockSignUp(String firstName, String lastName, String userName, String password)  {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		//inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		//inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		//inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		//inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		WebElement goToLogin =  webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("loginTag")));
		//button.click();

		//WebElement goToLogin = driver.findElement(By.linkText("login"));
		//webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginTag")));
		//WebElement goToLogin = driver.findElement(By.id("loginTag"));
		goToLogin.click();

		//Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		//Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/

	private void doLogIn(String userName, String password) {
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginButton")));
		WebElement loginButton = driver.findElement(By.id("loginButton"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		//webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * <p>
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() throws InterruptedException {

		// Create a test account
		String lastName="Test"+getStringBuilder(3);
		String userName="user"+numberToString(3);
		String password=numberToString(5);

		doMockSignUp("Redirection", lastName, userName, password);

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
	public void testBadUrl() {
		// Create a test account

		String lastName="Test"+getStringBuilder(5);
		String userName="user"+numberToString(5);
		String password=numberToString(5);

		doMockSignUp("URL", lastName, userName, password);
		doLogIn(userName, password);

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
	public void testLargeUpload() throws InterruptedException, MalformedURLException {

		// retrieves the current working directory of the Java application.
		String projectPath = System.getProperty("user.dir");

		//maximize window
		driver.manage().window().maximize();

		// Create a test account
		String lastName = "Test" + getStringBuilder(5);
		String userName = "user" + numberToString(5);
		String password = numberToString(5);
		System.out.println(projectPath);
		doMockSignUp("URL", lastName, userName, password);
		doLogIn(userName, password);

		// Try to upload an arbitrary large file//
		WebDriverWait webDriverWait = new WebDriverWait(driver, 50L);
		String fileName = "bigFile.zip";

		// getting the current url (should be home)
		String currentUrl = driver.getCurrentUrl();

		// Wait until the current URL becomes the expected one
		webDriverWait.until(ExpectedConditions.urlToBe(currentUrl));

		// Initialize file
		File file=new File(fileName);
		//driver.findElement(By.id("fileUpload")).sendKeys(projectPath+"\\files\\"+file);
		driver.findElement(By.id("fileUpload")).sendKeys(new File(fileName).getAbsolutePath());
		//Thread.sleep(5000);


		// upload button
		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("Internal server error. It seems the file you try to upload exceeds the max size of 10M"));

	}
}


