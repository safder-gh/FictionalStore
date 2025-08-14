package com.fs.Admin_UI.views;

import com.fs.Admin_UI.model.UserSession;
import com.fs.Admin_UI.services.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

//import javax.swing.text.html.ListView;

public class MainLayout extends SecuredView {
private SecurityService securityService;
    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        UserSession userSession = VaadinSession.getCurrent().getAttribute(UserSession.class);
        H1 logo = new H1("Fictional Store Admin Panel");
        logo.addClassNames("text-l", "m-m");
        Button logOut = new Button("Logout "+userSession.getUsername(),e ->  {

        }  );
        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                logOut
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {
//        RouterLink listLink = new RouterLink("List", ListView.class);
//        listLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        addToDrawer(new VerticalLayout(
//                listLink
//        ));
    }
}