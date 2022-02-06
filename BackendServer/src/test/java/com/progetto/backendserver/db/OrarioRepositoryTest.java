package com.progetto.backendserver.db;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.repository.AziendaRepository;
import com.progetto.backendserver.db.repository.OrarioRepository;
import com.progetto.backendserver.db.repository.UtenteRepository;
import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrarioRepositoryTest {
    @Autowired
    private OrarioRepository orarRepo;
    @Autowired private AziendaRepository azRepo;
    @Autowired private UtenteRepository utRep;
    @Autowired private DataSource dataSource;

    @Test
    void autowiredNotNull(){
        assertThat(orarRepo).isNotNull();
        assertThat(azRepo).isNotNull();
        assertThat(utRep).isNotNull();
        assertThat(dataSource).isNotNull();
    }

    @Test
    void ifFoundByOk(){
        //Given
        Azienda az = new Azienda("PI1233", "PinCoPalla Ent.", "somewhere.com");
        Azienda azNot = new Azienda("PI8542", "PinCoPalla Nor Ent.", "nothere.com");
        Orario or = new Orario(az,
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(-1,-1),
                new Giorno(-1,-1)
                );
        Orario orNot = new Orario(azNot,
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(8,16),
                new Giorno(-1,-1),
                new Giorno(-1,-1)
        );
        //When
        azRepo.save(az);
        orarRepo.save(or);
        //Then
        assertThat(orarRepo.findOrarioByAzienda(az))
                .isEqualTo(or);
        assertThat(orarRepo.findOrarioByAzienda(azNot))
                .isNull();
    }
}
