package it.trekkete.coreo.lib.config;

import it.trekkete.coreo.ui.utils.StateEventKeys;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class State {

    private static State instance;

    private State() {
        support = new PropertyChangeSupport(this);
    }

    public static State getInstance() {
        if(instance == null) {
            synchronized (State.class) {
                if (instance == null)
                    instance = new State();
            }
        }
        return instance;
    }

    private final PropertyChangeSupport support;

    private Config.Atleta atleta;
    private Config.Brano brano;
    private Config.Brano.Coreografia coreografia;
    private int passo;
    private int bpm;
    private boolean playing;

    public Config.Atleta getAtleta() {
        return atleta;
    }

    public void setAtleta(Config.Atleta atleta) {
        support.firePropertyChange(StateEventKeys.CHANGE_ATLETA, this.atleta, atleta);

        this.atleta = atleta;
    }

    public Config.Brano getBrano() {
        return brano;
    }

    public void setBrano(Config.Brano brano) {
        support.firePropertyChange(StateEventKeys.CHANGE_BRANO, this.brano, brano);

        this.brano = brano;

        setPasso(0);
        setPlaying(false);
    }

    public Config.Brano.Coreografia getCoreografia() {
        return coreografia;
    }

    public void setCoreografia(Config.Brano.Coreografia coreografia) {
        support.firePropertyChange(StateEventKeys.CHANGE_COREOGRAFIA, this.coreografia, coreografia);

        this.coreografia = coreografia;
    }

    public int getPasso() {
        return passo;
    }

    public void setPasso(int passo) {
        System.out.println("Changing passo from " + this.passo + " to " + passo);
        support.firePropertyChange(StateEventKeys.CHANGE_PASSO, this.passo, passo);

        this.passo = passo;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        support.firePropertyChange(StateEventKeys.CHANGE_BPM, this.bpm, bpm);

        this.bpm = bpm;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        System.out.println("Changing play from " + this.playing + " to " + playing);
        support.firePropertyChange(StateEventKeys.CHANGE_PLAY, this.playing, playing);

        this.playing = playing;
    }

    public void add(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void remove(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
