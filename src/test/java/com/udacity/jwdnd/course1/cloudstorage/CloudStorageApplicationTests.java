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


import java.security.SecureRandom;

//import static java.lang.Thread.sleep;
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
	@Test
	public void testHomePageNotAccessibleWithoutLogin() {
		// Open the home page URL directly
		driver.get("http://localhost:" + this.port +"/home");

		// Check the current URL to ensure it redirects to the login page
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/login"), "User was not redirected to the login page");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);

	}


	// New user signs up, logs in, accesses home page, logs out and is not allowed to access home page
	@Test
	public void testUserSignUpLoginLogout() {

		// random string
		int length=5;
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		// Loop to create the random string
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}

		// Sign up a new user page
		driver.get("http://localhost:" + this.port +"/signup");

		// Fill out the sign-up form
		WebElement userFirstName = driver.findElement(By.id("inputFirstName"));
		WebElement userLastName = driver.findElement(By.id("inputLastName"));
		WebElement usernameInput = driver.findElement(By.id("inputUsername"));
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		WebElement submitButton = driver.findElement(By.id("signupButton"));

		String firstName="FirstName"+sb.toString();
		String lastName="LastName"+sb.toString();
		String username = sb.toString();
		String password = "78910";

		userFirstName.sendKeys(firstName);
		userLastName.sendKeys(lastName);
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		submitButton.click();

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
		driver.get("http://localhost:" + this.port +"/home");
		currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("/login"), "User was not redirected to the login page after logout");
		// Pause the test for 5 seconds (5000 milliseconds)
		//sleep(5000);

	}


	// Test for logging, creating a note and verify it is in list of notes
	@Test
	public void testLogCreateNoteAndVerifyInList() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		// inputs string
		int randomNumber = (int) (Math.random() * 1000) + 1459;
		String numberStr = String.valueOf(randomNumber);
		String newString="000000"+numberStr;
		String noteName="Notes_"+newString.substring(newString.length() - 4);
		String noteDescrip="Lorem Ipsum simply dummy text";
		String username = "benarrik";
		String password = "123";

		// Log in
		driver.get("http://localhost:" + this.port +"/login");

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
		WebElement noteSubmit=driver.findElement(By.id("noteSubmitForm"));
		//WebElement noteSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Save changes")));
		noteSubmit.click();

		//redirect to home
		WebElement  resultNoteSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultNoteSuccess")));
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

		// random string
		int length=5;
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		// Loop to create the random string
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}

		// Log in
		driver.get("http://localhost:" + this.port +"/login");

		// user credentials
		String username = "benarrik";
		String password = "123";

		// note to update
		String newNoteTitle="The Old Tree "+sb.toString().substring(3);
		String newNoteDescription="Lorem ipsum dolor sit amet, adipiscing elit "+sb.toString();


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
		WebElement noteSubmit=driver.findElement(By.id("noteSubmitForm"));
		noteSubmit.click();

		//redirect to home
		WebElement  resultNoteSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultNoteSuccess")));
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
		driver.get("http://localhost:" + this.port +"/login");

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
		int randomNumber = (int) (Math.random() * 1000) + 1459;
		String numberStr = String.valueOf(randomNumber);
		String newString="000000"+numberStr;
		String str="cred"+newString.substring(newString.length() - 4);
		String credUser="user_"+str;
		String credUrl="www.http://"+str+".com";
		String credPassword="123456";

		// Log in
		driver.get("http://localhost:" + this.port +"/login");

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
		WebElement noteSubmit=driver.findElement(By.id("credentialSubmitForm"));
		noteSubmit.click();

		//redirect to home
		WebElement  resultNoteSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultCredentialSuccess")));
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
			int randomNumber = (int) (Math.random() * 1000) + 12;
			String numberStr = String.valueOf(randomNumber);
			String newString="000000"+numberStr;
			String str="newcred"+newString.substring(newString.length() - 4);
			String newCredemtialUser="newuser"+numberStr;
			String newCredentialUrl="www.http://"+str+".com";
			String newCreddentialPassword="123"+numberStr;

			// 1. Log in
			driver.get("http://localhost:" + this.port +"/login");

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
			WebElement credentialSubmit=driver.findElement(By.id("credentialSubmitForm"));
			credentialSubmit.click();


			 //redirect to home
			 WebElement  resultCredentialSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultCredentialSuccess")));
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
		driver.get("http://localhost:" + this.port +"/login");

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


}
