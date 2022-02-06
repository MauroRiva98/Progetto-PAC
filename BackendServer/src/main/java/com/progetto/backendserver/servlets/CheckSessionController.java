package com.progetto.backendserver.servlets;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class CheckSessionController {
    @PostMapping("/checksession")
    public int checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) //se non è presente una sessione utente
            return 0;
        else if (session.getAttribute("email") == null) //se è presente una sessione ma non c'è la PK dell'utente
            return 0;
        else return 1;
    }
}
