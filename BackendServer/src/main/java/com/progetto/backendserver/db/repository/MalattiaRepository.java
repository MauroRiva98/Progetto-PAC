package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Malattia;
import com.progetto.backendserver.db.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MalattiaRepository extends JpaRepository<Malattia, Integer> {
    List<Malattia> findMalattiaByUtente(Utente utente);
}
