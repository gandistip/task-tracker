package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private HttpServer server;
    private Gson gson;
    public TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getTaskManagerFile();
        gson = new GsonBuilder().setPrettyPrinting().create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().toString();
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {

                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        writeResponse(httpExchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^/tasks/history$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        writeResponse(httpExchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/epic/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/epic/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubtasksOfEpic(taskManager.getEpicById(id)));
                            writeResponse(httpExchange, response, 200);
                        } else {
                            System.out.println("Не может быть такого id");
                            httpExchange.sendResponseHeaders(405,0);
                        }
                        break;
                    }

                    if (Pattern.matches("^/tasks/task$", path)) {
                        String response = gson.toJson(taskManager.getTasks());
                        writeResponse(httpExchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            writeResponse(httpExchange, response, 200);
                        } else {
                            System.out.println("Не может быть такого id");
                            httpExchange.sendResponseHeaders(405,0);
                        }
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        writeResponse(httpExchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpicById(id));
                            writeResponse(httpExchange, response, 200);
                        } else {
                            System.out.println("Не может быть такого id");
                            httpExchange.sendResponseHeaders(405,0);
                        }
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        String response = gson.toJson(taskManager.getSubtasks());
                        writeResponse(httpExchange, response, 200);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubtaskById(id));
                            writeResponse(httpExchange, response, 200);
                        } else {
                            System.out.println("Не может быть такого id");
                            httpExchange.sendResponseHeaders(405,0);
                        }
                        break;
                    }
                }

                case "DELETE": {
                    if (Pattern.matches("^/tasks/task$", path)) {
                        taskManager.delAllTasks();
                        writeResponse(httpExchange, "Все задачи удалены", 200);
                        return;
                    }
                    if (Pattern.matches("^/tasks/task/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.delTaskById(id);
                            writeResponse(httpExchange, "Задача удалена", 200);
                        } else {
                            writeResponse(httpExchange, "Не может быть такого id", 405);
                        }
                        return;
                    }

                    if (Pattern.matches("^/tasks/epic$", path)) {
                        taskManager.delAllEpics();
                        writeResponse(httpExchange, "Все эпики удалены", 200);
                        return;
                    }
                    if (Pattern.matches("^/tasks/epic/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.delEpicById(id);
                            writeResponse(httpExchange, "Эпик удален", 200);
                        } else {
                            writeResponse(httpExchange, "Не может быть такого id", 405);
                        }
                        return;
                    }

                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        taskManager.delAllSubtasks();
                        writeResponse(httpExchange, "Все подзадачи удалены", 200);
                        return;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\?id=\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/\\?id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.delSubtaskById(id);
                            writeResponse(httpExchange, "Подзадача удалена", 200);
                        } else {
                            writeResponse(httpExchange, "Не может быть такого id", 405);
                        }
                        return;
                    }

                    else {
                        writeResponse(httpExchange, "Нет такого URL", 405);
                    }
                }

                case "POST": {
                    if (Pattern.matches("^/tasks/task$", path)) {
                        Task task;
                        try {
                            task = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Task.class);
                        } catch (JsonSyntaxException exception) {
                            writeResponse(httpExchange, "Получен некорректный JSON", 400);
                            return;
                        }
                        try {
                            taskManager.getTaskById(task.getId());
                            taskManager.updateTask(task);
                            writeResponse(httpExchange, "Задача обновлена", 200);
                        } catch (NullPointerException exception) {
                            taskManager.addTask(task);
                            writeResponse(httpExchange, "Задача добавлена", 200);
                            return;
                        }
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic$", path)) {
                        Epic epic;
                        try {
                            epic = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Epic.class);
                        } catch (JsonSyntaxException exception) {
                            writeResponse(httpExchange, "Получен некорректный JSON", 400);
                            return;
                        }
                        try {
                            taskManager.getEpicById(epic.getId());
                            taskManager.updateEpic(epic);
                            writeResponse(httpExchange, "Эпик обновлен", 200);
                        } catch (NullPointerException exception) {
                            taskManager.addEpic(epic);
                            writeResponse(httpExchange, "Эпик добавлен", 200);
                            return;
                        }
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        Subtask subtask;
                        try {
                            subtask = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Subtask.class);
                        } catch (JsonSyntaxException exception) {
                            writeResponse(httpExchange, "Получен некорректный JSON", 400);
                            return;
                        }
                        try {
                            taskManager.getSubtaskById(subtask.getId());
                            taskManager.updateSubtask(subtask);
                            writeResponse(httpExchange, "Подзадача обновлена", 200);
                        } catch (NullPointerException exception) {
                            taskManager.addSubtask(subtask);
                            writeResponse(httpExchange, "Подзадача добавлена", 200);
                            return;
                        }
                        break;
                    }
                }

                default: {
                    writeResponse(httpExchange, "Нет такого метода", 405);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private void writeResponse (HttpExchange httpExchange, String responseString, int responseCode) throws IOException {
        if(responseString.isBlank()) {
            httpExchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        httpExchange.close();
    }

}
