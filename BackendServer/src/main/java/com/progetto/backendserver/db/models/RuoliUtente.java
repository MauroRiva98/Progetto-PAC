package com.progetto.backendserver.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "ruoli_utente")
public class RuoliUtente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "PK_RUOLI_UTENTE")
    private Integer idRuoliUtente;

    @NotNull
    @JoinColumn(name = "role_name")
    private String nomeRuolo;

    @NotNull
    @JoinColumn(name = "azienda")
    private String azienda;

    @NotNull
    @JoinColumn(name = "pk_utente")
    private String email;

    public RuoliUtente() {
    }

    public RuoliUtente(String nomeRuolo, String AziendaPI, String email) {
        this.setNomeRuolo(nomeRuolo);
        this.setAzienda(AziendaPI);
        this.setEmail(email);
    }

    public RuoliUtente(Ruoli r, Utente u) {
        this.setNomeRuolo(r.getRuoloID().getRoleName());
        this.setAzienda(r.getRuoloID().getAzienda().getPartitaIva());
        this.setEmail(u.getEmail());
    }

    public RuoliUtente(Ruoli r, String e) {
        this.setNomeRuolo(r.getRuoloID().getRoleName());
        this.setAzienda(r.getRuoloID().getAzienda().getPartitaIva());
        this.setEmail(e);
    }

    public Integer getIdRuoliUtente() {
        return idRuoliUtente;
    }

    public void setIdRuoliUtente(Integer idRuoliUtente) {
        this.idRuoliUtente = idRuoliUtente;
    }

    public String getNomeRuolo() {
        return nomeRuolo;
    }

    public void setNomeRuolo(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }

    public String getAzienda() {
        return azienda;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuoliUtente that = (RuoliUtente) o;
        return getNomeRuolo().equals(that.getNomeRuolo()) && getAzienda().equals(that.getAzienda()) && getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNomeRuolo(), getAzienda(), getEmail());
    }
}
