package com.progetto.backendserver.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "ferie")
public class Ferie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFerie;

    @OneToOne
    @JoinColumn(name = "PK_UTENTE")
    @NotNull
    private Utente utente;

    @NotNull
    private LocalDate dataInizio;

    @NotNull
    private LocalDate dataFine;

    private Boolean accettate;

    private boolean daValutare;

    public Ferie(){}

    public Ferie(Utente u, LocalDate i, LocalDate f){
        this.utente = u;
        this.dataInizio = i;
        this.dataFine = f;
        this.accettate = null;
        this.daValutare = true;
    }

    public Integer getIdFerie() {
        return idFerie;
    }

    public void setIdFerie(Integer idFerie) {
        this.idFerie = idFerie;
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

    public Boolean getAccettate() {
        return accettate;
    }

    public void setAccettate(Boolean accettate) {
        this.accettate = accettate;
    }

    public boolean isDaValutare() {
        return daValutare;
    }

    public void setDaValutare(boolean daValutare) {
        this.daValutare = daValutare;
    }

    @Override
    public String toString() {
        return "Ferie{" +
                "idFerie=" + idFerie +
                ", utente=" + utente +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", accettate=" + accettate +
                ", daValutare=" + daValutare +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ferie ferie)) return false;
        return getUtente().equals(ferie.getUtente()) && getDataInizio().equals(ferie.getDataInizio()) && getDataFine().equals(ferie.getDataFine());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUtente(), getDataInizio(), getDataFine());
    }
}
