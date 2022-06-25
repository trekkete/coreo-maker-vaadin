package it.trekkete.coreo.ui.utils;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;

import java.util.List;

public class MenuItemDefinition {

    private String title;
    private ComponentEventListener<ClickEvent<MenuItem>> listener;
    private List<MenuItemDefinition> subMenuItems;

    public MenuItemDefinition(String title, ComponentEventListener<ClickEvent<MenuItem>> listener, List<MenuItemDefinition> subMenuItems) {
        this.title = title;
        this.listener = listener;
        this.subMenuItems = subMenuItems;
    }

    public String getTitle() {
        return title;
    }

    public ComponentEventListener<ClickEvent<MenuItem>> getListener() {
        return listener;
    }

    public List<MenuItemDefinition> getSubMenuItems() {
        return subMenuItems;
    }
}
