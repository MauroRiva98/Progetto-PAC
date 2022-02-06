package com.progetto.backendserver.JSON;

public class TurnoUtenteSingolo {
    private String nomeUtente;
    private String cognomeUtente;
    private boolean straordinario;
    private boolean trasferta;
    private int inizio;
    private int fine;

    public TurnoUtenteSingolo(){}

    public TurnoUtenteSingolo(String n, String c, boolean s, boolean t, int i, int f){
        this.nomeUtente = n;
        this.cognomeUtente = c;
        this.straordinario = s;
        this.trasferta = t;
        this.inizio = i;
        this.fine = f;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getCognomeUtente() {
        return cognomeUtente;
    }

    public void setCognomeUtente(String cognomeUtente) {
        this.cognomeUtente = cognomeUtente;
    }

    public boolean isStraordinario() {
        return straordinario;
    }

    public void setStraordinario(boolean straordinario) {
        this.straordinario = straordinario;
    }

    public boolean isTrasferta() {
        return trasferta;
    }

    public void setTrasferta(boolean trasferta) {
        this.trasferta = trasferta;
    }

    public int getInizio() {
        return inizio;
    }

    public void setInizio(int inizio) {
        this.inizio = inizio;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}
