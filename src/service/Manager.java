package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Manager {
    Integer id = 0;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int counterId() {
        id++;
        return id;
    }

    public String getEpicStatus(Epic epic) {
        if (epic.getSubtaskId().equals(Collections.emptyList())) {
            return "NEW";
        }
        String status;
        boolean isStatusNew = false;
        boolean isStatusInProgress = false;
        boolean isStatusDone = false;
        for (Integer subtaskId : epic.getSubtaskId()) {
            if (subtasks.get(subtaskId).getStatus().equals("NEW")) {
                isStatusNew = true;
            }
            if (subtasks.get(subtaskId).getStatus().equals("IN_PROGRESS")) {
                isStatusInProgress = true;
            }
            if (subtasks.get(subtaskId).getStatus().equals("DONE")) {
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

    public Integer addTask(Task task) {
        final int id = counterId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer addEpic(Epic epic) {
        final int id = counterId();
        epic.setId(id);
        epic.setStatus(getEpicStatus(epic));
        epics.put(id, epic);
        return id;
    }

    public Integer addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        final int id = counterId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.getSubtaskId().add(id);
        epic.setStatus(getEpicStatus(epic));
        return id;
    }

    public void updateTask(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        final Task savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epics.put(id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final Task savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setStatus(getEpicStatus(epic));
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasksOfEpic.add(subtasks.get(subtaskId));
        }
        return subtasksOfEpic;
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
        subtasks.clear();
    }

    public void delAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            epic.setStatus(getEpicStatus(epic));
        }
        subtasks.clear();
    }

    public void delTaskById(Integer id) {
        tasks.remove(id);
    }

    public void delEpicById(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void delSubtasksById(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtaskId().remove(id);
        epic.setStatus(getEpicStatus(epic));
        subtasks.remove(id);
    }
}
