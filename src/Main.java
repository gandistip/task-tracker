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

        // Создадим 2 задачи
        int task1Id = taskManager.addTask(new Task("T1", "", 0, StatusOfTask.NEW));
        int task2Id = taskManager.addTask(new Task("T2", "", 0, StatusOfTask.NEW));

        // Создадим 1 эпик
        int epic1Id = taskManager.addEpic(new Epic("E1", "", 0, StatusOfTask.NEW, new ArrayList<Integer>()));

        // Создадим 2 подзадачами
        int epic1subtask1Id = taskManager.addSubtask(new Subtask("E1S1", "", 0, StatusOfTask.NEW, epic1Id));
        int epic1subtask2Id = taskManager.addSubtask(new Subtask("E1S2", "", 0, StatusOfTask.NEW, epic1Id));

        // Просмотрим задачу 2 раза
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task1Id);

        // Просмотрим историю показов задач
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        // Просмотрим эпик 100 раз
        int i = 0;
        while (i < 100) {
            taskManager.getEpicById(epic1Id);
            i++;
        }

        // Просмотрим историю показов задач
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
    }
}
