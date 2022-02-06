package com.progetto.backendserver.db.models;

import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "ruoli_azienda")
public class Ruoli {

    @EmbeddedId
    private RuoliID ruoloID;

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Ruoli(RuoliID ruoloID, Boolean isAdmin, Integer minRuolo) {
        this.ruoloID = ruoloID;
        this.isAdmin = isAdmin;
        this.minRuolo = minRuolo;
    }

    public Ruoli() {
    }


    @NotNull
    Boolean isAdmin;

    @NotNull
    Integer minRuolo;

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean accessType) {
        this.isAdmin = accessType;
    }

    public Integer getMinRuolo() {
        return minRuolo;
    }

    public void setMinRuolo(Integer minRuolo) {
        this.minRuolo = minRuolo;
    }

    public RuoliID getRuoloID() {
        return ruoloID;
    }

    public void setRuoloID(RuoliID ruoloID) {
        this.ruoloID = ruoloID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruoli ruoli = (Ruoli) o;
        return Objects.equals(getRuoloID(), ruoli.getRuoloID()) && Objects.equals(getIsAdmin(), ruoli.getIsAdmin()) && Objects.equals(getMinRuolo(), ruoli.getMinRuolo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRuoloID(), getIsAdmin(), getMinRuolo());
    }
}
