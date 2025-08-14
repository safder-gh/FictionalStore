package com.fs.Admin_UI.views;


import com.fs.Admin_UI.model.UserSession;
import com.fs.Admin_UI.services.TokenService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

public abstract class SecuredView extends AppLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//        UserSession userSession = VaadinSession.getCurrent().getAttribute(UserSession.class);
//        if (userSession == null || userSession.getJwtToken() == null) {
//            event.forwardTo(LoginView.class);
//        }
    }
}
