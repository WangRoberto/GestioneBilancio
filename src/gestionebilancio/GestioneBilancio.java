/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebilancio;

import gestionebilancio.grafica.*;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author Wang
 */
public class GestioneBilancio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * Creazione dell'oggetto JFrame
         */
        JFrame frame = new JFrame("Gestione Bilancio");

        /**
         * Imposta l'icona del frame
         */
        frame.setIconImage(new ImageIcon("Icona" + File.separator + "icona.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        /**
         * Creazione dell'oggetto myPanel, il quale racchiudere tutti gli altri
         * componenti grafici
         */
        myPanel panel = new myPanel();
        frame.add(panel);

        /**
         * Creazione dell'oggetto Thread, il quale ha il compito di salvare le
         * voci della tabella in un file temporaneo
         */
        Thread t = new Thread(panel);
        t.start();

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}
