package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SedeRepository extends JpaRepository<Sede, Integer> {
    List<Sede> findSedeByAzienda(Azienda azienda);
    Sede getSedeByIndirizzo(String indirizzo);
    Sede getSedeByIndirizzoAndAzienda(String indirizzo, Azienda azienda);
}
