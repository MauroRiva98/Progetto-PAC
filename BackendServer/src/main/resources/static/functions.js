function bodyLoad() {
    $.post("/checksession", function (result, status) {
        if (result == 0) { //No sessione
		//if(result == 1){
            $("#div_main").html("<div id=\"div_start\" class=\" h-100 d-flex justify-content-center align-items-center\">\n" +
                "\t\t\t<div id=\"div_start_box\">\n" +
                "\t\t\t\t<h2 class=\"unselectable\" style=\"margin-left:40px; margin-right:40px; margin-top:20px;\">Welcome!</h2>\n" +
                "\t\t\t\t<br><br><br>\n" +
                "\t\t\t\t<button type=\"button\" class=\"btn btn-primary\" onclick=\"openLogin()\" style=\"margin-left:5px; margin-bottom: 10px; width:45%;\">Login</button>\n" +
                "\t\t\t\t<button type=\"button\" class=\"btn btn-primary\" onclick=\"openRegistration()\" style=\"margin-left:8px; margin-bottom: 10px; width:45%;\">Registrati</button>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>");
        } else {
            //TODO se utente è già loggato
			openMainPage();
        }
    });
}

var idAlert = 0;

function showAlert(testo, tipo) { //tipo = 0 errore, tipo = 1 successo
    idAlert++;
    var id = idAlert;
    if (!tipo)
        $("body").append("<div id=\"div_alert" + id + "\" class=\"alert alert-danger alert-dismissible unselectable center-block\">\
						  <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\
						  <strong>Errore: </strong>" + testo + "\
						</div>");
    else
        $("body").append("<div id=\"div_alert" + id + "\" class=\"alert alert-success alert-dismissible unselectable center-block\">\
							  <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\
							  <strong>Successo: </strong>" + testo + "\
						</div>");
    setTimeout(function () {
        $("#div_alert" + id).alert("close");
    }, 5000);
}

function openLogin() {
    $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">LOGIN</h2>\
	<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
	  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"email\">Email:</label>\
	  <input type=\"email\" class=\"form-control\" id=\"email_login\">\
	</div>\
	<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
	  <label class=\"unselectable\" for=\"pwd\">Password:</label>\
	  <input type=\"password\" class=\"form-control\" id=\"password_login\">\
	</div>\
	<div class=\"col text-center\">\
      <button type=\"button\" class=\"btn btn-primary\" style=\"margin-bottom:10px;\" onclick=\"submitLogin()\">Accedi</button>\
    </div>");
}

function submitLogin() {
    if ($("#email_login").val() != "" && $("#password_login").val() != "") {
		var login = {
			"email": $("#email_login").val(),
			"password": $("#password_login").val()
		}
		$.ajax({
        url: "/login",
        type: 'POST',
        data: JSON.stringify(login),
        dataType: "html",
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (result) {
			if(result == 0)
				showAlert("Login fallito!", 0);
			else if(result == -1)
				showAlert("Credenziali errate!", 0);
			else if(result == 2){
				//showAlert("Login riuscito!", 1);
				setTimeout(function () {
					openMainPage();
				}, 500);
			}
			else if(result == 1){
				showAlert("Accesso non consentito ai dipendenti!", 0);
				logout();
			}
		}
		});
    } else
        showAlert("Inserisci una email e una password!", 0);
}

function logout(){
	$.ajax({
		url: "/logoutPagina",
		type: 'POST',
		success: function (r) {
			if(r == 1)
				setTimeout(function () {
					location.reload();
				}, 1500);							
			else
				showAlert("Errore logout!", 0);
		}
	});
}

function openRegistration() {
    $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">REGISTRAZIONE</h2>\
								<div class=\"progress\" style=\"width:84%; margin-left:8%;\">\
								  <div id=\"progress_bar\" class=\"progress-bar progress-bar-striped progress-bar-animated\" style=\"width:0%\">0%</div>\
								</div>\
								<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"email\">Email:</label>\
									  <input type=\"email\" class=\"form-control\" id=\"email_registrazione\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" for=\"pwd\">Password:</label>\
									  <input type=\"password\" class=\"form-control\" id=\"password1_registrazione\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" for=\"pwd\">Conferma password:</label>\
									  <input type=\"password\" class=\"form-control\" id=\"password2_registrazione\">\
									</div>\
									<div class=\"col text-center\">\
									  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-bottom:10px;\" onclick=\"nextRegistration(1)\">Avanti</button>\
								</div>");
}

var email, psw, nome, cognome, partitaIVA, ragioneSociale, suffissoEmail, sedeHQ;
var numeroRuoli = 0;
var ruoli = [];
var dipendenti = [];
var numeroDipendenti = 0;
var orario;

