package it.trekkete.coreo.ui.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import it.trekkete.coreo.lib.config.Config;
import it.trekkete.coreo.lib.config.State;
import it.trekkete.coreo.ui.component.AppBar;
import it.trekkete.coreo.ui.component.BeatKeeper;
import it.trekkete.coreo.ui.component.InfoTabs;
import it.trekkete.coreo.ui.component.Visualizer;
import it.trekkete.coreo.ui.utils.TimeManager;

@JavaScript(value = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
@Route
public class MainView extends VerticalLayout {

    public static TimeManager time;

    private VerticalLayout appBar;
    private HorizontalLayout appContainer;
    private VerticalLayout jsContainer;

    private VerticalLayout externalContainer;
    private VerticalLayout songContainer;
    private VerticalLayout infoContainer;

    private BeatKeeper beatKeeper;

    private final Config currentConfig;
    private final State currentState;

    public MainView() {

        currentConfig = Config.getInstance();
        currentState = State.getInstance();

        beatKeeper = new BeatKeeper(0);

        appBar = new AppBar();
        appBar.setSizeFull();
        appBar.setMaxHeight("150px");
        appBar.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        appContainer = new HorizontalLayout();
        appContainer.setSizeFull();

        Visualizer visualizer = new Visualizer();

        jsContainer = new VerticalLayout();
        jsContainer.setSizeFull();
        jsContainer.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        jsContainer.add(visualizer);

        externalContainer = new VerticalLayout();
        externalContainer.setSizeFull();

        songContainer = new VerticalLayout();
        songContainer.setWidthFull();
        songContainer.setHeight("50%");
        songContainer.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        songContainer.add(beatKeeper);

        InfoTabs infoTabs = new InfoTabs();

        infoContainer = new VerticalLayout();
        infoContainer.setSizeFull();
        infoContainer.add(infoTabs);

        externalContainer.add(songContainer, infoContainer);

        appContainer.add(jsContainer, externalContainer);

        setSizeFull();
        add(appBar, appContainer);

        currentState.setAtleta(null);
        currentState.setBrano(null);
        currentState.setCoreografia(null);
        currentState.setBpm(150);
        currentState.setPasso(0);
        currentState.setPlaying(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        TimeManager.getInstance().setUI(attachEvent.getUI());
    }
}
