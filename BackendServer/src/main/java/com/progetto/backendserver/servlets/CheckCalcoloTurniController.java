package com.progetto.backendserver.servlets;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class CheckCalcoloTurniController {

    @Autowired
    private SedeRepository sedeRep;

    @Autowired
    private TurnoRepository turnoRep;

    @PostMapping("/checkCalcoloTurni")
    public int checkTurni(@RequestBody String meseAnno, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("azienda") == null)
            return 0;
        Azienda azienda = (Azienda) session.getAttribute("azienda");
        String[] temp = meseAnno.split("\"");
        String[] s = temp[1].split(";");
        int mese = Integer.parseInt(s[0]);
        int anno = Integer.parseInt(s[1]);

        List<Sede> sedi = sedeRep.findSedeByAzienda(azienda);
        List<Turno> turni = turnoRep.findTurnoBySedeOrderByDataDesc(sedi.get(0));

        for (Turno turno : turni) {
            if (turno.getData().getYear() < anno)
                return 0;
            if (turno.getData().getYear() == anno && turno.getData().getMonthValue() < mese)
                return 0;
            if (turno.getData().getYear() == anno && turno.getData().getMonthValue() == mese)
                return 1;
        }
        return 0;
    }
}
