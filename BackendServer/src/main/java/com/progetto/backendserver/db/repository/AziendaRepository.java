package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AziendaRepository extends JpaRepository<Azienda, String> {
    Azienda findAziendaByPartitaIva(String pIva);
    Azienda findAziendaByRagioneSociale(String nomeAzienda);
}
