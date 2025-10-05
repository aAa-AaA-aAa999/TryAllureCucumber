package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestPropManager {
    private static TestPropManager instance;
    private final Properties properties = new Properties();

    private TestPropManager() {
        loadProperties();
    }

    public static TestPropManager getInstance() {
        if (instance == null) {
            instance = new TestPropManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден файл application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки application.properties", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
