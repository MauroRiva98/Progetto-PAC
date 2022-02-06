package com.progetto.backendserver.db.models.EmbeddedIDs;

import com.progetto.backendserver.db.models.Azienda;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RuoliID implements Serializable {
    @ManyToOne
    @JoinColumn(name = "azienda")
    private Azienda azienda;

    public RuoliID(Azienda aziendaId, String roleName) {
        this.azienda = aziendaId;
        this.roleName = roleName;
    }

    @Column(name = "role_name")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public RuoliID() {

    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda aziendaId) {
        this.azienda = aziendaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuoliID ruoliID = (RuoliID) o;
        return getAzienda().equals(ruoliID.getAzienda()) && getRoleName().equals(ruoliID.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAzienda(), getRoleName());
    }

    @Override
    public String toString() {
        return "RuoliID{" +
                "azienda=" + azienda +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
