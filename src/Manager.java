import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Manager {
    Integer id = 0;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Integer counterId() {
        id++;
        return id;
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.epicId == epic.id) {
                subtasksOfEpic.add(subtask);
            }
        }
        return subtasksOfEpic;
    }

    public ArrayList<Integer> getSubtaskIdOfEpic(Epic epic) {
        ArrayList<Integer> subtaskIdsOfEpic = new ArrayList<>();
        for (Subtask subtask : getSubtasksOfEpic(epic)) {
            subtaskIdsOfEpic.add(subtask.id);
        }
        return subtaskIdsOfEpic;
    }

    public Epic getEpicOfSubtask(Subtask subtask) {
        return epics.get(subtask.epicId);
    }

    public String getEpicStatus(Epic epic) {
        if (getSubtasksOfEpic(epic).equals(Collections.emptyList())) {
            return "NEW";
        }
        String status;
        boolean isStatusNew = false;
        boolean isStatusInProgress = false;
        boolean isStatusDone = false;
        for (Subtask subtask : getSubtasksOfEpic(epic)) {
            if (subtask.status.equals("NEW")) {
                isStatusNew = true;
            }
            if (subtask.status.equals("IN_PROGRESS")) {
                isStatusInProgress = true;
            }
            if (subtask.status.equals("DONE")) {
                isStatusDone = true;
            }
        }
        if (isStatusNew && !isStatusInProgress && !isStatusDone) {
            status = "NEW";
        } else if (isStatusDone && !isStatusNew && !isStatusInProgress) {
            status = "DONE";
        } else {
            status = "IN_PROGRESS";
        }
        return status;
    }

    public void addTask(Task task) {
        tasks.put(task.id, task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.id, epic);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
        updateEpic(getEpicOfSubtask(subtask));
    }

    public void updateTask(Task task) {
        addTask(task);
    }

    public void updateEpic(Epic epic) {
        epic.subtaskId = getSubtaskIdOfEpic(epic);
        epic.status = getEpicStatus(epic);
        addEpic(epic);
    }

    public void updateSubtask(Subtask subtask) {
        addSubtask(subtask);
        updateEpic(getEpicOfSubtask(subtask));
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskList.add(task);
        }
        return taskList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(epic);
        }
        return epicList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtaskList.add(subtask);
        }
        return subtaskList;
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public void delAllTasks() { tasks.clear(); }

    public void delAllEpics() {
        epics.clear();
    }

    public void delAllSubtasks() {
        subtasks.clear();
        delAllEpics();
    }

    public void delTaskById(Integer id) {
        tasks.remove(id);
    }

    public void delEpicById(Integer id) {
        for (Subtask subtask : getSubtasksOfEpic(getEpicById(id))) {
            delSubtasksById(subtask.id);
        }
        epics.remove(id);
    }

    public void delSubtasksById(Integer id) {
        Epic epic = getEpicOfSubtask(getSubtaskById(id));
        subtasks.remove(id);
        updateEpic(epic);
    }
}
