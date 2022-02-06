package com.progetto.backendserver.db.models;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Giorno {
    private Integer apertura;
    private Integer chiusura;

    public Giorno(){}

    public Giorno(Integer a, Integer c){
        this.apertura = a;
        this.chiusura = c;
    }

    public Integer getApertura() {
        return apertura;
    }

    public void setApertura(Integer apertura) {
        this.apertura = apertura;
    }

    public Integer getChiusura() {
        return chiusura;
    }

    public void setChiusura(Integer chiusura) {
        this.chiusura = chiusura;
    }

    @Override
    public String toString() {
        return "Giorno{" +
                "apertura=" + apertura +
                ", chiusura=" + chiusura +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Giorno giorno)) return false;
        return getApertura().equals(giorno.getApertura()) && getChiusura().equals(giorno.getChiusura());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getApertura(), getChiusura());
    }
}
