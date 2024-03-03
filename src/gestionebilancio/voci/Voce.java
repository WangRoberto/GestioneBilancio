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
public class Voce {

    private LocalDate data;
    private String descrizione;
    private double ammontare;

    /**
     * Costrutttore 1
     *
     * @param data
     * @param descrizione
     */
    public Voce(LocalDate data, String descrizione) {
        this.data = data;
        this.descrizione = descrizione;
    }

    /**
     * Costruttore 2
     */
    public Voce() {
        this.data = null;
        this.descrizione = "";
        this.ammontare = 0;
    }

    /**
     * Funzione per ottenere la data di una voce
     *
     * @return data
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Funzione per impostare la data di una voce
     *
     * @param data 
     */
    public void setData(LocalDate data) {
        this.data = data;
    }

    /**
     * Funzione per ottenere la descrizione di una voce
     *
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Funzione per impostare la descrizione di una voce
     *
     * @param descrizione 
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Funzione per ottenere l'ammontare di una voce
     *
     * @return ammontare
     */
    public double getAmmontare() {
        return ammontare;
    }

    /**
     * Funzione per impostare l'ammontare di una voce
     *
     * @param ammontare 
     */
    public void setAmmontare(double ammontare) {
        this.ammontare = ammontare;
    }

    /**
     * Funzione per ottenere le caratteristiche che compongono una voce
     *
     * @param separatore per separare un parametro dall'altro
     * @return le caratteristiche che compongono una voce
     */
    public String toString(String separatore) {
        return data + separatore + descrizione + separatore + ammontare;
    }

}
