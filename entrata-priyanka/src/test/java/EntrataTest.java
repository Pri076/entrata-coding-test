import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class EntrataTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/priya/Downloads/chromedriver_win32/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(description = "Test for Navigating to the Homepage and Verifying the Title")
    public void testHomePageTitle() {
        driver.get("https://www.entrata.com/"); //Go to Website
        String expectedTitle = "Property Management Software | Entrata";
        String actualTitle = driver.getTitle(); //get the Title of the Homepage
        Assert.assertEquals(actualTitle, expectedTitle, "Homepage title does not match."); //Compare the Title
    }

    @Test(description = "Test for Navigating to the CareerPage and Verifying the Location Filter",dependsOnMethods = "testHomePageTitle")
    public void testCareerPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        //wait for Close Button to load
        WebElement closeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='close']")));
        closeButton.click();
        driver.findElement(By.id("cookie-close")).click(); //close the cookie
        driver.findElement(By.xpath("//a[normalize-space()='Careers']")).click(); //Click on careers
        String homeTab = driver.getWindowHandle(); //Store the current tab's handle
        driver.findElement(By.xpath("//a[normalize-space()='Search Jobs']")).click(); //click on search jobs
        // Switch to the new tab
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(homeTab)) {
                driver.switchTo().window(tab);
                break;
            }
        }
        driver.findElement(By.xpath("//div[normalize-space()='Location']")).click(); //Click on Location Dropdown
        driver.findElement(By.xpath("//a[normalize-space()='Pune, India']")).click(); // Select Pune, India
        //Get the selected Location Filter Name
        String locationFilter = driver.findElement(By.xpath("//div[@class='filter-button has-selected-filter filter-button-mlp']")).getText();
        Assert.assertEquals(locationFilter, "PUNE, INDIA"); //Compare the Filter which we applied
        driver.switchTo().window(homeTab); //Switch to the Original Tab
    }

    @Test(description = "Test for Navigating to the BlogPage and Verifying the Title and Blog",dependsOnMethods = "testCareerPage")
    public void testBlogPage() {
        driver.findElement(By.xpath("//a[@aria-label='home']//img[@loading='lazy']")).click(); //Go to Home Page
        WebElement resources = driver.findElement(By.xpath("//div[@id='w-dropdown-toggle-13']//div[@class='dropdown-menu-text---brix'][normalize-space()='Resources']"));
        Actions action = new Actions(driver);
        action.moveToElement(resources).perform(); //Hover over Resources
        driver.findElement(By.xpath("//nav[@id='w-dropdown-list-13']//a[@class='sub-menu'][normalize-space()='Blog']")).click(); //Click on Blog
        String expectedUrl = "https://www.entrata.com/blog";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "Failed to navigate to the Blog page."); //Verify Current page url
        String headingBlog = driver.findElement(By.xpath("//h2[@class='heading-blg']")).getText(); //Get the Heading of Blog
        driver.findElement(By.xpath("//div[normalize-space()='Continue Reading']")).click(); //click on continue reading
        String headingFullBlog = driver.findElement(By.xpath("//h2[@class='blog-header_top_1']")).getText(); //Get the Heading of Full Blog after clicking on continue reading
        //Compare the Heading of Blog to verify that it is the same blog on which we have clicked continue reading
        Assert.assertEquals(headingBlog, headingFullBlog);
    }

    @Test(description = "Test for Sign In as a Property Manager", dependsOnMethods = "testBlogPage")
    public void testSignIn() {
        driver.findElement(By.xpath("//a[normalize-space()='Sign In']")).click(); //click on Sign In Button
        driver.findElement(By.xpath("//a[@class='button hover_black new-button w-inline-block']")).click(); //Click On Property Manager Login
        driver.findElement(By.name("company_user[username]")).sendKeys("Priyanka"); //Enter Username: Priyanka
        driver.findElement(By.name("company_user[password]")).sendKeys("NothingSpecial"); //Enter Password: NothingSpecial
        driver.findElement(By.xpath("//button[@type='submit']")).click(); //Click on Sign In Button
        String statusMessage = driver.findElement(By.xpath("//p[@id='statusMessage']")).getText(); //Get the Status message
        //Compare the Status message, it should be invalid because credentials were wrong
        Assert.assertEquals(statusMessage, "The username and password provided are not valid. Please try again.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}

