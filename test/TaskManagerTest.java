import exceptions.InputException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TypeOfTask;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static model.StatusOfTask.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    Task t1 = new Task(
            0,TypeOfTask.TASK,"t1",NEW,"d",
            LocalDateTime.of(2023,01,1,1,0),Duration.ofMinutes(1));
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
    Subtask s1 = new Subtask(
            0,TypeOfTask.SUBTASK,"s1",NEW,"d",
            LocalDateTime.of(2023,01,1,3,0),Duration.ofMinutes(1),
            e1Id);
    int s1Id = taskManager.addSubtask(s1);
    Subtask s2 = new Subtask(
            0,TypeOfTask.SUBTASK,"s2",NEW,"d",
            LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1),
            e1Id);
    int s2Id = taskManager.addSubtask(s2);
    Epic e2 = new Epic(
            0,TypeOfTask.EPIC,"e2",NEW,"d",
            null,null,
            new ArrayList<>());
    int e2Id = taskManager.addEpic(e2);

    public TaskManagerTest() throws InputException, IOException, InterruptedException {
    }

    @Test
    public void getNewTask() {
        Task getT1 = taskManager.getTaskById(t1Id);
        assertNotNull(getT1, "Задача не найдена.");
        assertEquals(t1, getT1, "Задачи не совпадают.");
    }

    @Test
    public void getAllNewTask() {
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(t1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateNewTask() throws InputException {
        taskManager.updateTask(t1);
        Task upT1 = taskManager.getTaskById(t1Id);

        assertEquals(t1, upT1, "Одинаковые задачи не совпадают.");

        taskManager.updateTask(new Task(
                t1Id,TypeOfTask.TASK,"upT1",NEW,"d",
                LocalDateTime.of(2023,01,1,1,0),Duration.ofMinutes(1)));
        upT1 = taskManager.getTaskById(t1Id);

        assertNotEquals(t1, upT1, "Разные задачи совпадают.");
    }

    @Test
    public void delNewTask() {
        taskManager.delTaskById(t1Id);

        assertThrows(NullPointerException.class, ()-> taskManager.getTaskById(t1Id), "Задача не удалилась");

        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Удалились все задачи.");
        assertEquals(1, tasks.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    public void delAllNewTask() {
        taskManager.delAllTasks();

        assertEquals(0, taskManager.getTasks().size(), "Удалены не все задачи.");
    }

    @Test
    public void getNewSubtask() {
        Subtask getS1 = taskManager.getSubtaskById(s1Id);
        assertNotNull(getS1, "Задача не найдена.");
        assertEquals(s1, getS1, "Задачи не совпадают.");
    }

    @Test
    public void getAllNewSubtask() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(s1, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateNewSubtask() throws InputException {
        taskManager.updateSubtask(s1);
        Subtask upS1 = taskManager.getSubtaskById(s1Id);

        assertEquals(s1, upS1, "Одинаковые задачи не совпадают.");

        taskManager.updateSubtask(new Subtask(
                s1Id,TypeOfTask.SUBTASK,"upS1",NEW,"d",
                LocalDateTime.of(2023,01,1,3,0),Duration.ofMinutes(1),
                e1Id));
        upS1 = taskManager.getSubtaskById(s1Id);

        assertNotEquals(s1, upS1, "Разные задачи совпадают.");
    }

    @Test
    public void delNewSubtask() {
        taskManager.delSubtaskById(s1Id);

        assertThrows(NullPointerException.class, ()-> taskManager.getSubtaskById(s1Id), "Задача не удалилась");

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Удалились все задачи.");
        assertEquals(1, subtasks.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    public void delAllNewSubtask() {
        taskManager.delAllSubtasks();

        assertEquals(0, taskManager.getSubtasks().size(), "Удалены не все задачи.");
    }

    // Epic epic

    @Test
    public void getNewEpic() {
        Epic getE1 = taskManager.getEpicById(e1Id);
        assertNotNull(getE1, "Задача не найдена.");
        assertEquals(e1, getE1, "Задачи не совпадают.");
    }

    @Test
    public void getAllNewEpic() {
        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(e1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateNewEpic() {
        taskManager.updateEpic(e1);
        Epic upE1 = taskManager.getEpicById(e1Id);

        assertEquals(e1, upE1, "Одинаковые задачи не совпадают.");

        taskManager.updateEpic(new Epic(
                e1Id,TypeOfTask.EPIC,"upE1",NEW,"d",
                null,null,
                e1.getSubtaskId()));
        upE1 = taskManager.getEpicById(e1Id);

        assertNotEquals(e1, upE1, "Разные задачи совпадают.");
    }

    @Test
    public void delNewEpic() {
        taskManager.delEpicById(e1Id);

        assertThrows(NullPointerException.class, ()-> taskManager.getEpicById(e1Id), "Задача не удалилась");

        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Удалились все задачи.");
        assertEquals(1, epics.size(), "Неверное количество оставшихся задач.");
    }

    @Test
    public void delAllNewEpic() {
        taskManager.delAllEpics();

        assertEquals(0, taskManager.getEpics().size(), "Удалены не все задачи.");
    }

    @Test
    public void getWrongTask() {
        assertThrows(NullPointerException.class, ()-> taskManager.getTaskById(999), "Нет такой задачи");
    }

    @Test
    public void getWrongEpic() {
        assertThrows(NullPointerException.class, ()-> taskManager.getEpicById(999), "Нет такой задачи");
    }

    @Test
    public void getWrongSubtask() {
        assertThrows(NullPointerException.class, ()-> taskManager.getSubtaskById(999), "Нет такой задачи");
    }

}
