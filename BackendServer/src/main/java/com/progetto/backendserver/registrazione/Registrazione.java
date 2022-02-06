package com.progetto.backendserver.registrazione;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Registrazione {
    private ManagerRegistrazione manager;
    private AziendaRegistrazione azienda;
    private RuoloRegistrazione[] ruoli;
    private DipendenteRegistrazione[] dipendenti;
    private OrarioRegistrazione orario;

    public Registrazione() {
    }

    public ManagerRegistrazione getManager() {
        return manager;
    }

    public void setManager(ManagerRegistrazione manager) {
        this.manager = manager;
    }

    public AziendaRegistrazione getAzienda() {
        return azienda;
    }

    public void setAzienda(AziendaRegistrazione azienda) {
        this.azienda = azienda;
    }

    public RuoloRegistrazione[] getRuoli() {
        return ruoli;
    }

    public void setRuoli(RuoloRegistrazione[] ruoli) {
        this.ruoli = ruoli;
    }

    public DipendenteRegistrazione[] getDipendenti() {
        return dipendenti;
    }

    public void setDipendenti(DipendenteRegistrazione[] dipendenti) {
        this.dipendenti = dipendenti;
    }

    public OrarioRegistrazione getOrario() {
        return orario;
    }

    public void setOrario(OrarioRegistrazione orario) {
        this.orario = orario;
    }


    @Override
    public String toString() {
        return "Registrazione{" +
                "manager=" + manager +
                ", azienda=" + azienda +
                ", ruoli=" + Arrays.toString(ruoli) +
                ", dipendenti=" + Arrays.toString(dipendenti) +
                ", orario=" + orario +
                '}';
    }
}
