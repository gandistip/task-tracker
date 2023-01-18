import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        // Создадим 2 задачи
        Task task1 = new Task("T1", "", manager.counterId(), "NEW");
        Task task2 = new Task("T2", "", manager.counterId(), "NEW");

        manager.addTask(task1);
        manager.addTask(task2);

        // Создадим эпик с 2 подзадачами
        Epic epic1 = new Epic("E1", "", manager.counterId(), "", new ArrayList<Integer>());
        Subtask epic1subtask1 = new Subtask("E1S1", "", manager.counterId(), "NEW", epic1.id);
        Subtask epic1subtask2 = new Subtask("E1S2", "", manager.counterId(), "NEW", epic1.id);

        manager.addEpic(epic1);
        manager.addSubtask(epic1subtask1);
        manager.addSubtask(epic1subtask2);

        // Создадим эпик с 1 подзадачей
        Epic epic2 = new Epic("E2", "", manager.counterId(), "", new ArrayList<Integer>());
        Subtask epic2subtask1 = new Subtask("E2S1", "", manager.counterId(), "NEW", epic2.id);

        manager.addEpic(epic2);
        manager.addSubtask(epic2subtask1);

        // Распечатаем списки эпиков, задач и подзадач
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        // Изменим статусы созданных объектов и распечатаем
        Task task1upd1 = new Task("T1", "", task1.id, "DONE");
        Task task2upd1 = new Task("T2", "", task2.id, "DONE");
        Subtask epic1subtask1upd1 = new Subtask("E1S1", "", epic1subtask1.id, "DONE", epic1.id);
        Subtask epic1subtask2upd1 = new Subtask("E1S2", "", epic1subtask2.id, "DONE", epic1.id);
        Subtask epic2subtask1upd1 = new Subtask("E2S1", "", epic2subtask1.id, "DONE", epic2.id);

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
        manager.delTaskById(1);
        manager.delEpicById(3);

        System.out.println("\nПосле удаления задач");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
