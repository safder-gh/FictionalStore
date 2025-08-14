package com.fs.Admin_UI.HttpService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs.Admin_UI.dtos.UserDTO;
import com.fs.Admin_UI.model.UserSession;
import com.vaadin.flow.server.VaadinSession;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UserHttpService {

    private static final String BASE_URL = "http://127.0.0.1:8080/auth-service/api/auth"; // adjust as needed
    private final HttpClient client;
    private final ObjectMapper mapper;

    public UserHttpService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }
    private HttpRequest.Builder authorizedRequest(URI uri) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json");

        UserSession userSession = VaadinSession.getCurrent().getAttribute(UserSession.class);
        String token = userSession.getJwtToken();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }
    public HttpResponse<String> login(String userName,String password) throws Exception
    {
        String body = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                userName,
                password
        );

        HttpRequest httpRequest = authorizedRequest(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
    public boolean register(String userName,String password) throws Exception {
        String body = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                userName,
                password
        );

        HttpRequest httpRequest = authorizedRequest(URI.create(BASE_URL + "/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public List<UserDTO> getUsers() throws Exception {
        HttpRequest httpRequest = authorizedRequest(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<UserDTO>>() {});
        } else {
            throw new RuntimeException("Failed to fetch users: " + response.body());
        }
    }
}
