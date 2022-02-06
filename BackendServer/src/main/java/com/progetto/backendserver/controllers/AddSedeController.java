package com.progetto.backendserver.controllers;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.SediScambioID;
import com.progetto.backendserver.db.repository.*;
import com.progetto.backendserver.inserimento.NuovaSede;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;

@RestController
public class AddSedeController {

    @Autowired
    private SedeRepository sedeRep;

    @Autowired
    private UtenteRepository utenteRep;

    @Autowired
    private RuoliRepository ruoliRep;

    @Autowired
    private RuoliUtenteRepository ruoliUtenteRep;

    @Autowired
    private VicinanzaSediRepository vicinanzaSediRep;

    @Transactional
    @PostMapping("/aggiungiSede")
    public int registrazione(@RequestBody NuovaSede sedeRecord, HttpServletRequest request) throws ParseException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if(session == null || session.getAttribute("azienda") == null)
                return 0;
            Azienda azienda = (Azienda) session.getAttribute("azienda");
            if(sedeRep.getSedeByIndirizzoAndAzienda(sedeRecord.getSede().getIndirizzo(), azienda) != null)
                return -1;
            Sede sede, sedeVicina = null;
            VicinanzaSedi vicSedi = null;
            if(sedeRecord.getSede().getSedeVicina() == null)
                sede = new Sede(azienda, sedeRecord.getSede().getIndirizzo());
            else {
                sede = new Sede(azienda, sedeRecord.getSede().getIndirizzo());
                sedeVicina = sedeRep.getSedeByIndirizzoAndAzienda(sedeRecord.getSede().getSedeVicina(), azienda);
                SediScambioID scambio = new SediScambioID(sede, sedeVicina);
                vicSedi = new VicinanzaSedi(scambio, sedeRecord.getSede().getDistanza());
            }
            sedeRep.save(sede);
            if (sedeVicina != null){
                vicinanzaSediRep.save(vicSedi);
            }
            for (int i = 0; i < sedeRecord.getDipendenti().length; i++) {
                String hashPasswordDipendente = Utente.sha256(sedeRecord.getDipendenti()[i].getCognomeDipendente());
                String emailDipendente;
                int contatore = 0;
                do {
                    if (contatore == 0) {
                        emailDipendente = sedeRecord.getDipendenti()[i].getNomeDipendente() + "." + sedeRecord.getDipendenti()[i].getCognomeDipendente() + "@" + azienda.getDomainAzienda();
                    } else {
                        emailDipendente = sedeRecord.getDipendenti()[i].getNomeDipendente() + "." + sedeRecord.getDipendenti()[i].getCognomeDipendente() + contatore + "@" + azienda.getDomainAzienda();
                    }
                    contatore++;
                    emailDipendente = emailDipendente.toLowerCase();
                } while (utenteRep.existsById(emailDipendente));
                Utente dipendente = new Utente(emailDipendente, sedeRecord.getDipendenti()[i].getNomeDipendente(), sedeRecord.getDipendenti()[i].getCognomeDipendente(), azienda, sedeRecord.getDipendenti()[i].isPartTime(), sede, hashPasswordDipendente);
                for (int j = 0; j < sedeRecord.getDipendenti()[i].getRuoliDipendente().length; j++) {
                    Ruoli rDipendente = ruoliRep.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(azienda, sedeRecord.getDipendenti()[i].getRuoloDipendente(j));
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
