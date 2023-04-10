package service;

import http.HttpTaskManager;

import java.io.File;
import java.net.URI;

public class Managers {
    static File file = new File("file.csv");
    static URI urlKVServer = URI.create("http://localhost:8078");

    public static HttpTaskManager getDefault() { return new HttpTaskManager(file, urlKVServer); }
    public static FileBackedTasksManager getTaskManagerFile() {
        return new FileBackedTasksManager(file);
    }
    public static TaskManager getTaskManagerMemory() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