function nextRegistration(fase) {
    switch (fase) {
        case 1:
            if ($("#email_registrazione").val() != "" && $("#password1_registrazione").val() != "" && $("#password2_registrazione").val() != "") {
                if ($("#email_registrazione").val().includes('@') && $("#email_registrazione").val().includes('.') && $("#email_registrazione").val().length >= 5) {
                    if ($("#password1_registrazione").val() == $("#password2_registrazione").val()) {
                        email = $("#email_registrazione").val();
                        psw = $("#password1_registrazione").val();
                        $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">REGISTRAZIONE</h2>\
									<div class=\"progress\" style=\"width:84%; margin-left:8%;\">\
									  <div id=\"progress_bar\" class=\"progress-bar progress-bar-striped progress-bar-animated\" style=\"width:20%\">20%</div>\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"nome\">Nome:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"nome_registrazione\">\
										</div>\
										<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" for=\"cognome\">Cognome:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"cognome_registrazione\">\
										</div>\
										<div class=\"col text-center\">\
										  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-bottom:10px;\" onclick=\"nextRegistration(2)\">Avanti</button>\
									</div>");
                    } else
                        showAlert("Password diverse!", 0);
                } else
                    showAlert("Formato email non corretto!", 0);
            } else
                showAlert("Compila tutti i campi!", 0);
            break;
        case 2:
            if ($("#nome_registrazione").val() != "" && $("#cognome_registrazione").val() != "") {
                nome = $("#nome_registrazione").val();
                cognome = $("#cognome_registrazione").val();
                $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">REGISTRAZIONE</h2>\
								<div class=\"progress\" style=\"width:84%; margin-left:8%;\">\
								  <div id=\"progress_bar\" class=\"progress-bar progress-bar-striped progress-bar-animated\" style=\"width:40%\">40%</div>\
								</div>\
								<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"partitaIVA\">Partita IVA:</label>\
									  <input type=\"text\" class=\"form-control\" id=\"partitaIVA_registrazione\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" for=\"ragioneSociale\">Ragione sociale:</label>\
									  <input type=\"text\" class=\"form-control\" id=\"ragione_sociale_registrazione\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										<label class=\"unselectable\" for=\"suffissoEmail\">Suffisso email aziendale:</label>\
										<div class=\"input-group mb-3\">\
											<div class=\"input-group-prepend\">\
											  <span class=\"input-group-text\">@</span>\
											</div>\
											<input type=\"text\" class=\"form-control\" id=\"email_aziendale_registrazione\">\
										  </div>\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" for=\"sedeHQ\">Indirizzo sede centrale:</label>\
									  <input type=\"text\" class=\"form-control\" id=\"sedeHQ_registrazione\">\
									</div>\
									<div class=\"col text-center\">\
									  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-bottom:10px;\" onclick=\"nextRegistration(3)\">Avanti</button>\
								</div>");
            } else
                showAlert("Compila tutti i campi!", 0);
            break;
        case 3:
            if ($("#partitaIVA_registrazione").val() != "" && $("#ragione_sociale_registrazione").val() != "" && $("#email_aziendale_registrazione").val() != "" && $("#sedeHQ_registrazione").val() != "") {
                if ($("#email_aziendale_registrazione").val().includes('.') && !$("#email_aziendale_registrazione").val().includes('@') && $("#email_aziendale_registrazione").val().length >= 3) {
                    partitaIVA = $("#partitaIVA_registrazione").val();
                    ragioneSociale = $("#ragione_sociale_registrazione").val();
                    suffissoEmail = $("#email_aziendale_registrazione").val();
                    sedeHQ = $("#sedeHQ_registrazione").val();
                    numeroRuoli = 1;
                    $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">REGISTRAZIONE</h2>\
									<div class=\"progress\" style=\"width:84%; margin-left:8%;\">\
									  <div id=\"progress_bar\" class=\"progress-bar progress-bar-striped progress-bar-animated\" style=\"width:60%\">60%</div>\
									</div>\
									<div name=\"div_ruolo_registrazione\" id=\"div_ruolo" + numeroRuoli + "_registrazione\" class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"ruolo\">Ruolo " + numeroRuoli + ":</label>\
										  <input name=\"ruolo_registrazione\" type=\"text\" class=\"form-control\" id=\"ruolo" + numeroRuoli + "_registrazione\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"ruolo_nDipendenti\">Numero dipendenti necessari:</label>\
										  <input name=\"numero_dipendenti_registrazione\" type=\"number\" class=\"form-control\" min=\"1\" id=\"numero_dipendenti" + numeroRuoli + "_registrazione\">\
										</div>\
										<div id=\"div_button_registrazione1\" class=\"col text-center btn-group\">\
										  <button type=\"button\" class=\"btn btn-success\" onclick=\"plusButtonRuolo()\">+</button>\
										  <button type=\"button\" class=\"btn btn-danger\" onclick=\"minusButtonRuolo()\">-</button>\
										</div>\
										<div id=\"div_button_registrazione2\" class=\"col text-center\">\
										  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-top: 10px; margin-bottom:10px;\" onclick=\"nextRegistration(4)\">Avanti</button>\
									</div>");
                } else
                    showAlert("Formato dell'email aziendale non corretto!", 0);
            } else
                showAlert("Compila tutti i campi!", 0);
            break;
        case 4:
            if ($("#ruolo" + numeroRuoli + "_registrazione").val() != "" && $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() != "" && $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() >= 1 && Math.floor($("#numero_dipendenti" + numeroRuoli + "_registrazione").val()) == $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() && $.isNumeric($("#numero_dipendenti" + numeroRuoli + "_registrazione").val())) {
                var nomeRuoloUguale = false;
                for (var i = numeroRuoli - 1; (i > 0 && !nomeRuoloUguale); i--) {
                    if ($("#ruolo" + numeroRuoli + "_registrazione").val().toLowerCase() == $("#ruolo" + i + "_registrazione").val().toLowerCase())
                        nomeRuoloUguale = true;
                }
                if (!nomeRuoloUguale) {
                    for (var i = 1; i <= numeroRuoli; i++) {
                        var recordRuolo = {
                            "nomeRuolo": $("#ruolo" + i + "_registrazione").val(),
                            "numeroDipendenti": $("#numero_dipendenti" + i + "_registrazione").val()
                        }
                        ruoli.push(recordRuolo);
                    }
                    numeroDipendenti = 1;
                    $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">REGISTRAZIONE</h2>\
									<div class=\"progress\" style=\"width:84%; margin-left:8%;\">\
									  <div id=\"progress_bar\" class=\"progress-bar progress-bar-striped progress-bar-animated\" style=\"width:80%\">80%</div>\
									</div>\
									<div name=\"div_dipendente_registrazione\" id=\"div_dipendente" + numeroDipendenti + "_registrazione\" class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteNumero\">Dipendente " + numeroDipendenti + ":</label><br>\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteNome\">Nome:</label>\
										  <input name=\"nome_dipendente_registrazione\" type=\"text\" class=\"form-control\" id=\"nome_dipendente" + numeroDipendenti + "_registrazione\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteCognome\">Cognome:</label>\
										  <input name=\"cognome_dipendente_registrazione\" type=\"text\" class=\"form-control\"id=\"cognome_dipendente" + numeroDipendenti + "_registrazione\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteRuoli\">Ruoli:</label><br>\
										  <select class=\"select_ruoli_dipendente_registrazione\" name=\"select_ruoli_dipendente_registrazione\" id=\"select1_ruoli_dipendente" + numeroDipendenti + "_registrazione\" onchange=\"attivaRuoloDipendente(2)\">\
											<option value=\"\">-</option>\
										  </select><br>\
										  <select class=\"select_ruoli_dipendente_registrazione\" name=\"select_ruoli_dipendente_registrazione\" id=\"select2_ruoli_dipendente" + numeroDipendenti + "_registrazione\" disabled onchange=\"attivaRuoloDipendente(3)\">\
											<option value=\"\">-</option>\
										  </select><br>\
										  <select class=\"select_ruoli_dipendente_registrazione\" name=\"select_ruoli_dipendente_registrazione\" id=\"select3_ruoli_dipendente" + numeroDipendenti + "_registrazione\" disabled onchange=\"controlloRuoloDipendente(3)\">\
											<option value=\"\">-</option>\
										  </select>\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendentePartTime\">Part time:</label>\
										  <input name=\"part_time_dipendente_registrazione\" type=\"checkbox\" style=\"margin-left: 10px;\" id=\"part_time_dipendente" + numeroDipendenti + "_registrazione\">\
										</div>\
										<div id=\"div_button_registrazione1\" class=\"col text-center btn-group\">\
										  <button type=\"button\" class=\"btn btn-success\" onclick=\"plusButtonDipendente()\">+</button>\
										  <button type=\"button\" class=\"btn btn-danger\" onclick=\"minusButtonDipendente()\">-</button>\
										</div>\
										<div id=\"div_button_registrazione2\" class=\"col text-center\">\
										  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-top: 10px; margin-bottom:10px;\" onclick=\"nextRegistration(5)\">Avanti</button>\
									</div>");
                    for (var i = 0; i < numeroRuoli; i++) {
                        $("#select1_ruoli_dipendente" + numeroDipendenti + "_registrazione").append("<option value=\"" + ruoli[i]["nomeRuolo"] + "\">" + ruoli[i]["nomeRuolo"] + "</option>");
                    }
                } else
                    showAlert("Non ci possono essere 2 ruoli con lo stesso nome!", 0);
            } else if ($("#numero_dipendenti" + numeroRuoli + "_registrazione").val() < 1)
                showAlert("Almeno un dipendente necessario per ruolo!", 0);
            else if (Math.floor($("#numero_dipendenti" + numeroRuoli + "_registrazione").val()) != $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() || !$.isNumeric($("#numero_dipendenti" + numeroRuoli + "_registrazione").val()))
                showAlert("Inserire un numero intero!", 0);
            else
                showAlert("Compila tutti i campi!", 0);
            break;
        case 5:
            if ($("#nome_dipendente" + numeroDipendenti + "_registrazione").val() != "" && $("#cognome_dipendente" + numeroDipendenti + "_registrazione").val() != "" && ($("#select1_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "" || $("#select2_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "" || $("#select3_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "")) {
                for (var i = 1; i <= numeroDipendenti; i++) {
                    var ruoliDipendete = [];
                    for (var j = 1; j <= 3; j++) {
                        if ($("#select" + j + "_ruoli_dipendente" + i + "_registrazione").val() != "")
                            ruoliDipendete.push($("#select" + j + "_ruoli_dipendente" + i + "_registrazione").val());
                    }
                    var pt = false;
                    if ($("#part_time_dipendente" + i + "_registrazione").is(":checked"))
                        pt = true;
                    var recordDipendente = {
                        "nomeDipendente": $("#nome_dipendente" + i + "_registrazione").val(),
                        "cognomeDipendente": $("#cognome_dipendente" + i + "_registrazione").val(),
                        "ruoliDipendente": ruoliDipendete,
                        "partTime": pt
                    }
                    dipendenti.push(recordDipendente);
                }
                $("#div_start_box").html("<h2 class=\"unselectable text-center\" style=\"margin-top:20px;\">REGISTRAZIONE</h2>\
								<div class=\"progress\" style=\"width:84%; margin-left:8%;\">\
								  <div id=\"progress_bar\" class=\"progress-bar progress-bar-striped progress-bar-animated\" style=\"width:100%\">100%</div>\
								</div>\
								<div name=\"div_orario_registrazione\" id=\"div_orario_registrazione\" class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"orario\">Orario di apertura:</label><br>\
									  <div class=\"input-group\ select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Lunedì</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_11_registrazione\" onchange=\"controlloInserimentoOrario(1,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_12_registrazione\" onchange=\"controlloInserimentoOrario(1,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									  <div class=\"input-group select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Martedì</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_21_registrazione\" onchange=\"controlloInserimentoOrario(2,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_22_registrazione\" onchange=\"controlloInserimentoOrario(2,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									  <div class=\"input-group\ select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Mercoledì</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_31_registrazione\" onchange=\"controlloInserimentoOrario(3,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_32_registrazione\" onchange=\"controlloInserimentoOrario(3,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									  <div class=\"input-group\ select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Giovedì</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_41_registrazione\" onchange=\"controlloInserimentoOrario(4,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_42_registrazione\" onchange=\"controlloInserimentoOrario(4,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									  <div class=\"input-group\ select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Venerdì</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_51_registrazione\" onchange=\"controlloInserimentoOrario(5,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_52_registrazione\" onchange=\"controlloInserimentoOrario(5,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									  <div class=\"input-group\ select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Sabato</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_61_registrazione\" onchange=\"controlloInserimentoOrario(6,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_62_registrazione\" onchange=\"controlloInserimentoOrario(6,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									  <div class=\"input-group\ select_orario\">\
										<div class=\"input-group-prepend\">\
										  <span class=\"input-group-text span_orario\">Domenica</span>\
										</div>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_71_registrazione\" onchange=\"controlloInserimentoOrario(7,1)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
										<select name=\"select_orario_registrazione\" id=\"select_orario_72_registrazione\" onchange=\"controlloInserimentoOrario(7,2)\">\
											<option value=\"\">Chiuso</option>\
										</select>\
									  </div>\
									<div id=\"div_button_registrazione2\" class=\"col text-center\">\
									  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-top: 10px; margin-bottom:10px;\" onclick=\"submitRegistration()\">Avanti</button>\
								</div>");
                for (var i = 1; i <= 7; i++) {
                    for (var j = 0; j <= 24; j++) {
                        $("#select_orario_" + i + "1_registrazione").append("<option value=\"" + j + "\">" + j + "</option>");
                        $("#select_orario_" + i + "2_registrazione").append("<option value=\"" + j + "\">" + j + "</option>");
                    }
                }
            } else
                showAlert("Compila tutti i campi obbligatori!", 0);
            break;
    }
}

