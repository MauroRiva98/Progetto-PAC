package com.progetto.backendserver;

public class Utils {
    /**
     * Utility wrapper per ottenere il nome del metodo chiamante chiamando questo metodo
     * in un singolo passaggio (get strack trace ritorna lo stack trace del thread corrente,
     * e l'array in prima posizione ritorna il metodo chiamante da cui proviene la chiamata)
     */
    public static String getMethodName() {
        return ("[" + new Throwable().getStackTrace()[1].getMethodName() + "]: ");
    }
}
