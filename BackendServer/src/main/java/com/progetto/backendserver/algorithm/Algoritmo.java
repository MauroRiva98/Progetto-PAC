package com.progetto.backendserver.algorithm;

import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.models.EmbeddedIDs.SediScambioID;
import com.progetto.backendserver.db.repository.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

@Service
public class Algoritmo {

    private static final int durataTurno = 4;

    private static final double DistanzaMax = 50;

    @Autowired private SedeRepository sedeRep;

    @Autowired private RuoliRepository ruoliRep;

    @Autowired private RuoliUtenteRepository ruoliUtenteRep;

    @Autowired private TurnoRepository turnoRep;

    @Autowired private OrarioRepository orarioRep;

    @Autowired private UtenteRepository utRep;

    @Autowired private StraordinarioRepository straRep;

    @Autowired private TurnoUtenteRepository turnoUtenteRep;

    @Autowired private MalattiaRepository malattiaRep;

    @Autowired private FerieRepository ferieRep;

    @Autowired private VicinanzaSediRepository vicSediRep;

    public boolean calculateBestTurni(Azienda az, int anno, int mese){

        List<Sede> sedi = sedeRep.findSedeByAzienda(az);
        List<Ruoli> ruoli = ruoliRep.getRuoliByRuoloID_Azienda(az);
        Orario orario = orarioRep.findOrarioByAzienda(az);
        boolean assegnamentoCompleto = true;

        HashMap<Sede, List<Turno>> turniPerSede = new HashMap<>();
        HashMap<Utente, Integer> utentiOreMensili = new HashMap<>();
        HashMap<Utente, Integer> utentiOreStraordinario = new HashMap<>();

        //Inizializzo le ore lavorate questo mese
        List <Utente> uteList = utRep.findUtenteByAzienda(az);
        for(Utente u : uteList){
            utentiOreMensili.put(u, 0);
            utentiOreStraordinario.put(u, 0);
        }

        //Calcolo periodo temporale
        LocalDate startTime = LocalDate.of(anno, mese, 1);
        //Se devo calcolare solo la parte restante del mese attuale (non posso modificare il passato!)
        if(startTime.isBefore(LocalDateTime.now().toLocalDate()))
            startTime = LocalDate.now();

        LocalDate endTime = startTime.plusMonths(1);
        endTime = endTime.withDayOfMonth(1);

        //Per ogni sede, recupero tutti i turni disponibili.
        for (Sede sede : sedi) {
            List <Turno> lista = turnoRep.findTurnoBySedeAndDataGreaterThanAndDataLessThanOrderByDataDesc(sede,startTime.minusDays(1), endTime);
            //Rimuovo eventuali turni (con annessi straordinari) già presenti nel database
            for(Turno t : lista){
                if((t.getData().isBefore(ChronoLocalDate.from(endTime)) && t.getData().isAfter(ChronoLocalDate.from(startTime))) || t.getData().isEqual(ChronoLocalDate.from(startTime))){
                    turnoUtenteRep.deleteByTurno(t);
                    straRep.deleteByTurno(t);
                    turnoRep.delete(t);
                }
                else if((t.getData().isBefore(ChronoLocalDate.from(endTime))))
                    break;
            }
            //Creo i turni a cui dovranno essere assegnati i dipendenti
            List <Turno> listaTurni = new ArrayList<>();
            for(Ruoli r : ruoli) {
                if (!r.getAdmin()){
                    LocalDate time = LocalDate.from(startTime);
                    while (time.isBefore(endTime)) {
                        if (orario.isOpenForCurrentDay(time)) {
                            Giorno giorno = orario.getCorrespondingWorkingHours(time);
                            LocalDate data = LocalDate.from(time);
                            int oreAperturaRimaste = giorno.getChiusura() - giorno.getApertura();
                            int apertura = giorno.getApertura();
                            int chiusura;
                            //FIXME:Eventualmente da aggiungere la pausa pranzo
                            while (oreAperturaRimaste > 0) {
                                if (oreAperturaRimaste < durataTurno)
                                    chiusura = apertura + oreAperturaRimaste;
                                else
                                    chiusura = apertura + durataTurno;
                                Turno turno = new Turno(sede, r.getRuoloID(), data, apertura, chiusura);
                                //Definisco le ore-dipendente necessarie per coprire il turno
                                turno.setOreScoperte((chiusura - apertura) * r.getMinRuolo());
                                listaTurni.add(turno);
                                apertura = chiusura;
                                oreAperturaRimaste -= durataTurno;
                            }
                        }
                        time = time.plusDays(1);
                    }
                }
            }
            //E li salvo in una coppia sede:turni.
            turniPerSede.put(sede, listaTurni);

            //Cerco le ore già effettuate dai dipendenti questo mese
            LocalDate st = LocalDate.of(startTime.getYear(), startTime.getMonth(), 1);
            List <Turno> listaTurniQuestoMese = turnoRep.findTurnoBySedeAndDataGreaterThanAndDataLessThanOrderByDataDesc(sede,st.minusDays(1), endTime);
            for(Turno t : listaTurniQuestoMese){
                if(t.getData().getMonthValue() == st.getMonthValue()){
                    List<TurnoUtente> turUt = turnoUtenteRep.getTurnoUtenteByTurno(t);
                    for(TurnoUtente tu : turUt){
                        int ore = tu.getOraFine() - tu.getOraInizio();
                        if(!tu.isFuoriOrario()){
                            Integer oreLav = utentiOreMensili.get(tu.getUtente());
                            oreLav += ore;
                            utentiOreMensili.put(tu.getUtente(), oreLav);
                        }
                        else{
                            Integer oreLav = utentiOreStraordinario.get(tu.getUtente());
                            oreLav += ore;
                            utentiOreStraordinario.put(tu.getUtente(), oreLav);
                        }
                    }
                }
            }
        }

        List<Turno> uncovered = new ArrayList<>();

        //Per ogni sede...
        for (Sede sede: sedi){
            HashMap<Ruoli, List<Utente>> utentiRuoloPrimario = new HashMap<>();
            HashMap<Ruoli, List<Utente>> utentiRuoloSecondario = new HashMap<>();
            HashMap<Ruoli, List<Utente>> utentiRuoloTerziario = new HashMap<>();

            //Creo le liste di priorità per ruolo
            for(Ruoli r : ruoli) {
                if (!r.getAdmin()){
                    List<Utente> listaUtenti = new ArrayList<>();
                    List<RuoliUtente> emailUtenti = ruoliUtenteRep.getEmailByAziendaAndNomeRuolo(az.getPartitaIva(), r.getRuoloID().getRoleName());
                    List<Utente> listaPrimaria = new ArrayList<>();
                    List<Utente> listaSecondaria = new ArrayList<>();
                    List<Utente> listaTerziaria = new ArrayList<>();

                    for(RuoliUtente e : emailUtenti){
                        Utente ut = utRep.findUtenteByEmail(e.getEmail());
                        if(ut.getSede().getIndirizzo().equals(sede.getIndirizzo()))
                            listaUtenti.add(ut);
                    }

                    for (Utente u : listaUtenti) {
                        List<RuoliUtente> lru = ruoliUtenteRep.getRuoloUtenteByEmail(u.getEmail());
                        if (lru.size() == 1)
                            listaPrimaria.add(u);
                        else if (lru.size() > 1) {
                            if (lru.get(0).getNomeRuolo().equals(r.getRuoloID().getRoleName()))
                                listaPrimaria.add(u);
                            else if (lru.get(1).getNomeRuolo().equals(r.getRuoloID().getRoleName()))
                                listaSecondaria.add(u);
                            else if (lru.size() > 2 && lru.get(2).getNomeRuolo().equals(r.getRuoloID().getRoleName()))
                                listaTerziaria.add(u);
                        }
                    }

                    utentiRuoloPrimario.put(r, listaPrimaria);
                    utentiRuoloSecondario.put(r, listaSecondaria);
                    utentiRuoloTerziario.put(r, listaTerziaria);
                }
            }

            //Per ogni turno...
            for(Turno turno : turniPerSede.get(sede)){
                boolean assegnato = false;
                turnoRep.save(turno);
                Ruoli ruolo = ruoliRep.getRuoliByRuoloID(turno.getRuoloID());
                List<Turno> turniDiOggi = turnoRep.findTurnoBySedeAndData(sede, turno.getData());
                //Per ogni utente che ha quel ruolo come primario
                if(utentiRuoloPrimario.get(ruolo) != null) {
                    for (Utente utente : utentiRuoloPrimario.get(ruolo)) {
                        assegnato = assegnaUtente(utente, turniDiOggi, turno, utentiOreMensili);
                        if (assegnato)
                            break;
                    }
                }
                //Se non sono riuscito a completare il turno con gli utenti che hanno quel ruolo come principale
                if(!assegnato){
                    if(utentiRuoloSecondario.get(ruolo) != null) {
                        //Per ogni utente che ha quel ruolo come secondario
                        for (Utente utente : utentiRuoloSecondario.get(ruolo)) {
                            assegnato = assegnaUtente(utente, turniDiOggi, turno, utentiOreMensili);
                            if (assegnato)
                                break;
                        }
                    }
                }
                //Se non sono riuscito a completare il turno con gli utenti che hanno quel ruolo come secondario
                if(!assegnato){
                    if(utentiRuoloTerziario.get(ruolo) != null) {
                        //Per ogni utente che ha quel ruolo come terziario
                        for (Utente utente : utentiRuoloTerziario.get(ruolo)) {
                            assegnato = assegnaUtente(utente, turniDiOggi, turno, utentiOreMensili);
                            if (assegnato)
                                break;
                        }
                    }
                }
                //Se non sono riuscito a completare il turno con il gli utenti che hanno quel ruolo come terziario
                if(!assegnato){
                    uncovered.add(turno);
                }
            }
        }

        //Se ci sono turni scoperti
        if(uncovered.size() > 0){
            //Creo il grafo delle sedi
            SimpleWeightedGraph<Sede, DefaultWeightedEdge> grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
            HashMap<Sede, List<Sede>> mappaSediVicine = new HashMap<>();

            //Se ci sono più sedi le aggiungo al grafo
            if(sedi.size() > 1) {
                List<VicinanzaSedi> listaVicinanza = vicSediRep.findAll();
                for (VicinanzaSedi vc : listaVicinanza) {
                    SediScambioID sediScambio = vc.getSediDiScambio();
                    Sede s1 = sediScambio.getSedeFrom();
                    Sede s2 = sediScambio.getSedeTo();
                    if (!grafo.containsVertex(s1))
                        grafo.addVertex(s1);
                    if (!grafo.containsVertex(s2))
                        grafo.addVertex(s2);
                    Float distanza = vc.getDistanza();
                    DefaultWeightedEdge e = grafo.addEdge(s1, s2);
                    grafo.setEdgeWeight(e, distanza.doubleValue());
                }
                //Calcolo le sedi vicine (entro DistanzaMax) per ogni sede
                for(Sede sede : sedi){
                    List<Sede> listaSediVicine = visitaInAmpiezza(Algoritmo.DistanzaMax, sede, grafo);
                    mappaSediVicine.put(sede, listaSediVicine);
                }
            }

            //Per ogni turno scoperto...
            for(Turno turnoScoperto : uncovered){
                boolean assegnato = false;
                Ruoli ruolo = ruoliRep.getRuoliByRuoloID(turnoScoperto.getRuoloID());
                List<Turno> turniDiOggi = turnoRep.findTurnoBySedeAndData(turnoScoperto.getSede(), turnoScoperto.getData());

                //Trovo tutti quelli che possono svolgere quel ruolo nella stessa sede
                List<RuoliUtente> emailUtenti = ruoliUtenteRep.getEmailByAziendaAndNomeRuolo(az.getPartitaIva(), turnoScoperto.getRuoloID().getRoleName());
                HashMap<Sede, List<Utente>> mappaUtentiRuoloSede = new HashMap<>();

                for(RuoliUtente e : emailUtenti){
                    Utente ut = utRep.findUtenteByEmail(e.getEmail());
                    mappaUtentiRuoloSede.computeIfAbsent(ut.getSede(), k -> new ArrayList<>());
                    mappaUtentiRuoloSede.get(ut.getSede()).add(ut);
                }

                mappaUtentiRuoloSede.computeIfAbsent(turnoScoperto.getSede(), k -> new ArrayList<>());

                //Per ogni utente della sede che può svolgere il ruolo...
                if(mappaUtentiRuoloSede.get(turnoScoperto.getSede()) != null) {
                    for (Utente utente : mappaUtentiRuoloSede.get(turnoScoperto.getSede())) {
                        assegnato = assegnaUtenteStraordinario(utente, turniDiOggi, turnoScoperto, utentiOreStraordinario);
                        if (assegnato)
                            break;
                    }
                }

                //se non sono riuscito a completare il turno facendo fare straordinari ai dipendenti della stessa sede...
                if(!assegnato){
                    List<Sede> sediVicine = mappaSediVicine.get(turnoScoperto.getSede());
                    turniDiOggi = turnoRep.findTurnoByData(turnoScoperto.getData());

                    if(sediVicine != null && sediVicine.size() > 0){
                        //Per ogni sede vicina...
                        for(Sede sede : sediVicine){
                           //Trovo qualcuno che può fare la trasferta
                            if(mappaUtentiRuoloSede.get(sede) != null) {
                                for (Utente utente : mappaUtentiRuoloSede.get(sede)) {
                                    List<TurnoUtente> turniDiOggiUtente = turnoUtenteRep.findTurnoUtenteByUtenteAndTurnoIn(utente, turniDiOggi);
                                    assegnato = assegnaUtenteTrasferta(utente, turniDiOggiUtente, turnoScoperto, utentiOreMensili);
                                    if (assegnato)
                                        break;
                                }
                            }
                            if(assegnato)
                                break;
                        }
                    }
                }
                if(!assegnato)
                    assegnamentoCompleto = false;
            }
        }
        return assegnamentoCompleto;
    }

