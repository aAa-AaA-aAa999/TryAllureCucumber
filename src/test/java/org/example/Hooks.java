package org.example;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import org.example.drivers.DriverFactory;
import org.openqa.selenium.WebDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Hooks {
    protected static WebDriver driver;
    protected static Connection connection;
    protected static Statement statement;

    @BeforeAll
    public static void setUp() {
        try {
            driver = DriverFactory.getDriver();

            connection = DriverManager.getConnection(
                    "jdbc:h2:tcp://qualit.applineselenoid.fvds.ru/mem:testdb",
                    "user",
                    "pass");

            statement = connection.createStatement();

            System.out.println("Подключение к базе установлено");
        } catch (SQLException sqlExc) {
            System.err.println("Не удалось установить подключение к базе данных: " + sqlExc.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации теста: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🧹 WebDriver закрыт");
        }

        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Подключение к базе закрыто");
                }
            } catch (SQLException sqlExc) {
                System.err.println("Ошибка при закрытии подключения к базе: " + sqlExc.getMessage());
            }
        }
    }
}
