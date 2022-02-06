package com.progetto.backendserver.registrazione;

public class RuoloRegistrazione {
    private String nomeRuolo;
    private int numeroDipendenti;

    public RuoloRegistrazione() {
    }

    public String getNomeRuolo() {
        return nomeRuolo;
    }

    public void setNomeRuolo(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }

    public int getNumeroDipendenti() {
        return numeroDipendenti;
    }

    public void setNumeroDipendenti(int numeroDipendenti) {
        this.numeroDipendenti = numeroDipendenti;
    }

    @Override
    public String toString() {
        return "RuoloRegistrazione{" +
                "nomeRuolo='" + nomeRuolo + '\'' +
                ", numeroDipendenti=" + numeroDipendenti +
                '}';
    }
}