    private int calcolaOreLavorateGiorno(List<Turno> turniDiOggi, Utente utente){
        int oreLavorateOggi = 0;
        //Calcolo quante ore ha già lavorato oggi
        for(Turno turnoOggi : turniDiOggi){
            List<TurnoUtente> turnoUtenteOggi = turnoUtenteRep.getTurnoUtenteByTurno(turnoOggi);
            for(TurnoUtente tuo : turnoUtenteOggi){
                if(tuo.getUtente().getEmail().equals(utente.getEmail()))
                    oreLavorateOggi += (tuo.getOraFine() - tuo.getOraInizio());
            }
        }
        return oreLavorateOggi;
    }

    private int calcolaOreLavorateGiornoAzienda(List<TurnoUtente> turniDiOggiUtente, Utente utente, Turno turnoScoperto, int[] oreSedeTurno){
        int oreLavorateOggi = 0;
        oreSedeTurno[0] = 0;
        //Calcolo quante ore ha già lavorato oggi su tutte le sedi
        for(TurnoUtente tuo : turniDiOggiUtente){
            if(tuo.getUtente().getEmail().equals(utente.getEmail()))
                oreLavorateOggi += (tuo.getOraFine() - tuo.getOraInizio());
            if(tuo.getTurno().getSede().getIndirizzo().equals(turnoScoperto.getSede().getIndirizzo()))
                oreSedeTurno[0] += (tuo.getOraFine() - tuo.getOraInizio());
        }
        return oreLavorateOggi;
    }

