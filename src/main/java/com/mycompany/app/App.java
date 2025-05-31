package com.mycompany.app;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("data/data.txt"));
        String artist = lines.get(0);
        String title = lines.get(1);
        List<String> tracks = lines.subList(2, lines.size());

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\natal\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-debugging-port=9222");

        String sessionDir = "D:\\forGit\\testing\\ST-8\\chrome-session-" + System.currentTimeMillis();
        options.addArguments("--user-data-dir=" + sessionDir);
        
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", Paths.get("result").toAbsolutePath().toString());
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);
        
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        try {
            driver.get("http://www.papercdcase.com/index.php");
            
            driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[1]/td[2]/input"))
                  .sendKeys(artist);
                  
            driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[2]/td[2]/input"))
                  .sendKeys(title);
            
            int trackIndex = 0;
            for (int column = 1; column <= 2; column++) {
                for (int row = 1; row <= 8; row++) {
                    if (trackIndex < tracks.size()) {
                        String xpath = String.format(
                            "/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[3]/td[2]/table/tbody/tr/td[%d]/table/tbody/tr[%d]/td[2]/input",
                            column, row
                        );
                        driver.findElement(By.xpath(xpath)).sendKeys(tracks.get(trackIndex));
                        trackIndex++;
                    }
                }
            }
            
            driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[4]/td[2]/input[2]"))
                .click();
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[5]/td[2]/input[2]"))
                .click();
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/p/input"))
                .click();
            
            Thread.sleep(5000);
            
            File downloadDir = new File("result");
            File[] files = downloadDir.listFiles();
            if (files != null && files.length > 0) {
                File downloadedFile = files[0];
                File newFile = new File("result/cd.pdf");
                if (newFile.exists()) newFile.delete();
                downloadedFile.renameTo(newFile);
            }
            
        } finally {
            driver.quit();
        }
    }
}