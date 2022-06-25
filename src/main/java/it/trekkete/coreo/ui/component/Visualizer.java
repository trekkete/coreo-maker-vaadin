package it.trekkete.coreo.ui.component;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Visualizer extends VerticalLayout {

    private Div jsContainer;

    public Visualizer() {

        jsContainer = new Div();
        jsContainer.setClassName("heatmap-container");
        jsContainer.setWidthFull();
        jsContainer.setHeightFull();
        jsContainer.getStyle().remove("max-width");
        jsContainer.getStyle().set("border", "1px solid var(--lumo-shade-20pct)");

        add(jsContainer);

        setSizeFull();
    }
}
