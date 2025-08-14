package com.fs.Admin_UI.views.users;

import com.fs.Admin_UI.HttpService.UserHttpService;
import com.fs.Admin_UI.model.UserSession;
import com.fs.Admin_UI.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route(value = "register",layout = MainLayout.class)
public class RegisterView extends VerticalLayout {
    private final UserHttpService httpService = new UserHttpService();
    public RegisterView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Title
        H2 heading = new H2("User Registration");

        // Fields
        TextField usernameField = new TextField("Username");
        usernameField.setWidth("300px");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setWidth("300px");

        // Button
        Button registerButton = new Button("Register", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Username and password are required", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                boolean success = httpService.register(username, password);
                if (success) {
                    Notification.show("Registration successful!", 3000, Notification.Position.MIDDLE);
                    UI.getCurrent().navigate("login");
                } else {
                    Notification.show("Registration failed!", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }

        });

        registerButton.setWidth("300px");

        // Add to layout
        VerticalLayout formLayout = new VerticalLayout(heading, usernameField, passwordField, registerButton);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "8px")
                .set("padding", "20px")
                .set("background-color", "#fff");

        add(formLayout);
    }
}
