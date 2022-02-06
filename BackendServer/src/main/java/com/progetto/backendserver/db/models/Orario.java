package com.progetto.backendserver.db.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orario")
public class Orario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_ORARIO")
    private Integer idOrario;

    @OneToOne
    @JoinColumn(name = "PK_AZIENDA")
    private Azienda azienda;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_lunedi")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_lunedi"))
    })
    private Giorno lunedi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_martedi")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_martedi"))
    })
    private Giorno martedi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_mercoledi")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_mercoledi"))
    })
    private Giorno mercoledi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_giovedi")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_giovedi"))
    })
    private Giorno giovedi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_venerdi")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_venerdi"))
    })
    private Giorno venerdi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_sabato")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_sabato"))
    })
    private Giorno sabato;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "apertura", column = @Column(name = "apertura_domenica")),
            @AttributeOverride( name = "chiusura", column = @Column(name = "chiusura_domenica"))
    })
    private Giorno domenica;

    public Orario(){}

    public Orario(Azienda a){
        this.azienda = a;
    }

    public Orario(Azienda azienda, Giorno lunedi, Giorno martedi, Giorno mercoledi, Giorno giovedi, Giorno venerdi, Giorno sabato, Giorno domenica) {
        this.azienda = azienda;
        this.lunedi = lunedi;
        this.martedi = martedi;
        this.mercoledi = mercoledi;
        this.giovedi = giovedi;
        this.venerdi = venerdi;
        this.sabato = sabato;
        this.domenica = domenica;
    }

    public Integer getIdOrario() {
        return idOrario;
    }

    public void setIdOrario(Integer idOrario) {
        this.idOrario = idOrario;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public Giorno getLunedi() {
        return lunedi;
    }

    public void setLunedi(Giorno lunedi) {
        this.lunedi = lunedi;
    }

    public Giorno getMartedi() {
        return martedi;
    }

    public void setMartedi(Giorno martedi) {
        this.martedi = martedi;
    }

    public Giorno getMercoledi() {
        return mercoledi;
    }

    public void setMercoledi(Giorno mercoledi) {
        this.mercoledi = mercoledi;
    }

    public Giorno getGiovedi() {
        return giovedi;
    }

    public void setGiovedi(Giorno giovedi) {
        this.giovedi = giovedi;
    }

    public Giorno getVenerdi() {
        return venerdi;
    }

    public void setVenerdi(Giorno venerdi) {
        this.venerdi = venerdi;
    }

    public Giorno getSabato() {
        return sabato;
    }

    public void setSabato(Giorno sabato) {
        this.sabato = sabato;
    }

    public Giorno getDomenica() {
        return domenica;
    }

    public void setDomenica(Giorno domenica) {
        this.domenica = domenica;
    }

    @Override
    public String toString() {
        return "Orario{" +
                "idOrario=" + idOrario +
                ", azienda=" + azienda +
                ", lunedi=" + lunedi +
                ", martedi=" + martedi +
                ", mercoledi=" + mercoledi +
                ", giovedi=" + giovedi +
                ", venerdi=" + venerdi +
                ", sabato=" + sabato +
                ", domenica=" + domenica +
                '}';
    }

    public Giorno getCorrespondingWorkingHours(LocalDate day){
        return switch (day.getDayOfWeek()){
            case MONDAY -> getLunedi();
            case TUESDAY -> getMartedi();
            case WEDNESDAY -> getMercoledi();
            case THURSDAY -> getGiovedi();
            case FRIDAY -> getVenerdi();
            case SATURDAY -> getSabato();
            case SUNDAY -> getDomenica();
        };
    }
    public Boolean isOpenForCurrentDay(LocalDate day){
        Giorno cur = getCorrespondingWorkingHours(day);
        return (cur.getApertura() > 0 && cur.getChiusura() > 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orario orario)) return false;
        return getIdOrario().equals(orario.getIdOrario()) && getAzienda().equals(orario.getAzienda()) && getLunedi().equals(orario.getLunedi()) && getMartedi().equals(orario.getMartedi()) && getMercoledi().equals(orario.getMercoledi()) && getGiovedi().equals(orario.getGiovedi()) && getVenerdi().equals(orario.getVenerdi()) && getSabato().equals(orario.getSabato()) && getDomenica().equals(orario.getDomenica());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdOrario(), getAzienda(), getLunedi(), getMartedi(), getMercoledi(), getGiovedi(), getVenerdi(), getSabato(), getDomenica());
    }
}
