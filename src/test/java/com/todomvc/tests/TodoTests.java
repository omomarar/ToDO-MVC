package com.todomvc.tests;

import com.todomvc.base.TestBase;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

@Epic("TodoMVC Application")
@Feature("Todo Management")
public class TodoTests extends TestBase {

    @Test(description = "TDM-TC-1: Check framework using")
    @Story("Basic functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify basic framework setup and page loading")
    public void testFrameworkSetup() {
        TodoPage todoPage = new TodoPage(driver);
        todoPage.waitForTodosToLoad();
        Allure.step("Verify page title contains 'React'");
        Assert.assertTrue(driver.getTitle().contains("React"), "Framework is working correctly");
    }

    @Test(description = "TDM-TC-2: Add New To Do Task")
    @Story("Todo Creation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can add a new todo item")
    public void testAddNewTodo() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add new todo 'Buy milk'");
        todoPage.addTodo("Buy milk");

        Allure.step("Verify todo appears in list with correct text");
        Assert.assertEquals(todoPage.getTodoText(0), "Buy milk", "New todo was not added correctly");

        Allure.step("Verify todo count is updated correctly");
        Assert.assertEquals(todoPage.getTodoCount(), 1, "Todo count is incorrect");

        Allure.addAttachment("Page Screenshot",
                new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Test(description = "TDM-TC-3: Add an Empty To Do Task")
    @Story("Todo Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify system prevents adding empty todo items")
    public void testAddEmptyTodo() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Get initial todo count");
        int initialCount = todoPage.getVisibleTodoCount();

        Allure.step("Attempt to add empty todo");
        todoPage.addTodo("");

        Allure.step("Verify todo count remains unchanged");
        Assert.assertEquals(todoPage.getVisibleTodoCount(), initialCount, "Empty todo was added when it shouldn't be");
    }

    @Test(description = "TDM-TC-4: Mark a TodoTask as Completed")
    @Story("Todo Completion")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can mark a todo as completed")
    public void testMarkTodoAsCompleted() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add new todo 'Pay bills'");
        todoPage.addTodo("Pay bills");

        Allure.step("Mark todo as completed");
        todoPage.completeTodo(0);

        Allure.step("Verify todo is marked as completed");
        Assert.assertTrue(todoPage.isTodoCompleted(0), "Todo was not marked as completed");

        Allure.step("Verify active todo count is updated");
        Assert.assertEquals(todoPage.getTodoCount(), 0, "Todo count after completion is incorrect");
    }

    @Test(description = "TDM-TC-5: Delete a To Do Task")
    @Story("Todo Deletion")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can delete a todo item")
    public void testDeleteTodo() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add new todo 'Call mom'");
        todoPage.addTodo("Call mom");

        Allure.step("Get initial todo count");
        int initialCount = todoPage.getVisibleTodoCount();

        Allure.step("Delete the todo");
        todoPage.deleteTodo(0);

        Allure.step("Verify todo count decreased by 1");
        Assert.assertEquals(todoPage.getVisibleTodoCount(), initialCount - 1, "Todo was not deleted");
    }

    @Test(description = "TDM-TC-6: Reload the page")
    @Story("Persistence")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify todos persist after page reload")
    public void testPageReload() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add new todo 'Persistent task'");
        todoPage.addTodo("Persistent task");

        Allure.step("Reload the page");
        driver.navigate().refresh();
        todoPage.waitForTodosToLoad();

        Allure.step("Verify todo persists after reload");
        Assert.assertTrue(todoPage.getVisibleTodoCount() > 0, "Todos did not persist after page reload");
    }

    @Test(description = "TDM-TC-7: Edit an Existing Todo Task")
    @Story("Todo Editing")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can edit an existing todo item")
    public void testEditTodo() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add new todo 'Original task'");
        todoPage.addTodo("Original task");

        Allure.step("Edit the todo to 'Edited task'");
        todoPage.editTodo(0, "Edited task");

        Allure.step("Verify todo text was updated");
        Assert.assertEquals(todoPage.getTodoText(0), "Edited task", "Todo was not edited correctly");
    }

    @Test(description = "TDM-TC-8: Add Duplicate To Do Task")
    @Story("Todo Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify system allows adding duplicate todo items")
    public void testAddDuplicateTodo() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add first 'Duplicate' todo");
        todoPage.addTodo("Duplicate");

        Allure.step("Add second 'Duplicate' todo");
        todoPage.addTodo("Duplicate");

        Allure.step("Verify both todos were added");
        Assert.assertEquals(todoPage.getVisibleTodoCount(), 2, "Duplicate todos were not added correctly");
    }

    @Test(description = "TDM-TC-9: Filter Active Tasks")
    @Story("Todo Filtering")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify active tasks filter works correctly")
    public void testFilterActiveTasks() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add 'Active task'");
        todoPage.addTodo("Active task");

        Allure.step("Add 'Completed task' and mark it complete");
        todoPage.addTodo("Completed task");
        todoPage.completeTodo(1);

        Allure.step("Apply active filter");
        todoPage.filterActive();

        Allure.step("Verify only active tasks are visible");
        Assert.assertEquals(todoPage.getVisibleTodoCount(), 1, "Active filter did not work correctly");
        Assert.assertEquals(todoPage.getTodoText(0), "Active task", "Wrong todo is visible in active filter");
    }

    @Test(description = "TDM-TC-10: Clear Completed Tasks")
    @Story("Todo Cleanup")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify completed tasks can be cleared")
    public void testClearCompletedTasks() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add 'Keep this' todo");
        todoPage.addTodo("Keep this");

        Allure.step("Add 'Complete this' todo and mark it complete");
        todoPage.addTodo("Complete this");
        todoPage.completeTodo(1);

        Allure.step("Clear completed tasks");
        todoPage.clearCompleted();

        Allure.step("Verify only incomplete tasks remain");
        Assert.assertEquals(todoPage.getVisibleTodoCount(), 1, "Completed tasks were not cleared");
        Assert.assertEquals(todoPage.getTodoText(0), "Keep this", "Wrong todo remains after clearing completed");
    }

    @Test(description = "TDM-TC-11: Check persistence after logout")
    @Story("Persistence")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify todos persist after browser restart")
    public void testPersistenceAfterLogout() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add 'Persistent task'");
        todoPage.addTodo("Persistent task");

        Allure.step("Close and reopen browser");
        driver.quit();
        setUp();

        Allure.step("Verify todo persists after browser restart");
        todoPage = new TodoPage(driver);
        todoPage.waitForTodosToLoad();
        Assert.assertTrue(todoPage.getVisibleTodoCount() > 0, "Todos did not persist after browser restart");
    }

    @Test(description = "TDM-TC-12: Browser compatibility")
    @Story("Cross-browser")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify basic functionality works in different browsers")
    public void testBrowserCompatibility() {
        TodoPage todoPage = new TodoPage(driver);
        Allure.step("Add 'Browser test' todo");
        todoPage.addTodo("Browser test");

        Allure.step("Verify basic functionality works");
        Assert.assertEquals(todoPage.getTodoText(0), "Browser test", "Basic functionality failed in this browser");
    }
}