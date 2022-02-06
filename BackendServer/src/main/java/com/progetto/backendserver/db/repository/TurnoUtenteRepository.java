package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Turno;
import com.progetto.backendserver.db.models.TurnoUtente;
import com.progetto.backendserver.db.models.Utente;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TurnoUtenteRepository extends JpaRepository<TurnoUtente, Integer> {
    long deleteByTurno(Turno turno);
    List<TurnoUtente> getTurnoUtenteByTurno(Turno turno);
    List<TurnoUtente> findTurnoUtenteByTurnoAndFuoriOrario(Turno turno, Boolean fuoriOrario);
    List<TurnoUtente> findTurnoUtenteByUtenteAndTurnoIn(Utente utente, List<Turno> turno);
    // List<TurnoUtente> findTurnoUtenteByUtente(Utente utente, LocalDate);
}
