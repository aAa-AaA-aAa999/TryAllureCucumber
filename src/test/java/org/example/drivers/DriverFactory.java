package org.example.drivers;

import org.example.TestPropManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static String baseAddress = "http://localhost:8080/food";
    private static WebDriver driver;
    private static final TestPropManager props = TestPropManager.getInstance();

    private static void initDriver() {
        if ("remote".equalsIgnoreCase(props.getProperty("type.driver"))) {
            initRemoteDriver();
        } else {
            initLocalDriver();
        }
    }

    private static void initRemoteDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("browserName", props.getProperty("browser.name"));
        selenoidOptions.put("browserVersion", props.getProperty("browser.version"));
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        capabilities.setCapability("selenoid:options", selenoidOptions);
        try {
                driver = new RemoteWebDriver(
                        URI.create(props.getProperty("selenoid.url")).toURL(),
                        capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initLocalDriver() {
        System.setProperty("webdriver.edge.driver",
                System.getProperty("user.dir") + "/src/test/resources/msedgedriver.exe");
        driver = new EdgeDriver(); // присваиваем полю класса
        driver.get(baseAddress);
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }
}
