package com.fs.Admin_UI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@Theme("my-theme")
public class AdminUiApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(AdminUiApplication.class, args);
    }
}
