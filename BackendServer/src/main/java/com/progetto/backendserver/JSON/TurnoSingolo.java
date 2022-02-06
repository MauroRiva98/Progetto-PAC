package com.progetto.backendserver.JSON;

public class TurnoSingolo {
    private String nomeRuolo;
    private int oraInizio;
    private int oraFine;
    private int oreScoperte;
    private TurnoUtenteSingolo[] utenti;

    public TurnoSingolo(){}

    public TurnoSingolo(String nr, int oi, int of, int os, TurnoUtenteSingolo[] ts){
        this.nomeRuolo = nr;
        this.oraInizio = oi;
        this.oraFine = of;
        this.oreScoperte = os;
        this.utenti = ts;
    }

    public String getNomeRuolo() {
        return nomeRuolo;
    }

    public void setNomeRuolo(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }

    public int getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(int oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getOraFine() {
        return oraFine;
    }

    public void setOraFine(int oraFine) {
        this.oraFine = oraFine;
    }

    public int getOreScoperte() {
        return oreScoperte;
    }

    public void setOreScoperte(int oreScoperte) {
        this.oreScoperte = oreScoperte;
    }

    public TurnoUtenteSingolo[] getUtenti() {
        return utenti;
    }

    public void setUtenti(TurnoUtenteSingolo[] utenti) {
        this.utenti = utenti;
    }
}
