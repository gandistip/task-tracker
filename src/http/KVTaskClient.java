package http;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    URI urlKVServer;
    String token;
    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(URI urlKVServer) {
        this.urlKVServer = urlKVServer;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlKVServer.toString() + "/register"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
            token = response.body();
        } catch (NullPointerException | IOException | InterruptedException | ManagerSaveException e) {
            throw new RuntimeException("Can't do save request", e);
        }
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlKVServer.toString() + "/save/" + key + "?API_TOKEN=" + token))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException | ManagerSaveException e) {
            throw new RuntimeException("Can't do save request", e);
        }

    }

    public String load(String key) throws ManagerSaveException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlKVServer.toString() + "/load/" + key + "?API_TOKEN=" + token))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
            return response.body();
        } catch (NullPointerException | IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do save request");
        }
    }

}
