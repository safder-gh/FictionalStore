package com.fs.Admin_UI.listener;

import com.fs.Admin_UI.model.UserSession;
import com.fs.Admin_UI.views.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

@Component
public class AuthRouteGuard implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().addBeforeEnterListener(beforeEnterListener())
        );
    }

    private BeforeEnterListener beforeEnterListener() {
        return event -> {
            // Public routes — don't block these
            if (LoginView.class.equals(event.getNavigationTarget())) {
                return;
            }

            // Check if user is authenticated
            UserSession userSession = VaadinSession.getCurrent().getAttribute(UserSession.class);

            if (userSession == null || userSession.getJwtToken() == null) {
                event.forwardTo(LoginView.class);
            }
        };
    }
}