function plusButtonRuolo() {
    if ($("#ruolo" + numeroRuoli + "_registrazione").val() != "" && $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() != "" && $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() >= 1 && Math.floor($("#numero_dipendenti" + numeroRuoli + "_registrazione").val()) == $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() && $.isNumeric($("#numero_dipendenti" + numeroRuoli + "_registrazione").val())) {
        var nomeRuoloUguale = false;
        for (var i = numeroRuoli - 1; (i > 0 && !nomeRuoloUguale); i--) {
            if ($("#ruolo" + numeroRuoli + "_registrazione").val().toLowerCase() == $("#ruolo" + i + "_registrazione").val().toLowerCase())
                nomeRuoloUguale = true;
        }
        if (!nomeRuoloUguale) {
            $("#div_ruolo" + numeroRuoli + "_registrazione").hide();
            $("#div_button_registrazione1").remove();
            $("#div_button_registrazione2").remove();
            numeroRuoli++;
            $("#div_start_box").append("<div name=\"div_ruolo_registrazione\" id=\"div_ruolo" + numeroRuoli + "_registrazione\" class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
											  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"ruolo\">Ruolo " + numeroRuoli + ":</label>\
											  <input name=\"ruolo_registrazione\" type=\"text\" class=\"form-control\" id=\"ruolo" + numeroRuoli + "_registrazione\">\
											  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"ruolo_nDipendenti\">Numero dipendenti necessari:</label>\
											  <input name=\"numero_dipendenti_registrazione\" type=\"number\" class=\"form-control\" min=\"1\" id=\"numero_dipendenti" + numeroRuoli + "_registrazione\">\
											</div>\
											<div id=\"div_button_registrazione1\" class=\"col text-center btn-group\">\
											  <button type=\"button\" class=\"btn btn-success\" onclick=\"plusButtonRuolo()\">+</button>\
											  <button type=\"button\" class=\"btn btn-danger\" onclick=\"minusButtonRuolo()\">-</button>\
											</div>\
											<div id=\"div_button_registrazione2\" class=\"col text-center\">\
											  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-top: 10px; margin-bottom:10px;\" onclick=\"nextRegistration(4)\">Avanti</button>\
										</div>");
        } else
            showAlert("Non ci possono essere 2 ruoli con lo stesso nome!", 0);
    } else if ($("#numero_dipendenti" + numeroRuoli + "_registrazione").val() < 1)
        showAlert("Almeno un dipendente necessario per ruolo!", 0);
    else if (Math.floor($("#numero_dipendenti" + numeroRuoli + "_registrazione").val()) != $("#numero_dipendenti" + numeroRuoli + "_registrazione").val() || !$.isNumeric($("#numero_dipendenti" + numeroRuoli + "_registrazione").val()))
        showAlert("Inserire un numero intero!", 0);
    else
        showAlert("Compila tutti i campi!", 0);
}

function minusButtonRuolo() {
    if (numeroRuoli > 1) {
        $("#div_ruolo" + numeroRuoli + "_registrazione").remove();
        numeroRuoli--;
        $("#div_ruolo" + numeroRuoli + "_registrazione").show();
    }
}