    private boolean controllaConflittoTurni(List<Turno> turniDiOggi, Utente utente, Turno turno){
        //Controllo conflitto su turni già assegnati
        for(Turno t : turniDiOggi){
            List<TurnoUtente> turnoUtenteOggi = turnoUtenteRep.getTurnoUtenteByTurno(t);
            for(TurnoUtente tuo : turnoUtenteOggi){
                if(tuo.getUtente().getEmail().equals(utente.getEmail()))
                    if(controllaSovrapposizioni(tuo, turno))
                        return true;
            }
        }
        if(controllaMalattiaFerie(turno, utente))
            return true;
        return false; //Tutto ok!
    }

    private boolean controllaMalattiaFerie(Turno turno, Utente utente){
        //Controllo malattie e ferie
        List<Malattia> listaMalattia = malattiaRep.findMalattiaByUtente(utente);
        for(Malattia m : listaMalattia){
            if(m.getDataInizio().isBefore(turno.getData()) && m.getDataFine().isAfter(turno.getData()))
                return true;
            if(m.getDataInizio().isEqual(turno.getData()) && m.getDataFine().isAfter(turno.getData()))
                return true;
            if(m.getDataInizio().isBefore(turno.getData()) && m.getDataFine().isEqual(turno.getData()))
                return true;
        }
        List<Ferie> listaFerie = ferieRep.findFerieByUtente(utente);
        for(Ferie f : listaFerie){
            if(f.getDataInizio().isBefore(turno.getData()) && f.getDataFine().isAfter(turno.getData()))
                return true;
            if(f.getDataInizio().isEqual(turno.getData()) && f.getDataFine().isAfter(turno.getData()))
                return true;
            if(f.getDataInizio().isBefore(turno.getData()) && f.getDataFine().isEqual(turno.getData()))
                return true;
        }
        return false; //No ferie o malattia
    }

