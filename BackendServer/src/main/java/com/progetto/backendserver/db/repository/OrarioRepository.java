package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Orario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrarioRepository extends JpaRepository<Orario, Integer> {
    Orario findOrarioByAzienda(Azienda azienda);
}
