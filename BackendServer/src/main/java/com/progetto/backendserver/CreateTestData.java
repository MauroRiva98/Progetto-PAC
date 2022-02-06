package com.progetto.backendserver;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import com.progetto.backendserver.db.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Configuration
public class CreateTestData {
    private static final Logger log = LoggerFactory.getLogger(CreateTestData.class);

    @Bean
    CommandLineRunner AddUniTestData(UtenteRepository rep, AziendaRepository azi, RuoliRepository rol, SedeRepository sedeRep, OrarioRepository orRep, RuoliUtenteRepository ruoUtRep) {
        Azienda uni = new Azienda("PI6898", "UniBG", "unibg.it");
        Sede sede = new Sede(uni, "Via A. Einstein");
        Ruoli ric = new Ruoli(new RuoliID(uni, "Ricercatore"), false, 5);
        Ruoli man = new Ruoli(new RuoliID(uni, "Manager"), true, 1);
        Ruoli prof = new Ruoli(new RuoliID(uni, "Professore"), false, 10);
        Ruoli ammin = new Ruoli(new RuoliID(uni, "Amministrativo"), false, 3);
        Collection<Ruoli> allRoles = new ArrayList<>(List.of(ric, man, prof, ammin));
        Utente admin = new Utente("m.riva22@studenti.unibg.it", "Mauro", "Riva", uni, false, sede, Utente.sha256("Prova123"));
        Utente lomba = new Utente("f.lombardo@unibg.it", "Frida", "Lombardo", uni, false, sede, Utente.sha256("Lombardo"));
        Utente zito = new Utente("f.zito@unibg.it","Fabrizia", "Zito", uni, true, sede, Utente.sha256("Zito"));
        Utente barese = new Utente("i.barese@unibg.it", "Ireneo", "Barese", uni, false, sede, Utente.sha256("Barese"));
        Collection<Utente> allUsers = new ArrayList<>(List.of(admin, lomba, zito, barese));
        RuoliUtente amminRoles = new RuoliUtente(man.getRuoloID().getRoleName(),
                uni.getPartitaIva(),
                admin.getEmail()
        );
        ArrayList<RuoliUtente> lombaRoles = new ArrayList<>(List.of(
                new RuoliUtente(prof.getRuoloID().getRoleName(),uni.getPartitaIva(), lomba.getEmail()),
                new RuoliUtente(ric.getRuoloID().getRoleName(),uni.getPartitaIva(), lomba.getEmail()),
                new RuoliUtente(ammin.getRuoloID().getRoleName(), uni.getPartitaIva(), lomba.getEmail())
        ));
        RuoliUtente zitoRoles = new RuoliUtente(ric.getRuoloID().getRoleName(),
                uni.getPartitaIva(),
                zito.getEmail()
        );
        ArrayList<RuoliUtente> bareseRoles = new ArrayList<>(List.of(
                new RuoliUtente(prof.getRuoloID().getRoleName(),uni.getPartitaIva(), barese.getEmail()),
                new RuoliUtente(ammin.getRuoloID().getRoleName(), uni.getPartitaIva(), barese.getEmail())
        ));
        Collection<RuoliUtente> allCombRoles = new ArrayList<>(List.of(amminRoles, zitoRoles));
        allCombRoles.addAll(lombaRoles);
        allCombRoles.addAll(bareseRoles);
        Orario uniOrari = new Orario(uni, new Giorno(8,19),new Giorno(8, 18), new Giorno(9,15), new Giorno(11, 17), new Giorno(11, 12), new Giorno(-1,-1), new Giorno(-1, -1));
        return args -> {
            log.info(Utils.getMethodName() + ": Aggiungo " + azi.save(uni) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + sedeRep.save(sede) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + rol.saveAll(allRoles) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + rep.saveAll(allUsers) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + ruoUtRep.saveAll(allCombRoles) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + orRep.save(uniOrari) + "al database.");
        };
    }