function attivaRuoloDipendente(num) {
    if ($("#select" + num + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").prop('disabled')) {
        $("#select" + num + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").removeAttr('disabled');
        for (var i = 0; i < numeroRuoli; i++) {
            $("#select" + num + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").append("<option value=\"" + ruoli[i]["nomeRuolo"] + "\">" + ruoli[i]["nomeRuolo"] + "</option>");
        }
    }
    controlloRuoloDipendente(num - 1);
}

function controlloRuoloDipendente(num) {
    for (var i = 1; i <= 3; i++) {
        if (i != num && $("#select" + i + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() == $("#select" + num + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() && $("#select" + i + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "")
            $("#select" + i + "_ruoli_dipendente" + numeroDipendenti + "_registrazione").val("");
    }
}

function plusButtonDipendente() {
    if ($("#nome_dipendente" + numeroDipendenti + "_registrazione").val() != "" && $("#cognome_dipendente" + numeroDipendenti + "_registrazione").val() != "" && ($("#select1_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "" || $("#select2_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "" || $("#select3_ruoli_dipendente" + numeroDipendenti + "_registrazione").val() != "")) {
        $("#div_dipendente" + numeroDipendenti + "_registrazione").hide();
        $("#div_button_registrazione1").remove();
        $("#div_button_registrazione2").remove();
        numeroDipendenti++;
        $("#div_start_box").append("<div name=\"div_dipendente_registrazione\" id=\"div_dipendente" + numeroDipendenti + "_registrazione\" class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteNumero\">Dipendente " + numeroDipendenti + ":</label><br>\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteNome\">Nome:</label>\
									  <input name=\"nome_dipendente_registrazione\" type=\"text\" class=\"form-control\" id=\"nome_dipendente" + numeroDipendenti + "_registrazione\">\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteCognome\">Cognome:</label>\
									  <input name=\"cognome_dipendente_registrazione\" type=\"text\" class=\"form-control\"id=\"cognome_dipendente" + numeroDipendenti + "_registrazione\">\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendenteRuoli\">Ruoli:</label><br>\
									  <select class=\"select_ruoli_dipendente_registrazione\" name=\"select_ruoli_dipendente_registrazione\" id=\"select1_ruoli_dipendente" + numeroDipendenti + "_registrazione\" onchange=\"attivaRuoloDipendente(2)\">\
										<option value=\"\">-</option>\
									  </select><br>\
									  <select class=\"select_ruoli_dipendente_registrazione\" name=\"select_ruoli_dipendente_registrazione\" id=\"select2_ruoli_dipendente" + numeroDipendenti + "_registrazione\" disabled onchange=\"attivaRuoloDipendente(3)\">\
										<option value=\"\">-</option>\
									  </select><br>\
									  <select class=\"select_ruoli_dipendente_registrazione\" name=\"select_ruoli_dipendente_registrazione\" id=\"select3_ruoli_dipendente" + numeroDipendenti + "_registrazione\" disabled onchange=\"controlloRuoloDipendente(3)\">\
										<option value=\"\">-</option>\
									  </select>\
									  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendentePartTime\">Part time:</label>\
									  <input name=\"part_time_dipendente_registrazione\" type=\"checkbox\" style=\"margin-left: 10px;\" id=\"part_time_dipendente" + numeroDipendenti + "_registrazione\">\
									</div>\
									<div id=\"div_button_registrazione1\" class=\"col text-center btn-group\">\
									  <button type=\"button\" class=\"btn btn-success\" onclick=\"plusButtonDipendente()\">+</button>\
									  <button type=\"button\" class=\"btn btn-danger\" onclick=\"minusButtonDipendente()\">-</button>\
									</div>\
									<div id=\"div_button_registrazione2\" class=\"col text-center\">\
									  <button type=\"button\" class=\"btn btn-primary\" style=\"margin-top: 10px; margin-bottom:10px;\" onclick=\"nextRegistration(5)\">Avanti</button>\
								</div>");
        for (var i = 0; i < numeroRuoli; i++) {
            $("#select1_ruoli_dipendente" + numeroDipendenti + "_registrazione").append("<option value=\"" + ruoli[i]["nomeRuolo"] + "\">" + ruoli[i]["nomeRuolo"] + "</option>");
        }
    } else
        showAlert("Compila tutti i campi obbligatori!", 0);
}

function minusButtonDipendente() {
    if (numeroDipendenti > 1) {
        $("#div_dipendente" + numeroDipendenti + "_registrazione").remove();
        numeroDipendenti--;
        $("#div_dipendente" + numeroDipendenti + "_registrazione").show();
    }
}

function controlloInserimentoOrario(giorno, tipo) {
    switch (tipo) {
        case 1:
            if ($("#select_orario_" + giorno + "1_registrazione").val() != "") {
                if ($("#select_orario_" + giorno + "2_registrazione").val() == "") {
                    if ($("#select_orario_" + giorno + "1_registrazione").val() < 24)
                        $("#select_orario_" + giorno + "2_registrazione").val(parseInt($("#select_orario_" + giorno + "1_registrazione").val()) + 1);
                    else
                        $("#select_orario_" + giorno + "2_registrazione").val("");
                } else if (parseInt($("#select_orario_" + giorno + "1_registrazione").val()) >= parseInt($("#select_orario_" + giorno + "2_registrazione").val())) {
                    if (parseInt($("#select_orario_" + giorno + "1_registrazione").val()) < 24)
                        $("#select_orario_" + giorno + "2_registrazione").val(parseInt($("#select_orario_" + giorno + "1_registrazione").val()) + 1);
                    else
                        $("#select_orario_" + giorno + "2_registrazione").val("");
                }
            } else
                $("#select_orario_" + giorno + "2_registrazione").val("");
            break;
        case 2:
            if ($("#select_orario_" + giorno + "2_registrazione").val() != "") {
                if ($("#select_orario_" + giorno + "1_registrazione").val() == "") {
                    if ($("#select_orario_" + giorno + "2_registrazione").val() > 0)
                        $("#select_orario_" + giorno + "1_registrazione").val(parseInt($("#select_orario_" + giorno + "2_registrazione").val()) - 1);
                    else
                        $("#select_orario_" + giorno + "1_registrazione").val("");
                } else if (parseInt($("#select_orario_" + giorno + "2_registrazione").val()) <= parseInt($("#select_orario_" + giorno + "1_registrazione").val())) {
                    if (parseInt($("#select_orario_" + giorno + "2_registrazione").val()) > 0)
                        $("#select_orario_" + giorno + "1_registrazione").val(parseInt($("#select_orario_" + giorno + "2_registrazione").val()) - 1);
                    else
                        $("#select_orario_" + giorno + "1_registrazione").val("");
                }
            } else
                $("#select_orario_" + giorno + "1_registrazione").val("");
            break;
    }
}

function submitRegistration() {
    var settimana = [];
    for (var i = 1; i <= 7; i++) {
        if ($("#select_orario_" + i + "1_registrazione").val() != "" && $("#select_orario_" + i + "2_registrazione").val() != "")
            var giorno = {
                "apertura": parseInt($("#select_orario_" + i + "1_registrazione").val()),
                "chiusura": parseInt($("#select_orario_" + i + "2_registrazione").val())
            }
        else
            var giorno = {
                "apertura": -1,
                "chiusura": -1
            }
        settimana.push(giorno);
    }
    orario = {
        "lunedi": settimana[0],
        "martedi": settimana[1],
        "mercoledi": settimana[2],
        "giovedi": settimana[3],
        "venerdi": settimana[4],
        "sabato": settimana[5],
        "domenica": settimana[6]
    }
    var manager = {
        "email": email,
        "password": psw,
        "nome": nome,
        "cognome": cognome
    }
    var azienda = {
        "partitaIVA": partitaIVA,
        "ragioneSociale": ragioneSociale,
        "suffissoEmail": suffissoEmail,
        "sedeHQ": sedeHQ
    }
    var registrazione = {
        "manager": manager,
        "azienda": azienda,
        "ruoli": ruoli,
        "dipendenti": dipendenti,
        "orario": orario
    }
    $.ajax({
        url: "/registrazione",
        type: 'POST',
        data: JSON.stringify(registrazione),
        dataType: "html",
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (result) {
			if(result == 0)
				showAlert("Registrazione fallita!", 0);
			else if(result == 1){
				showAlert("Registrazione riuscita!", 1);
				setTimeout(function () {
					location.reload();
				}, 5000);
			}
        }
    });
    //console.log(registrazione);
}

function openMainPage(){
	$("#div_main").html("<nav class=\"navbar navbar-expand-sm bg-dark navbar-dark\">\
						  <ul class=\"navbar-nav\">\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"notImplemented()\">Profilo</a>\
							</li>\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"notImplemented()\">Azienda</a>\
							</li>\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"openSedi()\">Sedi</a>\
							</li>\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"notImplemented()\">Ruoli</a>\
							</li>\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"notImplemented()\">Dipendenti</a>\
							</li>\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"openCalendario()\">Calendario</a>\
							</li>\
							<li class=\"nav-item active\">\
							  <a class=\"nav-link unselectable cursor-pointer\" onclick=\"notImplemented()\">Richieste ferie</a>\
							</li>\
						  </ul>\
						  <ul class=\"navbar-nav ml-auto\">\
							<li class=\"nav-item\">\
							<a class=\"nav-link active unselectable cursor-pointer\" onclick=\"logout()\">Logout</a>\
							</li>\
						  </ul>\
						</nav>\
						<div id=\"div_centrale\" class=\"container\">\
						  <div class=\"row\">\
							<div id=\"div_contenuto\" class=\"col-sm-12\">\
							  <h2 class=\"unselectable\">Welcome back!</h2>\
							</div>\
						  </div>\
						</div>");
}

function notImplemented(){
	showAlert("Funzionalità non implementata!", 0);
}

function openSedi(){
	$("#div_centrale").show();
	$("#div_centrale_giorno").remove();
	$("#div_centrale").html("<h2 class=\"unselectable\">Gestione sedi</h2>\
							  <div class=\"div_button_centrali\">\
								  <button type=\"button\" class=\"btn btn-success\" onclick=\"openAddSede(1)\">Aggiungi</button>\
								  <button type=\"button\" class=\"btn btn-primary\" onclick=\"notImplemented()\">Modifica</button>\
								  <button type=\"button\" class=\"btn btn-danger\" onclick=\"notImplemented()\">Cancella</button>\
							  </div>");
}

var altre_sedi, ruoli_aggiunta_sede;
var indirizzo_sede_aggiunta;
var indirizzo_sede_vicina_aggiunta;
var distanza_sede_vicina_aggiunta;
var numero_dipendenti_sede = 0;

function openAddSede(fase){
	if(fase == 1){
		openSedi();
		$("#div_centrale").append("<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"indirizzoSede\">Indirizzo:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"indirizzo_sede\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"sedeVicina\">Indirizzo sede più vicina:</label>\
										  <select id=\"select_sede_vicina\">\
											<option value=\"\">-</option>\
										  </select>\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"distanzaSede\">Distanza (km):</label>\
										  <input type=\"number\" class=\"form-control\" id=\"distanzaSede\">\
									</div>\
									<br><br>\
									<div style=\"position: relative; width: 100%;\">\
										<button type=\"button\" class=\"btn btn-dark button-right\" onclick=\"openAddSede(2)\">Avanti</button>\
									</div>\
									");
		$.ajax({
			url: "/indirizzoSedi",
			type: 'POST',
			success: function (r) {
				if(r == null)
					showAlert("Impossibile ottenere le altre sedi!", 0);
				else{
					altre_sedi = r;
					for(var i = 0; i < r.length; i++){
						$("#select_sede_vicina").append("<option value=\""+r[i]+"\">"+r[i]+"</option>");
					}
				}
			}
		});
	}
	else if(fase == 2){
		if($("#indirizzo_sede").val() == "" || ($("#select_sede_vicina").val() != "" && ($("#distanzaSede").val() == "" || parseFloat($("#distanzaSede").val()) <= 0)))
			showAlert("Campi non compilati correttamente!", 0);
		else{
			indirizzo_sede_aggiunta = $("#indirizzo_sede").val();
			if($("#select_sede_vicina").val() != ""){
				indirizzo_sede_vicina_aggiunta = $("#select_sede_vicina").val();
				distanza_sede_vicina_aggiunta = parseFloat($("#distanzaSede").val());
			}
			else{
				indirizzo_sede_vicina_aggiunta = null;
				distanza_sede_vicina_aggiunta = null;
			}
			$.ajax({
				url: "/nomeRuoli",
				type: 'POST',
				success: function (resp) {
					if(resp == null)
						showAlert("Impossibile ottenere i ruoli dei dipendenti!", 0);
					else{
						ruoli_aggiunta_sede = resp;
						openSedi();
						numero_dipendenti_sede = 1;
						$("#div_centrale").append("<div id=\"div_dipendente_sede_1\"><br>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label>Dipendente 1:</label><br>\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"nomeDipendente\">Nome:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"nome_dipendente_sede_1\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"cognomeDipendente\">Cognome:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"cognome_dipendente_sede_1\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"ruoliDipendente\">Ruoli:</label>\
										  <select style=\"width:100%;\" id=\"select1_ruoli_dipendente_sede_1\" onchange=\"attivaRuoloDipendenteSede(2)\">\
											<option value=\"\">-</option>\
										  </select>\
										  <select style=\"width:100%; margin-top: 10px;\" id=\"select2_ruoli_dipendente_sede_1\" disabled onchange=\"attivaRuoloDipendenteSede(3)\">\
											<option value=\"\">-</option>\
										  </select>\
										  <select style=\"width:100%; margin-top: 10px;\" id=\"select3_ruoli_dipendente_sede_1\" disabled onchange=\"controlloRuoloDipendenteSede(3)\">\
											<option value=\"\">-</option>\
										  </select>\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										<label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendentePartTime\">Part time:</label>\
										<input name=\"part_time_dipendente_sede\" type=\"checkbox\" style=\"margin-left: 10px;\" id=\"part_time_1_dipendente_sede\">\
									</div>\
									<div id=\"div_button_sede_pm\" style=\"position: relative; width: 100%;\">\
										<button id=\"plus_button_dipendenti_sede\" type=\"button\" class=\"btn btn-success button-right\" onclick=\"plusbuttonDipendenteSede()\">+</button>\
										<button id=\"minus_button_dipendenti_sede\" type=\"button\" class=\"btn btn-danger button-left\" onclick=\"minusButtonDipendenteSede()\">-</button>\
									</div>\
									<br><br><br>\
									<div id=\"div_button_sede\" style=\"position: relative; width: 100%;\">\
										<button type=\"button\" class=\"btn btn-dark button-center\" onclick=\"submitSede()\">Conferma</button>\
									</div>\
									</div>");
						for(var j = 0; j < resp.length; j++){
							$("#select1_ruoli_dipendente_sede_1").append("<option value=\""+resp[j]+"\">"+resp[j]+"</option>");						
						}
						$("#minus_button_dipendenti_sede").css("width", $("#plus_button_dipendenti_sede").css("width"));
					}
				}
			});
		}
	}
}

function attivaRuoloDipendenteSede(num){
	if ($("#select"+num+"_ruoli_dipendente_sede_"+numero_dipendenti_sede).prop('disabled')) {
        $("#select"+num+"_ruoli_dipendente_sede_"+numero_dipendenti_sede).removeAttr('disabled');
        for (var i = 0; i < ruoli_aggiunta_sede.length; i++) {
            $("#select"+num+"_ruoli_dipendente_sede_"+numero_dipendenti_sede).append("<option value=\"" + ruoli_aggiunta_sede[i] + "\">" + ruoli_aggiunta_sede[i] + "</option>");
        }
    }
    controlloRuoloDipendenteSede(num - 1);
}

function controlloRuoloDipendenteSede(num){
	for (var i = 1; i <= 3; i++) {
        if (i != num && $("#select"+ i +"_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() == $("#select"+ num +"_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() && $("#select"+i+"_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "")
            $("#select"+i+"_ruoli_dipendente_sede_"+numero_dipendenti_sede).val("");
    }
}

function plusbuttonDipendenteSede(){
	if ($().val("#nome_dipendente_sede_"+numero_dipendenti_sede) != "" && $("#cognome_dipendente_sede_"+numero_dipendenti_sede).val() != "" && ($("#select1_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "" || $("#select2_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "" || $("#select3_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "")) {
        $("#div_dipendente_sede_"+numero_dipendenti_sede).hide();
        numero_dipendenti_sede++;
		var resp = ruoli_aggiunta_sede;
        $("#div_centrale").append("<div id=\"div_dipendente_sede_"+numero_dipendenti_sede+"\"><br>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label>Dipendente "+numero_dipendenti_sede+":</label><br>\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"nomeDipendente\">Nome:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"nome_dipendente_sede_"+numero_dipendenti_sede+"\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"cognomeDipendente\">Cognome:</label>\
										  <input type=\"text\" class=\"form-control\" id=\"cognome_dipendente_sede_"+numero_dipendenti_sede+"\">\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										  <label class=\"unselectable\" style=\"margin-top:10px;\" for=\"ruoliDipendente\">Ruoli:</label>\
										  <select style=\"width:100%;\" id=\"select1_ruoli_dipendente_sede_"+numero_dipendenti_sede+"\" onchange=\"attivaRuoloDipendenteSede(2)\">\
											<option value=\"\">-</option>\
										  </select>\
										  <select style=\"width:100%; margin-top: 10px;\" id=\"select2_ruoli_dipendente_sede_"+numero_dipendenti_sede+"\" disabled onchange=\"attivaRuoloDipendenteSede(3)\">\
											<option value=\"\">-</option>\
										  </select>\
										  <select style=\"width:100%; margin-top: 10px;\" id=\"select3_ruoli_dipendente_sede_"+numero_dipendenti_sede+"\" disabled onchange=\"controlloRuoloDipendenteSede(3)\">\
											<option value=\"\">-</option>\
										  </select>\
									</div>\
									<div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
										<label class=\"unselectable\" style=\"margin-top:10px;\" for=\"dipendentePartTime\">Part time:</label>\
										<input name=\"part_time_dipendente_sede\" type=\"checkbox\" style=\"margin-left: 10px;\" id=\"part_time_"+numero_dipendenti_sede+"_dipendente_sede\">\
									</div>\
									<div style=\"position: relative; width: 100%;\">\
										<button id=\"plus_button_dipendenti_sede\" type=\"button\" class=\"btn btn-success button-right\" onclick=\"plusbuttonDipendenteSede()\">+</button>\
										<button id=\"minus_button_dipendenti_sede\" type=\"button\" class=\"btn btn-danger button-left\" onclick=\"minusButtonDipendenteSede()\">-</button>\
									</div>\
									<br><br><br>\
									<div style=\"position: relative; width: 100%;\">\
										<button type=\"button\" class=\"btn btn-dark button-center\" onclick=\"submitSede()\">Conferma</button>\
									</div>\
									</div>");
        for(var j = 0; j < resp.length; j++){
							$("#select1_ruoli_dipendente_sede_"+numero_dipendenti_sede).append("<option value=\""+resp[j]+"\">"+resp[j]+"</option>");						
						}
    } else
        showAlert("Compila tutti i campi obbligatori!", 0);
}

function minusButtonDipendenteSede(){
	if (numero_dipendenti_sede > 1) {
        $("#div_dipendente_sede_"+numero_dipendenti_sede).remove();
        numero_dipendenti_sede--;
        $("#div_dipendente_sede_"+numero_dipendenti_sede).show();
    }
}

function submitSede(){
	if ($().val("#nome_dipendente_sede_"+numero_dipendenti_sede) != "" && $("#cognome_dipendente_sede_"+numero_dipendenti_sede).val() != "" && ($("#select1_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "" || $("#select2_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "" || $("#select3_ruoli_dipendente_sede_"+numero_dipendenti_sede).val() != "")) {
		var dipendenti_sede = [];
		for (var i = 1; i <= numero_dipendenti_sede; i++) {
            var ruoliDipendete = [];
            for (var j = 1; j <= 3; j++) {
                if ($("#select"+j+"_ruoli_dipendente_sede_"+i).val() != "")
                    ruoliDipendete.push($("#select"+j+"_ruoli_dipendente_sede_"+i).val());
            }
            var pt = false;
            if ($("#part_time_"+i+"_dipendente_sede").is(":checked"))
                pt = true;
            var recordDipendente = {
                "nomeDipendente": $("#nome_dipendente_sede_"+i).val(),
                "cognomeDipendente": $("#cognome_dipendente_sede_"+i).val(),
                "ruoliDipendente": ruoliDipendete,
                "partTime": pt
            }
            dipendenti_sede.push(recordDipendente);
        }
		var sede = {
				"indirizzo": indirizzo_sede_aggiunta,
				"sedeVicina": indirizzo_sede_vicina_aggiunta,
				"distanza": distanza_sede_vicina_aggiunta
		}
		var registrazioneSede = {
			"sede": sede,
			"dipendenti": dipendenti_sede
		}
		$.ajax({
			url: "/aggiungiSede",
			type: 'POST',
			data: JSON.stringify(registrazioneSede),
			dataType: "html",
			contentType: 'application/json',
			mimeType: 'application/json',
			success: function (result) {
				if(result == 0)
					showAlert("Inserimento fallito!", 0);
				else if(result == -1)
					showAlert("Indirizzo sede già presente!", 0);
				else if(result == 1){
					showAlert("Inserimento riuscito!", 1);
					setTimeout(function () {
						location.reload();
					}, 500);
				}
			}
		});
		console.log(registrazioneSede);
	}
	else
		showAlert("Compila tutti i campi obbligatori!", 0);
}

function openCalendario(){
	$("#div_centrale").show();
	$("#div_centrale_giorno").remove();
	const d = new Date();
	let year = d.getFullYear();
	$("#div_centrale").html("<h2 class=\"unselectable\">Calendario</h2>\
							  <div class=\"form-group\" style=\"margin-left:25px; margin-right:25px;\">\
									<label class=\"unselectable\" style=\"margin-top:10px;\" for=\"sede\">Sede:</label>\
									<select style=\"width:100%;\" id=\"select_sede_calendario\" onchange=\"\">\
									</select>\
									<label class=\"unselectable\" style=\"margin-top:10px;\" for=\"mese\">Mese:</label>\
									<select style=\"width:100%; margin-top: 0px;\" id=\"select_mese_calendario\" onchange=\"\">\
										<option id=\"option_mese_1\" value=\"1\">Gennaio</option>\
										<option id=\"option_mese_2\" value=\"2\">Febbraio</option>\
										<option id=\"option_mese_3\" value=\"3\">Marzo</option>\
										<option id=\"option_mese_4\" value=\"4\">Aprile</option>\
										<option id=\"option_mese_5\" value=\"5\">Maggio</option>\
										<option id=\"option_mese_6\" value=\"6\">Giugno</option>\
										<option id=\"option_mese_7\" value=\"7\">Luglio</option>\
										<option id=\"option_mese_8\" value=\"8\">Agosto</option>\
										<option id=\"option_mese_9\" value=\"9\">Settembre</option>\
										<option id=\"option_mese_10\" value=\"10\">Ottobre</option>\
										<option id=\"option_mese_11\" value=\"11\">Novembre</option>\
										<option id=\"option_mese_12\" value=\"12\">Dicembre</option>\
									</select>\
									<label class=\"unselectable\" style=\"margin-top:10px;\" for=\"anno\">Anno:</label>\
									<select style=\"width:100%;\" id=\"select_anno_calendario\" onchange=\"\">\
										<option value=\""+year+"\">"+year+"</option>\
										<option value=\""+(year + 1)+"\">"+(year + 1)+"</option>\
									</select>\
								</div>\
								<div class=\"div_button_centrali\">\
								  <button type=\"button\" class=\"btn btn-primary\" onclick=\"visualizzaCalendario()\">Visualizza</button>\
								  <button type=\"button\" id=\"button_ricalcola\" class=\"btn btn-primary\" onclick=\"ricalcolaTurni()\">Ricalcola</button>\
							  </div>\
							  ");
	$.ajax({
			url: "/indirizzoSedi",
			type: 'POST',
			success: function (r) {
				if(r == null)
					showAlert("Impossibile ottenere le sedi!", 0);
				else{
					for(var i = 0; i < r.length; i++){
						$("#select_sede_calendario").append("<option value=\""+r[i]+"\">"+r[i]+"</option>");
					}
				}
			}
		});
}

function annoBisestile(year) {
	return (year % 100 === 0) ? (year % 400 === 0) : (year % 4 === 0);
}

var anno_global;
var mese_global;
var sede_global;

function visualizzaCalendario(){
	const d = new Date();
	let year_now = d.getFullYear();
	let month_now = d.getMonth();
	var mese = $("#select_mese_calendario").val();
	var anno = $("#select_anno_calendario").val();
	anno_global = anno;
	mese_global = mese;
	sede_global = $("#select_sede_calendario").val();
	var stringa = mese + ";" + anno;
	$("#div_centrale_giorno").remove();
	$.ajax({
			url: "/checkCalcoloTurni",
			type: 'POST',
			data: JSON.stringify(stringa),
			dataType: "html",
			contentType: 'application/json',
			mimeType: 'application/json',
			success: function (result) {
				if(result == 0)
					if(year_now >= anno && month_now > (mese - 1))
						showAlert("Turni non presenti nel database!", 0);
					else
						showAlert("I turni per la data selezionata non sono stati ancora calcolati!", 0);
				else{
					$("#div_calendario").remove();
					$("#div_centrale").append("<div id=\"div_calendario\" class=\"unselectable\">\
												<div id=\"div_titolo_calendario\"><h4 style=\"text-align: center; color: white; margin-bottom: 0px;\">"+$("#option_mese_"+mese).html()+" "+ anno +"</h4></div>\
													<table id=\"tabella_calendario\" class=\"table table-bordered\">\
														<thead>\
														  <tr>\
															<th>Lunedì</th>\
															<th>Martedì</th>\
															<th>Mercoledì</th>\
															<th>Giovedì</th>\
															<th>Venerdì</th>\
															<th>Sabato</th>\
															<th>Domenica</th>\
														  </tr>\
														</thead>\
														<tbody>\
														  <tr>\
															<td id=\"td_calendario_00\" onclick=\"openGiorno(0,0)\"></td>\
															<td id=\"td_calendario_01\" onclick=\"openGiorno(0,1)\"></td>\
															<td id=\"td_calendario_02\" onclick=\"openGiorno(0,2)\"></td>\
															<td id=\"td_calendario_03\" onclick=\"openGiorno(0,3)\"></td>\
															<td id=\"td_calendario_04\" onclick=\"openGiorno(0,4)\"></td>\
															<td id=\"td_calendario_05\" onclick=\"openGiorno(0,5)\"></td>\
															<td id=\"td_calendario_06\" onclick=\"openGiorno(0,6)\"></td>\
														  </tr>\
														  <tr>\
															<td id=\"td_calendario_10\" onclick=\"openGiorno(1,0)\"></td>\
															<td id=\"td_calendario_11\" onclick=\"openGiorno(1,1)\"></td>\
															<td id=\"td_calendario_12\" onclick=\"openGiorno(1,2)\"></td>\
															<td id=\"td_calendario_13\" onclick=\"openGiorno(1,3)\"></td>\
															<td id=\"td_calendario_14\" onclick=\"openGiorno(1,4)\"></td>\
															<td id=\"td_calendario_15\" onclick=\"openGiorno(1,5)\"></td>\
															<td id=\"td_calendario_16\" onclick=\"openGiorno(1,6)\"></td>\
														  </tr>\
														  <tr>\
															<td id=\"td_calendario_20\" onclick=\"openGiorno(2,0)\"></td>\
															<td id=\"td_calendario_21\" onclick=\"openGiorno(2,1)\"></td>\
															<td id=\"td_calendario_22\" onclick=\"openGiorno(2,2)\"></td>\
															<td id=\"td_calendario_23\" onclick=\"openGiorno(2,3)\"></td>\
															<td id=\"td_calendario_24\" onclick=\"openGiorno(2,4)\"></td>\
															<td id=\"td_calendario_25\" onclick=\"openGiorno(2,5)\"></td>\
															<td id=\"td_calendario_26\" onclick=\"openGiorno(2,6)\"></td>\
														  </tr>\
														  <tr>\
															<td id=\"td_calendario_30\" onclick=\"openGiorno(3,0)\"></td>\
															<td id=\"td_calendario_31\" onclick=\"openGiorno(3,1)\"></td>\
															<td id=\"td_calendario_32\" onclick=\"openGiorno(3,2)\"></td>\
															<td id=\"td_calendario_33\" onclick=\"openGiorno(3,3)\"></td>\
															<td id=\"td_calendario_34\" onclick=\"openGiorno(3,4)\"></td>\
															<td id=\"td_calendario_35\" onclick=\"openGiorno(3,5)\"></td>\
															<td id=\"td_calendario_36\" onclick=\"openGiorno(3,6)\"></td>\
														  </tr>\
														  <tr>\
															<td id=\"td_calendario_40\" onclick=\"openGiorno(4,0)\"></td>\
															<td id=\"td_calendario_41\" onclick=\"openGiorno(4,1)\"></td>\
															<td id=\"td_calendario_42\" onclick=\"openGiorno(4,2)\"></td>\
															<td id=\"td_calendario_43\" onclick=\"openGiorno(4,3)\"></td>\
															<td id=\"td_calendario_44\" onclick=\"openGiorno(4,4)\"></td>\
															<td id=\"td_calendario_45\" onclick=\"openGiorno(4,5)\"></td>\
															<td id=\"td_calendario_46\" onclick=\"openGiorno(4,6)\"></td>\
														  </tr>\
														  <tr id=\"ultima_riga_tabella\">\
															<td id=\"td_calendario_50\" onclick=\"openGiorno(5,0)\"></td>\
															<td id=\"td_calendario_51\" onclick=\"openGiorno(5,1)\"></td>\
															<td id=\"td_calendario_52\" onclick=\"openGiorno(5,2)\"></td>\
															<td id=\"td_calendario_53\" onclick=\"openGiorno(5,3)\"></td>\
															<td id=\"td_calendario_54\" onclick=\"openGiorno(5,4)\"></td>\
															<td id=\"td_calendario_55\" onclick=\"openGiorno(5,5)\"></td>\
															<td id=\"td_calendario_56\" onclick=\"openGiorno(5,6)\"></td>\
														  </tr>\
														</tbody>\
													  </table>\
											</div>");
					const primo_giorno = new Date(anno, ($("#option_mese_"+mese).val() - 1), 1, 0, 0, 0, 0);
					var giorno_settimana = primo_giorno.getDay();
					if(giorno_settimana > 0)
						giorno_settimana--;
					else
						giorno_settimana = 6;
					$("#td_calendario_0"+giorno_settimana).html("1");
					$("#td_calendario_0"+giorno_settimana).css("cursor", "pointer");
					var giorni_mese = 30;
					if($("#option_mese_"+mese).val() == 1 || $("#option_mese_"+mese).val() == 3 || $("#option_mese_"+mese).val() == 5 || $("#option_mese_"+mese).val() == 7 || $("#option_mese_"+mese).val() == 8 || $("#option_mese_"+mese).val() == 10 || $("#option_mese_"+mese).val() == 12)
						giorni_mese = 31;
					else if($("#option_mese_"+mese).val() == 2){
						if(annoBisestile(anno))
							giorni_mese = 29;
						else
							giorni_mese = 28;
					}
					var riga = 0;
					var colonna = giorno_settimana + 1;
					for(var i = 2;i <= giorni_mese;i++){
						if(colonna > 6){
							colonna = 0;
							riga++;
						}
						$("#td_calendario_"+riga+""+colonna).html(i);
						$("#td_calendario_"+riga+""+colonna).css("cursor", "pointer");
						colonna++;
					}
					if(riga < 5)
						$("#ultima_riga_tabella").remove();
					coloraGiorni(anno, mese, $("#select_sede_calendario").val());
				}
			}
		});
}

function ricalcolaTurni(){
	const d = new Date();
	let year_now = d.getFullYear();
	let month_now = d.getMonth();
	var mese = $("#select_mese_calendario").val();
	var anno = $("#select_anno_calendario").val();
	var stringa = mese + ";" + anno;
	
	if(anno < year_now || (anno == year_now && (mese-1) < month_now))
		showAlert("Non è possibile ricalcolare i turni di una data nel passato!", 0);
	else{
		$("#div_calendario").remove();
		$("#div_centrale_giorno").remove();
		$("#button_ricalcola").html("<span class=\"spinner-border spinner-border-sm\"></span>");
		$("#button_ricalcola").append("Loading..");
		$.ajax({
				url: "/ricalcolaTurni",
				type: 'POST',
				data: JSON.stringify(stringa),
				dataType: "html",
				contentType: 'application/json',
				mimeType: 'application/json',
				success: function (r) {
					if(r == null || r == 0)
						showAlert("Impossibile ricalcolare i turni!", 0);
					else if(r == 1)
						showAlert("Turni assegnati in modo incompleto!", 1);
					else
						showAlert("Turni assegnati in modo completo!", 1);
					$("#button_ricalcola").html("Ricalcola");
				}
			});
	}
}

var status_giorni;

function coloraGiorni(anno, mese, indirizzo_sede_calendario){
	var richiesta = {
			"anno": anno,
			"mese": mese,
			"indirizzoSede": indirizzo_sede_calendario
	}
	$.ajax({
				url: "/getStatusGiorni",
				type: 'POST',
				data: JSON.stringify(richiesta),
				dataType: "html",
				contentType: 'application/json',
				mimeType: 'application/json',
				success: function (r) {
					if(r == null)
						showAlert("Impossibile ottenere lo status dei turni!", 0);
					else{
						status_giorni = JSON.parse(r);
						var trovato = false;
						var riga = 0;
						var colonna = 0;
						while(!trovato){
							if($("#td_calendario_"+riga+""+colonna).html() == "1")
								trovato = true;
							else{
								colonna++
								if(colonna == 7){
									colonna = 0;
									riga++;
								}
							}
						}
						for(i=0;i<status_giorni.length;i++){
							//giorno di chiusura
							if(status_giorni[i] == -1){
								$("#td_calendario_"+riga+""+colonna).css("backgroundColor", "white");
								$("#td_calendario_"+riga+""+colonna).css("cursor", "default");
							}
							//turno scoperto
							else if(status_giorni[i] == 0){
								$("#td_calendario_"+riga+""+colonna).css("backgroundColor", "#ff000070");
							}
							else
								$("#td_calendario_"+riga+""+colonna).css("backgroundColor", "#0080008f");
							colonna++;
							if(colonna == 7){
								colonna = 0;
								riga++;
							}
						}
					}
				}
			});
}

function openGiorno(riga, colonna){
	var giorno = $("#td_calendario_"+riga+""+colonna).html();
	if(status_giorni[giorno-1] != -1 && giorno != ""){
		$("#div_centrale").hide();
		$("#div_main").append("<div id=\"div_centrale_giorno\" class=\"container unselectable\"></div>");
		var richiesta = {
			"anno": anno_global,
			"mese": mese_global,
			"giorno": giorno,
			"indirizzoSede": sede_global
		}
		$.ajax({
				url: "/getTurniSedeGiorno",
				type: 'POST',
				data: JSON.stringify(richiesta),
				dataType: "html",
				contentType: 'application/json',
				mimeType: 'application/json',
				success: function (r) {
					if(r == null)
						showAlert("No", 0);
					else{
						//console.log(r);
						var risposta = JSON.parse(r);
						$("#div_centrale_giorno").html("<button style=\"position: relative; margin-top: 10px; left: 92%;\" type=\"button\" class=\"btn btn-info\" onclick=\"indietroCalendario()\">Indietro</button>");
						$("#div_centrale_giorno").append("<h3>Turni del "+giorno+"/"+mese_global+"/"+anno_global+"</h3>");
						for(i=0;i<risposta["turni"].length;i++){
							if(risposta["turni"][i]["utenti"] != null)
								$("#div_centrale_giorno").append("<div id=\"div_turno_"+i+"\" class=\"div_turno\" onclick=\"changeTurnoUtente("+i+","+risposta["turni"][i]["utenti"].length+")\"></div>");
							else
								$("#div_centrale_giorno").append("<div id=\"div_turno_"+i+"\" class=\"div_turno\" style=\"cursor: default;\"></div>");
							$("#div_turno_"+i).html("<p class=\"testo_turni\">Ruolo: "+risposta["turni"][i]["nomeRuolo"]+"</p>");
							$("#div_turno_"+i).append("<p class=\"testo_turni\">Ora inizio: "+risposta["turni"][i]["oraInizio"]+"</p>");
							$("#div_turno_"+i).append("<p class=\"testo_turni\">Ora fine: "+risposta["turni"][i]["oraFine"]+"</p>");
							$("#div_turno_"+i).append("<p class=\"testo_turni\">Ore scoperte: "+risposta["turni"][i]["oreScoperte"]+"</p>");
							if(risposta["turni"][i]["oreScoperte"] > 0)
								$("#div_turno_"+i).css("backgroundColor", "rgba(255, 0, 0, 0.44)");
							if(risposta["turni"][i]["utenti"] != null){
								for(j=0;j<risposta["turni"][i]["utenti"].length;j++){
									$("#div_centrale_giorno").append("<div name=\"div_turno_utente_"+i+"\" id=\"div_turno_utente_"+i+"_"+j+"\" class=\"div_turno_utente\"></div>");
									$("#div_turno_utente_"+i+"_"+j).html("<p class=\"testo_turni\">Nome: "+risposta["turni"][i]["utenti"][j]["nomeUtente"]+"</p>");
									$("#div_turno_utente_"+i+"_"+j).append("<p class=\"testo_turni\">Cognome: "+risposta["turni"][i]["utenti"][j]["cognomeUtente"]+"</p>");
									$("#div_turno_utente_"+i+"_"+j).append("<p class=\"testo_turni\">Straordinario: "+risposta["turni"][i]["utenti"][j]["straordinario"]+"</p>");
									$("#div_turno_utente_"+i+"_"+j).append("<p class=\"testo_turni\">Trasferta: "+risposta["turni"][i]["utenti"][j]["trasferta"]+"</p>");
									$("#div_turno_utente_"+i+"_"+j).append("<p class=\"testo_turni\">Inizio: "+risposta["turni"][i]["utenti"][j]["inizio"]+"</p>");
									$("#div_turno_utente_"+i+"_"+j).append("<p class=\"testo_turni\">Fine: "+risposta["turni"][i]["utenti"][j]["fine"]+"</p>");
									
									if(risposta["turni"][i]["utenti"][j]["straordinario"] == true)
										$("#div_turno_utente_"+i+"_"+j).css("backgroundColor", "#ffff0085");
									if(risposta["turni"][i]["utenti"][j]["trasferta"] == true)
										$("#div_turno_utente_"+i+"_"+j).css("backgroundColor", "#009aff94");
									$("#div_turno_utente_"+i+"_"+j).hide();
								}
							}
						}
					}
				}
			});
	}
}

function indietroCalendario(){
	$("#div_centrale_giorno").remove();
	$("#div_centrale").show();
}

function changeTurnoUtente(index, size){
	for(i=0;i<size;i++){
		if($("#div_turno_utente_"+index+"_"+i).is(":hidden"))
			$("#div_turno_utente_"+index+"_"+i).show();
		else
			$("#div_turno_utente_"+index+"_"+i).hide();
	}
		
}