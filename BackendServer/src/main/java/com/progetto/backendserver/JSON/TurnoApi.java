package com.progetto.backendserver.JSON;

import java.time.LocalDate;

public class TurnoApi {
    LocalDate data;
    int ora_inizio;
    int ora_fine;
    String indirizzo;
    Boolean isTrasferta;
    Boolean isStraordinario;
    String ruolo;

    public TurnoApi(){

    }

    public TurnoApi(LocalDate data, int ora_inizio, int ora_fine, String indirizzo, Boolean isTrasferta, Boolean isStraordinario, String ruolo) {
        this.data = data;
        this.ora_inizio = ora_inizio;
        this.ora_fine = ora_fine;
        this.indirizzo = indirizzo;
        this.isTrasferta = isTrasferta;
        this.isStraordinario = isStraordinario;
        this.ruolo = ruolo;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra_inizio(int ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    public void setOra_fine(int ora_fine) {
        this.ora_fine = ora_fine;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setTrasferta(Boolean trasferta) {
        isTrasferta = trasferta;
    }

    public void setStraordinario(Boolean straordinario) {
        isStraordinario = straordinario;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public LocalDate getData() {
        return data;
    }

    public int getOra_inizio() {
        return ora_inizio;
    }

    public int getOra_fine() {
        return ora_fine;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Boolean getTrasferta() {
        return isTrasferta;
    }

    public Boolean getStraordinario() {
        return isStraordinario;
    }

    public String getRuolo() {
        return ruolo;
    }

    @Override
    public String toString() {
        return "TurnoApi{" +
                "data=" + data +
                ", ora_inizio=" + ora_inizio +
                ", ora_fine=" + ora_fine +
                ", indirizzo='" + indirizzo + '\'' +
                ", isTrasferta=" + isTrasferta +
                ", isStraordinario=" + isStraordinario +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }
}
