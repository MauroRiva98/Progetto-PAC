package com.progetto.backendserver.db.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "utente")
public class Utente {
    @Id
    @Column(name = "PK_UTENTE")
    @Email(message = "L'email inserita dovrebbe essere un email valida!")
    @NotNull
    private String email;
    @NotNull(message = "Il nome dell'utente non dovrebbe essere nullo!")
    private String nome;
    @NotNull(message = "Il cognome dell'utente non dovrebbe essere nullo!")
    private String cognome;
    private String hashPassword;
    private String saltPassword;
    @ManyToOne
    @JoinColumn(name = "PK_AZIENDA")
    @NotNull(message = "L'idAzienda non dovrebbe essere nullo!")
    private Azienda azienda;
    @OneToOne
    @JoinColumn(name = "PK_SEDE")
    private Sede sede;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //Questo lo usiamo come parametro ritornato al client per mantenerne
    //l'accesso.
    private String token;

    public boolean isPartTime() {
        return partTime;
    }

    @NotNull(message = "L'utente deve lavorare full o part time!")
    private boolean partTime;

    public Utente(String email, String nome, String cognome, Azienda azienda, boolean partTime) {
        this.setEmail(email);
        this.setNome(nome);
        this.setCognome(cognome);
        this.setAzienda(azienda);
        this.setPartTime(partTime);
    }

    public Utente(String email, String nome, String cognome, Azienda azienda, boolean partTime, Sede sede, String hashPassword) {
        this.setEmail(email);
        this.setNome(nome);
        this.setCognome(cognome);
        this.setAzienda(azienda);
        this.setPartTime(partTime);
        this.setSede(sede);
        this.setHashPassword(hashPassword);
    }

    public Utente() {

    }

    public Utente(String email, String nome, String cognome, Azienda azienda, Collection<Ruoli> role, boolean partTime, String hashPassword) {
        this (email, nome, cognome, azienda, partTime);
        this.setHashPassword(hashPassword);
    }

    public Utente(String email, String nome, String cognome, Azienda azienda, String hashpass, String saltpass, boolean partTime) {
        this(email, nome, cognome, azienda, partTime);
        this.setHashPassword(hashpass);
        this.setSaltPassword(saltpass);
    }

    public Utente(String email, String nome, String cognome, Azienda azienda, String hashpass, String saltpass, Sede sede, boolean partTime) {
        this(email, nome, cognome, azienda, hashpass, saltpass, partTime);
        this.setSede(sede);
    }

    public boolean getPartTime() {
        return partTime;
    }

    public void setPartTime(boolean partTime) {
        this.partTime = partTime;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getSaltPassword() {
        return saltPassword;
    }

    public void setSaltPassword(String saltPassword) {
        this.saltPassword = saltPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Utente {" +
                " email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", azienda=" + azienda +
                " }";
    }


    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utente utente)) return false;
        return getEmail().equals(utente.getEmail()) && getNome().equals(utente.getNome()) && getCognome().equals(utente.getCognome()) && getAzienda().equals(utente.getAzienda()) && getSede().equals(utente.getSede());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getNome(), getCognome(), getAzienda(), getSede());
    }
}