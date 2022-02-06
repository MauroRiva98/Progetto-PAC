package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Ferie;
import com.progetto.backendserver.db.models.Utente;
import com.progetto.backendserver.db.repository.AziendaRepository;
import com.progetto.backendserver.db.repository.FerieRepository;
import com.progetto.backendserver.db.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FerieRepositoryTest {

    @Autowired private FerieRepository ferRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private UtenteRepository utRep;
    @Autowired private DataSource dataSource;

    @Test
    void autowiredNotNull(){
        assertThat(ferRepo).isNotNull();
        assertThat(azRepo).isNotNull();
        assertThat(utRep).isNotNull();
        assertThat(dataSource).isNotNull();
    }

    @Test
    void findByOk(){
        //Given
        Azienda az = new Azienda("PI1233", "PinCoPalla Ent.", "somewhere.com");
        Utente ut = new Utente("pincopalla@somewhere.com", "Pippo", "Baudo", az, false);
        Utente utNotEx = new Utente("inex@somewhere.com", "Topolino", "Minnie", az, false);
        Ferie available = new Ferie(ut, LocalDate.of(2021,10,1), LocalDate.of(2021,11,1));
        Ferie unavailable = new Ferie(ut, LocalDate.of(2022,1,1),LocalDate.of(2021,2,1));
        //When
        azRepo.save(az);
        utRep.save(ut);
        ferRepo.save(available);
        //Then
        //Always identical to exists
        assertThat(ferRepo.findFerieByUtente(ut))
                .asList()
                .contains(available)
        //Always not equal to
                .asList()
                .doesNotContain(unavailable);
        //Always null
        assertThat(ferRepo.findFerieByUtente(utNotEx))
                .asList()
                .isEmpty();
    }
}
