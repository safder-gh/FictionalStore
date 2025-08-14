package com.fs.Admin_UI.views;

import com.fs.Admin_UI.model.UserSession;
import com.fs.Admin_UI.views.users.RegisterView;
import com.fs.Admin_UI.views.users.UserListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

//import javax.swing.text.html.ListView;

public class MainLayout extends SecuredView {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        UserSession userSession = VaadinSession.getCurrent().getAttribute(UserSession.class);
        H1 logo = new H1("Fictional Store Admin Panel");
        logo.addClassNames("text-l", "m-m");
        Button logOut = new Button("Logout "+userSession.getUsername(),e ->  {
            VaadinSession.getCurrent().setAttribute(UserSession.class, null); // remove JWT + user data
            VaadinSession.getCurrent().close(); // close the session
            UI.getCurrent().getPage().setLocation("login");
        }  );

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle()
                .set("background-color", "#f8f9fa")
                .set("border-bottom", "1px solid #ddd");

        // Make title expand to push logout button right
        header.add(new DrawerToggle());
        header.add(logo);
        header.setFlexGrow(1, logo);
        header.add(logOut);

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink products = new RouterLink("Products", ProductView.class);
        products.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink register = new RouterLink("Create User", RegisterView.class);
        register.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink users = new RouterLink("User List", UserListView.class);
        users.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                register,
                products,
                users

        ));
    }
}