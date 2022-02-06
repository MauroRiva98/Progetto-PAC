package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Ferie;
import com.progetto.backendserver.db.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FerieRepository extends JpaRepository<Ferie, Integer> {
    List<Ferie> findFerieByUtente(Utente utente);
}
