package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class MainTest {

    private final String chromeDriverPath = "chromedriver.exe";
    private final String baseURL = "https://sakshingp.github.io/assignment/login.html";
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get(baseURL);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }

    @AfterMethod
    public void closeDriver() {
        driver.close();
    }

    @Test(dataProvider = "loginDataProvider")
    public void testLogin(String name, String pass, String errMsg) {
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("log-in"));

        username.sendKeys(name);
        password.sendKeys(pass);

        loginButton.click();

        try {

            WebElement errorMsg = driver.findElement(By.xpath("//div[contains(@id,'random_id')]"));
            Assert.assertEquals(errorMsg.getText(), errMsg);
        } catch (NoSuchElementException e) {
            Assert.assertFalse(name.isEmpty());
            Assert.assertFalse(pass.isEmpty());
            Assert.assertEquals(driver.getTitle(), "Demo App");
        }
    }

    @DataProvider(name = "loginDataProvider")
    public Object[][] loginDataProvider() {
        Object[][] objs = new Object[3][3];
        // without username and password
        objs[0][0] = "";
        objs[0][1] = "";
        objs[0][2] = "Both Username and Password must be present";
        // without password and with username
        objs[1][0] = "abcd";
        objs[1][1] = "";
        objs[1][2] = "Password must be present";
        // without username and with password
        objs[2][0] = "abcd";
        objs[2][1] = "1234";
        objs[2][2] = "";

        return objs;
    }
}