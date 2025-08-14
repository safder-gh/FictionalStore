package com.fs.Admin_UI.views;


import com.fs.Admin_UI.services.TokenService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class SecuredView extends AppLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!TokenService.isValid()) {
            TokenService.clear();
            event.rerouteTo("login");
        }
    }
}
