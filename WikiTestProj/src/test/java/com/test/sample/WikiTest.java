package com.test.sample;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WikiTest {

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {

		System.setProperty("webdriver.gecko.driver", "D:\\Jars\\drivers\\geckodriver.exe");
		driver = new FirefoxDriver();
		baseUrl = "https://www.wikipedia.org/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(baseUrl);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	@Test(dataProvider ="languages")
	public void verifyWikiLanguages(String langShortName, String language) throws Exception {
		
		System.out.println("Wiki Page Launched");
		driver.findElement(By.id("searchLanguage")).click();

		System.out.println("Language Short name ---> "+ driver.findElement(By.id("jsLangLabel")).getText());

		// Select the Language
		Select selectFromLanglist = new Select(driver.findElement(By.xpath("//select[@id='searchLanguage']")));

		System.out.println("language --> "+ language);
		//selectFromLanglist.selectByValue(language);
		selectFromLanglist.selectByVisibleText(language);

		Thread.sleep(1000);

		//Verify The Actual & expected language Labels are same.
		assertEquals(driver.findElement(By.id("jsLangLabel")).getText().toLowerCase(), langShortName);

		System.out.println("Language selection Done");

	}

	@Test(dependsOnMethods = "verifyWikiLanguages")
	public void VerifyWikiLinks() {
		try {
			List<WebElement> links = driver.findElements(By.tagName("a"));

			for (WebElement wikiLinkEle : links) {

				String url = wikiLinkEle.getAttribute("href");
				verifyLink(url);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		/*
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='en'])[1]/following::option[12]")).click();
		driver.findElement(By.id("searchInput")).clear();
		driver.findElement(By.id("searchInput")).sendKeys("india");
		driver.findElement(By.id("search-form")).submit();
		driver.findElement(By.id("searchLanguage")).click();

		assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='India (disambiguation)'])[1]/following::div[2]")).getText(), "Republic of India");

		driver.navigate().back();

		new Select(driver.findElement(By.id("searchLanguage"))).selectByVisibleText("हिन्दी");
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='hi'])[1]/following::option[20]")).click();
		driver.findElement(By.id("searchInput")).click();
		driver.findElement(By.id("searchInput")).clear();
		driver.findElement(By.id("searchInput")).sendKeys("india");
		driver.findElement(By.id("search-form")).submit();
		driver.findElement(By.id("firstHeading")).click();

		driver.navigate().back();
		 */
	}

	private void verifyLink(String url) throws IOException {

		URL link = new URL(url); 

		System.out.println("---> "+ link);

		HttpURLConnection httpCon = (HttpURLConnection) link.openConnection();

		httpCon.connect();

		int respCode = httpCon.getResponseCode();
		System.out.println("Responce code ---> "+ respCode);

		if (respCode != 200) {
			fail("failed to conncet the link ---> "+ link);
		}

	}


	@DataProvider(name="languages")
	public Object[][] getLanguage() {

		Object langArr[][] = {
				{"nan" ,"Bân-lâm-gú / Hō-ló-oē"},
				{"be" ,"Беларуская (Акадэмічная)"},
				
				{"ca" ,"Català"},
				{"eo" ,"Esperanto"},
				{"eu" ,"Euskara"},
				{"fa" ,"فارسی"},
				{"fr" ,"Français"},
				{"gl" ,"Galego"},
				{"cs" ,"Čeština"},
				{"da" ,"Dansk"},
				{"de" ,"Deutsch"},
				{"et" ,"Eesti"},
				{"el" ,"Ελληνικά"},
				{"en" ,"English"},
				{"es" ,"Español"},
				{"az" ,"Azərbaycanca"},
				{"ar","العربية"},
				{"bg" ,"Български"},
				{"hy" ,"Հայերեն"},
				{"hi" ,"हिन्दी"},
				{"sv" ,"Svenska"},
				{"fi" ,"Suomi"}

		};

		return langArr;
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

}
