package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.EmbeddedIDs.SediScambioID;
import com.progetto.backendserver.db.models.VicinanzaSedi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VicinanzaSediRepository extends JpaRepository<VicinanzaSedi, SediScambioID> {
    List<VicinanzaSedi> findAll();
}
