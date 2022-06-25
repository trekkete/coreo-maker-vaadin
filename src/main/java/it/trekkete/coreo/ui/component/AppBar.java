package it.trekkete.coreo.ui.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.trekkete.coreo.lib.config.Config;
import it.trekkete.coreo.lib.config.State;
import it.trekkete.coreo.ui.utils.MenuItemDefinition;
import it.trekkete.coreo.ui.utils.StateEventKeys;
import it.trekkete.coreo.ui.utils.TimeManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

public class AppBar extends VerticalLayout implements PropertyChangeListener {

    private Config currentConfig;
    private State currentState;

    private Button newFile;
    private Button openFile;
    private Button saveFile;
    private Button addAtleta;
    private Button addBrano;
    private Button play;
    private Button stop;
    private IntegerField bpm;

    private final List<MenuItemDefinition> menus = Arrays.asList(
            new MenuItemDefinition("File", null, Arrays.asList(
                    new MenuItemDefinition("Nuovo Progetto", null, null),
                    new MenuItemDefinition("Apri...", null, null),
                    new MenuItemDefinition("Salva", null, null),
                    new MenuItemDefinition("Salva con nome...", null, null),
                    new MenuItemDefinition("Chiudi", null, null)
            )),
            new MenuItemDefinition("Modifica", click -> {}, null),
            new MenuItemDefinition("Visualizza", click -> {}, null),
            new MenuItemDefinition("About", click -> {}, null)
    );

    private final MenuBar menuBar;
    private final TextField projectTitle;
    private final TextField currAtleta;
    private final TextField currBrano;
    private final TextField currCoreografia;