    @Bean
    CommandLineRunner AddAziTestData(UtenteRepository rep, AziendaRepository azi, RuoliRepository rol, SedeRepository sedeRep, OrarioRepository orRep, RuoliUtenteRepository ruoUtRep) {
        Azienda azienda = new Azienda("01023830167", "REMAZEL ENGINEERING SpA", "remazel.com");
        Sede sede = new Sede(azienda, "Via Portici Manarini, 41/A 24060 Chiuduno (BG)");

        Ruoli ing = new Ruoli(new RuoliID(azienda, "Ingegnere"), false, 5);
        Ruoli man = new Ruoli(new RuoliID(azienda, "Manager"), true, 1);
        Ruoli IT = new Ruoli(new RuoliID(azienda, "IT"), false, 2);
        Ruoli ammin = new Ruoli(new RuoliID(azienda, "Amministratore"), false, 3);
        Collection<Ruoli> allRoles = new ArrayList<>(List.of(ing, man, IT, ammin));
        Utente admin = new Utente("andrearosa@gmail.com", "Andrea", "Rosa", azienda, false, sede, Utente.sha256("Rosa"));
        Utente ementa = new Utente("e.mentana@remazel.com", "Enrico", "Mentana", azienda, false, sede, Utente.sha256("Mentana"));
        Utente lannunciata = new Utente("l.annunciata@remazel.com","Lucia", "Annunciata", azienda, true, sede, Utente.sha256("Annunciata"));
        Utente mdamilano = new Utente("m.damilano@remazel.com", "Marco", "Da Milano", azienda, false, sede, Utente.sha256("Da Milano"));
        Utente pcelata = new Utente("p.celata@remazel.com", "Paolo", "Celata", azienda, false,sede, Utente.sha256("Celata"));
        Utente sbechis = new Utente("s.bechis@remazel.com", "Sergio", "Bechis", azienda, false, sede, Utente.sha256("Bechis"));
        Utente lcadorna = new Utente("l.cadorna@remazel.com", "Luigi", "Cadorna", azienda, false,sede, Utente.sha256("Cadorna"));
        Utente pbadoglio = new Utente("p.badoglio@remazel.com", "Pietro", "Badoglio", azienda, false,sede, Utente.sha256("Badoglio"));
        Utente bbeccaris = new Utente("b.beccaris@remazel.com", "Bava", "Beccaris", azienda, false,sede, Utente.sha256("Beccaris"));
        Utente bgates = new Utente("b.gates@remazel.com", "Bill", "Gates", azienda, false,sede, Utente.sha256("Gates"));
        Utente mriva = new Utente("m.riva24@studenti.unibg.it", "Mauro", "Riva", azienda, false, sede, Utente.sha256("Riva"));
        Collection<Utente> allUsers = new ArrayList<>(List.of(admin, ementa, lannunciata, mdamilano, pcelata, sbechis, lcadorna, pbadoglio, bbeccaris, bgates, mriva));
        Collection<RuoliUtente> allUtRoles = new ArrayList<>(List.of(
                new RuoliUtente(
                        man.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        admin.getEmail()
                ),
                new RuoliUtente(
                        ing.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        ementa.getEmail()
                ),
                new RuoliUtente(
                        ing.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        lannunciata.getEmail()
                ),
                new RuoliUtente(
                        ing.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        mdamilano.getEmail()
                ),
                new RuoliUtente(
                        ing.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        pcelata.getEmail()
                ),
                new RuoliUtente(
                        ing.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        sbechis.getEmail()
                ),
                new RuoliUtente(
                        ammin.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        lcadorna.getEmail()
                ),
                new RuoliUtente(
                        ammin.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        pbadoglio.getEmail()
                ),
                new RuoliUtente(
                        ammin.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        bbeccaris.getEmail()
                ),
                new RuoliUtente(
                        IT.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        bgates.getEmail()
                ),
                new RuoliUtente(
                        IT.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        mriva.getEmail()
                )
        ));
        Orario uniOrari = new Orario(azienda, new Giorno(8,16),new Giorno(8, 16), new Giorno(8,16), new Giorno(8, 16), new Giorno(8, 16), new Giorno(-1,-1), new Giorno(-1, -1));
        return args -> {
            log.info(Utils.getMethodName() + ": Aggiungo " + azi.save(azienda) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + sedeRep.save(sede) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + rol.saveAll(allRoles) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + rep.saveAll(allUsers) + " al database.");
            log.info(Utils.getMethodName() + ": Aggiungo " + ruoUtRep.saveAll(allUtRoles) + "al database");
            log.info(Utils.getMethodName() + ": Aggiungo " + orRep.save(uniOrari) + "al database.");
        };
    }
}
