package it.trekkete.coreo.ui.utils;

import com.vaadin.flow.component.UI;

public class TimeManager extends Thread{

    private final it.trekkete.coreo.lib.config.State state;

    private boolean stop;
    private UI ui;

    private static TimeManager instance;

    private TimeManager() {
        this.state = it.trekkete.coreo.lib.config.State.getInstance();
        this.stop = false;
    }

    public static TimeManager getInstance() {
        if(instance == null) {
            synchronized (TimeManager.class) {
                if (instance == null)
                    instance = new TimeManager();
            }
        }
        return instance;
    }

    public UI getUi() {
        return ui;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    @Override
    public void run() {

        while (!stop) {

            int passo = state.getPasso();

            if (passo > state.getBrano().getPassi())
                break;

            try {
                Thread.sleep(60000 / state.getBpm());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (ui != null)
                ui.push();

            state.setPasso(passo + 1);
        }

        instance = null;

        state.setPasso(0);
        state.setPlaying(false);

        if (ui != null)
            ui.push();
    }

    public void pause() {
        stop = true;
    }
}
