package com.progetto.backendserver.controllers;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import com.progetto.backendserver.db.repository.*;
import com.progetto.backendserver.registrazione.AziendaRegistrazione;
import com.progetto.backendserver.registrazione.GiornataRegistrazione;
import com.progetto.backendserver.registrazione.OrarioRegistrazione;
import com.progetto.backendserver.registrazione.Registrazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class RegistrationController {

    @Autowired
    private AziendaRepository aziendaRep;

    @Autowired
    private SedeRepository sedeRep;

    @Autowired
    private RuoliRepository ruoliRep;

    @Autowired
    private UtenteRepository utenteRep;

    @Autowired
    private RuoliUtenteRepository ruoliUtenteRep;

    @Autowired
    private OrarioRepository orarioRep;

    @Transactional
    @PostMapping("/registrazione")
    public int registrazione(@RequestBody Registrazione registrationRecord) throws ParseException, IOException {
        try {
            //System.out.println(registrationRecord.toString());

            //Inserimento azienda
            AziendaRegistrazione a = registrationRecord.getAzienda();
            if (aziendaRep.findAziendaByPartitaIva(a.getPartitaIVA()) != null)
                throw new Exception("Azienda già inserita!");
            Azienda azienda = new Azienda(a.getPartitaIVA(), a.getRagioneSociale(), a.getSuffissoEmail());
            aziendaRep.save(azienda);

            //Inserimento orario
            OrarioRegistrazione o = registrationRecord.getOrario();
            Orario orario = new Orario(azienda);
            for(int i = 0; i < 7;i++){
                GiornataRegistrazione g = o.getByIndex(i);
                Giorno giorno = new Giorno(g.getApertura(), g.getChiusura());
                if(giorno.getApertura() < 0 || giorno.getChiusura() < 0){
                    giorno.setApertura(-1);
                    giorno.setChiusura(-1);
                }
                else if(giorno.getChiusura() <= giorno.getApertura())
                    throw new Exception("Orario di chiusura <= orario di apertura!");
                switch(i) {
                    case 0:
                        orario.setLunedi(giorno);
                        break;
                    case 1:
                        orario.setMartedi(giorno);
                        break;
                    case 2:
                        orario.setMercoledi(giorno);
                        break;
                    case 3:
                        orario.setGiovedi(giorno);
                        break;
                    case 4:
                        orario.setVenerdi(giorno);
                        break;
                    case 5:
                        orario.setSabato(giorno);
                        break;
                    case 6:
                        orario.setDomenica(giorno);
                        break;
                }
            }
            orarioRep.save(orario);

            //Inserimento sede centrale
            Sede sede = new Sede(azienda, a.getSedeHQ());
            sedeRep.save(sede);

            //Inserimento ruolo manager
            Ruoli manager = new Ruoli(new RuoliID(azienda, "Manager"), true, 1);
            ruoliRep.save(manager);

            //Inserimento ruoli dipendenti
            for (int i = 0; i < registrationRecord.getRuoli().length; i++) {
                Ruoli ruolo = new Ruoli(new RuoliID(azienda, registrationRecord.getRuoli()[i].getNomeRuolo()), false, registrationRecord.getRuoli()[i].getNumeroDipendenti());
                ruoliRep.save(ruolo);
            }

            //Inserimento manager
            String hashPasswordManager = Utente.sha256(registrationRecord.getManager().getPassword());
            if (utenteRep.findUtenteByEmail(registrationRecord.getManager().getEmail()) != null)
                throw new Exception("Email manager già presente!");
            Utente admin = new Utente(registrationRecord.getManager().getEmail(), registrationRecord.getManager().getNome(), registrationRecord.getManager().getCognome(), azienda, false, sede, hashPasswordManager);
            Ruoli rManager = ruoliRep.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(azienda, "Manager");
            RuoliUtente ru = new RuoliUtente(rManager, admin.getEmail());
            ruoliUtenteRep.save(ru);
            utenteRep.save(admin);

            //Inserimento dipendenti
            for (int i = 0; i < registrationRecord.getDipendenti().length; i++) {
                String hashPasswordDipendente = Utente.sha256(registrationRecord.getDipendenti()[i].getCognomeDipendente());
                String emailDipendente;
                int contatore = 0;
                do {
                    if (contatore == 0) {
                        emailDipendente = registrationRecord.getDipendenti()[i].getNomeDipendente() + "." + registrationRecord.getDipendenti()[i].getCognomeDipendente() + "@" + registrationRecord.getAzienda().getSuffissoEmail();
                    } else {
                        emailDipendente = registrationRecord.getDipendenti()[i].getNomeDipendente() + "." + registrationRecord.getDipendenti()[i].getCognomeDipendente() + contatore + "@" + registrationRecord.getAzienda().getSuffissoEmail();
                    }
                    contatore++;
                    emailDipendente = emailDipendente.toLowerCase();
                } while (utenteRep.existsById(emailDipendente));
                Utente dipendente = new Utente(emailDipendente, registrationRecord.getDipendenti()[i].getNomeDipendente(), registrationRecord.getDipendenti()[i].getCognomeDipendente(), azienda, registrationRecord.getDipendenti()[i].isPartTime(), sede, hashPasswordDipendente);
                for (int j = 0; j < registrationRecord.getDipendenti()[i].getRuoliDipendente().length; j++) {
                    Ruoli rDipendente = ruoliRep.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(azienda, registrationRecord.getDipendenti()[i].getRuoloDipendente(j));
                    RuoliUtente ruDip = new RuoliUtente(rDipendente, emailDipendente);
                    ruoliUtenteRep.save(ruDip);
                }
                utenteRep.save(dipendente);
            }
            return 1;
        } catch (Exception ex) {
            System.out.println(ex);
            return 0;
        }
    }
}