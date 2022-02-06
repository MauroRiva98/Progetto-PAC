package com.progetto.backendserver.db.repository;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Ruoli;
import com.progetto.backendserver.db.models.EmbeddedIDs.RuoliID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuoliRepository extends JpaRepository<Ruoli, RuoliID> {
    List<Ruoli> getRuoliByRuoloID_Azienda(Azienda ruoloID_azienda);
    Ruoli getRuoliByRuoloID(RuoliID ruoloID);
    Ruoli getRuoliByRuoloID_AziendaAndRuoloID_RoleName(Azienda ruoloID_azienda, String ruoloID_roleName);
}
