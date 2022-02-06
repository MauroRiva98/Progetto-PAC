package com.progetto.backendserver.registrazione;

public class AziendaRegistrazione {
    private String partitaIVA;
    private String ragioneSociale;
    private String suffissoEmail;
    private String sedeHQ;

    public AziendaRegistrazione() {
    }

    public String getPartitaIVA() {
        return partitaIVA;
    }

    public void setPartitaIVA(String partitaIVA) {
        this.partitaIVA = partitaIVA;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getSuffissoEmail() {
        return suffissoEmail;
    }

    public void setSuffissoEmail(String suffissoEmail) {
        this.suffissoEmail = suffissoEmail;
    }

    public String getSedeHQ() {
        return sedeHQ;
    }

    public void setSedeHQ(String sedeHQ) {
        this.sedeHQ = sedeHQ;
    }

    @Override
    public String toString() {
        return "AziendaRegistrazione{" +
                "partitaIVA='" + partitaIVA + '\'' +
                ", ragioneSociale='" + ragioneSociale + '\'' +
                ", suffissoEmail='" + suffissoEmail + '\'' +
                ", sedeHQ='" + sedeHQ + '\'' +
                '}';
    }
}
