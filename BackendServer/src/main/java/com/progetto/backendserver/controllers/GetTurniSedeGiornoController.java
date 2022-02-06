package com.progetto.backendserver.controllers;

import com.progetto.backendserver.JSON.RichiestaTurniSedeGiorno;
import com.progetto.backendserver.JSON.RispostaTurniSedeGiorno;
import com.progetto.backendserver.JSON.TurnoSingolo;
import com.progetto.backendserver.JSON.TurnoUtenteSingolo;
import com.progetto.backendserver.db.models.Azienda;
import com.progetto.backendserver.db.models.Sede;
import com.progetto.backendserver.db.models.Turno;
import com.progetto.backendserver.db.models.TurnoUtente;
import com.progetto.backendserver.db.repository.SedeRepository;
import com.progetto.backendserver.db.repository.TurnoRepository;
import com.progetto.backendserver.db.repository.TurnoUtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@RestController
public class GetTurniSedeGiornoController {

    @Autowired
    private TurnoRepository turnoRep;

    @Autowired
    private TurnoUtenteRepository turnoUtenteRep;

    @Autowired
    private SedeRepository sedeRep;

    @PostMapping("/getTurniSedeGiorno")
    public RispostaTurniSedeGiorno getStatusGiorni(@RequestBody RichiestaTurniSedeGiorno richiesta, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("azienda") == null)
            return null;
        Azienda azienda = (Azienda) session.getAttribute("azienda");

        LocalDate data = LocalDate.of(richiesta.getAnno(), richiesta.getMese(), richiesta.getGiorno());
        Sede sede = sedeRep.getSedeByIndirizzoAndAzienda(richiesta.getIndirizzoSede(), azienda);
        if(sede == null)
            return null;
        List<Turno> listaTurni = turnoRep.findTurnoBySedeAndData(sede, data);
        if(listaTurni == null || listaTurni.size() == 0)
            return null;

        RispostaTurniSedeGiorno risposta = new RispostaTurniSedeGiorno(listaTurni.size());

        for(int i=0;i<listaTurni.size();i++){
            List<TurnoUtente> listaTurnoUtente = turnoUtenteRep.getTurnoUtenteByTurno(listaTurni.get(i));
            TurnoUtenteSingolo[] arrayTurniUtente = null;

            if(listaTurnoUtente != null && listaTurnoUtente.size() > 0) {
                arrayTurniUtente = new TurnoUtenteSingolo[listaTurnoUtente.size()];
                for (int j = 0; j < listaTurnoUtente.size(); j++) {
                    arrayTurniUtente[j] = new TurnoUtenteSingolo(listaTurnoUtente.get(j).getUtente().getNome(), listaTurnoUtente.get(j).getUtente().getCognome(), listaTurnoUtente.get(j).getFuoriOrario(),  listaTurnoUtente.get(j).getTrasferta(),  listaTurnoUtente.get(j).getOraInizio(),  listaTurnoUtente.get(j).getOraFine());
                }
            }

            risposta.getTurni()[i] = new TurnoSingolo(listaTurni.get(i).getRuoloID().getRoleName(), listaTurni.get(i).getOraInizio(), listaTurni.get(i).getOraFine(), listaTurni.get(i).getOreScoperte(), arrayTurniUtente);
        }

        return risposta;
    }
}