    private boolean controllaSovrapposizioni(TurnoUtente tu, Turno turno){
        //Se inizia prima e finisce dopo
        if((tu.getOraInizio() < turno.getOraInizio()) && (tu.getOraFine() > turno.getOraFine()))
            return true;
        //Se inizia prima e finisce durante
        if((tu.getOraInizio() < turno.getOraInizio()) && ((tu.getOraFine() <= turno.getOraFine()) && tu.getOraFine() > turno.getOraInizio()))
            return true;
        //Se inizia durante e finisce durante
        if((tu.getOraInizio() >= turno.getOraInizio()) && (tu.getOraFine() <= turno.getOraFine()))
            return true;
        //Se inizia durante e finisce dopo
        if((tu.getOraInizio() >= turno.getOraInizio() && tu.getOraInizio() < turno.getOraFine()) && (tu.getOraFine() > turno.getOraFine()))
            return true;
        return false; //Non ci sono sovrapposizioni
    }

    private boolean assegnaTurno(Turno turno, Utente utente){
        turno.setOreScoperte(turno.getOreScoperte() - (turno.getOraFine() - turno.getOraInizio()));
        TurnoUtente turnUte = new TurnoUtente(utente, turno, turno.getOraInizio(), turno.getOraFine());
        turnoUtenteRep.save(turnUte);
        turnoRep.save(turno);
        if(turno.getOreScoperte() == 0)
            return true; //Turno completamente coperto
        return false;
    }

