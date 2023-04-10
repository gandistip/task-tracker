package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    URI urlKVServer;
    String token;
    Gson gson = new GsonBuilder().create();
    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(URI urlKVServer) {
        this.urlKVServer = urlKVServer;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlKVServer.toString() + "/register"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            token = response.body();
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlKVServer.toString() + "/save/" + key + "?API_TOKEN=" + token))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlKVServer.toString() + "/load/" + key + "?API_TOKEN=" + token))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return null;
    }

}
