package com.progetto.backendserver.db.models;

import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "turno")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_TURNO")
    @NotNull
    private Integer idTurno;

    @ManyToOne
    @JoinColumn(name = "PK_SEDE")
    @NotNull
    private Sede sede;

    @Embedded
    @NotNull
    private RuoliID ruoloID;

    @NotNull
    private LocalDate data;

    @NotNull
    private Integer oraInizio;

    @NotNull
    private Integer oraFine;

    public Integer getOreScoperte() {
        return oreScoperte;
    }

    public void setOreScoperte(Integer oreScoperte) {
        this.oreScoperte = oreScoperte;
    }

    private Integer oreScoperte;

    public Turno(){}

    public Turno(Sede s, RuoliID r, LocalDate d, int oI, int oF){
        this.sede = s;
        this.ruoloID = r;
        this.data = d;
        this.oraInizio = oI;
        this.oraFine = oF;
    }

    public Turno(Sede s, RuoliID r, LocalDate d, int ora_in, int ora_fin, int or_scop){
        this(s,r,d,ora_in, ora_fin);
        this.setOreScoperte(or_scop);
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public RuoliID getRuoloID() {
        return ruoloID;
    }

    public void setRuoloID(RuoliID ruoloID) {
        this.ruoloID = ruoloID;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "Turno{" +
                "idTurno=" + idTurno +
                ", sede=" + sede +
                ", ruoloID=" + ruoloID +
                ", data=" + data +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno turno)) return false;
        return getIdTurno().equals(turno.getIdTurno()) && getSede().equals(turno.getSede()) && getRuoloID().equals(turno.getRuoloID()) && getData().equals(turno.getData()) && getOraInizio().equals(turno.getOraInizio()) && getOraFine().equals(turno.getOraFine()) && getOreScoperte().equals(turno.getOreScoperte());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdTurno(), getSede(), getRuoloID(), getData(), getOraInizio(), getOraFine(), getOreScoperte());
    }
}