    private boolean assegnaUtente(Utente utente, List<Turno> turniDiOggi, Turno turno, HashMap<Utente, Integer> utentiOreMensili){
        boolean assegnato = false;
        int oreLavorateOggi = calcolaOreLavorateGiorno(turniDiOggi, utente);
        if(!utente.isPartTime()){
            //Se può coprire interamente il turno
            if((8 - oreLavorateOggi) >= (turno.getOraFine() - turno.getOraInizio())){
                //Se non ci sono conflitti
                if(!controllaConflittoTurni(turniDiOggi, utente, turno)){
                    assegnato = assegnaTurno(turno, utente);
                    utentiOreMensili.put(utente,utentiOreMensili.get(utente) + (turno.getOraFine() - turno.getOraInizio()));
                }
            }
        }
        else{
            //Se può coprire interamente il turno
            if((4 - oreLavorateOggi) >= (turno.getOraFine() - turno.getOraInizio())){
                //Se non ci sono conflitti
                if(!controllaConflittoTurni(turniDiOggi, utente, turno)){
                    assegnato = assegnaTurno(turno, utente);
                    utentiOreMensili.put(utente,utentiOreMensili.get(utente) + (turno.getOraFine() - turno.getOraInizio()));
                }
            }
        }
        return assegnato;
    }

