package org.example.drivers;

import org.example.TestPropManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static String baseAddress = "https://qualit.applineselenoid.fvds.ru/food";
    private static WebDriver driver;
    private static final TestPropManager props = TestPropManager.getInstance();

    private static void initDriver() {
        String typeDriver = System.getProperty("type.driver", props.getProperty("type.driver"));
        if ("remote".equalsIgnoreCase(typeDriver)) {
            initRemoteDriver();
        } else {
            initLocalDriver();
        }
    }

    private static void initRemoteDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        Map<String, Object> selenoidOptions = new HashMap<>();


        String browserName = System.getProperty("browser.name", props.getProperty("browser.name"));
        String browserVersion = System.getProperty("browser.version", props.getProperty("browser.version"));

        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("browserVersion", browserVersion);

        /*
        capabilities.setCapability("browserName", props.getProperty("browser.name"));
        capabilities.setCapability("browserVersion", props.getProperty("browser.version"));
*/
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        capabilities.setCapability("selenoid:options", selenoidOptions);
        try {
            driver = new RemoteWebDriver(
                    URI.create(props.getProperty("selenoid.url")).toURL(),
                    capabilities);
            driver.get(baseAddress);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initLocalDriver() {
        String localBrowserName = System.getProperty("localbrowser.name", props.getProperty("localbrowser.name"));
        System.out.println(localBrowserName);
        if("edge".equalsIgnoreCase(localBrowserName)){
            System.setProperty("webdriver.edge.driver",
                    System.getProperty("user.dir") + "/src/test/resources/msedgedriver.exe");
            driver = new EdgeDriver();
            driver.get(baseAddress);
        }
        else {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "/src/test/resources/geckodriver.exe");
            driver = new FirefoxDriver();
            driver.get(baseAddress);
        }
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }
}
