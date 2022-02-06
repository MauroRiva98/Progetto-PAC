package com.progetto.backendserver.controllers;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.repository.RuoliUtenteRepository;
import com.progetto.backendserver.db.repository.UtenteRepository;
import com.progetto.backendserver.registrazione.AziendaRegistrazione;
import com.progetto.backendserver.registrazione.Login;
import com.progetto.backendserver.registrazione.Registrazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UtenteRepository utenteRep;

    @Autowired
    private RuoliUtenteRepository ruoliUtenteRep;

    @PostMapping("/login")
    public int login(@RequestBody Login credenziali, HttpServletRequest request) throws ParseException, IOException { //Ritorna -1 se le credenziali sono errate, 1 se sono giuste (dipendente), 2 se sono giuste (manager)
        try {
            Utente utente = utenteRep.findUtenteByEmail(credenziali.getEmail());
            if(utente == null)
                return -1;
            String passwordHash = Utente.sha256(credenziali.getPassword());
            if(!passwordHash.equals(utente.getHashPassword()))
                return -1;
            HttpSession session = request.getSession(true);
            session.setAttribute("email", utente.getEmail());
            session.setAttribute("nome", utente.getNome());
            session.setAttribute("cognome", utente.getCognome());
            session.setAttribute("partTime", utente.isPartTime());
            session.setAttribute("azienda", utente.getAzienda());
            session.setAttribute("sede", utente.getSede());
            RuoliUtente ru = ruoliUtenteRep.findRuoloUtenteByEmailAndNomeRuolo(utente.getEmail(), "Manager");
            if(ru == null)
                return 1;
            return 2;
        } catch (Exception ex) {
            System.out.println(ex);
            return 0;
        }
    }
}
