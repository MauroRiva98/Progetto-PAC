package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import com.progetto.backendserver.db.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StraordinarioRepositoryTest {
    @Autowired private SedeRepository sedRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private TurnoRepository turRepo;
    @Autowired private UtenteRepository utRepo;
    @Autowired private StraordinarioRepository straRepo;
    @Autowired private RuoliRepository rolesRepo;
    @Autowired private RuoliUtenteRepository utRolesRepo;

    @Test
    void autowiredNotNull(){
        assertThat(azRepo).isNotNull();
        assertThat(sedRepo).isNotNull();
        assertThat(turRepo).isNotNull();
        assertThat(utRepo).isNotNull();
        assertThat(straRepo).isNotNull();
        assertThat(rolesRepo).isNotNull();
        assertThat(utRolesRepo).isNotNull();
    }

    @Test
    void findByOk(){
        //Given
        Azienda azienda = new Azienda("01023830167", "REMAZEL ENGINEERING SpA", "remazel.com");
        Sede sede = new Sede(azienda, "Via Portici Manarini, 41/A 24060 Chiuduno (BG)");
        Ruoli ing = new Ruoli(new RuoliID(azienda, "Ingegnere"), false, 5);
        Ruoli man = new Ruoli(new RuoliID(azienda, "Manager"), true, 1);
        Ruoli IT = new Ruoli(new RuoliID(azienda, "IT"), false, 2);
        Ruoli ammin = new Ruoli(new RuoliID(azienda, "Amministratore"), false, 3);
        Collection<Ruoli> allRoles = List.of(ing, man, IT, ammin);
        Utente admin = new Utente("andrearosa@gmail.com", "Andrea", "Rosa", azienda, false, sede, Utente.sha256("Rosa"));
        Utente ementa = new Utente("e.mentana@remazel.com", "Enrico", "Mentana", azienda, false, sede, Utente.sha256("Mentana"));
        Utente lannunciata = new Utente("l.annunciata@remazel.com","Lucia", "Annunciata", azienda, true, sede, Utente.sha256("Annunciata"));
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
                ),
                new RuoliUtente(
                        ing.getRuoloID().getRoleName(),
                        azienda.getPartitaIva(),
                        mdamilano.getEmail()
                )
        );
        Turno turTest = new Turno(
                sede,
                ing.getRuoloID(),
                LocalDate.now(),
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getHour()+8
        );
        Straordinario straTest = new Straordinario(ementa, turTest, LocalDate.now(), 4);
        Straordinario straNotIn = new Straordinario(ementa, turTest, LocalDate.now().plusDays(1), 5);
        //When
        azRepo.save(azienda);
        sedRepo.save(sede);
        rolesRepo.saveAll(allRoles);
        utRepo.saveAll(allUsers);
        utRolesRepo.saveAll(allUtRoles);
        turRepo.save(turTest);
        straRepo.save(straTest);
        //Then
        assertThat(straRepo.findStraordinarioByTurno(turTest))
                .asList()
                .contains(straTest)
                .asList()
                .doesNotContain(straNotIn);
    }
}
