package com.todomvc.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class TodoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(className = "new-todo")
    private WebElement newTodoInput;

    @FindBy(css = ".todo-list li")
    private List<WebElement> todoItems;

    @FindBy(css = ".todo-count strong")
    private WebElement itemsLeftCount;

    @FindBy(linkText = "Active")
    private WebElement activeFilter;

    @FindBy(linkText = "Completed")
    private WebElement completedFilter;

    @FindBy(className = "clear-completed")
    private WebElement clearCompletedButton;

    public TodoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void addTodo(String todoText) {
        newTodoInput.sendKeys(todoText + "\n");
    }

    public void editTodo(int index, String newText) {
        WebElement todoItem = todoItems.get(index);
        WebElement label = todoItem.findElement(By.cssSelector("label"));

        // Double click to edit
        new Actions(driver)
                .doubleClick(label)
                .perform();

        WebElement editField = todoItem.findElement(By.cssSelector("input.edit"));
        editField.clear();
        editField.sendKeys(newText + "\n");
    }

    public void completeTodo(int index) {
        todoItems.get(index).findElement(By.cssSelector("input.toggle")).click();
    }

    public void deleteTodo(int index) {
        WebElement todoItem = todoItems.get(index);
        new Actions(driver).moveToElement(todoItem).perform();
        todoItem.findElement(By.cssSelector("button.destroy")).click();
    }

    public void filterActive() {
        activeFilter.click();
    }

    public void filterCompleted() {
        completedFilter.click();
    }

    public void clearCompleted() {
        clearCompletedButton.click();
    }

    public int getTodoCount() {
        return Integer.parseInt(itemsLeftCount.getText());
    }

    public int getVisibleTodoCount() {
        return todoItems.size();
    }

    public String getTodoText(int index) {
        return todoItems.get(index).getText();
    }

    public boolean isTodoCompleted(int index) {
        return todoItems.get(index).getAttribute("class").contains("completed");
    }

    public void waitForTodosToLoad() {
        wait.until(ExpectedConditions.visibilityOf(newTodoInput));
    }
}