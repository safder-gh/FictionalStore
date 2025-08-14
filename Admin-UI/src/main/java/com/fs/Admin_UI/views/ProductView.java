package com.fs.Admin_UI.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "products",layout = MainLayout.class)
public class ProductView extends VerticalLayout {
    public ProductView(){
        add(new H1("Products"));
    }
}
