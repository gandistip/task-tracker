import exceptions.InputException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TypeOfTask;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.StatusOfTask.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();
    Task t1 = new Task(
            0, TypeOfTask.TASK,"t1",NEW,"d",
            LocalDateTime.of(2023,01,1,1,0), Duration.ofMinutes(1));
    int t1Id = taskManager.addTask(t1);

    Task t2 = new Task(
            0,TypeOfTask.TASK,"t2",NEW,"d",
            LocalDateTime.of(2023,01,1,2,0),Duration.ofMinutes(1));
    int t2Id = taskManager.addTask(t2);

    Epic e1 = new Epic(
            0,TypeOfTask.EPIC,"e1",NEW,"d",
            null,null,
            new ArrayList<>());
    int e1Id = taskManager.addEpic(e1);

    public HistoryManagerTest() throws InputException {
    }

    @Test
    public void historyEmpty() {
        assertEquals(Collections.emptyList(), taskManager.getHistory(), "История не пуста.");
    }

    @Test
    public void historyDuplication() {
        taskManager.getTaskById(t1Id);
        taskManager.getTaskById(t1Id);

        List<Task> history = taskManager.getHistory();
        assertNotEquals(Collections.emptyList(), history, "История пуста.");
        assertEquals(1, history.size(), "Дубль в истории.");
        assertEquals(t1, history.get(0), "Задачи не совпадают.");
    }

    @Test
    public void historyDeleteMiddle() {
        taskManager.getTaskById(t1Id);
        taskManager.getTaskById(t2Id);
        taskManager.getEpicById(e1Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "Объем истории отличается от ожидаемого.");
        assertEquals(t2, history.get(1), "Отсутствует задача в середине списка.");

        taskManager.delTaskById(t2Id);
        history = taskManager.getHistory();
        assertEquals(2, history.size(), "Объем истории отличается от ожидаемого.");
        assertNotEquals(t2, history.get(0), "Задача не удалена.");
        assertNotEquals(t2, history.get(1), "Задача не удалена.");
    }

    @Test
    public void historyDeleteFirst() {
        taskManager.getTaskById(t1Id);
        taskManager.getTaskById(t2Id);
        taskManager.getEpicById(e1Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(t1, history.get(0), "Отсутствует ожидаемая в начале списка задача.");

        taskManager.delTaskById(t1Id);
        history = taskManager.getHistory();
        assertNotEquals(t1, history.get(0), "Задача не удалена.");
        assertNotEquals(t1, history.get(1), "Задача не удалена.");
    }

    @Test
    public void historyDeleteEnd() {
        taskManager.getTaskById(t1Id);
        taskManager.getTaskById(t2Id);
        taskManager.getEpicById(e1Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(e1, history.get(2), "Отсутствует ожидаемая в конце списка задача.");

        taskManager.delEpicById(e1Id);
        history = taskManager.getHistory();
        assertNotEquals(e1, history.get(0), "Задача не удалена.");
        assertNotEquals(e1, history.get(1), "Задача не удалена.");
    }
}
