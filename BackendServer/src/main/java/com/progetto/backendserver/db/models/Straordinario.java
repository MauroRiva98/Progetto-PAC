package com.progetto.backendserver.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "straordinario")
public class Straordinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer idStraordinario;

    @ManyToOne
    @JoinColumn(name = "PK_UTENTE")
    @NotNull
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "PK_TURNO")
    @NotNull
    private Turno turno;

    @NotNull
    private LocalDate data;

    @NotNull
    private Integer numeroOre;

    public Straordinario(){}

    public Straordinario(Utente u, Turno t, LocalDate d, int no){
        this.utente = u;
        this.turno = t;
        this.data = d;
        this.numeroOre = no;
    }

    public Utente getUtente() {
        return utente;
    }

    public Integer getIdStraordinario() {
        return idStraordinario;
    }

    public void setIdStraordinario(Integer idStraordinario) {
        this.idStraordinario = idStraordinario;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getNumeroOre() {
        return numeroOre;
    }

    public void setNumeroOre(Integer numeroOre) {
        this.numeroOre = numeroOre;
    }

    @Override
    public String toString() {
        return "Straordinario{" +
                "idStraordinario=" + idStraordinario +
                ", utente=" + utente +
                ", turno=" + turno +
                ", data=" + data +
                ", numeroOre=" + numeroOre +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Straordinario that)) return false;
        return getIdStraordinario().equals(that.getIdStraordinario()) && getUtente().equals(that.getUtente()) && getTurno().equals(that.getTurno()) && getData().equals(that.getData()) && getNumeroOre().equals(that.getNumeroOre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdStraordinario(), getUtente(), getTurno(), getData(), getNumeroOre());
    }
}
