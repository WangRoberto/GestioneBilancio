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
public class Entrata extends Voce {

    /**
     * La classe Entrata, la quale estende la classe Voce, non accetta valori
     * negativi per il parametro "ammontare"; Costruttore 1
     *
     * @param data
     * @param descrizione
     * @param ammontare
     */
    public Entrata(LocalDate data, String descrizione, double ammontare) {
        super(data, descrizione);
        if (ammontare > 0) {//Controllo utilizzato per non avere il parametro "ammontare" negativo
            super.setAmmontare(ammontare);
        }
    }

    /**
     * Costruttore 2
     *
     */
    public Entrata() {
        super();
    }

    /**
     * Funzione per impostare l'ammontare, la quale al contrario di quello
     * presente nella classe Voce esegue un controllo sul parametro "ammontare"
     *
     * @param ammontare
     */
    @Override
    public void setAmmontare(double ammontare) {
        if (ammontare > 0) {
            super.setAmmontare(ammontare);
        }
    }

}
