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
public class TurnoRepositoryTest {
    @Autowired private SedeRepository sedRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private TurnoRepository turRepo;
    @Autowired private UtenteRepository utRepo;
    @Autowired private RuoliRepository rolesRepo;
    @Autowired private RuoliUtenteRepository utRolesRepo;

    @Test
    void autowiredNotNull(){
        assertThat(azRepo).isNotNull();
        assertThat(sedRepo).isNotNull();
        assertThat(turRepo).isNotNull();
        assertThat(utRepo).isNotNull();
        assertThat(rolesRepo).isNotNull();
        assertThat(utRolesRepo).isNotNull();
    }

    @Test
    void findByOk(){
        //Given
        Azienda azienda = new Azienda("01023830167", "REMAZEL ENGINEERING SpA", "remazel.com");
        Sede sede = new Sede(azienda, "Via Portici Manarini, 41/A 24060 Chiuduno (BG)");
        Sede notSed = new Sede(azienda, "Pippo Baudo");
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
        Turno turScoperto = new Turno(
                sede,
                ing.getRuoloID(),
                LocalDate.now().plusDays(1),
                LocalDateTime.now().plusDays(1).getHour(),
                LocalDateTime.now().plusDays(1).getHour()+8,
                4
        );
        Turno turCoperto = new Turno(
                sede,
                ing.getRuoloID(),
                LocalDate.now().plusDays(2),
                LocalDateTime.now().plusDays(2).getHour(),
                LocalDateTime.now().plusDays(2).getHour()+8,
                0
        );
        Turno turNot = new Turno(
                sede,
                IT.getRuoloID(),
                LocalDate.now(),
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getHour()+8
        );
        Collection<Turno> allTurno = List.of(turTest, turCoperto, turScoperto);
        Collection<Turno> allTurnoOrdered = List.of(turCoperto, turScoperto, turTest);
        //When
        azRepo.save(azienda);
        sedRepo.save(sede);
        rolesRepo.saveAll(allRoles);
        utRepo.saveAll(allUsers);
        utRolesRepo.saveAll(allUtRoles);
        turRepo.saveAll(allTurno);
        //Then
        assertThat(turRepo.findTurnoByData(turTest.getData()))
                .asList()
                .contains(turTest)
                .asList()
                .doesNotContain(turNot);
        assertThat(turRepo.findTurnoByDataAndSedeIn(turTest.getData(), List.of(sede)))
                .asList()
                .contains(turTest)
                .asList()
                .doesNotContain(turNot);
        assertThat(turRepo.findTurnoByDataAndSedeIn(turTest.getData().plusDays(1), List.of(sede)))
                .asList()
                .doesNotContain(turTest)
                .asList()
                .doesNotContain(turNot);
        assertThat(turRepo.findTurnoBySedeAndData(sede, turTest.getData()))
                .asList()
                .contains(turTest);
        assertThat(turRepo.findTurnoBySedeAndData(sede, turTest.getData().plusDays(3)))
                .asList()
                .isEmpty();
        assertThat(turRepo.findTurnoBySedeAndDataAndOreScoperteGreaterThan(sede,turScoperto.getData(),3))
                .asList()
                .contains(turScoperto)
                .asList()
                .doesNotContain(turCoperto);
        assertThat(turRepo.findTurnoBySedeAndDataAndOreScoperteGreaterThan(sede,turScoperto.getData(),6))
                .asList()
                .isEmpty();
        assertThat(turRepo.findTurnoBySedeAndDataAndRuoloID(sede, turTest.getData(), turTest.getRuoloID()))
                .asList()
                .contains(turTest);
        assertThat(turRepo.findTurnoBySedeAndDataAndRuoloID(sede, turNot.getData(), ammin.getRuoloID()))
                .asList()
                .isEmpty();
        assertThat(turRepo.findTurnoBySedeInAndDataGreaterThanAndDataLessThanOrderByDataDesc(List.of(sede),LocalDate.now().minusDays(3),LocalDate.now().minusDays(2)))
                .asList()
                .isEmpty();
        assertThat(turRepo.findTurnoBySedeInAndDataGreaterThanAndDataLessThanOrderByDataDesc(List.of(sede), LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)))
                .asList()
                .hasSize(3);
        assertThat(turRepo.findTurnoBySedeAndDataGreaterThanAndDataLessThanOrderByDataDesc(sede, LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)))
                .asList()
                .hasSize(3);
        assertThat(turRepo.findTurnoBySedeAndDataGreaterThanAndDataLessThanOrderByDataDesc(sede,LocalDate.now().minusDays(3),LocalDate.now().minusDays(2)))
                .asList()
                .isEmpty();
        assertThat(turRepo.findTurnoBySedeOrderByDataDesc(sede))
                .asList()
                .isEqualTo(allTurnoOrdered);
        assertThat(turRepo.findTurnoBySedeOrderByDataDesc(sede))
                .asList()
                .isNotEqualTo(allTurno);
    }
}
