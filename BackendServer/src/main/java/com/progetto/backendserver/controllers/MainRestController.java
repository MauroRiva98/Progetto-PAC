package com.progetto.backendserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progetto.backendserver.JSON.ElencoTurni;
import com.progetto.backendserver.JSON.Token;
import com.progetto.backendserver.JSON.TurnoApi;
import com.progetto.backendserver.Utils;
import com.progetto.backendserver.db.models.*;
import com.progetto.backendserver.db.repository.*;
import com.progetto.backendserver.registrazione.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class MainRestController {
    public static final Logger log = LoggerFactory.getLogger(MainRestController.class);
    @Autowired
    private final UtenteRepository utRep;
    @Autowired
    private final AziendaRepository azRep;
    @Autowired
    private final RuoliRepository ruRep;
    @Autowired
    private RuoliUtenteRepository roleUserRep;
    @Autowired
    private MalattiaRepository malattiaRepository;
    @Autowired
    private SedeRepository sedeRepository;
    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private TurnoUtenteRepository turnoUtenteRepository;


    public MainRestController(UtenteRepository utRep, AziendaRepository azRep, RuoliRepository ruRep) {
        this.utRep = utRep;
        this.azRep = azRep;
        this.ruRep = ruRep;
    }

    @PostMapping("/login")
    public Token loginUtente(Login user){
        log.info(user.toString());
        String email = user.getEmail();
        String password = user.getPassword();


        Utente ut = utRep.findUtenteByEmailAndHashPassword(email, Utente.sha256(password));

        if(ut == null) return null;

        List<RuoliUtente> roles = roleUserRep.getRuoloUtenteByEmail(ut.getEmail());

        if(roles.size() == 1 && !roles.get(0).getNomeRuolo().equals("Manager")){
            log.info("OPK ");
            ut.setToken(UUID.randomUUID().toString());
            utRep.save(ut);
            log.info(ut.getToken());
            Token t = new Token(ut.getToken());
            return t;
        }else{
            log.info("Errore, manager o errate");
            return null;
        }

    }

    @PostMapping("/logout")
    String logoutUtente(String token, String email){
        log.info("token " + token);
        log.info("email " + email);
        Utente u = utRep.findUtenteByEmail(email);
        log.info("utente " + u.toString());
        if(u.getToken().equals(token)){
            u.setToken("");
            utRep.save(u);
            log.info("FATTO ");
            return "DONE";
        }else{
            log.info("nOT done ");
            return "ERROR";
        }


    }

    @PostMapping("/register")
    Utente registerAzienda(@RequestBody Utente admin, @RequestBody Azienda azienda) {
        if (checkAziendaForNewRegistration(admin, azienda)) {
            azienda.setRegState(RegistrationStates.ADMIN_REGISTERED);
            azRep.save(azienda);
            return utRep.save(admin);
        } else {
            throw new RuntimeException("We shouldn't really be here, check checkAdminAccessForIdAzienda!");
        }
    }

    @PostMapping("/richiestaMalattia")
    String malattiaRequest(String token, String data_inizio, String data_fine){

        log.info("TOKEN " + token);

        String[] component = data_inizio.split("/");

        LocalDate start = LocalDate.of(Integer.parseInt(component[2]), Integer.parseInt(component[1]), Integer.parseInt(component[0]));
        component = data_fine.split("/");
        LocalDate end = LocalDate.of(Integer.parseInt(component[2]), Integer.parseInt(component[1]), Integer.parseInt(component[0]));

        if(end.isBefore(start)){
            return "ERROR";
        }

        Utente u = utRep.findUtenteByToken(token);
        if(u == null) return "ERRORE";

        List<Malattia> lista_m = malattiaRepository.findMalattiaByUtente(u);

        Malattia malattia = new Malattia(u, start, end);

        // Controllo not overlapping
        if(lista_m.size() != 0 && lista_m != null){
            for (Malattia m: lista_m
                 ) {
                // Se inizia durante e finisce durante
                if( (m.getDataInizio().isAfter(start) || m.getDataInizio().isEqual(start))  && (m.getDataFine().isAfter(end) || m.getDataFine().isEqual(end))) return "ERROR";
                // Se inizia prima e finisce durante
                if(m.getDataInizio().isBefore(start) && ((m.getDataFine().isBefore(end) || m.getDataFine().isEqual(end)) && m.getDataFine().isAfter(start))) return "ERROR";
                // Se inizia durante e finisce dopo
                if( ((m.getDataInizio().isEqual(start) || m.getDataInizio().isAfter(start)) &&m.getDataInizio().isBefore(end)) && m.getDataFine().isAfter(end)) return "ERROR";
            }
        }

        try {
            malattiaRepository.save(malattia);
        }catch (IllegalArgumentException e){
            return  "ERROR";
        }

        return "OK";
    }

    @PostMapping("/richiestaTurni")
    ElencoTurni turniRequest(String token, String data_inizio, String data_fine){
        String[] component = data_inizio.split("/");

        LocalDate start = LocalDate.of(Integer.parseInt(component[2]), Integer.parseInt(component[1]), Integer.parseInt(component[0]));
        component = data_fine.split("/");
        LocalDate end = LocalDate.of(Integer.parseInt(component[2]), Integer.parseInt(component[1]), Integer.parseInt(component[0]));


        if(end.isBefore(start)){
            return null;
        }

        Utente u = utRep.findUtenteByToken(token);
        if(u == null){
            return null;
        }

        Azienda a = u.getAzienda();

        List<Sede> lista_sedi = sedeRepository.findSedeByAzienda(a);

        List<Turno> lista_turni = turnoRepository.findTurnoBySedeInAndDataGreaterThanAndDataLessThanOrderByDataDesc(lista_sedi, start.minusDays(1), end.plusDays(1));


        if(lista_turni == null || lista_turni.size() == 0){
            return null;
        }

        List<TurnoUtente> list_turniUtente = turnoUtenteRepository.findTurnoUtenteByUtenteAndTurnoIn(u, lista_turni);

        if(list_turniUtente == null || list_turniUtente.size() == 0) {
            return null;
        }

        ElencoTurni response = new ElencoTurni(list_turniUtente.size());

        for (int i = 0; i < list_turniUtente.size(); i++){

            TurnoApi ta = new TurnoApi();

            LocalDate data = list_turniUtente.get(i).getTurno().getData();
            ta.setData(data);

            ta.setOra_inizio(list_turniUtente.get(i).getOraInizio());
            ta.setOra_fine(list_turniUtente.get(i).getOraFine());

            ta.setIndirizzo(list_turniUtente.get(i).getTurno().getSede().getIndirizzo());

            ta.setStraordinario(list_turniUtente.get(i).getFuoriOrario());
            ta.setTrasferta(list_turniUtente.get(i).getTrasferta());

            ta.setRuolo(list_turniUtente.get(i).getTurno().getRuoloID().getRoleName());

            response.setTurni(i, ta);

        }

        return response;
    }

    /**
     * Questo metodo viene chiamato in registrazione in modo da poter popolare i ruoli dell'azienda selezionata.
     * Tutti gli altri metodi per accedere alla tabella dei ruoli richiede la presenza dei ruoli, quindi finché la
     * registrazione non verrà completata per la corrispondente azienda, gli altri metodi non funzioneranno.
     *
     * @param idAzienda L'id dell'azienda a cui vogliamo aggiungere i ruoli
     * @param ruoli     Una collezione di ruoli passati da aggiungere all'azienda
     * @param admin     L'utente collegato attualmente di cui useremo solo email e GUID, visto che proviene dal mondo esterno.
     * @return La lista di ruoli aggiunta al database.
     */
    /**
    @PostMapping("/{idAzienda}/roles")
    Collection<Ruoli> registerRoles(@PathVariable(name = "idAzienda") String idAzienda, @RequestBody Collection<Ruoli> ruoli, @RequestBody Utente admin) {
        if (!Objects.equals(admin.getToken(), utRep.getById(admin.getEmail()).getToken())) {
            throw new AdminNotFoundException(admin.getEmail());
        } else if (!azRep.existsById(idAzienda) && utRep.getById(admin.getEmail()).getAzienda().equals(azRep.getById(idAzienda))) {
            throw new AziendaNotFoundException(admin.getAzienda().getPartitaIva());
        } else if (azRep.getById(idAzienda).getRegState() != RegistrationStates.ADMIN_REGISTERED) {
            throw new StateAlreadyCompletedException(azRep.getById(idAzienda));
        } else {
            Collection<Ruoli> accepted = checkRolesValidity(ruoli, admin);
            log.info(Utils.getMethodName() + "I ruoli sono stati salvati.");
            Azienda az = azRep.getById(idAzienda);
            az.setRegState(RegistrationStates.ADMIN_AND_ROLES_REGISTERED);
            azRep.save(az);
            return ruRep.saveAll(accepted);
        }
    }
     **/

    /**
    @GetMapping("/{idAzienda}/roles")
    Collection<Ruoli> getRuoli(@PathVariable(name = "idAzienda") String idAzienda, @RequestBody Utente admin) {
        if (checkAdminAccessForIdAzienda(admin, idAzienda)) {
            return ruRep.getRuoliByRuoloID_Azienda(azRep.getById(idAzienda));
        } else {
            throw new UserNotAdminException(admin);
        }
    }
    **/

    /**
     * Questa call gestisce l'ottenimento di uno specifico ruolo di una specifica azienda.
     *
     * @param idAzienda L'ID dell'azienda corrispondente.
     * @param idRole    L'ID specifico del ruolo desiderato.
     * @param utente    L'utente che richiede i dati specificati sopra.
     * @return Il ruolo specifico, se l'utente è autorizzato ad accederci.
     */
    /**
    @GetMapping("/{idAzienda}/roles/{idRole}")
    Ruoli getSpecificRole(@PathVariable(name = "idAzienda") String idAzienda, @PathVariable(name = "idRole") String idRole, @RequestBody Utente utente) {
        if (completedRegistration(idAzienda)) {
            if (validLogin(utente)) {
                if (checkAdminAccessForIdAzienda(utente, idAzienda)) {
                    log.info(Utils.getMethodName() + "Amministratore rilevato, accesso sempre consentito ai singoli ruoli");
                    return ruRep.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(azRep.getById(idAzienda), idRole);
                } else if (checkEmployeeAccessForSpecificRole(utente, idRole)) {
                    return ruRep.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(utRep.getById(utente.getEmail()).getAzienda(), idRole);
                } else throw new UnauthorizedUserException(utente.getEmail());
            } else throw new UnauthorizedLoginException(utente.getEmail());
        } else throw new IncompleteRegistrationException(idAzienda);
    }
    **/

    //Metodi di supporto per le chiamate rest

    /**
     * Questo metodo controlla se per l'azienda specificata nel parametro è stata completata correttamente il processo di registrazione.
     * Se si, ritorna true. Se no, ritorna false.
     *
     * @param idAzienda L'azienda da verificare
     * @return Il completamento o meno della registrazione.
     */
    private boolean completedRegistration(String idAzienda) {
        return azRep.existsById(idAzienda) && azRep.getById(idAzienda).getRegState() == RegistrationStates.REGISTRATION_COMPLETE;
    }

    /**
     * Questo metodo controlla se l'utente specificato ha almeno un ruolo che gli fornisce l'accesso come amministratore
     * all'interno dei ruoli dell'azienda. Se i ruoli di una specifica azienda non sono presenti, e quindi il suo stato
     * corrispondente è uguale allo stato RegistrationStates.ADMIN_REGISTERED, allora skippo il controllo sullo stesso e
     * aggiungo direttamente i ruoli assumento che l'utente loggato sia l'amministratore dell'azienda stessa.
     * <p>
     * Questo metodo NON controlla la validità del login, ma solo se l'utente specificato ha accesso amministratore.
     *
     * @param ruoli Il set di ruoli da aggiungere.
     * @param admin L'utente passato come parametro al metodo rest.
     * @return La collezione di ruoli aggiunti al db.
     */
    /**
    private Collection<Ruoli> checkRolesValidity(Collection<Ruoli> ruoli, Utente admin) {
        if (utRep.getById(admin.getEmail()).getAzienda().getRegState() == RegistrationStates.ADMIN_REGISTERED) {
            log.info(Utils.getMethodName() + "Non risulta alcun ruolo per l'azienda corrispondente, aggiungo i ruoli all'azienda senza controllare se l'utente loggato è amministratore.");
            return checkPassedRulesForConsistency(ruoli, admin);
        } else {
            log.info(Utils.getMethodName() + "Controllo che l'utente loggato ha accesso amministratore in uno dei suoi ruoli.");
            for (Ruoli admRuolo : utRep.getById(admin.getEmail()).getRuoli()) {
                if (admRuolo.getAdmin()) {
                    return checkPassedRulesForConsistency(ruoli, admin);
                }
            }
            log.debug(Utils.getMethodName() + "L'utente non risulta avere accesso amministratore in nessuno dei suoi gruppi, accesso negato.");
            throw new UtenteNotAdminException(admin.getEmail());
        }
    }
     **/

    /**
     * Questo metodo controlla che per ogni ruolo aggiunto nel set di ruoli passati siano uguali all'azienda specificata
     * nella tabella utente, altrimenti solleva un eccezione.
     *
     * @param ruoli La collezione di ruoli da verificare
     * @param admin L'utente collegato attualmente.
     * @return I ruoli, se gli stessi sono coerenti.
     */
    private Collection<Ruoli> checkPassedRulesForConsistency(Collection<Ruoli> ruoli, Utente admin) {
        Collection<Ruoli> accepted = new ArrayList<>(ruoli.size());
        for (Ruoli ruolo : ruoli) {
            if (ruolo.getRuoloID().getAzienda().equals(utRep.getById(admin.getEmail()).getAzienda())) {
                accepted.add(ruolo);
            } else {
                log.error(Utils.getMethodName() + "Alcuni ruoli passati non appartengono all'azienda dell'utente collegato!");
                throw new UnauthorizedUserException(admin.getEmail());
            }
        }
        log.info(Utils.getMethodName() + "Tutti i ruoli richiesti rispettano le regole imposte.");
        return accepted;
    }

    /**
     * Se l'utente non è amministratore, controlla che lo stesso abbia quindi il ruolo
     * registrato per poterci accedere correttamente.
     *
     * @param utente L'utente loggato.
     * @param idRole Il ruolo richiesto.
     * @return True se ha il ruolo richiesto. False altrimenti.
     */
    /**
    private boolean checkEmployeeAccessForSpecificRole(Utente utente, String idRole) {
        for (Ruoli ruolo : utRep.getById(utente.getEmail()).getRuoli()) {
            if (ruolo == ruRep.getRuoliByRuoloID_AziendaAndRuoloID_RoleName(utRep.getById(utente.getEmail()).getAzienda(), idRole))
                return true;
        }
        return false;
    }
     **/

    private boolean validAzienda(Utente utente) {
        return utRep.existsById(utente.getEmail()) && azRep.existsById(utRep.getById(utente.getEmail()).getAzienda().getPartitaIva());
    }

    private boolean validLogin(Utente utente) {
        return utRep.existsById(utente.getEmail()) && utRep.getById(utente.getEmail()).getToken().equals(utente.getToken());
    }

    /**
    Boolean checkAdminAccessForIdAzienda(Utente admin, String idAzienda) {
        if (!validLogin(admin)) {
            log.debug(Utils.getMethodName() + "Token diversi salvati all'interno del db! Accesso negato.");
            throw new AdminNotFoundException(admin.getEmail());
        } else if (!validAzienda(admin)) {
            log.debug(Utils.getMethodName() + "L'utente non risulta fare parte dell'azienda specificata o l'azienda non esiste! Accesso negato.");
            throw new AziendaNotFoundException(admin.getAzienda().getPartitaIva());
        } else {
            for (Ruoli ruolo : utRep.getById(admin.getEmail()).getRuoli()) {
                if (ruolo.getAdmin()) {
                    return true;
                }
            }
            return false;
        }
    }
     **/

    Boolean checkAziendaForNewRegistration(Utente admin, Azienda azienda) {
        if (utRep.existsById(admin.getEmail())) throw new UserAlreadyPresentException(admin.getEmail());
        else if (azRep.existsById(azienda.getPartitaIva())) {
            throw new AziendaAlreadyPresentException(azienda.getRagioneSociale());
        } else return true;
    }

    private static class UserAlreadyPresentException extends RuntimeException {
        UserAlreadyPresentException(String email) {
            super("A user with email " + email + " already exists!");
        }
    }

    private static class AziendaAlreadyPresentException extends RuntimeException {
        public AziendaAlreadyPresentException(String nomeAzienda) {
            super("Un'azienda con la partita iva uguale all'azienda con il nome " + nomeAzienda + "esiste già!");
        }
    }

    private static class InvalidDipException extends RuntimeException {
        public InvalidDipException(String s) {
            super("I dati sull'utente" + s + "non sono validi!");
        }
    }

    private static class AdminNotFoundException extends RuntimeException {
        public AdminNotFoundException(String email) {
            super("L'admin " + email + " non è stato trovato!");
        }
    }

    private static class AziendaNotFoundException extends RuntimeException {
        public AziendaNotFoundException(String partitaIva) {
            super("L'azienda con partita iva " + partitaIva + "non è stata trovata!");
        }
    }

    private static class UtenteNotAdminException extends RuntimeException {
        public UtenteNotAdminException(String email) {
            super("L'utente con email " + email + " non può creare ruoli!");
        }
    }

    private static class UnauthorizedUserException extends RuntimeException {
        public UnauthorizedUserException(String email) {
            super("L'utente con email " + email + " non è autorizzato ad effettuare l'operazione richiesta!");
        }
    }

    private static class UserNotAdminException extends RuntimeException {
        public UserNotAdminException(Utente admin) {
            super("L'utente con email " + admin.getEmail() + " non è amministratore in nessuno dei suoi ruoli, non può aggiungere ruoli post registrazione azienda!");
        }
    }

    private static class UnauthorizedLoginException extends RuntimeException {
        public UnauthorizedLoginException(String email) {
            super("L'utente con email" + email + "non risulta loggato correttamente!");
        }
    }

    private static class StateAlreadyCompletedException extends RuntimeException {
        public StateAlreadyCompletedException(Azienda azi) {
            super("L'azienda di nome " + azi.getRagioneSociale() + "ha già completato la registrazione dei ruoli, impossibile proseguire!");
        }
    }

    private static class IncompleteRegistrationException extends RuntimeException {
        public IncompleteRegistrationException(String idAzienda) {
            super("L'azienda con email " + idAzienda + "non ha ancora completato la registrazione, accesso negato.");
        }
    }
}