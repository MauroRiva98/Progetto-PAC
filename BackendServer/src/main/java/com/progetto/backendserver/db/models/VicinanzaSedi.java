package com.progetto.backendserver.db.models;

import com.progetto.backendserver.db.models.EmbeddedIDs.SediScambioID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "vicinanza_sedi")
public class VicinanzaSedi {
    @EmbeddedId
    @NotNull
    SediScambioID sediDiScambio;

    @Column(name="distanza")
    @NotNull
    Float distanza;

    public VicinanzaSedi(SediScambioID sediDiScambio, Float distanza) {
        this.sediDiScambio = sediDiScambio;
        this.distanza = distanza;
    }

    public VicinanzaSedi() {

    }

    public SediScambioID getSediDiScambio() {
        return sediDiScambio;
    }

    public void setSediDiScambio(SediScambioID sediDiScambio) {
        this.sediDiScambio = sediDiScambio;
    }

    public Float getDistanza() {
        return distanza;
    }

    public void setDistanza(Float distanza) {
        this.distanza = distanza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VicinanzaSedi that)) return false;
        return getSediDiScambio().equals(that.getSediDiScambio()) && getDistanza().equals(that.getDistanza());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSediDiScambio(), getDistanza());
    }
}
