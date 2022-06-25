package it.trekkete.coreo.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MenuButton extends Button {

    public MenuButton(VaadinIcon icon) {
        super(new Icon(icon));

        setClassName("coreomaker-menu-button");
    }
}
