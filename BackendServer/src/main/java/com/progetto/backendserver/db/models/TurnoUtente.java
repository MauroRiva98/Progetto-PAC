package com.progetto.backendserver.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "turno_utente")
public class TurnoUtente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_TURNO_UTENTE")
    @NotNull
    private Integer idTurnoUtente;

    @OneToOne
    @JoinColumn(name = "PK_UTENTE")
    @NotNull
    private Utente utente;

    @OneToOne
    @JoinColumn(name = "PK_TURNO")
    @NotNull
    private Turno turno;

    @OneToOne
    @JoinColumn(name = "PK_STRAORDINARIO")
    @NotNull
    private Straordinario straordinario;

    private Boolean fuoriOrario;

    private Integer oraInizio;

    private Integer oraFine;

    private Boolean trasferta;

    public TurnoUtente(){}

    public TurnoUtente(Utente u, Turno t, int oI, int oF){
        this.utente = u;
        this.turno = t;
        this.oraInizio = oI;
        this.oraFine = oF;
        this.fuoriOrario = false;
        this.trasferta = false;
    }

    public TurnoUtente(Utente u, Turno t, Straordinario s, int oI, int oF){
        this.utente = u;
        this.turno = t;
        this.oraInizio = oI;
        this.oraFine = oF;
        this.straordinario = s;
        this.fuoriOrario = true;
        this.trasferta = false;
    }

    public TurnoUtente(Utente u, Turno t, int oI, int oF, boolean trasf){
        this.utente = u;
        this.turno = t;
        this.oraInizio = oI;
        this.oraFine = oF;
        this.fuoriOrario = false;
        this.trasferta = trasf;
    }

    public Integer getIdTurnoUtente() {
        return idTurnoUtente;
    }

    public void setIdTurnoUtente(Integer idTurnoUtente) {
        this.idTurnoUtente = idTurnoUtente;
    }

    public Utente getUtente() {
        return utente;
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

    public Straordinario getStraordinario() {
        return straordinario;
    }

    public void setStraordinario(Straordinario straordinario) {
        this.straordinario = straordinario;
    }

    public Boolean isFuoriOrario() {
        return fuoriOrario;
    }

    public void setFuoriOrario(Boolean fuoriOrario) {
        this.fuoriOrario = fuoriOrario;
    }

    public Integer getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Integer oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Integer getOraFine() {
        return oraFine;
    }

    public void setOraFine(Integer oraFine) {
        this.oraFine = oraFine;
    }

    public Boolean getFuoriOrario() {
        return fuoriOrario;
    }

    public Boolean getTrasferta() {
        return trasferta;
    }

    public void setTrasferta(Boolean trasferta) {
        this.trasferta = trasferta;
    }

    @Override
    public String toString() {
        return "TurnoUtente{" +
                "idTurnoUtente=" + idTurnoUtente +
                ", utente=" + utente +
                ", turno=" + turno +
                ", straordinario=" + straordinario +
                ", fuoriOrario=" + fuoriOrario +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                ", trasferta=" + trasferta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TurnoUtente that)) return false;
        return getIdTurnoUtente().equals(that.getIdTurnoUtente()) && getUtente().equals(that.getUtente()) && getTurno().equals(that.getTurno()) && getStraordinario().equals(that.getStraordinario()) && Objects.equals(getFuoriOrario(), that.getFuoriOrario()) && Objects.equals(getOraInizio(), that.getOraInizio()) && Objects.equals(getOraFine(), that.getOraFine()) && Objects.equals(getTrasferta(), that.getTrasferta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdTurnoUtente(), getUtente(), getTurno(), getStraordinario(), getFuoriOrario(), getOraInizio(), getOraFine(), getTrasferta());
    }
}
