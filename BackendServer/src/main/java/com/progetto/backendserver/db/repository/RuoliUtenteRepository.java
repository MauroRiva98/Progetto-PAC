package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Ruoli;
import com.progetto.backendserver.db.models.RuoliUtente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuoliUtenteRepository extends JpaRepository<RuoliUtente, Integer> {
    //List<Ruoli> getRuoliByEmail(String email);
    List<RuoliUtente> getEmailByAziendaAndNomeRuolo(String azienda, String nomeRuolo);
    RuoliUtente findRuoloUtenteByEmailAndNomeRuolo(String email, String nomeRuolo);
    List<RuoliUtente> getRuoloUtenteByEmail(String email);

}
