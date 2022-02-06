package com.progetto.backendserver.controllers;

import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Sede;
import com.progetto.backendserver.db.repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GetIndirizzoSediController {

    @Autowired
    private SedeRepository sedeRep;

    @PostMapping("/indirizzoSedi")
    public String[] getSedi(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("azienda") == null)
            return null;
        Azienda azienda = (Azienda) session.getAttribute("azienda");
        List<Sede> lista = sedeRep.findSedeByAzienda(azienda);
        if(lista == null)
            return null;
        String[] sedi = new String[lista.size()];
        for(int i = 0; i < lista.size(); i++) {
            sedi[i] = lista.get(i).getIndirizzo();
        }
        return sedi;
    }
}
