package com.fs.Admin_UI.views.users;

import com.fs.Admin_UI.HttpService.UserHttpService;
import com.fs.Admin_UI.dtos.UserDTO;
import com.fs.Admin_UI.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "users",layout = MainLayout.class)
public class UserListView extends VerticalLayout {

    private final UserHttpService httpService = new UserHttpService();

    public UserListView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 heading = new H2("User List");

        Grid<UserDTO> grid = new Grid<>(UserDTO.class, false);
        grid.addColumn(UserDTO::getUsername).setHeader("Username").setAutoWidth(true);

        try {
            List<UserDTO> users = httpService.getUsers();
            grid.setItems(users);
        } catch (Exception e) {
            Notification.show("Failed to load users: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }

        add(heading, grid);
    }
}
