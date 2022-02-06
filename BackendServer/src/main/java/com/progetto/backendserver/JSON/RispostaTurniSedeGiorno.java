package com.progetto.backendserver.JSON;

public class RispostaTurniSedeGiorno {
    private TurnoSingolo[] turni;

    public RispostaTurniSedeGiorno(){}

    public RispostaTurniSedeGiorno(int dimensione){
        this.turni = new TurnoSingolo[dimensione];
    }

    public TurnoSingolo[] getTurni() {
        return turni;
    }

    public void setTurni(TurnoSingolo[] turni) {
        this.turni = turni;
    }
}
