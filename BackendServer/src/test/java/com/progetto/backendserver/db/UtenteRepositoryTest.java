package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import com.progetto.backendserver.db.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UtenteRepositoryTest {
    @Autowired private SedeRepository sedRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private TurnoRepository turRepo;
    @Autowired private UtenteRepository utRepo;
    @Autowired private RuoliRepository rolesRepo;
    @Autowired private RuoliUtenteRepository ruUtRep;

    @Test
    void autowiredNotNull(){
        assertThat(azRepo).isNotNull();
        assertThat(sedRepo).isNotNull();
        assertThat(turRepo).isNotNull();
        assertThat(utRepo).isNotNull();
        assertThat(rolesRepo).isNotNull();
        assertThat(ruUtRep).isNotNull();
    }
    @Test
    void findByOk() {
        Azienda azienda = new Azienda("01023830167", "REMAZEL ENGINEERING SpA", "remazel.com");
        Azienda azNot = new Azienda("93128778712", "Inexistent", "nowhere.com");
        Sede sede = new Sede(azienda, "Via Portici Manarini, 41/A 24060 Chiuduno (BG)");
        Sede notSed = new Sede(azienda, "Pippo Baudo");
        Ruoli ing = new Ruoli(new RuoliID(azienda, "Ingegnere"), false, 5);
        Ruoli man = new Ruoli(new RuoliID(azienda, "Manager"), true, 1);
        Ruoli IT = new Ruoli(new RuoliID(azienda, "IT"), false, 2);
        Ruoli ammin = new Ruoli(new RuoliID(azienda, "Amministratore"), false, 3);
        Collection<Ruoli> allRoles = List.of(ing, man, IT, ammin);
        Utente admin = new Utente("andrearosa@gmail.com", "Andrea", "Rosa", azienda, false, sede, Utente.sha256("Rosa"));
        Utente ementa = new Utente("e.mentana@remazel.com", "Enrico", "Mentana", azienda, false, sede, Utente.sha256("Mentana"));
        ementa.setToken(UUID.randomUUID().toString());
        Utente lannunciata = new Utente("l.annunciata@remazel.com", "Lucia", "Annunciata", azienda, true, sede, Utente.sha256("Annunciata"));
        Utente mdamilano = new Utente("m.damilano@remazel.com", "Marco", "Da Milano", azienda, false, sede, Utente.sha256("Da Milano"));
        Collection<Utente> allUsers = List.of(admin, ementa, lannunciata, mdamilano);
        Collection<RuoliUtente> allUtRoles = List.of(
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
                )
        );
        RuoliUtente annuziataRU = new RuoliUtente(
                ing.getRuoloID().getRoleName(),
                azienda.getPartitaIva(),
                lannunciata.getEmail()
        );
        RuoliUtente ementanaRU = new RuoliUtente(
                ing.getRuoloID().getRoleName(),
                azienda.getPartitaIva(),
                ementa.getEmail()
        );

        //When
        azRepo.save(azienda);
        sedRepo.save(sede);
        rolesRepo.saveAll(allRoles);
        utRepo.saveAll(allUsers);
        ruUtRep.saveAll(allUtRoles);
        //Then
        assertThat(utRepo.findUtenteByAzienda(azienda))
                .asList()
                .contains(allUsers.toArray())
                .asList()
                .hasSize(4);
        assertThat(utRepo.findUtenteByAzienda(azNot))
                .asList()
                .isEmpty();
        assertThat(utRepo.findUtenteByEmail(mdamilano.getEmail()))
                .isNotNull()
                .isEqualTo(mdamilano);
        assertThat(utRepo.findUtenteByEmail("pippo"))
                .isNull();
        assertThat(utRepo.findUtenteByEmailAndHashPassword(mdamilano.getEmail(), mdamilano.getHashPassword()))
                .isNotNull()
                .isEqualTo(mdamilano);
        assertThat(utRepo.findUtenteByEmailAndHashPassword(mdamilano.getEmail(), Utente.sha256("Invalid")))
                .isNull();
        assertThat(utRepo.findUtenteByToken(ementa.getToken()))
                .isNotNull()
                .isEqualTo(ementa);
        assertThat(utRepo.findUtenteByToken(UUID.randomUUID().toString()))
                .isNull();
    }
}
