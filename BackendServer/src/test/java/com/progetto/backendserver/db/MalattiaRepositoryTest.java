package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Ferie;
import com.progetto.backendserver.db.models.Malattia;
import com.progetto.backendserver.db.models.Utente;
import com.progetto.backendserver.db.repository.AziendaRepository;
import com.progetto.backendserver.db.repository.MalattiaRepository;
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
public class MalattiaRepositoryTest {
    @Autowired private MalattiaRepository malRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private UtenteRepository utRep;
    @Autowired private DataSource dataSource;

    @Test
    void autowiredNotNull(){
        assertThat(malRepo).isNotNull();
        assertThat(azRepo).isNotNull();
        assertThat(utRep).isNotNull();
        assertThat(dataSource).isNotNull();
    }

    @Test
    void ifFoundByOk(){
        //Given
        Azienda az = new Azienda("PI1233", "PinCoPalla Ent.", "somewhere.com");
        Utente ut = new Utente("pincopalla@somewhere.com", "Pippo", "Baudo", az, false);
        Utente utNotEx = new Utente("inex@somewhere.com", "Topolino", "Minnie", az, false);
        Malattia available = new Malattia(ut, LocalDate.of(2021,10,1), LocalDate.of(2021,11,1));
        Malattia available2 = new Malattia(ut, LocalDate.of(2021,10,1), LocalDate.of(2021,11,1));
        Malattia unavailable = new Malattia(ut, LocalDate.of(2022,1,1),LocalDate.of(2021,2,1));
        //When
        azRepo.save(az);
        utRep.save(ut);
        malRepo.save(available);
        malRepo.save(available2);
        //Then
        //Always identical to exists
        assertThat(malRepo.findMalattiaByUtente(ut))
                .asList()
                .contains(available, available2)
        //Always not equal to
                .asList()
                .doesNotContain(unavailable)
                .asList()
                .hasSize(2);
        //Always null
        assertThat(malRepo.findMalattiaByUtente(utNotEx))
                .asList()
                .isEmpty();
    }
}
