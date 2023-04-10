import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static model.StatusOfTask.*;
import static model.TypeOfTask.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager taskManager = taskServer.taskManager;
    HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Task t1,t2;
    private Epic e1,e2;
    private Subtask s1,s2;

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void init() throws IOException, InterruptedException {
        taskServer.start();

        t1 = new Task(0,TASK,"t1",NEW,"d",
                LocalDateTime.of(2023,01,1,1,0), Duration.ofMinutes(1));
        t2 = new Task(0,TASK,"t2",NEW,"d",
                LocalDateTime.of(2023,01,1,2,0),Duration.ofMinutes(1));

        e1 = new Epic(0, EPIC,"e1",NEW,"d",null,null, new ArrayList<>());
        e2 = new Epic(0, EPIC,"e2",NEW,"d",null,null, new ArrayList<>());

        s1 = new Subtask(0, SUBTASK,"s1",NEW,"d",
                LocalDateTime.of(2023,01,1,3,0),Duration.ofMinutes(1), 3);
        s2 = new Subtask(0, SUBTASK,"s2",NEW,"d",
                LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1), 3);

        HttpRequest requestT1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(t1))).build();
        HttpResponse<String> responseT1 = client.send(requestT1, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseT1.body(), "Задача добавлена");
        HttpRequest requestT2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(t2))).build();
        HttpResponse<String> responseT2 = client.send(requestT2, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseT2.body(), "Задача добавлена");

        HttpRequest requestE1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(e1))).build();
        HttpResponse<String> responseE1 = client.send(requestE1, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseE1.body(), "Эпик добавлен");
        HttpRequest requestE2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(e2))).build();
        HttpResponse<String> responseE2 = client.send(requestE2, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseE2.body(), "Эпик добавлен");

        HttpRequest requestS1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(s1))).build();
        HttpResponse<String> responseS1 = client.send(requestS1, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseS1.body(), "Подзадача добавлена");
        HttpRequest requestS2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(s2))).build();
        HttpResponse<String> responseS2 = client.send(requestS2, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseS2.body(), "Подзадача добавлена");
    }

    @AfterEach
    void tearDown() { taskServer.stop(); }

    @Test
    void getTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getTaskById(1).toString(), gson.fromJson(response.body(), Task.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getEpicById(3).toString(), gson.fromJson(response.body(), Epic.class).toString(),
                "Эпики не совпадают");
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getSubtaskById(5).toString(), gson.fromJson(response.body(), Subtask.class).toString(),
                "Подзадачи не совпадают");
    }

    @Test
    void delTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Задача удалена", response.body(), "Задача не удалена");
    }

    @Test
    void delEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Эпик удален", response.body(), "Эпик не удален");
    }

    @Test
    void delSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Подзадача удалена", response.body(), "Подзадача не удалена");
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getPrioritizedTasks()), response.body(), "Задачи не совпадают");
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getTasks()), response.body(), "Задачи не совпадают");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getEpics()), response.body(), "Задачи не совпадают");
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getSubtasks()), response.body(), "Задачи не совпадают");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getHistory()), response.body(), "История не совпадает");
    }

    @Test
    void getSubtasksOfEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getSubtasksOfEpic(taskManager.getEpicById(3))),
                response.body(), "Подзадачи не совпадают");
    }

}