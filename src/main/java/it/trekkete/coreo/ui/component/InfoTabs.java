package it.trekkete.coreo.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import it.trekkete.coreo.lib.config.Config;
import it.trekkete.coreo.lib.config.State;
import it.trekkete.coreo.ui.utils.ConfigEventKeys;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class InfoTabs extends VerticalLayout implements PropertyChangeListener {

    private Tabs tabs;
    private Tab atleti;
    private Tab brani;
    private Tab coreografie;
    private Config config;
    private State state;

    private Grid<Config.Atleta> atletaGrid;
    private Grid<Config.Brano> branoGrid;
    private Grid<Config.Brano.Coreografia> coreografiaGrid;

    public void updateTabs(Map<Tab, Component> tabsToPages) {
        tabsToPages.values().forEach(page -> page.setVisible(false));
        Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
        selectedPage.setVisible(true);
    }

    public InfoTabs() {

        config = Config.getInstance();
        state = State.getInstance();

        config.add(this);

        atleti = new Tab("Atleti");
        brani = new Tab("Brani");
        coreografie = new Tab("Coreografie");

        VerticalLayout atletiLayout = atletiLayout();
        VerticalLayout braniLayout = braniLayout();
        VerticalLayout coreografieLayout = coreografieLayout();

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(atleti, atletiLayout);
        tabsToPages.put(brani, braniLayout);
        tabsToPages.put(coreografie, coreografieLayout);
        tabs = new Tabs(atleti, brani, coreografie);
        tabs.setSelectedTab(atleti);
        Div pages = new Div(atletiLayout, braniLayout, coreografieLayout);
        pages.setSizeFull();

        add(tabs, pages);
        setSizeFull();

        tabs.addSelectedChangeListener(e -> updateTabs(tabsToPages));
        tabs.setWidthFull();
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        updateTabs(tabsToPages);
    }

    private VerticalLayout atletiLayout() {

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        atletaGrid = new Grid<>();
        atletaGrid.setSizeFull();

        atletaGrid.addItemClickListener(click -> {
            state.setAtleta(click.getItem());
        });

        atletaGrid.addColumn(Config.Atleta::getName).setHeader("Nome");
        atletaGrid.addColumn(Config.Atleta::getClasse).setHeader("Tipo");
        atletaGrid.addComponentColumn(atleta -> {

            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            Button delete = new Button(new Icon(VaadinIcon.TRASH), click -> {

                if (atleta.equals(state.getAtleta()))
                    state.setAtleta(null);

                config.delete(atleta);
            });
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, delete);
        });

        Config.Atleta one = new Config.Atleta();
        one.setName("Uno");
        one.setClasse(Config.Atleta.Classe.CHIARINA);

        Config.Atleta two = new Config.Atleta();
        two.setName("Due");
        two.setClasse(Config.Atleta.Classe.CHIARINA);

        atletaGrid.setItems(one, two);

        container.add(atletaGrid);

        return container;
    }

    private VerticalLayout braniLayout() {

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        branoGrid = new Grid<>();
        branoGrid.setSizeFull();

        branoGrid.addItemClickListener(click -> state.setBrano(click.getItem()));

        branoGrid.addColumn(Config.Brano::getName).setHeader("Nome");
        branoGrid.addColumn(Config.Brano::getDiffTamburi).setHeader("C. Tamburi");
        branoGrid.addColumn(Config.Brano::getDiffChiarine).setHeader("C. Chiarine");
        branoGrid.addColumn(brano -> brano.getCoreografia() == null ? "0" : brano.getCoreografia().size()).setHeader("Coreografie");

        branoGrid.addComponentColumn(brano -> {

            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            Button delete = new Button(new Icon(VaadinIcon.TRASH), click -> {

                if (brano.equals(state.getBrano()))
                    state.setBrano(null);

                config.delete(brano);
            });
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, delete);
        });

        Config.Brano brano = new Config.Brano();
        brano.setName("Title");
        brano.setDiffTamburi(Config.Brano.Classe.AA);
        brano.setDiffChiarine(Config.Brano.Classe.V);
        brano.setPassi(80);

        Config.Brano another = new Config.Brano();
        another.setName("Another");
        another.setDiffTamburi(Config.Brano.Classe.D);
        another.setDiffChiarine(Config.Brano.Classe.Dp);
        another.setPassi(60);

        branoGrid.setItems(brano, another);
        container.add(branoGrid);

        return container;
    }

    private VerticalLayout coreografieLayout() {

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        coreografiaGrid = new Grid<>();
        coreografiaGrid.setSizeFull();

        coreografiaGrid.addItemClickListener(click -> state.setCoreografia(click.getItem()));

        coreografiaGrid.addColumn(Config.Brano.Coreografia::getId).setHeader("ID");
        coreografiaGrid.addColumn(Config.Brano.Coreografia::getInizio).setHeader("Passo d'Inizio");
        coreografiaGrid.addColumn(Config.Brano.Coreografia::getDurata).setHeader("Numero di Passi");
        coreografiaGrid.addComponentColumn(brano -> {

            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(edit, delete);
        });

        //grid.setItems(config.getCoreografie());
        container.add(coreografiaGrid);

        return container;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        switch (propertyChangeEvent.getPropertyName()) {
            case ConfigEventKeys.CHANGE_ATLETA -> {

                atletaGrid.setItems(config.getAtleti());
            }
            case ConfigEventKeys.CHANGE_BRANO -> {

                branoGrid.setItems(config.getBrani());
            }
        }
    }
}
