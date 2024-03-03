/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebilancio.voci;

import java.time.*;

/**
 *
 * @author Wang
 */
public class Uscita extends Voce {

    /**
     * La classe Uscita, la quale estende la classe Voce, non accetta valori
     * positivi per il parametro "ammontare"; Costruttore 1
     *
     * @param data
     * @param descrizione
     * @param ammontare
     */
    public Uscita(LocalDate data, String descrizione, double ammontare) {
        super(data, descrizione);
        if (ammontare < 0) {//Controllo utilizzato per non avere il parametro "ammontare" positivo
            super.setAmmontare(ammontare);
        }
    }

    /**
     * Costruttore 2
     *
     */
    public Uscita() {
        super();
    }

    /**
     * Funzione per impostare l'ammontare, la quale al contrario della classe
     * Voce esegue un controllo al parametro "ammontare"
     *
     * @param ammontare
     */
    @Override
    public void setAmmontare(double ammontare) {
        if (ammontare < 0) {
            super.setAmmontare(ammontare);
        }
    }

}
