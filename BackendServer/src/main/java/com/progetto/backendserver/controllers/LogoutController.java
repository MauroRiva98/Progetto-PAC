package com.progetto.backendserver.controllers;

import com.progetto.backendserver.db.models.Utente;
import com.progetto.backendserver.db.repository.UtenteRepository;
import com.progetto.backendserver.registrazione.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LogoutController {
    @Autowired
    private UtenteRepository utenteRep;

    @PostMapping("/logoutPagina")
    public int logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null)
            return 0;
        session.invalidate();
        return 1;
    }
}
