package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    static File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("file.csv");
        TaskManager taskManagerFile = Managers.getDefaultFile(file);

        System.out.println("1.1 Создадим таски");
        int t1Id = taskManagerFile.addTask(new Task(0, TypeOfTask.TASK,"T1", StatusOfTask.NEW, "dT1"));
        int e1Id = taskManagerFile.addEpic(new Epic(0, TypeOfTask.EPIC,"E1", StatusOfTask.NEW, "dE1", new ArrayList<Integer>()));
        int s1Id = taskManagerFile.addSubtask(new Subtask(0, TypeOfTask.SUBTASK,"S1", StatusOfTask.NEW, "dS1", e1Id));
        System.out.println(taskManagerFile.getTasks());
        System.out.println(taskManagerFile.getEpics());
        System.out.println(taskManagerFile.getSubtasks());

        System.out.println("1.2 Запросим таски для создания истории");
        System.out.println(taskManagerFile.getTaskById(t1Id));
        System.out.println(taskManagerFile.getTaskById(t1Id));
        System.out.println(taskManagerFile.getEpicById(e1Id));
        System.out.println(taskManagerFile.getTaskById(t1Id));

        System.out.println("1.3 Просмотрим историю");
        for (Task task : taskManagerFile.getHistory()) { System.out.println(task); }

        System.out.println("\n2.1 Восстановим таски из CSV");
        TaskManager taskManagerFileBackup = FileBackedTasksManager.loadFromFile(file);

        System.out.println("2.2 Восстановленные таски");
        System.out.println(taskManagerFileBackup.getTasks());
        System.out.println(taskManagerFileBackup.getEpics());
        System.out.println(taskManagerFileBackup.getSubtasks());

        System.out.println("2.3 Восстановленная история");
        for (Task task : taskManagerFileBackup.getHistory()) { System.out.println(task); }
    }

    private void save() {
        List<Task> allTask = new ArrayList<>();
        allTask.addAll(getTasks());
        allTask.addAll(getEpics());
        allTask.addAll(getSubtasks());
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : allTask) {
                fileWriter.write(toString(task) + "\n");
            }
            fileWriter.write("\n" + historyToString(historyManager));
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    private String toString(Task task) {
        String taskString;
        taskString = String.format("%s,%s,%s,%s,%s,",
                task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription());
        if (task.getType() == TypeOfTask.SUBTASK) {
            taskString = taskString + String.format("%s", ((Subtask) task).getEpicId());
        }
        return taskString;
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder historyString = new StringBuilder();
        for (Task task : manager.getHistory()) {
            historyString.append(task.getId() + ",");
        }
        if (historyString.length() > 1) {
            historyString.deleteCharAt((historyString.length() - 1));
        }
        return historyString.toString();
    }

    private static Task fromString(String value) {
        final String[] values = value.split(",");
        final Integer id = Integer.parseInt(values[0]);
        final TypeOfTask type = TypeOfTask.valueOf(values[1]);
        final String name = values[2];
        final StatusOfTask status = StatusOfTask.valueOf(values[3]);
        final String description = values[4];
        if (type == TypeOfTask.TASK) {
            return new Task(id, type, name, status, description);
        } else if (type == TypeOfTask.EPIC) {
            return new Epic(id, type, name, status, description, new ArrayList<Integer>());
        } else if (type == TypeOfTask.SUBTASK) {
            final Integer epicId = Integer.parseInt(values[5]);
            return new Subtask(id, type, name, status, description, epicId);
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyIdList = new ArrayList<>();
        for (String s : value.split(",")) {
            historyIdList.add(Integer.parseInt(s));
        }
        return historyIdList;
    }

    private static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        List<String> linesInFile = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (!line.equals("")) {
                    linesInFile.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            e.printStackTrace();
        }

        int countTasksInFile;
        boolean isExistHistory = false;
        String lastLine = linesInFile.get(linesInFile.size() - 1);
        if (lastLine.contains("TASK") || lastLine.contains("EPIC")) {
            countTasksInFile = linesInFile.size();
        } else {
            countTasksInFile = linesInFile.size() - 1;
            isExistHistory = true;
        }
        for (int i = 1; i < countTasksInFile; i++) {
            Task task = fromString(linesInFile.get(i));
            if (task.getType() == TypeOfTask.TASK) {
                fileBackedTasksManager.addTask(task);
            } else if (task.getType() == TypeOfTask.EPIC) {
                fileBackedTasksManager.addEpic((Epic) task);
            } else if (task.getType() == TypeOfTask.SUBTASK) {
                fileBackedTasksManager.addSubtask((Subtask) task);
            }
        }
        if (isExistHistory) {
            for (Integer integer : historyFromString(lastLine)) {
                if (fileBackedTasksManager.tasks.containsKey(integer)) {
                    fileBackedTasksManager.getTaskById(integer);
                } else if (fileBackedTasksManager.epics.containsKey(integer)) {
                    fileBackedTasksManager.getEpicById(integer);
                } else if (fileBackedTasksManager.subtasks.containsKey(integer)) {
                    fileBackedTasksManager.getSubtaskById(integer);
                }
            }
        }
        return fileBackedTasksManager;
    }

    @Override
    public Integer addTask(Task task) {
        Integer id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        Integer id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        Integer id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        save();
        super.updateTask(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        save();
        super.updateEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        save();
        super.updateSubtask(subtask);
    }

    @Override
    public void delAllTasks() {
        save();
        super.delAllTasks();
    }

    @Override
    public void delAllEpics() {
        save();
        super.delAllEpics();
    }

    @Override
    public void delAllSubtasks() {
        save();
        super.delAllSubtasks();
    }

    @Override
    public void delTaskById(Integer id) {
        save();
        super.delTaskById(id);
    }

    @Override
    public void delEpicById(Integer id) {
        save();
        super.delEpicById(id);
    }

    @Override
    public void delSubtasksById(Integer id) {
        save();
        super.delSubtasksById(id);
    }
}