    public AppBar() {

        currentConfig = Config.getInstance();
        currentState = State.getInstance();

        currentState.add(this);

        setSpacing(false);

        projectTitle = new TextField();
        projectTitle.setPlaceholder("Progetto senza nome");
        projectTitle.addClassName("app-bar-project-title");
        projectTitle.setWidth("500px");

        menuBar = new MenuBar();
        menuBar.addClassName("app-bar-menu");

        menus.forEach(menuItemDefinition -> {

            MenuItem item = menuBar.addItem(menuItemDefinition.getTitle(), menuItemDefinition.getListener());
            SubMenu subMenu = item.getSubMenu();

            if (menuItemDefinition.getSubMenuItems() != null && !menuItemDefinition.getSubMenuItems().isEmpty()) {
                menuItemDefinition.getSubMenuItems().forEach(subMenuItem -> subMenu.addItem(subMenuItem.getTitle(), subMenuItem.getListener()));
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addClassName("app-bar-buttons");
        horizontalLayout.setSpacing(false);
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        newFile = new Button(new Icon(VaadinIcon.FILE_ADD), click -> {});
        newFile.setClassName("app-bar-button");

        openFile = new Button(new Icon(VaadinIcon.FOLDER_OPEN_O), click -> {});
        openFile.setClassName("app-bar-button");
        Upload upload = new Upload(new FileBuffer());
        upload.setDropAllowed(false);
        upload.setUploadButton(openFile);

        saveFile = new Button(new Icon(VaadinIcon.HARDDRIVE_O), click -> {});
        saveFile.setClassName("app-bar-button");

        addAtleta = new Button(new Icon(VaadinIcon.USER), click -> {
            Dialog dialog = new Dialog();

            VerticalLayout dialogContainer = new VerticalLayout();
            dialogContainer.setWidth("500px");
            dialogContainer.setHeight("400px");

            H2 title = new H2("Aggiungi un atleta");
            title.addClassName("window-title");

            TextField nome = new TextField("Nome Atleta");
            nome.setWidthFull();

            ComboBox<Config.Atleta.Classe> classe = new ComboBox<>("Tipo");
            classe.setWidthFull();
            classe.setItems(Config.Atleta.Classe.values());
            classe.setValue(Config.Atleta.Classe.CHIARINA);

            dialogContainer.add(title, nome, classe);

            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setJustifyContentMode(JustifyContentMode.END);
            buttons.setWidthFull();

            Button add = new Button("Aggiungi", addClick -> {

                Config.Atleta atleta = new Config.Atleta();
                atleta.setId(currentConfig.getNextFreeAID());
                atleta.setName(nome.getValue());
                atleta.setClasse(classe.getValue());

                currentConfig.insert(atleta);

                currentState.setAtleta(atleta);

                dialog.close();
            });
            add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button cancel = new Button("Annulla", cancelClick -> dialog.close());

            buttons.add(add, cancel);

            dialogContainer.addAndExpand(new Span());
            dialogContainer.add(buttons);

            dialog.add(dialogContainer);

            dialog.open();
        });
        addAtleta.setClassName("app-bar-button");;

        addBrano = new Button(new Icon(VaadinIcon.MUSIC), click -> {

            Dialog dialog = new Dialog();

            VerticalLayout dialogContainer = new VerticalLayout();
            dialogContainer.setWidth("500px");
            dialogContainer.setHeight("400px");

            H2 title = new H2("Aggiungi un brano");
            title.addClassName("window-title");

            TextField nome = new TextField("Nome Brano");
            nome.setWidthFull();

            ComboBox<Config.Brano.Classe> chiarine = new ComboBox<>("Categoria Chiarine");
            chiarine.setWidthFull();
            chiarine.setItems(Config.Brano.Classe.values());
            chiarine.setValue(Config.Brano.Classe.Z);

            ComboBox<Config.Brano.Classe> tamburi = new ComboBox<>("Categoria Tamburi");
            tamburi.setWidthFull();
            tamburi.setItems(Config.Brano.Classe.values());
            tamburi.setValue(Config.Brano.Classe.Z);

            IntegerField passi = new IntegerField("Passi totali");
            passi.setWidthFull();
            passi.setMin(1);
            passi.setMax(200);
            passi.setStep(1);

            dialogContainer.add(title, nome, chiarine, tamburi, passi);

            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setJustifyContentMode(JustifyContentMode.END);
            buttons.setWidthFull();

            Button add = new Button("Aggiungi", addClick -> {

                Config.Brano brano = new Config.Brano();
                brano.setId(currentConfig.getNextFreeBID());
                brano.setName(nome.getValue());
                brano.setDiffChiarine(chiarine.getValue());
                brano.setDiffTamburi(tamburi.getValue());
                brano.setPassi(passi.getValue());

                currentConfig.insert(brano);

                currentState.setBrano(brano);

                dialog.close();
            });
            add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button cancel = new Button("Annulla", cancelClick -> dialog.close());

            buttons.add(add, cancel);

            dialogContainer.addAndExpand(new Span());
            dialogContainer.add(buttons);

            dialog.add(dialogContainer);

            dialog.open();

        });
        addBrano.setClassName("app-bar-button");

        play = new Button(new Icon(VaadinIcon.PLAY), click -> {

            if (TimeManager.getInstance().getUi() == null)
                TimeManager.getInstance().setUI(UI.getCurrent());

            TimeManager.getInstance().start();
            currentState.setPlaying(true);
        });
        play.setClassName("app-bar-button");

        stop = new Button(new Icon(VaadinIcon.STOP), click -> {

            if (TimeManager.getInstance().getUi() == null)
                TimeManager.getInstance().setUI(UI.getCurrent());

            TimeManager.getInstance().pause();
            currentState.setPlaying(false);
        });
        stop.setClassName("app-bar-button");

        bpm = new IntegerField();
        bpm.addClassName("beat-keeper-bpm");
        bpm.setValue(currentState.getBpm());
        bpm.setMin(1);
        bpm.setMax(250);
        bpm.setStep(1);
        bpm.setPlaceholder("bpm");

        bpm.addValueChangeListener( event -> currentState.setBpm(event.getValue()));
        bpm.setValueChangeMode(ValueChangeMode.ON_BLUR);

        Div suffix = new Div();
        suffix.setText("BPM");

        bpm.setSuffixComponent(suffix);

        currAtleta = new TextField();
        currAtleta.setPlaceholder("Atleta selezionato");

        currBrano = new TextField();
        currBrano.setPlaceholder("Brano selezionato");

        currCoreografia = new TextField();
        currCoreografia.setPlaceholder("Coreografia selezionata");

        horizontalLayout.add(newFile, upload, saveFile, new Separator(Separator.Orientation.VERTICAL),
                addAtleta, addBrano, new Separator(Separator.Orientation.VERTICAL),
                play, stop, new Separator(Separator.Orientation.VERTICAL),
                bpm, new Separator(Separator.Orientation.VERTICAL),
                currAtleta, new Separator(Separator.Orientation.VERTICAL),
                currBrano, new Separator(Separator.Orientation.VERTICAL),
                currCoreografia);

        add(projectTitle, menuBar, horizontalLayout);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        switch (propertyChangeEvent.getPropertyName()) {
            case StateEventKeys.CHANGE_ATLETA -> {

                Config.Atleta newValue = (Config.Atleta) propertyChangeEvent.getNewValue();

                if (newValue == null) {
                    currAtleta.clear();
                    return;
                }

                currAtleta.setValue(newValue.getName());
            }
            case StateEventKeys.CHANGE_BRANO -> {

                Config.Brano newValue = (Config.Brano) propertyChangeEvent.getNewValue();

                play.setEnabled(newValue != null);
                stop.setEnabled(false);

                if (newValue == null) {
                    currBrano.clear();
                    return;
                }

                currBrano.setValue(newValue.getName());
            }
            case StateEventKeys.CHANGE_COREOGRAFIA -> {

                Config.Brano.Coreografia newValue = (Config.Brano.Coreografia) propertyChangeEvent.getNewValue();

                if (newValue == null) {
                    currCoreografia.clear();
                    return;
                }

                currCoreografia.setValue(newValue.getId() + "");
            }
            case StateEventKeys.CHANGE_BPM -> {

                Integer newValue = (Integer) propertyChangeEvent.getNewValue();

                if (newValue == null)
                    return;

                bpm.setValue(newValue);
            }
            case StateEventKeys.CHANGE_PLAY -> {
                boolean isPlaying = (boolean) propertyChangeEvent.getNewValue();
                if (isPlaying) {
                    stop.setEnabled(true);
                    play.setEnabled(false);
                } else {
                    stop.setEnabled(false);
                    play.setEnabled(true);
                }
            }
        }
    }
}
