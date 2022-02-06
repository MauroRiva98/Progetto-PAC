package com.progetto.backendserver.inserimento;

import com.progetto.backendserver.registrazione.DipendenteRegistrazione;

import java.util.Arrays;

public class NuovaSede {
    private SedeInserimento sede;
    private DipendenteRegistrazione[] dipendenti;

    public NuovaSede(){

    }

    public SedeInserimento getSede() {
        return sede;
    }

    public void setSede(SedeInserimento sede) {
        this.sede = sede;
    }

    public DipendenteRegistrazione[] getDipendenti() {
        return dipendenti;
    }

    public void setDipententi(DipendenteRegistrazione[] dipendenti) {
        this.dipendenti = dipendenti;
    }

    @Override
    public String toString() {
        return "NuovaSede{" +
                "sede=" + sede +
                ", dipendenti=" + Arrays.toString(dipendenti) +
                '}';
    }
}
