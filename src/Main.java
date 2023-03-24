import exceptions.InputException;
import model.*;
import service.FileBackedTasksManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static model.StatusOfTask.NEW;

public class Main {
    public static void main(String[] args) throws InputException {

        File file = new File("file.csv");

        TaskManager taskManagerFile = Managers.getDefaultFile(file);

        System.out.println("Создадим задачи (2 задачи, 1 эпик с 3 подзадачами,1 эпик без подзадач)");
        Task t1 = new Task(
                0,TypeOfTask.TASK,"t1",NEW,"d",
                LocalDateTime.of(2023,01,1,1,0),Duration.ofMinutes(1));
        int t1Id = taskManagerFile.addTask(t1);

        Task t2 = new Task(
                0,TypeOfTask.TASK,"t2",NEW,"d",
                LocalDateTime.of(2023,01,1,2,0),Duration.ofMinutes(1));
        int t2Id = taskManagerFile.addTask(t2);

        Epic e1 = new Epic(
                0,TypeOfTask.EPIC,"e1",NEW,"d",
                null,null,
                new ArrayList<>());
        int e1Id = taskManagerFile.addEpic(e1);

        Subtask s1 = new Subtask(
                0,TypeOfTask.SUBTASK,"s1",NEW,"d",
                LocalDateTime.of(2023,01,1,3,0),Duration.ofMinutes(1),
                e1Id);
        int s1Id = taskManagerFile.addSubtask(s1);

        Subtask s2 = new Subtask(
                0,TypeOfTask.SUBTASK,"s2",NEW,"d",
                LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1),
                e1Id);
        int s2Id = taskManagerFile.addSubtask(s2);

        Subtask s3 = new Subtask(
                0,TypeOfTask.SUBTASK,"s3",NEW,"d",
                LocalDateTime.of(2023,01,1,5,0),Duration.ofMinutes(1),
                e1Id);
        int s3Id = taskManagerFile.addSubtask(s3);

        Epic e2 = new Epic(
                0,TypeOfTask.EPIC,"e2",NEW,"d",
                null,null,
                new ArrayList<>());
        int e2Id = taskManagerFile.addEpic(e2);

        System.out.println("\nСписок всех задач");
        for (Task task : taskManagerFile.getTasks()) { System.out.println(task); }
        for (Task task : taskManagerFile.getEpics()) { System.out.println(task); }
        for (Task task : taskManagerFile.getSubtasks()) { System.out.println(task); }

        System.out.println("\nОтсортированные по дате задачи и подзадачи");
        for (Object prioritizedTask : taskManagerFile.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);
        }

        System.out.println("\nЗапросим задачи для создания истории просмотров задач");
        System.out.println(taskManagerFile.getTaskById(t1Id));
        System.out.println(taskManagerFile.getTaskById(t1Id));
        System.out.println(taskManagerFile.getTaskById(t2Id));
        System.out.println(taskManagerFile.getEpicById(e1Id));
        System.out.println(taskManagerFile.getSubtaskById(s1Id));
        System.out.println(taskManagerFile.getSubtaskById(s2Id));
        System.out.println(taskManagerFile.getSubtaskById(s3Id));
        System.out.println(taskManagerFile.getSubtaskById(s2Id));
        System.out.println(taskManagerFile.getEpicById(e2Id));
        System.out.println(taskManagerFile.getEpicById(e2Id));

        System.out.println("\nИстория просмотров задач");
        for (Task task : taskManagerFile.getHistory()) { System.out.println(task); }

        System.out.println("\nУдалим задачу t1 и эпик e1 содержащий подзадачи");
        taskManagerFile.delTaskById(t1Id);
        taskManagerFile.delEpicById(e1Id);

        System.out.println("\nСписок всех задач");
        for (Task task : taskManagerFile.getTasks()) { System.out.println(task); }
        for (Task task : taskManagerFile.getEpics()) { System.out.println(task); }
        for (Task task : taskManagerFile.getSubtasks()) { System.out.println(task); }

        System.out.println("\nИстория просмотров задач");
        for (Task task : taskManagerFile.getHistory()) { System.out.println(task); }

        System.out.println("\nВосстановим задачи из CSV");
        TaskManager taskManagerFileBackup = FileBackedTasksManager.loadFromFile(file);

        System.out.println("\nВосстановленные задачи");
        for (Task task : taskManagerFile.getTasks()) { System.out.println(task); }
        for (Task task : taskManagerFile.getEpics()) { System.out.println(task); }
        for (Task task : taskManagerFile.getSubtasks()) { System.out.println(task); }

        System.out.println("\nВосстановленная история просмотров задач");
        for (Task task : taskManagerFileBackup.getHistory()) { System.out.println(task); }

    }
}
