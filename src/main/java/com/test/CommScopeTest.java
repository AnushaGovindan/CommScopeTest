package com.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommScopeTest {

    WebDriver webDriver;
    private String url = "https://www.commscope.com";
    private String searchText = "ServAssure";
    private By searchBar = By.id("desktop-search-bar");
    private By acceptCookies = By.id("onetrust-accept-btn-handler");
    private By searchResults = By.xpath("//div[@id='results-area']//following::div//a[@class='title srch-link']");
    private static final String SERVASSURE_NXT = "SERVASSURE-NXT | ServAssure® NXT";
    private static final String SERVASSURE_NXT_ALARM = "SERVASSURE-NXT-ALARM-CENTRAL | ServAssure® NXT Alarm Central";
    private static final String SA_DOMAIN = "SA-DOMAIN-MANAGER | ServAssure® Domain Manager";

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
        webDriver = new ChromeDriver();
    }

    @Test
    public void getSearchStrings() {
        try {
            webDriver.get(url);
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            webDriver.manage().window().maximize();
            if (webDriver.findElement(acceptCookies).isDisplayed() || webDriver.findElement(acceptCookies).isEnabled()) {
                webDriver.findElement(acceptCookies).click();
            }
            fluentWait(Duration.ofSeconds(15), Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(searchBar)).sendKeys(searchText);
            webDriver.findElement(searchBar).sendKeys(Keys.RETURN);
            List<WebElement> webElemList = webDriver.findElements(searchResults);
            List<String> elemList = webElemList.stream().map(e -> e.getText()).collect(Collectors.toList());
            System.out.println("Search Results:::   "+elemList);
            Assert.assertTrue(elemList.toString().contains(SERVASSURE_NXT),"'SERVASSURE-NXT' search result did not appear");
            Assert.assertTrue(elemList.toString().contains(SERVASSURE_NXT_ALARM), "'SERVASSURE-NXT-ALARM-CENTRAL' search result did not appear");
            Assert.assertTrue(elemList.toString().contains(SA_DOMAIN), "'SA-DOMAIN-MANAGER' search result did not appear");
        } catch (Exception ex) {
            System.out.println("Exception occurred::  " + ex.getMessage());
        }
    }

    /**
     * Wait for <timeOut> for an element to be present on the page, checking for its presence once every <pollingInterval>.
     * @param timeOut
     * @param pollingInterval
     * @return
     */
    private FluentWait<WebDriver> fluentWait(Duration timeOut, Duration pollingInterval) {
        return new FluentWait<WebDriver>(webDriver)
                .withTimeout(timeOut)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    @AfterClass
    public void tearDown() {
        webDriver.quit();
    }
}
