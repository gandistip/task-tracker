package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    int historyTasksSize = 10;
    List<Task> historyTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        historyTasks.add(task);
        if (historyTasks.size() > historyTasksSize) {
            historyTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

}
