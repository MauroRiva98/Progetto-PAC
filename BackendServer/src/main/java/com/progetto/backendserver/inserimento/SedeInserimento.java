package com.progetto.backendserver.inserimento;

public class SedeInserimento {
    private String indirizzo;
    private String sedeVicina;
    private Float distanza;

    public SedeInserimento(){

    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getSedeVicina() {
        return sedeVicina;
    }

    public void setSedeVicina(String sedeVicina) {
        this.sedeVicina = sedeVicina;
    }

    public Float getDistanza() {
        return distanza;
    }

    public void setDistanza(Float distanza) {
        this.distanza = distanza;
    }

    @Override
    public String toString() {
        return "SedeInserimento{" +
                "indirizzo='" + indirizzo + '\'' +
                ", sedeVicina='" + sedeVicina + '\'' +
                ", distanza=" + distanza +
                '}';
    }
}
