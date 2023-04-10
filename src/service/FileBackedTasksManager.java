package service;

import exceptions.InputException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    static File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws InputException {
        FileBackedTasksManager fileBackedTasksManager = Managers.getTaskManagerFile();
        if (file.length() == 0) {
            throw new InputException("Файл пуст");
        }
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

    public void save() {
        List<Task> allTask = new ArrayList<>();
        allTask.addAll(getTasks());
        allTask.addAll(getEpics());
        allTask.addAll(getSubtasks());
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,endTime,epic" + "\n");
            for (Task task : allTask) {
                fileWriter.write(toString(task) + "\n");
            }
            fileWriter.write("\n" + historyToString(historyManager));
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    protected String toString(Task task) {
        String taskString;
        taskString = String.format("%s,%s,%s,%s,%s,%s,%s,%s,",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getStartTime(),
                task.getDuration(),
                task.getEndTime()
        );
        if (task.getType() == TypeOfTask.SUBTASK) {
            taskString = taskString + String.format("%s", ((Subtask) task).getEpicId());
        }
        return taskString;
    }

    private static Task fromString(String value) {
        final String[] values = value.split(",");

        final Integer id = Integer.parseInt(values[0]);
        final TypeOfTask type = TypeOfTask.valueOf(values[1]);
        final String name = values[2];
        final StatusOfTask status = StatusOfTask.valueOf(values[3]);
        final String description = values[4];
        final LocalDateTime startTime;
        final Duration duration;

        if (values[5].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(values[5]);
        }
        if (values[6].equals("null")) {
            duration = null;
        } else {
            duration = Duration.parse(values[6]);
        }

        if (type == TypeOfTask.TASK) {
            return new Task(id, type, name, status, description, startTime, duration);
        } else if (type == TypeOfTask.EPIC) {
            return new Epic(id, type, name, status, description, startTime, duration, new ArrayList<>());
        } else if (type == TypeOfTask.SUBTASK) {
            final Integer epicId = Integer.parseInt(values[8]);
            return new Subtask(id, type, name, status, description, startTime, duration, epicId);
        }
        return null;
    }

    protected static String historyToString(HistoryManager manager) {
        StringBuilder historyString = new StringBuilder();
        for (Task task : manager.getHistory()) {
            historyString.append(task.getId() + ",");
        }
        if (historyString.length() > 1) {
            historyString.deleteCharAt((historyString.length() - 1));
        }
        return historyString.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyIdList = new ArrayList<>();
        for (String s : value.split(",")) {
            historyIdList.add(Integer.parseInt(s));
        }
        return historyIdList;
    }

    @Override
    public Integer addTask(Task task) throws InputException {
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
    public Integer addSubtask(Subtask subtask) throws InputException {
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
    public void updateTask(Task task) throws InputException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws InputException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void delAllTasks() {
        super.delAllTasks();
        save();
    }

    @Override
    public void delAllEpics() {
        super.delAllEpics();
        save();
    }

    @Override
    public void delAllSubtasks() {
        super.delAllSubtasks();
        save();
    }

    @Override
    public void delTaskById(Integer id) {
        super.delTaskById(id);
        save();
    }

    @Override
    public void delEpicById(Integer id) {
        super.delEpicById(id);
        save();
    }

    @Override
    public void delSubtaskById(Integer id) {
        super.delSubtaskById(id);
        save();
    }
}