    private boolean assegnaUtenteStraordinario(Utente utente, List<Turno> turniDiOggi, Turno turnoScoperto, HashMap<Utente, Integer> utentiOreStraordinario){
        boolean assegnato = false;
        int oreLavorateOggi = calcolaOreLavorateGiorno(turniDiOggi, utente);
        int oraInizio = turnoScoperto.getOraInizio();

        //Trovo se ci sono altri straordinari su quel turno
        List<TurnoUtente> listaTurniUtente = turnoUtenteRep.findTurnoUtenteByTurnoAndFuoriOrario(turnoScoperto, true);
        if(listaTurniUtente.size() > 0){
            //Prendo l'ultimo
            int oraFine = listaTurniUtente.get(listaTurniUtente.size() - 1).getOraFine();
            //Se è finito prima del termine del turno
            if(oraFine != turnoScoperto.getOraFine())
                oraInizio = oraFine;
        }

        if(!utente.isPartTime()){
            if(oreLavorateOggi < 10){
                //Se non ci sono conflitti
                if(!controllaConflittoTurniStraordinario(turniDiOggi, utente, turnoScoperto, oraInizio)){
                    int oreDisponibili = 10 - oreLavorateOggi;
                    //Se le ore disponibili superano la durata del turno
                    if(oreDisponibili > (turnoScoperto.getOraFine() - oraInizio))
                        oreDisponibili = turnoScoperto.getOraFine() - oraInizio;
                    //Se non ho raggiunto il limite mensile di straordinari
                    if(utentiOreStraordinario.get(utente) <= (10 - oreDisponibili)){
                        assegnato = assegnaStraordinario(turnoScoperto, utente, oreDisponibili, oraInizio);
                        utentiOreStraordinario.put(utente,utentiOreStraordinario.get(utente) + oreDisponibili);
                    }
                }
            }
        }
        else{
            if(oreLavorateOggi < 5){
                //Se non ci sono conflitti
                if(!controllaConflittoTurni(turniDiOggi, utente, turnoScoperto)){
                    int oreDisponibili = 5 - oreLavorateOggi;
                    //Se le ore disponibili superano la durata del turno
                    if(oreDisponibili > (turnoScoperto.getOraFine() - oraInizio))
                        oreDisponibili = turnoScoperto.getOraFine() - oraInizio;
                    if(utentiOreStraordinario.get(utente) <= (5 - oreDisponibili)){
                        assegnato = assegnaStraordinario(turnoScoperto, utente, oreDisponibili, oraInizio);
                        utentiOreStraordinario.put(utente,utentiOreStraordinario.get(utente) + oreDisponibili);
                    }
                }
            }
        }
        return assegnato;
    }

