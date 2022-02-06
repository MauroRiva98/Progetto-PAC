package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Sede;
import com.progetto.backendserver.db.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class SedeRepositoryTest {
    @Autowired private SedeRepository sedRepo;
    @Autowired private AziendaRepository azRepo;

    @Test
    void autowiredNotNull(){
        assertThat(azRepo).isNotNull();
        assertThat(sedRepo).isNotNull();
    }
    @Test
    void ifFoundByOk() {
        //Given
        Azienda az = new Azienda("PI1233", "PinCoPalla Ent.", "somewhere.com");
        Azienda azNot = new Azienda("PI8542", "PinCoPalla Nor Ent.", "nothere.com");
        Sede sede = new Sede(az, "Via A. Einstein");
        Sede sedeNot = new Sede(azNot, "Via Portici Manarini, 41/A 24060 Chiuduno (BG)");
        //When
        azRepo.save(az);
        sedRepo.save(sede);
        //Then
        assertThat(sedRepo.findSedeByAzienda(az))
                .asList()
                .contains(sede)
                .asList()
                .doesNotContain(sedeNot);
        assertThat(sedRepo.findSedeByAzienda(azNot))
                .asList()
                .isEmpty();
    }
}
