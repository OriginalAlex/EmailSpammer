package com.originalalex.github.backend;

import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

public class User {

    private String recoveryEmail;
    private String email;
    private String password; // Yes I'm storing passwords as plain text --> I may add simple encryption in the future
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String GMAIL_SIGNUP_PAGE = "https://accounts.google.com/ServiceLogin?hl=en";
    private static final String GMAIL_PAGE = "https://mail.google.com/mail/u/0/";
    private static final String PATH_TO_CHROMEDRIVER = "C:/Users/Alex/Desktop/chromedriver.exe";

    private boolean isLoggedIn;

    public User(@NotNull String email, @NotNull String password) {
        this.email = email;
        this.password = password;
        this.isLoggedIn = false;

        System.setProperty("webdriver.chrome.driver", PATH_TO_CHROMEDRIVER); // inform Selenium the location of my chromedriver
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, 20);
    }

    public User(@NotNull String email, @NotNull String password, @NotNull String recoveryEmail) {
        this(email, password);
        this.recoveryEmail = recoveryEmail;
    }

    private void wait(String elementName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementName))); // I always use xpath so no need for enumators etc.
    }

    public void logout() {
        driver.get("https://mail.google.com/mail/logout?hl=en");
    }

    public void sendSpamEmail(ObservableList<String> messages, String subject, String target, int number) {
        driver.get(GMAIL_PAGE);
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            driver.findElement(By.xpath("//div[@gh=\"cm\"]")).click(); // the compose email button

            wait("//div[@data-tooltip-delay=\"800\"]"); // wait until the prescence of a send button

            driver.findElement(By.xpath("//textarea[@aria-label=\"To\"]")).sendKeys(target); // the address to send the email to
            driver.findElement(By.xpath("//input[@placeholder=\"Subject\"]")).sendKeys(subject);
            driver.findElement(By.xpath("//div[@role=\"textbox\"]")).sendKeys(messages.get(random.nextInt(messages.size()-1))); // get a random number
            driver.findElement(By.xpath("//div[@data-tooltip-delay=\"800\"]")).click();

            try {
                Thread.sleep(random.nextInt(400) + 10); // attempt to make it look not like a spam
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean login() { // //a[@class="gmail-nav__nav-link gmail-nav__nav-link__sign-in"]
        driver.get(GMAIL_SIGNUP_PAGE);
        wait("//input[@type=\"email\"]");
        driver.findElement(By.xpath("//input[@type=\"email\"]")).sendKeys(email);
        driver.findElement(By.xpath("//span[@class=\"RveJvd snByac\"]")).click(); // presumably Google has weird class names to make code harder to read

        if (!driver.findElement(By.xpath("(//div[@aria-live=\"polite\"])[1]")).getText().equals("")) { // this happens when an invalid email is entered
            return false;
        }

        wait("//input[@type=\"password\"]");

        driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys(password);
        driver.findElement(By.xpath("//span[@class=\"RveJvd snByac\"]")).click(); // With valid credentials we should be logged in by this stage

        if (!driver.findElement(By.xpath("(//div[@aria-live=\"polite\"])[1]")).getText().equals("")) { // this happens when an invalid email is entered
            return false;
        }

        // Unused code [have not run into an issue that requires this right now]

//        if (driver.getTitle().toLowerCase().contains("sign in")) { // They have asked for some kind of recovery E-Mail
//            wait("//div[@class=\"vdE7Oc\"]");
//
//            driver.findElement(By.xpath("//div[@class=\"vdE7Oc\"]")).click(); // the button which leads to the recovery page part
//
//            wait("//input[@type=\"text\"]"); // wait until the box exists (page loaded properly)
//
//            if (recoveryEmail == null) {
//                String recoveryEmailHidden = driver.findElement(By.xpath("(//b)")).getText();
//                Scanner reader = new Scanner(System.in);
//                System.out.println("Google has requested a recovery E-Mail matching format of " + recoveryEmailHidden + ".");
//                System.out.println("Please enter the recovery email to complete the signin process");
//                recoveryEmail = reader.nextLine();
//            }
//
//            driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys(recoveryEmail);
//            driver.findElement(By.xpath("//div[@class=\"ZFr60d CeoRYc\"]")).click();
//        }
        return true;
    }

}
