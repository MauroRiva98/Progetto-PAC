package com.progetto.backendserver.JSON;

public class RichiestaStatusGiorno {

    private int anno;
    private int mese;
    private String indirizzoSede;

    public RichiestaStatusGiorno(){}


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

    public String getIndirizzoSede() {
        return indirizzoSede;
    }

    public void setIndirizzoSede(String indirizzoSede) {
        this.indirizzoSede = indirizzoSede;
    }

    @Override
    public String toString() {
        return "RichiestaStatusGiorno{" +
                "anno=" + anno +
                ", mese=" + mese +
                ", indirizzoSede='" + indirizzoSede + '\'' +
                '}';
    }
}
