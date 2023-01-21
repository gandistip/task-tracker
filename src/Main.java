import model.Epic;
import model.Subtask;
import model.Task;
import service.Manager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        // Создадим 2 задачи
        Task task1 = new Task("T1", "", 0, "NEW");
        Task task2 = new Task("T2", "", 0, "NEW");

        int task1Id = manager.addTask(task1);
        int task2Id = manager.addTask(task2);

        // Создадим эпик с 2 подзадачами
        Epic epic1 = new Epic("E1", "", 0, "", new ArrayList<Integer>());
        int epic1Id = manager.addEpic(epic1);

        Subtask epic1subtask1 = new Subtask("E1S1", "", 0, "NEW", epic1Id);
        Subtask epic1subtask2 = new Subtask("E1S2", "", 0, "NEW", epic1Id);
        int epic1subtask1Id = manager.addSubtask(epic1subtask1);
        int epic1subtask2Id = manager.addSubtask(epic1subtask2);

        // Создадим эпик с 1 подзадачей
        Epic epic2 = new Epic("E2", "", 0, "", new ArrayList<Integer>());
        int epic2Id = manager.addEpic(epic2);

        Subtask epic2subtask1 = new Subtask("E2S1", "", 0, "NEW", epic2Id);
        int epic2subtask1Id = manager.addSubtask(epic2subtask1);

        // Распечатаем списки эпиков, задач и подзадач
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        // Изменим статусы созданных объектов и распечатаем
        Task task1upd1 = new Task("T1", "", task1Id, "DONE");
        Task task2upd1 = new Task("T2", "", task2Id, "DONE");
        Subtask epic1subtask1upd1 = new Subtask("E1S1", "", epic1subtask1Id, "DONE", epic1Id);
        Subtask epic1subtask2upd1 = new Subtask("E1S2", "", epic1subtask2Id, "DONE", epic1Id);
        Subtask epic2subtask1upd1 = new Subtask("E2S1", "", epic2subtask1Id, "DONE", epic2Id);

        manager.updateTask(task1upd1);
        manager.updateTask(task2upd1);
        manager.updateSubtask(epic1subtask1upd1);
        manager.updateSubtask(epic1subtask2upd1);
        manager.updateSubtask(epic2subtask1upd1);

        System.out.println("\nПосле изменения статусов задач");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        // Удалим 1 задачу и 1 эпик и распечатаем
        manager.delTaskById(task1Id);
        manager.delEpicById(epic1Id);

        System.out.println("\nПосле удаления задач");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
