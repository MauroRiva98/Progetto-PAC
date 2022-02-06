package com.progetto.backendserver.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "sede")
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_SEDE")
    private Integer idSede;

    @ManyToOne
    @JoinColumn(name = "PK_AZIENDA")
    private Azienda azienda;

    public Sede(Azienda azienda, String indirizzo) {
        this.azienda = azienda;
        this.indirizzo = indirizzo;
    }

    public Sede() {

    }

    public Azienda getAzienda() {
        return azienda;
    }

    @NotNull(message = "Ogni sede deve avere un indirizzo valido!")
    private String indirizzo;


    public String getIndirizzo(){
        return this.indirizzo;
    }

    @Override
    public String toString() {
        return "Sede{" +
                "idSede=" + idSede +
                ", azienda=" + azienda +
                ", indirizzo='" + indirizzo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sede sede)) return false;
        return idSede.equals(sede.idSede) && getAzienda().equals(sede.getAzienda()) && getIndirizzo().equals(sede.getIndirizzo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSede, getAzienda(), getIndirizzo());
    }
}