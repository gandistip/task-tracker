import exceptions.InputException;
import http.HttpTaskManager;
import model.*;
import http.KVServer;
import service.Managers;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static model.StatusOfTask.*;
import static model.TypeOfTask.*;

public class Main {
    public static void main(String[] args) throws InputException, IOException, InterruptedException {

        KVServer kvServer = new KVServer();
        kvServer.start();

        System.out.println("1. Создадим HttpTaskManager и задачи");
        HttpTaskManager taskManager = Managers.getDefault();

        Task t1 = new Task(0, TASK,"t1",NEW,"d",
                LocalDateTime.of(2023,01,1,1,0),Duration.ofMinutes(1));
        int t1Id = taskManager.addTask(t1);
        Task t2 = new Task(0, TASK,"t2",NEW,"d",
                LocalDateTime.of(2023,01,1,2,0),Duration.ofMinutes(1));
        int t2Id = taskManager.addTask(t2);

        Epic e1 = new Epic(0, EPIC,"e1",NEW,"d",
                null,null, new ArrayList<>());
        int e1Id = taskManager.addEpic(e1);
        Epic e2 = new Epic(0, EPIC,"e2",NEW,"d",
                null,null, new ArrayList<>());
        int e2Id = taskManager.addEpic(e2);

        Subtask s1 = new Subtask(0, SUBTASK,"s1",NEW,"d",
                LocalDateTime.of(2023,01,1,3,0),Duration.ofMinutes(1), e1Id);
        int s1Id = taskManager.addSubtask(s1);
        Subtask s2 = new Subtask(0, SUBTASK,"s2",NEW,"d",
                LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1), e1Id);
        int s2Id = taskManager.addSubtask(s2);

        System.out.println("\nСписок всех задач");
        for (Task task : taskManager.getTasks()) { System.out.println(task); }
        for (Task task : taskManager.getEpics()) { System.out.println(task); }
        for (Task task : taskManager.getSubtasks()) { System.out.println(task); }

        System.out.println("\nЗапросим задачи для создания истории просмотров задач");
        System.out.println(taskManager.getTaskById(t1Id));
        System.out.println(taskManager.getTaskById(t1Id));
        System.out.println(taskManager.getTaskById(t2Id));
        System.out.println(taskManager.getEpicById(e1Id));
        System.out.println(taskManager.getSubtaskById(s1Id));
        System.out.println(taskManager.getSubtaskById(s2Id));
        System.out.println(taskManager.getSubtaskById(s2Id));
        System.out.println(taskManager.getEpicById(e2Id));
        System.out.println(taskManager.getEpicById(e2Id));

        System.out.println("\nИстория просмотров задач");
        for (Task task : taskManager.getHistory()) { System.out.println(task); }

        System.out.println("\n2. Восстановим задачи из KVServer в новый HttpTaskManager");
        HttpTaskManager loadTaskManager = Managers.getDefault();

        System.out.println("\nВосстановленные задачи");
        for (Task task : loadTaskManager.getTasks()) { System.out.println(task); }
        for (Task task : loadTaskManager.getEpics()) { System.out.println(task); }
        for (Task task : loadTaskManager.getSubtasks()) { System.out.println(task); }

        System.out.println("\nВосстановленная история просмотров задач");
        for (Task task : loadTaskManager.getHistory()) { System.out.println(task); }

        System.out.println("\nДобавим задачу t3");
        Task t3 = new Task(0, TASK,"t1",NEW,"d",
                LocalDateTime.of(2023,01,1,6,0),Duration.ofMinutes(1));
        int t3Id = loadTaskManager.addTask(t3);

        System.out.println("\nЗапросим новую задачу t3 для проверки формирования истории");
        System.out.println(loadTaskManager.getTaskById(t3Id));

        System.out.println("\nУдалим задачу t1 и эпик e1 содержащий подзадачи");
        loadTaskManager.delTaskById(t1Id);
        loadTaskManager.delEpicById(e1Id);

        System.out.println("\nСписок всех задач");
        for (Task task : loadTaskManager.getTasks()) { System.out.println(task); }
        for (Task task : loadTaskManager.getEpics()) { System.out.println(task); }
        for (Task task : loadTaskManager.getSubtasks()) { System.out.println(task); }

        System.out.println("\nИстория просмотров задач");
        for (Task task : loadTaskManager.getHistory()) { System.out.println(task); }

        kvServer.stop();

    }
}
