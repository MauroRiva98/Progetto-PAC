package com.progetto.backendserver.JSON;

public class RichiestaTurniSedeGiorno {
    private int anno;
    private int mese;
    private int giorno;
    private String indirizzoSede;

    public RichiestaTurniSedeGiorno(){}

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getMese() {
        return mese;
    }

    public void setMese(int mese) {
        this.mese = mese;
    }

    public int getGiorno() {
        return giorno;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    public String getIndirizzoSede() {
        return indirizzoSede;
    }

    public void setIndirizzoSede(String indirizzoSede) {
        this.indirizzoSede = indirizzoSede;
    }
}
