package com.progetto.backendserver.db.models.EmbeddedIDs;

import com.progetto.backendserver.db.models.Sede;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SediScambioID implements Serializable {
    @OneToOne
    @NotNull
    Sede sedeFrom;
    @NotNull
    @OneToOne
    Sede sedeTo;

    public Sede getSedeFrom() {
        return sedeFrom;
    }

    public void setSedeFrom(Sede sedeFrom) {
        this.sedeFrom = sedeFrom;
    }

    public Sede getSedeTo() {
        return sedeTo;
    }

    public void setSedeTo(Sede sedeTo) {
        this.sedeTo = sedeTo;
    }

    public SediScambioID(Sede fSede, Sede tSede){
        this.sedeFrom = fSede;
        this.sedeTo = tSede;
    }

    public SediScambioID() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SediScambioID that)) return false;
        return getSedeFrom().equals(that.getSedeFrom()) && getSedeTo().equals(that.getSedeTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSedeFrom(), getSedeTo());
    }
}
