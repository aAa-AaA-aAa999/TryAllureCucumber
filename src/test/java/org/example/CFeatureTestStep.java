package org.example;

import io.cucumber.java.ru.И;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.Hooks.driver;
import static org.example.Hooks.statement;

public class CFeatureTestStep {
    int beginId;

    //поиск последнего добавленного объекта
    String querySelectLastId = "SELECT MAX(FOOD_ID) AS MAXID FROM FOOD";

    //для проверки создания таблицы
    boolean tableExist = false;

    //создание тестовой таблицы для удаления
    String queryCreateTable = "CREATE TABLE test_table (\n" +
            "    id INT\n" +
            ");";

    //поиск тестовой таблицы
    String queryCheckTable = "SELECT COUNT(*) AS cnt\n" +
            "FROM INFORMATION_SCHEMA.TABLES\n" +
            "WHERE TABLE_SCHEMA = 'PUBLIC'\n" +
            "  AND TABLE_NAME = 'test_table';\n";

    @И("получение идентификатора последнего добавленного товара")
    @Description("Получает идентификатор последнего добавленного товара из таблицы FOOD")
    @Owner("Leonardo DiCaprio")
    public void получение_идентификатора_последнего_добавленного_товара() {
        beginId = getLastId();
    }

    @И("проверка открыта ли форма добавления")
    @Description("Проверяет, открыта ли форма добавления товара, и открывает её при необходимости")
    @Owner("Natalie Portman")
    public void проверка_открыта_ли_форма_добавления() {
        sleep(700);
        WebElement dialog = driver.findElement(By.id("editModal"));

        if (!dialog.isDisplayed()) {
            // если не отображается -> нажимаем кнопку "Добавить"
            buttonAdd();
        }
    }

    @И("поля Название Экзотический Тип заполняются данными {string}, {string}, {string}")
    @Description("Заполняет поля Название, Экзотический и Тип данными из сценария")
    @Owner("Tom Hanks")
    public void поля_название_экзотический_тип_заполняются_данными(String name, String isExotic, String type) {
        boolean exotic = Boolean.parseBoolean(isExotic);
        WebElement inputName = driver.findElement(By.xpath("//input[@placeholder='Наименование']"));
        WebElement checkBoxExotic = driver.findElement(By.xpath("//input[@name='exotic']"));
        WebElement selectElement = driver.findElement(By.xpath("//select[@id='type']"));
        Select select = new Select(selectElement);

        //вводим название
        inputName.clear();
        inputName.sendKeys(name);

        //проверяем что чек бокс не отмечен
        if (exotic) {
            if (!checkBoxExotic.isSelected()) {
                checkBoxExotic.click();
            }
        }
        else {
            if (checkBoxExotic.isSelected()) {
                checkBoxExotic.click();
            }
        }
        //выбираем тип
        select.selectByVisibleText(type);
    }

    @И("нажимается кнопка Сохранить")
    @Description("Нажимает кнопку Сохранить в форме добавления товара")
    @Owner("Scarlett Johansson")
    public void нажимается_кнопка_Сохранить() {
        WebElement buttonSave = driver.findElement(By.xpath("//button[text()='Сохранить']"));
        buttonSave.click();
        sleep(500);
    }

    @И("проверка разности идентификаторов после теста")
    @Description("Проверяет, что идентификатор последнего элемента изменился после добавления нового товара")
    @Owner("Brad Pitt")
    public void проверка_разности_идентификаторов_после_теста(){
        Assert.assertNotEquals(beginId, getLastId());
    }

    @И("проверка эквивалентности идентификаторов последних элементов до после теста")
    @Description("Проверяет, что идентификатор последнего элемента не изменился после операции")
    @Owner("Meryl Streep")
    public void проверка_эквивалентности_идентификаторов_последних_элементов_до_после_теста(){
        Assert.assertEquals(beginId, getLastId());
    }

    @И("создание таблицы для проверки удаления")
    @Description("Создаёт тестовую таблицу для проверки работы sql-инъекции")
    @Owner("Robert Downey Jr.")
    public void создание_таблицы_для_проверки_удаления(){
        // создаём пустую таблицу для удаления
        try{
            statement.executeUpdate(queryCreateTable);
            tableExist = true;
            System.out.println("Таблица создана");
        } catch (SQLException sqlExc) {
            System.err.println("Не удалось создать таблицу: " + sqlExc.getMessage());
        }
    }

    @И("проверка существования таблицы")
    @Description("Проверяет существование тестовой таблицы")
    @Owner("Jennifer Lawrence")
    public void проверка_существования_таблицы(){
        try(ResultSet resultSet = statement.executeQuery(queryCheckTable)) {
            if (resultSet.next()) {
                tableExist = resultSet.getInt("cnt") > 0;;
            }
        } catch (SQLException sqlExc) {
            System.err.println("Возникла ошибка при поиске таблицы: " + sqlExc.getMessage());
        }
    }

    @И("удаление таблицы если существует")
    @Description("Удаляет тестовую таблицу, если она существует и не была удалена инъекцией")
    @Owner("Chris Hemsworth")
    public void удаление_таблицы_если_существует(){
        if (tableExist){
            try{
                statement.executeUpdate("DROP TABLE test_table");
                System.out.println("Таблица удалена");
            } catch (SQLException sqlExc) {
                System.err.println("Возникла ошибка при удалении таблицы: " + sqlExc.getMessage());
            }
        }
    }

    @И("сравнение полученного результата")
    @Description("Проверяет, что таблица существует и не была удалена до сохранения итогов в идентификатор")
    @Owner("Emma Watson")
    public void сравнение_полученного_результата(){
        Assert.assertTrue("Таблица не существует!", tableExist);
    }


    //метод для возврата номера последнего найденного элемента
    public int getLastId(){
        int id = 0;

        try (ResultSet resultSet = statement.executeQuery(querySelectLastId)) {
            if (resultSet.next()) {
                id = resultSet.getInt("MAXID");
            }
            return id;

        } catch (SQLException sqlExc) {
            System.err.println("Не удалось получить данные о последнем ID: " + sqlExc.getMessage());
            return id;
        }
    }

    //метод для ожидания
    public void sleep(int timeOfSleep){
        try {
            Thread.sleep(timeOfSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void buttonAdd(){
        WebElement buttonAdd = driver.findElement(By.xpath("//button[text()='Добавить']"));
        buttonAdd.click();
        sleep(500);
    }
}