    private boolean assegnaStraordinario(Turno turnoScoperto, Utente utente, int oreDisponibili, int oraInizio){
        turnoScoperto.setOreScoperte(turnoScoperto.getOreScoperte() - oreDisponibili);

        Straordinario stra = new Straordinario(utente, turnoScoperto, turnoScoperto.getData(), oreDisponibili);
        straRep.save(stra);

        TurnoUtente turnUte = new TurnoUtente(utente, turnoScoperto, stra, oraInizio, (oraInizio + oreDisponibili));
        turnoUtenteRep.save(turnUte);
        turnoRep.save(turnoScoperto);
        if(turnoScoperto.getOreScoperte() == 0)
            return true; //Turno completamente coperto
        return false;
    }

    private boolean controllaConflittoTurniStraordinario(List<Turno> turniDiOggi, Utente utente, Turno turno, int oraInizio){
        //Controllo conflitto su turni già assegnati
        for(Turno t : turniDiOggi){
            List<TurnoUtente> turnoUtenteOggi = turnoUtenteRep.getTurnoUtenteByTurno(t);
            for(TurnoUtente tuo : turnoUtenteOggi){
                if(tuo.getUtente().getEmail().equals(utente.getEmail()))
                    if(controllaSovrapposizioniStraordinario(tuo, turno, oraInizio))
                        return true;
            }
        }
        if(controllaMalattiaFerie(turno, utente))
            return true;
        return false; //Tutto ok!
    }

    private boolean controllaSovrapposizioniStraordinario(TurnoUtente tu, Turno turno, int oraInizio){
        //Se inizia prima e finisce dopo
        if((tu.getOraInizio() < oraInizio) && (tu.getOraFine() > turno.getOraFine()))
            return true;
        //Se inizia prima e finisce durante
        if((tu.getOraInizio() < oraInizio) && ((tu.getOraFine() <= turno.getOraFine()) && tu.getOraFine() > oraInizio))
            return true;
        //Se inizia durante e finisce durante
        if((tu.getOraInizio() >= oraInizio) && (tu.getOraFine() <= turno.getOraFine()))
            return true;
        //Se inizia durante e finisce dopo
        if((tu.getOraInizio() >= oraInizio && tu.getOraInizio() < turno.getOraFine()) && (tu.getOraFine() > turno.getOraFine()))
            return true;
        return false; //Non ci sono sovrapposizioni
    }

    private List<Sede> visitaInAmpiezza(double distanzaMax, Sede sede, SimpleWeightedGraph<Sede, DefaultWeightedEdge> grafo){
        List<Sede> listaSediVicine = new ArrayList<>();
        HashMap<Sede, Boolean> marcature = new HashMap<>();
        HashMap<Sede, Double> distanze = new HashMap<>();
        Queue<Sede> coda = new LinkedList<>();

        marcature.put(sede, Boolean.TRUE);
        coda.add(sede);
        distanze.put(sede, (double) 0);

        while(!coda.isEmpty()){
            Sede verticeU = coda.poll();
            Set<DefaultWeightedEdge> listaEdge = grafo.edgesOf(verticeU);
            boolean marc = false;
            for(DefaultWeightedEdge arco : listaEdge){
                Sede verticeV = grafo.getEdgeTarget(arco);
                if(verticeU.getIndirizzo().equals(verticeV.getIndirizzo()))
                    verticeV = grafo.getEdgeSource(arco);
                //Se verticeV non è presente nella hashMap
                if(marcature.get(verticeV) == null) {
                    marcature.put(verticeV, true);
                    marc = true;
                }
                if(marc || !marcature.get(verticeV)){
                    if(!marc)
                        marcature.put(verticeV, true);
                    coda.add(verticeV);
                    //Se il verticeU è quello iniziale...
                    if(verticeU.getIndirizzo().equals(sede.getIndirizzo()))
                        distanze.put(verticeV, grafo.getEdgeWeight(arco));
                    else
                        distanze.put(verticeV, grafo.getEdgeWeight(arco) + distanze.get(verticeU));
                    if(distanzaMax >= distanze.get(verticeV)) {
                        listaSediVicine.add(verticeV);
                        //Metto il minimo al primo posto della lista
                        if(listaSediVicine.size() > 1){
                            if(distanze.get(listaSediVicine.get(0)) > distanze.get(verticeV))
                                Collections.swap(listaSediVicine, 0, listaSediVicine.indexOf(verticeV));
                        }
                    }
                }
            }
        }
        //Ordino la lista
        return ordinaListaDistanza(listaSediVicine, distanze);
    }

