/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebilancio.grafica;

import gestionebilancio.voci.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.swing.table.*;

/**
 *
 * @author Wang
 */
public class VociTabella extends AbstractTableModel {

    private ArrayList<Voce> voci;
    private String[] nomiColonne;

    /**
     *
     * @param voci ArrayList utilizzato per memorizzare le voci che mostra la
     * tabella
     * @param nomiColonne Vettore di stringhe utilizzato per memorizzare i nomi
     * delle colonne
     */
    public VociTabella(ArrayList<Voce> voci, String[] nomiColonne) {
        this.voci = voci;
        this.nomiColonne = nomiColonne;
    }

    /**
     * Funzione utilizzata per ottenere il nome di una colonna
     *
     * @return nome della colonna
     * @param colonna posizione della colonna
     */
    @Override
    public String getColumnName(int colonna) {
        return nomiColonne[colonna];
    }

    /**
     * Funzione che restituisce il numero di righe, il quale corrisponde al
     * numero di voci che si vogliono visualizzare
     *
     * @return numero di righe
     */
    @Override
    public int getRowCount() {
        return voci.size();
    }

    /**
     * Funzione che restituisce il numero di colonne
     *
     * @return numero di colonne
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Funzione utilizzata per ottenere il valore da una cella
     *
     * @param riga indica in quale riga
     * @param colonna indica in quale colonna
     * @return informazione richiesta
     */
    @Override
    public Object getValueAt(int riga, int colonna) {

        switch (colonna) {
            case 0: {
                return voci.get(riga).getData();
            }
            case 1: {
                return voci.get(riga).getDescrizione();
            }
            case 2: {
                return voci.get(riga).getAmmontare();
            }
            default: {
            }
        }
        return null;
    }

    /**
     * Funzione utilizzata per permettere che le celle della tabella siano editabili
     *
     * @param riga
     * @param colonna
     * @return
     */
    @Override
    public boolean isCellEditable(int riga, int colonna) {
        return true;
    }

    /**
     * Funzione utilizzata per modificare una cella
     *
     * @param value valore da impostare
     * @param riga indica in quale riga
     * @param colonna indica in quale colonna
     */
    @Override
    public void setValueAt(Object value, int riga, int colonna) {
        String temp = (String) value;
        switch (colonna) {
            case 0: {
                if (controlloData(temp)) {
                    LocalDate data = LocalDate.parse(temp);
                    voci.get(riga).setData(data);
                }
                break;
            }
            case 1: {
                voci.get(riga).setDescrizione(temp);
                break;
            }
            case 2: {
                if (controlloAmmontare(temp)) {
                    voci.get(riga).setAmmontare(Double.parseDouble(temp));
                }
                break;
            }
            default: {
            }
        }
        fireTableCellUpdated(riga, colonna);
    }

    /**
     * Funzione utilizzata per controllare che la stringa inserita quando si aggiunge una
     * riga è possibile convertirla in LocalDate
     *
     * @param s stringa da controllare
     * @return true se la conversione è possibile
     */
    private boolean controlloData(String s) {
        try {
            LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Funzione utilizzata per controllare che il valore inserito quando si aggiunge una riga
     * è possibile convertirlo in double
     *
     * @param s stinga da controllare
     * @return true se la conversione è possibile
     */
    private boolean controlloAmmontare(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
