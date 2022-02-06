package com.progetto.backendserver.controllers;

import com.progetto.backendserver.JSON.RichiestaStatusGiorno;
import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Sede;
import com.progetto.backendserver.db.models.Turno;
import com.progetto.backendserver.db.repository.SedeRepository;
import com.progetto.backendserver.db.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetStatusGiorniController {

    @Autowired
    private TurnoRepository turnoRep;

    @Autowired
    private SedeRepository sedeRep;

    @PostMapping("/getStatusGiorni")
    public int[] getStatusGiorni(@RequestBody RichiestaStatusGiorno richiesta, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("azienda") == null)
            return null;
        Azienda azienda = (Azienda) session.getAttribute("azienda");
        Sede sede = sedeRep.getSedeByIndirizzoAndAzienda(richiesta.getIndirizzoSede(), azienda);
        List<Integer> lista = new ArrayList<>();

        LocalDate data = LocalDate.of(richiesta.getAnno(), richiesta.getMese(), 1);
        while(data.getMonthValue() == richiesta.getMese()){
            List<Turno> listaTurni = turnoRep.findTurnoBySedeAndData(sede, data);
            if(listaTurni == null || listaTurni.size() == 0)
                lista.add(-1); //Giorno di chiusura
            else{
                List<Turno> lt = turnoRep.findTurnoBySedeAndDataAndOreScoperteGreaterThan(sede, data, 0);
                if(lt == null || lt.size() == 0)
                    lista.add(1); //Tutti i turni coperti
                else
                    lista.add(0); //Turno scoperto
            }
            data = data.plusDays(1);
        }

        int[] array = new int[lista.size()];
        for(int i = 0;i<lista.size();i++){
            array[i] = lista.get(i);
        }
        if(array.length > 0)
            return array;
        return null;
    }
}
