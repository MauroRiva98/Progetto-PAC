package com.progetto.backendserver.registrazione;

public class GiornataRegistrazione {
    private int apertura;
    private int chiusura;

    public GiornataRegistrazione() {
    }

    public int getApertura() {
        return apertura;
    }

    public void setApertura(int apertura) {
        this.apertura = apertura;
    }

    public int getChiusura() {
        return chiusura;
    }

    public void setChiusura(int chiusura) {
        this.chiusura = chiusura;
    }

    @Override
    public String toString() {
        return "GiornataRegistrazione{" +
                "apertura=" + apertura +
                ", chiusura=" + chiusura +
                '}';
    }
}
