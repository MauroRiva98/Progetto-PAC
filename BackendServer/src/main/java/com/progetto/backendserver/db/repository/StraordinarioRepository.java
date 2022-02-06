package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Straordinario;
import com.progetto.backendserver.db.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StraordinarioRepository extends JpaRepository<Straordinario, Integer> {
    List<Straordinario> findStraordinarioByTurno(Turno turno);
    void deleteByTurno(Turno turno);
}
