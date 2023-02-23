import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        //System.out.println("1. Создадим 2 задачи, 1 эпик с 3 подзадачами и 1 эпик без подзадач;");
        int task1Id = taskManager.addTask(new Task("T1", "", 0, StatusOfTask.NEW));
        int task2Id = taskManager.addTask(new Task("T2", "", 0, StatusOfTask.NEW));
        int epic1Id = taskManager.addEpic(new Epic("E1", "", 0, StatusOfTask.NEW, new ArrayList<Integer>()));
        int subtask1Epic1Id = taskManager.addSubtask(new Subtask("S1E1", "", 0, StatusOfTask.NEW, epic1Id));
        int subtask2Epic1Id = taskManager.addSubtask(new Subtask("S2E1", "", 0, StatusOfTask.NEW, epic1Id));
        int subtask3Epic1Id = taskManager.addSubtask(new Subtask("S3E1", "", 0, StatusOfTask.NEW, epic1Id));
        int epic2Id = taskManager.addEpic(new Epic("E2", "", 0, StatusOfTask.NEW, new ArrayList<Integer>()));

        System.out.println("2. Запросим созданные задачи несколько раз в разном порядке;");
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);
        taskManager.getEpicById(epic1Id);
        taskManager.getSubtaskById(subtask1Epic1Id);
        taskManager.getSubtaskById(subtask2Epic1Id);
        taskManager.getSubtaskById(subtask3Epic1Id);
        taskManager.getSubtaskById(subtask2Epic1Id);
        taskManager.getEpicById(epic2Id);
        taskManager.getEpicById(epic2Id);

        System.out.println("3. Просмотрим историю");
        for (Task task : taskManager.getHistory()) { System.out.println(task); }

        System.out.println("4. Удалим задачу task1Id;");
        taskManager.delTaskById(task1Id);

        System.out.println("5. Просмотрим историю");
        for (Task task : taskManager.getHistory()) { System.out.println(task); }

        System.out.println("6. Удалим эпик epic1Id с тремя его подзадачами;");
        taskManager.delEpicById(epic1Id);

        System.out.println("7. Просмотрим историю");
        for (Task task : taskManager.getHistory()) { System.out.println(task); }

    }
}
