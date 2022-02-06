package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * Un helper che serve ad avere parecchi funzioni utili per le operazioni
 * crud su un database. Estendendo <code>CrudRepository</code> otteniamo
 * tutte queste operazioni automaticamente.
 */
@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
    List<Utente> findUtenteByAzienda(Azienda azienda);
    Utente findUtenteByEmailAndHashPassword(String email, String hash);
    Utente findUtenteByEmail(String email);
    Utente findUtenteByToken(String token);

}
