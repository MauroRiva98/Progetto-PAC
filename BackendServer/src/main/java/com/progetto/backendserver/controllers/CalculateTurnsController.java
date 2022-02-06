package com.progetto.backendserver.controllers;

import com.progetto.backendserver.algorithm.Algoritmo;
import com.progetto.backendserver.db.models.Azienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

@RestController
public class CalculateTurnsController {

    @Autowired
    private Algoritmo algo;

    @Transactional
    @PostMapping("/ricalcolaTurni")
    public int calcolaTurni(@RequestBody String meseAnno, HttpServletRequest request) throws ParseException, IOException {
        try{
            HttpSession session = request.getSession(false);
            if(session == null || session.getAttribute("azienda") == null)
                return 0;
            Azienda azienda = (Azienda) session.getAttribute("azienda");
            String[] temp = meseAnno.split("\"");
            String[] s = temp[1].split(";");
            int mese = Integer.parseInt(s[0]);
            int anno = Integer.parseInt(s[1]);

            if(anno < LocalDate.now().getYear() || (anno == LocalDate.now().getYear() && mese < LocalDate.now().getMonthValue()))
                throw new Exception("Data nel passato!");

            boolean allocazioneCompleta = algo.calculateBestTurni(azienda, anno, mese);

            if(allocazioneCompleta)
                return 2; //Allocazione completa

            return 1; //Allocazione incompleta
        }catch (Exception e){
            System.out.println(e.toString());
            return 0; //Errore
        }
    }
}
