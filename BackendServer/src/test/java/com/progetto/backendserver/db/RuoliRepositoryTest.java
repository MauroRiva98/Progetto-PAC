package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import com.progetto.backendserver.db.repository.AziendaRepository;
import com.progetto.backendserver.db.repository.RuoliRepository;
import com.progetto.backendserver.db.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RuoliRepositoryTest {
    @Autowired private RuoliRepository ruoRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private UtenteRepository utRep;
    @Autowired private DataSource dataSource;

    @Test
    void autowiredNotNull(){
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
        Ruoli ric = new Ruoli(new RuoliID(az, "Ricercatore"), false, 5);
        Ruoli man = new Ruoli(new RuoliID(az, "Manager"), true, 1);
        Ruoli prof = new Ruoli(new RuoliID(azNot, "Professore"), false, 10);
        Ruoli ammin = new Ruoli(new RuoliID(azNot, "Amministrativo"), false, 3);
        //When
        azRepo.save(az);
        ruoRepo.save(ric);
        ruoRepo.save(man);
        //Then
        assertThat(ruoRepo.getRuoliByRuoloID_Azienda(az))
                .asList()
                .contains(ric, man);
        assertThat(ruoRepo.getRuoliByRuoloID_Azienda(azNot))
                .asList()
                .isEmpty();
        assertThat(ruoRepo.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(az, prof.getRuoloID().getRoleName())).isNull();
    }
}
