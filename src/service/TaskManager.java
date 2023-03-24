package service;

import exceptions.InputException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getHistory();
    Set<Task> getPrioritizedTasks();

    Integer addTask(Task task) throws InputException;
    Integer addEpic(Epic epic);
    Integer addSubtask(Subtask subtask) throws InputException;

    void updateTask(Task task) throws InputException;
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask) throws InputException;

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
    void delSubtaskById(Integer id);
}
