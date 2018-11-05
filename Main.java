import com.opencsv.CSVWriter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        setSeleniumPath();
        ChromeOptions cOptions = new ChromeOptions();
        cOptions.addArguments("disable-infobars");

        WebDriver driver = new ChromeDriver(cOptions);

        System.out.println("start");
        listTopCEOs(driver);

        driver.quit();
    }

    public static void listTopCEOs(WebDriver driver) throws InterruptedException, IOException {
        //Large Top CEOs
        //Source from Glassdoor: https://www.glassdoor.com/Award/Top-CEOs-LST_KQ0,8.htm
        //SMB Top CEOs
        //Source from Glassdoor: https://www.glassdoor.com/Award/Top-CEOs-at-SMBs-LST_KQ0,16.htm
        driver.get("https://www.glassdoor.com/Award/Top-CEOs-at-SMBs-LST_KQ0,16.htm");
        System.out.println(driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, 3600);
        WebElement waiter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = ' Ryan Lee']")));

        loadAllImages(driver);

        List<WebElement> topElements = driver.findElements(By.className("panel"));
        CEO[] topCEOs = new CEO[50];
        List<String> ceoList = new ArrayList<>();
        int count = 1;
        int realCount = 0;
        FileWriter writer = new FileWriter("/Users/adam/Downloads/topsmbceos.txt");

        for (WebElement webElement : topElements)
        {
            if(count > 50) { break; }

            CEO newPerson = new CEO();
            WebElement rank = webElement.findElement(By.cssSelector("#accordion > div:nth-child("+count+") > div.panel-heading.tbl.fill > div.cell.middle.headshot-wrapper > div.cell.middle.rank"));
            WebElement face = webElement.findElement(By.cssSelector("#accordion > div:nth-child("+count+") > div.panel-heading.tbl.fill > div.cell.middle.headshot-wrapper > div.cell.middle.headshot > img"));
            String faceSrc = face.getAttribute("src");
            WebElement name = webElement.findElement(By.cssSelector("#accordion > div:nth-child("+count+") > div.panel-heading.tbl.fill > div.cell.tbl.foldHH.middle > div.cell.middle.ceo-name.strong"));
            WebElement logo = webElement.findElement(By.cssSelector("#accordion > div:nth-child("+count+") > div.panel-heading.tbl.fill > div.cell.tbl.foldHH.middle > div.cell.middle.panel-header-employer-logo > span > img"));
            String logoSrc = logo.getAttribute("src");

            WebElement company = webElement.findElement(By.cssSelector("#accordion > div:nth-child("+count+") > div.panel-heading.tbl.fill > div.cell.tbl.foldHH.middle > div.cell.middle.panel-header-employer-name"));
            System.out.println("rank: " + Integer.parseInt(rank.getText()));
            System.out.println("name: " + name.getText());
            System.out.println("company: " + company.getText());
            System.out.println("face: " + faceSrc);
            System.out.println("logo: " + logoSrc);

            newPerson.rating = Integer.parseInt(rank.getText());
            newPerson.name = name.getText();
            newPerson.face = faceSrc;
            newPerson.companyLogo = logoSrc;
            newPerson.companyName = company.getText();

            topCEOs[realCount] = newPerson;
            ceoList.add(rank.getText());
            ceoList.add(name.getText());
            ceoList.add(faceSrc);
            ceoList.add(logoSrc);
            ceoList.add(company.getText());
            String temp = ceoList.toString();
            writer.write(temp.substring(1, temp.length()-1));
            writer.write("\n");

            ceoList = new ArrayList<>();

            count++;
            realCount++;
        }

        //NOTE: If you want CSV output, uncomment next line and comment lines 90-94
        // writer.write(ceoList.stream().collect(Collectors.joining(", ")));
        writer.write("\n");
        writer.close();

    }

    public static void loadAllImages(WebDriver driver) throws InterruptedException {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("scroll(0, 1000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 2000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 3000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 4000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 5000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 6000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 7000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 8000);");
        Thread.sleep(1000);
        jse.executeScript("scroll(0, 9050);");
        Thread.sleep(1000);
    }

    public static void setSeleniumPath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            System.getProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver");
        } else {
            System.getProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
        }
    }

}