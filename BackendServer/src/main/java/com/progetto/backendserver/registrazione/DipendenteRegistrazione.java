package com.progetto.backendserver.registrazione;

import java.util.Arrays;

public class DipendenteRegistrazione {
    private String nomeDipendente;
    private String cognomeDipendente;
    private String[] ruoliDipendente;
    private boolean partTime;

    public DipendenteRegistrazione() {
    }

    public String getNomeDipendente() {
        return nomeDipendente;
    }

    public void setNomeDipendente(String nomeDipendente) {
        this.nomeDipendente = nomeDipendente;
    }

    public String getCognomeDipendente() {
        return cognomeDipendente;
    }

    public void setCognomeDipendente(String cognomeDipendente) {
        this.cognomeDipendente = cognomeDipendente;
    }

    public String[] getRuoliDipendente() {
        return ruoliDipendente;
    }

    public String getRuoloDipendente(int index) {
        return ruoliDipendente[index];
    }

    public void setRuoliDipendente(String[] ruoliDipendente) {
        this.ruoliDipendente = ruoliDipendente;
    }

    public boolean isPartTime() {
        return partTime;
    }

    public void setPartTime(boolean partTime) {
        this.partTime = partTime;
    }

    @Override
    public String toString() {
        return "DipendenteRegistrazione{" +
                "nomeDipendente='" + nomeDipendente + '\'' +
                ", cognomeDipendente='" + cognomeDipendente + '\'' +
                ", ruoliDipendente=" + Arrays.toString(ruoliDipendente) +
                ", partTime=" + partTime +
                '}';
    }
}
