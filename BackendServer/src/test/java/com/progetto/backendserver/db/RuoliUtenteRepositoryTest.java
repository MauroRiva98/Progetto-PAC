package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import com.progetto.backendserver.db.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RuoliUtenteRepositoryTest {
    @Autowired private RuoliUtenteRepository ruoUtRepo;
    @Autowired private SedeRepository sedRepo;
    @Autowired private RuoliRepository ruoRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private UtenteRepository utRep;
    @Autowired private DataSource dataSource;

    @Test
    void autowiredNotNull(){
        assertThat(ruoUtRepo).isNotNull();
        assertThat(ruoRepo).isNotNull();
        assertThat(azRepo).isNotNull();
        assertThat(utRep).isNotNull();
        assertThat(dataSource).isNotNull();
    }

    @Test
    void ifFoundByOk(){
        //Given
        Azienda az = new Azienda("PI1233", "PinCoPalla Ent.", "somewhere.com");
        Azienda azNot = new Azienda("PI8542", "PinCoPalla Nor Ent.", "nothere.com");
        Sede sede = new Sede(az, "Via A. Einstein");
        Ruoli ric = new Ruoli(new RuoliID(az, "Ricercatore"), false, 5);
        Ruoli man = new Ruoli(new RuoliID(az, "Manager"), true, 1);
        Ruoli prof = new Ruoli(new RuoliID(az, "Professore"), false, 10);
        Ruoli ammin = new Ruoli(new RuoliID(az, "Amministrativo"), false, 3);
        Collection<Ruoli> allRoles = List.of(ric, man, prof, ammin);
        Utente admin = new Utente("m.riva22@studenti.unibg.it", "Mauro", "Riva", az, false, sede, Utente.sha256("Prova123"));
        Utente lomba = new Utente("f.lombardo@unibg.it", "Frida", "Lombardo", az, false, sede, Utente.sha256("Lombardo"));
        Utente zito = new Utente("f.zito@unibg.it","Fabrizia", "Zito", az, true, sede, Utente.sha256("Zito"));
        Utente barese = new Utente("i.barese@unibg.it", "Ireneo", "Barese", az, false, sede, Utente.sha256("Barese"));
        Collection<Utente> allUsers = List.of(admin, lomba);
        RuoliUtente amminRoles = new RuoliUtente(man.getRuoloID().getRoleName(),
                az.getPartitaIva(),
                admin.getEmail()
        );
        RuoliUtente toTest = new RuoliUtente(prof.getRuoloID().getRoleName(), az.getPartitaIva(), lomba.getEmail());
        Collection<RuoliUtente> lombaRoles = List.of(
                new RuoliUtente(prof.getRuoloID().getRoleName(), az.getPartitaIva(), lomba.getEmail()),
                new RuoliUtente(ammin.getRuoloID().getRoleName(), az.getPartitaIva(), lomba.getEmail())
        );
        RuoliUtente zitoRoles = new RuoliUtente(ric.getRuoloID().getRoleName(),
                az.getPartitaIva(),
                zito.getEmail()
        );
        Collection<RuoliUtente> bareseRoles = List.of(
                new RuoliUtente(prof.getRuoloID().getRoleName(), az.getPartitaIva(), barese.getEmail()),
                new RuoliUtente(ammin.getRuoloID().getRoleName(), az.getPartitaIva(), barese.getEmail())
        );
        Collection<RuoliUtente> allCombRoles = new ArrayList<>(List.of(amminRoles));
        allCombRoles.addAll(lombaRoles);
        //allCombRoles.addAll(bareseRoles);
        //When
        azRepo.save(az);
        sedRepo.save(sede);
        utRep.saveAll(allUsers);
        ruoRepo.saveAll(allRoles);
        ruoUtRepo.saveAll(allCombRoles);
        //Then
        //FIXME: Fallisce e non ho la più pallida idea del perché. Hashcode è definito, ma risulta diverso comunque.
        //FIXME: Controllare.
        assertThat(ruoUtRepo.findRuoloUtenteByEmailAndNomeRuolo(lomba.getEmail(),
                prof.getRuoloID().getRoleName()))
                .isEqualTo(toTest);
        assertThat(ruoRepo.getRuoliByRuoloID_Azienda(azNot))
                .asList()
                .isEmpty();
        //assertThat(ruoRepo.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(az, ric.getRuoloID().getRoleName())).isNull();
    }
}
