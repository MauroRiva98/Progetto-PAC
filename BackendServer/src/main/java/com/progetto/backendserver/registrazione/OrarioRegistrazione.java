package com.progetto.backendserver.registrazione;

public class OrarioRegistrazione {
    private GiornataRegistrazione lunedi;
    private GiornataRegistrazione martedi;
    private GiornataRegistrazione mercoledi;
    private GiornataRegistrazione giovedi;
    private GiornataRegistrazione venerdi;
    private GiornataRegistrazione sabato;
    private GiornataRegistrazione domenica;

    public OrarioRegistrazione() {
    }

    public GiornataRegistrazione getLunedi() {
        return lunedi;
    }

    public void setLunedi(GiornataRegistrazione lunedi) {
        this.lunedi = lunedi;
    }

    public GiornataRegistrazione getMartedi() {
        return martedi;
    }

    public void setMartedi(GiornataRegistrazione martedi) {
        this.martedi = martedi;
    }

    public GiornataRegistrazione getMercoledi() {
        return mercoledi;
    }

    public void setMercoledi(GiornataRegistrazione mercoledi) {
        this.mercoledi = mercoledi;
    }

    public GiornataRegistrazione getGiovedi() {
        return giovedi;
    }

    public void setGiovedi(GiornataRegistrazione giovedi) {
        this.giovedi = giovedi;
    }

    public GiornataRegistrazione getVenerdi() {
        return venerdi;
    }

    public void setVenerdi(GiornataRegistrazione venerdi) {
        this.venerdi = venerdi;
    }

    public GiornataRegistrazione getSabato() {
        return sabato;
    }

    public void setSabato(GiornataRegistrazione sabato) {
        this.sabato = sabato;
    }

    public GiornataRegistrazione getDomenica() {
        return domenica;
    }

    public void setDomenica(GiornataRegistrazione domenica) {
        this.domenica = domenica;
    }

    public GiornataRegistrazione getByIndex(int index){
        switch(index) {
            case 0:
                return getLunedi();
            case 1:
                return getMartedi();
            case 2:
                return getMercoledi();
            case 3:
                return getGiovedi();
            case 4:
                return getVenerdi();
            case 5:
                return getSabato();
            case 6:
                return getDomenica();
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "OrarioRegistrazione{" +
                "lunedi=" + lunedi +
                ", martedi=" + martedi +
                ", mercoledi=" + mercoledi +
                ", giovedi=" + giovedi +
                ", venerdi=" + venerdi +
                ", sabato=" + sabato +
                ", domenica=" + domenica +
                '}';
    }
}
