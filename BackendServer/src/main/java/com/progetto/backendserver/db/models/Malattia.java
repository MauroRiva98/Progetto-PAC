package com.progetto.backendserver.db.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "malattia")
public class Malattia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMalattia;

    @OneToOne
    @JoinColumn(name = "PK_UTENTE")
    private Utente utente;

    private LocalDate dataInizio;

    private LocalDate dataFine;

    public Malattia(){}

    public Malattia(Utente u, LocalDate i, LocalDate f){
        this.utente = u;
        this.dataInizio = i;
        this.dataFine = f;
    }

    public Integer getIdMalattia() {
        return idMalattia;
    }

    public void setIdMalattia(Integer idMalattia) {
        this.idMalattia = idMalattia;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    @Override
    public String toString() {
        return "Malattia{" +
                "idMalattia=" + idMalattia +
                ", utente=" + utente +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                '}';
    }
}
