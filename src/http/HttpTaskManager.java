package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;

import java.io.File;
import java.net.URI;
import java.util.*;

import static model.TypeOfTask.*;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient kvTaskClient;
    String key = "key";
    Gson gson = new Gson();

    public HttpTaskManager(File file, URI urlKVServer) {
        super(file);
        kvTaskClient = new KVTaskClient(urlKVServer);
    }

    @Override
    public void save() {
        Map<String, String> manager = new HashMap();
        manager.put("tasks", gson.toJson(tasks));
        manager.put("epics", gson.toJson(epics));
        manager.put("subtasks", gson.toJson(subtasks));
        manager.put("id", gson.toJson(id));
        manager.put("history", gson.toJson(getHistory()));
        kvTaskClient.put(key, gson.toJson(manager));
    }

    public void load() {
        String managerString = kvTaskClient.load(key);
        Map<String, String> manager = gson.fromJson(managerString, new TypeToken<Map<String, String>>(){}.getType());
        tasks = gson.fromJson(manager.get("tasks"), new TypeToken<Map<Integer,Task>>(){}.getType());
        epics = gson.fromJson(manager.get("epics"), new TypeToken<Map<Integer,Epic>>(){}.getType());
        subtasks = gson.fromJson(manager.get("subtasks"), new TypeToken<Map<Integer,Subtask>>(){}.getType());
        id = gson.fromJson(manager.get("id"), new TypeToken<Integer>() {}.getType());

        List<Task> history = gson.fromJson(manager.get("history"), new TypeToken<List<Task>>(){}.getType());
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getType() == TASK) {
                List<Task> taskList = gson.fromJson(manager.get("history"), new TypeToken<List<Task>>(){}.getType());
                historyManager.add(taskList.get(i));
            }
            if (history.get(i).getType() == EPIC) {
                List<Epic> epicList = gson.fromJson(manager.get("history"), new TypeToken<List<Epic>>(){}.getType());
                historyManager.add(epicList.get(i));
            }
            if (history.get(i).getType() == SUBTASK) {
                List<Subtask> subtaskList = gson.fromJson(manager.get("history"), new TypeToken<List<Subtask>>(){}.getType());
                historyManager.add(subtaskList.get(i));
            }
        }
    }
}