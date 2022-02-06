package com.progetto.backendserver.JSON;

public class ElencoTurni {
    private TurnoApi[] turni;

    public ElencoTurni(int size) {

        this.turni = new TurnoApi[size];

    }

    public void setTurni(int index, TurnoApi ta) {
        this.turni[index] = ta;
    }

    public TurnoApi[] getTurni() {
        return turni;
    }
}
