package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    List<Turno> findTurnoBySedeOrderByDataDesc(Sede sede);
    List<Turno> findTurnoBySedeAndDataGreaterThanAndDataLessThanOrderByDataDesc(Sede sede, LocalDate from, LocalDate to);
    List<Turno> findTurnoBySedeAndData(Sede sede, LocalDate data);
    List<Turno> findTurnoBySedeAndDataAndRuoloID(Sede sede, LocalDate data, RuoliID ruoloID);
    List<Turno> findTurnoByDataAndSedeIn(LocalDate data, List<Sede> sede);
    List<Turno> findTurnoByData(LocalDate data);
    List<Turno> findTurnoBySedeAndDataAndOreScoperteGreaterThan(Sede sede, LocalDate data, int oreScoperte);

    List<Turno> findTurnoBySedeInAndDataGreaterThanAndDataLessThanOrderByDataDesc(List<Sede> sede, LocalDate from, LocalDate to);

    //Turno findTurnoBySedeAndUtenteContainingAndData(Sede sede, Utente utente, LocalDate data);
    //Turno findTurnoByUtenteContainingAndDataAndOraInizioIsLessThanEqualAndOraFineGreaterThanEqual(Utente ut, LocalDate toLocalDate, Integer startFrom, Integer endTo);
}
