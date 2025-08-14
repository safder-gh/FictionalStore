package com.fs.Admin_UI.views;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs.Admin_UI.model.UserSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Route("app-login")
public class LoginView extends VerticalLayout {

    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final Button loginButton = new Button("Login");

    public LoginView() {
        // Take full screen
        setSizeFull();
        setSpacing(false);
        setPadding(false);

        // Center content both vertically & horizontally
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        // Create form fields
        H2 heading = new H2("Fictional Store Admin");
        heading.getStyle()
                .set("margin", "0")
                .set("font-weight", "600")
                .set("text-align", "center");

        username.setWidthFull();
        password.setWidthFull();
        loginButton.addClickListener(e -> doLogin());
        loginButton.setWidthFull();

        // Create a responsive "card" for the login form
        VerticalLayout loginCard = new VerticalLayout(heading,username, password, loginButton);
        loginCard.setWidth("90%");
        loginCard.setMaxWidth("400px"); // Prevents it from stretching too much
        loginCard.setSpacing(true);
        loginCard.setPadding(true);
        loginCard.setAlignItems(Alignment.STRETCH);

        // Style the card
        loginCard.getStyle()
                .set("background-color", "white")
                .set("border-radius", "10px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)")
                .set("padding", "20px");

        // Add to main layout
        add(loginCard);

        // Optional: set background color
        getStyle().set("background-color", "#f0f2f5");
    }

    private void doLogin() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String body = String.format(
                    "{\"username\":\"%s\", \"password\":\"%s\"}",
                    username.getValue(),
                    password.getValue()
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8080/AUTH-SERVICE/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());
                String token = jsonNode.get("token").asText();

                //VaadinSession.getCurrent().setAttribute("jwt", token);
                VaadinSession.getCurrent().setAttribute(UserSession.class, new UserSession(username.getValue(), token));

                Notification.show("Login successful");
                UI.getCurrent().navigate("");
            } else {
                Notification.show("Login failed: " + response.statusCode());
            }
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
