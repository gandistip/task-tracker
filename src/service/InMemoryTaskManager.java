package service;

import exceptions.InputException;
import model.Epic;
import model.StatusOfTask;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    Integer id = 0;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    private int counterId() {
        id++;
        return id;
    }

    private void updateEpicInfo(Epic epic) {
        updateEpicStatus(epic);
        updateEpicDateTime(epic);
    }

    private void updateEpicDateTime(Epic epic) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        Duration duration;
        if (epic.getSubtaskId().isEmpty()) {
            return;
        }
        startTime = getSubtasksOfEpic(epic).get(0).getStartTime();
        endTime = getSubtasksOfEpic(epic).get(0).getEndTime();
        duration = Duration.ofMinutes(0);
        for (Subtask subtask : getSubtasksOfEpic(epic)) {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
            duration = duration.plus(subtask.getDuration());
        }
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        epic.setEndTime(endTime);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskId().equals(Collections.emptyList())) {
            epic.setStatus(StatusOfTask.NEW);
            return;
        }
        boolean isStatusNew = false;
        boolean isStatusInProgress = false;
        boolean isStatusDone = false;
        for (Integer subtaskId : epic.getSubtaskId()) {
            if (subtasks.get(subtaskId).getStatus() == StatusOfTask.NEW) {
                isStatusNew = true;
            }
            if (subtasks.get(subtaskId).getStatus() == StatusOfTask.IN_PROGRESS) {
                isStatusInProgress = true;
            }
            if (subtasks.get(subtaskId).getStatus() == StatusOfTask.DONE) {
                isStatusDone = true;
            }
        }
        if (isStatusNew && !isStatusInProgress && !isStatusDone) {
            epic.setStatus(StatusOfTask.NEW);
        } else if (isStatusDone && !isStatusNew && !isStatusInProgress) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        }
    }

    private void validateTaskByDateTime(Task task) throws InputException {
        for (Task existTask : getPrioritizedTasks()) {
            if ((task.getStartTime().isAfter(existTask.getStartTime()) &&
                    task.getStartTime().isBefore(existTask.getEndTime())) ||
                    (task.getEndTime().isAfter(existTask.getStartTime()) &&
                            task.getEndTime().isBefore(existTask.getEndTime())) ||
                    task.getStartTime().equals(existTask.getStartTime())) {
                throw new InputException("Создаваемая задача " + task.getName() +
                        " пересекается по времени с задачей " + existTask.getName());
            }
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Set<Task> prioritizedTasks = new TreeSet<>(
                Comparator.comparing(Task::getStartTime,
                        Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId)
        );
        prioritizedTasks.addAll(getTasks());
        prioritizedTasks.addAll(getSubtasks());
        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Integer addTask(Task task) throws InputException {
        validateTaskByDateTime(task);
        final int id = counterId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        final int id = counterId();
        epic.setId(id);
        updateEpicInfo(epic);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) throws InputException {
        validateTaskByDateTime(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        final int id = counterId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.getSubtaskId().add(id);
        updateEpicInfo(epic);
        return id;
    }

    @Override
    public void updateTask(Task task) throws InputException {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.remove(id);
        validateTaskByDateTime(task);
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        final Task savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws InputException {
        final int id = subtask.getId();
        final Task savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.remove(id);
        validateTaskByDateTime(subtask);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicInfo(epic);
    }

    @Override
    public Task getTaskById(Integer id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasksOfEpic.add(subtasks.get(subtaskId));
        }
        return subtasksOfEpic;
    }

    @Override
    public void delAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void delAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void delAllSubtasks() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateEpicInfo(epic);
        }
        subtasks.clear();
    }

    @Override
    public void delTaskById(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void delEpicById(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void delSubtaskById(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtaskId().remove(id);
        updateEpicInfo(epic);
        subtasks.remove(id);
        historyManager.remove(id);
    }

}
