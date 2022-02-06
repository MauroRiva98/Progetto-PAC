package com.progetto.backendserver.db.models;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "azienda")
public class Azienda implements Serializable {
    @Id
    @Column(name = "PK_AZIENDA")
    @NotNull
    private String partitaIva;
    @UniqueElements
    @NotNull
    private String ragioneSociale;
    @UniqueElements
    @NotNull
    private String domainAzienda;

    public String getDomainAzienda() {
        return domainAzienda;
    }

    public RegistrationStates getRegState() {
        return regState;
    }

    public void setRegState(RegistrationStates regState) {
        this.regState = regState;
    }

    @Enumerated(EnumType.STRING)
    private RegistrationStates regState;

    public Azienda(String partitaIva, String ragioneSociale, String domainAzienda) {
        this.partitaIva = partitaIva;
        this.ragioneSociale = ragioneSociale;
        this.domainAzienda = domainAzienda;
    }

    public Azienda() {
    }

    @Override
    public String toString() {
        return "Azienda {" +
                " ragioneSociale='" + ragioneSociale + '\'' +
                ", partitaIva='" + partitaIva + '\'' +
                ", domainAzienda='" + domainAzienda + '\'' +
                " }";
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public void setDomainAzienda(String domainAzienda) {
        this.domainAzienda = domainAzienda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Azienda)) return false;
        Azienda azienda = (Azienda) o;
        return getPartitaIva().equals(azienda.getPartitaIva()) && getRagioneSociale().equals(azienda.getRagioneSociale()) && getDomainAzienda().equals(azienda.getDomainAzienda()) && getRegState() == azienda.getRegState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPartitaIva(), getRagioneSociale(), getDomainAzienda(), getRegState());
    }
}
