package it.trekkete.coreo.ui.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.trekkete.coreo.lib.config.Config;
import it.trekkete.coreo.lib.config.State;
import it.trekkete.coreo.ui.utils.StateEventKeys;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@JsModule("./src/beat-keeper.js")
public class BeatKeeper extends HorizontalLayout implements PropertyChangeListener {

    private State currentState;

    private VerticalLayout container;
    private VerticalLayout outside;
    private HorizontalLayout inner;

    Label current;

    public BeatKeeper(int passi) {

        this.currentState = State.getInstance();

        currentState.add(this);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.AROUND);

        outside = new VerticalLayout();
        outside.setClassName("beat-keeper-outer-container");
        outside.setSpacing(false);
        outside.setPadding(false);
        outside.setSizeUndefined();

        container = new VerticalLayout();
        container.setSpacing(false);
        container.setPadding(false);
        container.setSizeUndefined();
        container.setClassName("beat-keeper-container");
        container.setId("beat-keeper-scroll");

        current = new Label("Passo " + currentState.getPasso());
        current.setClassName("beat-keeper-current-step");

        inner = new HorizontalLayout();
        inner.setClassName("beat-keeper-inner-container");
        inner.setSpacing(false);

        Div arrow = new Div();
        arrow.add(new Icon("lumo", "chevron-down"));
        arrow.getStyle().set("margin", "auto").set("color", "#00ff00");

        container.add(inner);

        outside.add(arrow, container);

        add(current, outside);

        init(passi);
    }

    private void init(int passi) {
        inner.removeAll();

        for (int i = 0; i < passi; i++) {
            inner.add(new Separator(Separator.Orientation.VERTICAL));
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    public void step(int passo) {

        if (container.getId().isPresent()) {
            String cmd = "document.getElementById(\"" + container.getId().get() + "\").scrollLeft = 18 * " + passo + ";";
            getElement().executeJs(cmd);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        switch (propertyChangeEvent.getPropertyName()) {
            case StateEventKeys.CHANGE_PASSO -> {

                Integer newValue = (Integer) propertyChangeEvent.getNewValue();

                if (newValue == null)
                    return;

                step(newValue);
                current.setText("Passo " + (newValue + 1));
            }
            case StateEventKeys.CHANGE_BRANO -> {
                Config.Brano brano = (Config.Brano) propertyChangeEvent.getNewValue();
                if (brano == null)
                    return;
                init(brano.getPassi());
            }
        }
    }
}
