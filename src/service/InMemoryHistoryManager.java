package service;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_TASKS_SIZE = 10;
    LinkedList<Task> historyTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        historyTasks.add(task);
        if (historyTasks.size() > HISTORY_TASKS_SIZE) {
            historyTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

}
