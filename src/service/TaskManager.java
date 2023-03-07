package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    Integer addTask(Task task);
    Integer addEpic(Epic epic);
    Integer addSubtask(Subtask subtask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<Subtask> getSubtasks();
    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);
    Task getTaskById(Integer id);
    Epic getEpicById(Integer id);
    Subtask getSubtaskById(Integer id);

    void delAllTasks();
    void delAllEpics();
    void delAllSubtasks();
    void delTaskById(Integer id);
    void delEpicById(Integer id);
    void delSubtasksById(Integer id);
}