    private List<Sede> ordinaListaDistanza(List<Sede> sedi, HashMap<Sede, Double> distanze){
        //SelectionSort
        for(int i = 1;i < sedi.size();i++){
            Sede minimo = sedi.get(i);
            for(int j = i+1;j < sedi.size();j++){
                if(distanze.get(minimo) > distanze.get(sedi.get(j))){
                    Sede temp = sedi.get(j);
                    Collections.swap(sedi, sedi.indexOf(minimo), j);
                    minimo = temp;
                }
            }
        }
        return sedi;
    }

    private boolean assegnaUtenteTrasferta(Utente utente, List<TurnoUtente> turniDiOggiUtente, Turno turnoScoperto, HashMap<Utente,Integer> utentiOreMensili){
        boolean assegnato = false;
        int[] oreSedeTurno = {0}; //Per passarlo alla funzione tramite rifermento

        int oreLavorateOggi = calcolaOreLavorateGiornoAzienda(turniDiOggiUtente, utente, turnoScoperto, oreSedeTurno);

        //Se l'utente ha lavorato solo nella sede dove è in trasferta: non è permesso fare turni in sedi diverse lo stesso giorno
        if(oreLavorateOggi == oreSedeTurno[0]) {
            if (!utente.isPartTime()) {
                //Se può coprire interamente il turno
                if ((8 - oreLavorateOggi) >= (turnoScoperto.getOraFine() - turnoScoperto.getOraInizio())) {
                    //Se non ci sono conflitti
                    if (!controllaConflittoTurniTrasferta(turniDiOggiUtente, utente, turnoScoperto)) {
                        assegnato = assegnaTurnoTrasferta(turnoScoperto, utente);
                        utentiOreMensili.put(utente, utentiOreMensili.get(utente) + (turnoScoperto.getOraFine() - turnoScoperto.getOraInizio()));
                    }
                }
            } else {
                //Se può coprire interamente il turno
                if ((4 - oreLavorateOggi) >= (turnoScoperto.getOraFine() - turnoScoperto.getOraInizio())) {
                    //Se non ci sono conflitti
                    if (!controllaConflittoTurniTrasferta(turniDiOggiUtente, utente, turnoScoperto)) {
                        assegnato = assegnaTurnoTrasferta(turnoScoperto, utente);
                        utentiOreMensili.put(utente, utentiOreMensili.get(utente) + (turnoScoperto.getOraFine() - turnoScoperto.getOraInizio()));
                    }
                }
            }
        }
        return assegnato;
    }

    private boolean controllaConflittoTurniTrasferta(List<TurnoUtente> turniDiOggiUtente, Utente utente, Turno turno){
        //Controllo conflitto su turni già assegnati
        for(TurnoUtente tuo : turniDiOggiUtente){
            if(tuo.getUtente().getEmail().equals(utente.getEmail()))
                if(controllaSovrapposizioni(tuo, turno))
                    return true;
        }
        if(controllaMalattiaFerie(turno, utente))
            return true;
        return false; //Tutto ok!
    }

    private boolean assegnaTurnoTrasferta(Turno turno, Utente utente){
        turno.setOreScoperte(turno.getOreScoperte() - (turno.getOraFine() - turno.getOraInizio()));
        TurnoUtente turnUte = new TurnoUtente(utente, turno, turno.getOraInizio(), turno.getOraFine(), true);
        turnoUtenteRep.save(turnUte);
        turnoRep.save(turno);
        if(turno.getOreScoperte() == 0)
            return true; //Turno completamente coperto
        return false;
    }

}
