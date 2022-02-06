package com.progetto.backendserver.controllers;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Ruoli;
import com.progetto.backendserver.db.models.Sede;
import com.progetto.backendserver.db.repository.RuoliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GetNomeRuoliController {

    @Autowired
    private RuoliRepository ruoliRep;

    @PostMapping("/nomeRuoli")
    public String[] getRuoli(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("azienda") == null)
            return null;
        Azienda azienda = (Azienda) session.getAttribute("azienda");
        List<Ruoli> lista = ruoliRep.getRuoliByRuoloID_Azienda(azienda);
        if(lista == null)
            return null;
        int dimensione = lista.size() - 1;
        int indice = 0;
        String[] ruoli = new String[dimensione];
        for(int i = 0; i < lista.size(); i++) {
            if(!lista.get(i).getRuoloID().getRoleName().equals("Manager")) {
                ruoli[indice] = lista.get(i).getRuoloID().getRoleName();
                indice++;
            }
        }
        return ruoli;
    }
}
