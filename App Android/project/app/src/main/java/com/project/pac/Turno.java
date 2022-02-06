package com.project.pac;

import java.time.LocalDate;

public class Turno {
    String data;
    int ora_inizio;
    int ora_fine;
    String indirizzo;
    String ruolo;
    Boolean isTrasferta;
    Boolean isStraordinario;


    public Turno(){}

    public Turno(String data, int ora_inizio, int ora_fine, String indirizzo, String ruolo, Boolean isTrasferta, Boolean isStraordinario) {
        this.data = data;
        this.ora_inizio = ora_inizio;
        this.ora_fine = ora_fine;
        this.indirizzo = indirizzo;
        this.isTrasferta = isTrasferta;
        this.isStraordinario = isStraordinario;
        this.ruolo = ruolo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getOra_inizio() {
        return ora_inizio;
    }

    public void setOra_inizio(int ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    public int getOra_fine() {
        return ora_fine;
    }

    public void setOra_fine(int ora_fine) {
        this.ora_fine = ora_fine;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Boolean getTrasferta() {
        return isTrasferta;
    }

    public void setTrasferta(Boolean trasferta) {
        isTrasferta = trasferta;
    }

    public Boolean getStraordinario() {
        return isStraordinario;
    }

    public void setStraordinario(Boolean straordinario) {
        isStraordinario = straordinario;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

}
