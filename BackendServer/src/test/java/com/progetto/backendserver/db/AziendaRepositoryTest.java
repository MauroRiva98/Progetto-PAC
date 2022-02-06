package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.repository.AziendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AziendaRepositoryTest {
    @Autowired private AziendaRepository azRepo;
    @Autowired private DataSource dataSource;

    @Test
    void autowiredNotNull(){
        assertThat(azRepo).isNotNull();
        assertThat(dataSource).isNotNull();
    }

    @Test
    void findByOk(){
        Azienda exists = new Azienda("PI6898", "UniBG", "unibg.it");
        Azienda unavailable = new Azienda("PI07348", "Inesistente", "nowhere.com");
        azRepo.save(exists);
        //Always identical to exists
        assertThat(azRepo.findAziendaByPartitaIva(exists.getPartitaIva()))
                .isEqualTo(exists);
        //Always null
        assertThat(azRepo.findAziendaByPartitaIva(unavailable.getPartitaIva()))
                .isNull();
        //Always identical to exists
        assertThat(azRepo.findAziendaByRagioneSociale(exists.getRagioneSociale()))
                .isEqualTo(exists);
        //Always null
        assertThat(azRepo.findAziendaByRagioneSociale(unavailable.getRagioneSociale()))
                .isNull();
    }
}
