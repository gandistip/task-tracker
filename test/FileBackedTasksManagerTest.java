import exceptions.InputException;
import model.Task;
import model.TypeOfTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.Managers;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.StatusOfTask.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest {
    File file = new File("fileTest.csv");
    FileBackedTasksManager taskManagerFile = Managers.getDefaultFile(file);

    Task t1 = new Task(
            0, TypeOfTask.TASK,"t1",NEW,"d",
            LocalDateTime.of(2023,01,1,1,0), Duration.ofMinutes(1));
    Task t2 = new Task(
            0,TypeOfTask.TASK,"t2",NEW,"d",
            LocalDateTime.of(2023,01,1,2,0),Duration.ofMinutes(1));

    public FileBackedTasksManagerTest(){
    }

    @BeforeEach
    public void cleaContentFile() throws IOException {
        new PrintWriter("fileTest.csv").close();
    }

    @Test
    public void readEmptyFile(){
        assertThrows(InputException.class, ()-> taskManagerFile.loadFromFile(file).getTasks(),"Файл не пуст");
    }

    @Test
    public void addTaskToFile() throws InputException{
        int t1Id = taskManagerFile.addTask(t1);
        String taskFromManager = taskManagerFile.getTaskById(t1Id).toString();
        String taskFromFile = taskManagerFile.loadFromFile(file).getTaskById(t1Id).toString();

        assertEquals(taskFromManager, taskFromFile, "Задачи совпадают.");
    }

    @Test
    public void emptyHistory() throws InputException{
        int t1Id = taskManagerFile.addTask(t1);
        assertTrue(taskManagerFile.getHistory().isEmpty(), "История не пуста");
    }

    @Test
    public void addHistory() throws InputException{
        int t1Id = taskManagerFile.addTask(t1);
        int t2Id = taskManagerFile.addTask(t2);
        taskManagerFile.getTaskById(t1Id);
        taskManagerFile.getTaskById(t2Id);

        String historyFromManager = taskManagerFile.getHistory().toString();
        String historyFromFile = taskManagerFile.loadFromFile(file).getHistory().toString();

        assertEquals(historyFromManager, historyFromFile, "Задачи совпадают.");
    }

}
