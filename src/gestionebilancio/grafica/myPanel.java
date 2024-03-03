/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionebilancio.grafica;

import gestionebilancio.voci.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author Wang
 */
public class myPanel extends JPanel implements Runnable {

    /**
     * Lista dei componenti grafici
     */
    private ArrayList<Voce> vociTotali;
    private ArrayList<Voce> vociEffettivi;
    private ArrayList<Integer> vociSelezionati;
    private JButton aggiungi, cancella, salva, carica, esporta, ricercaButton;
    private JTextField ris, ricerca, inizioData, fineData;
    private JComboBox combo, combo2;
    private VociTabella vt;
    private String temp;//Utilizzato per ricordare la stringa precedente per la ricerca
    //Utilizzati per scegliere casualmente l'intervallo di tempo
    private LocalDate di;//Data Inizio
    private LocalDate df;//Data Fine

    public myPanel() {
        /**
         * Creazione degli oggetti e settaggio dei componenti grafici
         */
        vociTotali = new ArrayList<Voce>();
        vociEffettivi = new ArrayList<Voce>();
        vociSelezionati = new ArrayList<Integer>();

        String nomiColonne[] = {"Data", "Descrizione", "Ammontare"};
        vt = new VociTabella(vociEffettivi, nomiColonne);
        JTable t = new JTable(vt);

        LocalDate oggi = LocalDate.now();
        ris = new JTextField();
        inizioData = new JTextField();
        fineData = new JTextField();
        inizioData.setText(oggi.toString());
        fineData.setText(oggi.toString());
        ris.setBorder(new CompoundBorder(new TitledBorder("Somma"), new EmptyBorder(0, 0, 0, 0)));
        inizioData.setBorder(new CompoundBorder(new TitledBorder("Inizio"), new EmptyBorder(0, 0, 0, 0)));
        fineData.setBorder(new CompoundBorder(new TitledBorder("Fine"), new EmptyBorder(0, 0, 0, 0)));
        ris.setEditable(false);
        inizioData.setEditable(false);
        fineData.setEditable(false);

        aggiungi = new JButton("Aggiungi");
        cancella = new JButton("Cancella");
        salva = new JButton("Salva");
        carica = new JButton("Carica");
        combo = new JComboBox();
        combo2 = new JComboBox();

        combo.addItem("Oggi");
        combo.addItem("Ultima Settimana");
        combo.addItem("Ultimo Mese");
        combo.addItem("Ultimo Anno");
        combo.addItem("Altro");

        combo2.addItem("CSV");
        combo2.addItem("Testo");
        combo2.addItem("Excel");

        esporta = new JButton("Esporta");
        ricerca = new JTextField(20);
        ricercaButton = new JButton("Ricerca");
        setActionListener(t);

        JPanel tabella = new JPanel();
        tabella.setLayout(new BorderLayout(1, 1));

        tabella.add(new JScrollPane(t), BorderLayout.PAGE_START);

        tabella.add(ricerca, BorderLayout.CENTER);
        tabella.add(ricercaButton, BorderLayout.LINE_END);
        tabella.add(ris, BorderLayout.PAGE_END);

        JPanel contenitore = new JPanel();
        BorderLayout b = new BorderLayout();
        contenitore.setLayout(b);
        b.setVgap(5);

        JPanel date = new JPanel();
        GridLayout g = new GridLayout(0, 1);
        date.setLayout(g);
        date.setBorder(new CompoundBorder(new TitledBorder("Date"), new EmptyBorder(0, 0, 0, 0)));
        date.add(inizioData);
        date.add(fineData);

        JPanel opzioni2 = new JPanel();
        opzioni2.setBorder(new CompoundBorder(new TitledBorder("Opzioni"), new EmptyBorder(3, 3, 3, 3)));
        g = new GridLayout(0, 1);

        g.setVgap(10);
        g.setHgap(1);
        opzioni2.setLayout(g);

        opzioni2.add(combo);
        opzioni2.add(aggiungi);
        opzioni2.add(cancella);
        opzioni2.add(salva);
        opzioni2.add(carica);
        opzioni2.add(combo2);
        opzioni2.add(esporta);

        contenitore.add(date, BorderLayout.PAGE_START);
        contenitore.add(opzioni2, BorderLayout.PAGE_END);
        contenitore.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        this.add(tabella);
        this.add(contenitore);

    }

    protected void setActionListener(JTable t) {
        /**
         * Cliccando il JButton "aggiungi" si potrà aggiungere una riga alla
         * tabella
         */
        aggiungi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String temp = null;
                String desc = null;
                String ammontare = null;
                LocalDate oggi = LocalDate.now();
                /**
                 * Parametri richiesti: data, descrizione ed ammontare
                 */
                try {
                    do {
                        temp = oggi.toString();
                        temp = (String) JOptionPane.showInputDialog(null, "Inserire la data", "Data", JOptionPane.QUESTION_MESSAGE, null, null, temp);
                    } while (temp.isEmpty() || controlloData(temp) == false);

                    try {
                        do {
                            desc = JOptionPane.showInputDialog(null, "Inserire la descrizione", "Descrizione", JOptionPane.QUESTION_MESSAGE);
                        } while (desc.isEmpty());
                        try {
                            do {
                                ammontare = JOptionPane.showInputDialog(null, "Inserire l'ammontare", "Ammontare", JOptionPane.QUESTION_MESSAGE);
                            } while (temp.isEmpty() || controlloAmmontare(ammontare) == false);
                            String scelta = (String) combo.getSelectedItem();
                            Voce v;
                            LocalDate data = LocalDate.parse(temp);

                            /**
                             * In base al valore dell'ammontare si crea
                             * l'oggetto inerente
                             */
                            if (Double.parseDouble(ammontare) > 0) {
                                v = new Entrata();
                            } else {
                                v = new Uscita();
                            }
                            v.setData(data);
                            v.setDescrizione(desc);
                            v.setAmmontare(Double.parseDouble(ammontare));
                            vociTotali.add(v);

                            /**
                             * Una volta aggiunto la voce all'ArrayList
                             * "vociTotali" controlla se deve aggiungerlo anche
                             * all'ArrayList "vociEffettivi", ovvero le voci
                             * mostrate dalla tabella
                             */
                            switch (scelta) {
                                case "Oggi": {
                                    if (v.getData().equals(oggi)) {
                                        vociEffettivi.add(v);
                                    }
                                    break;
                                }
                                case "Ultima Settimana": {
                                    if (v.getData().isBefore(oggi.plusDays(1)) && v.getData().isAfter(oggi.minusWeeks(1))) {
                                        vociEffettivi.add(v);
                                    }
                                    break;
                                }
                                case "Ultimo Mese": {
                                    if (v.getData().isBefore(oggi.plusDays(1)) && v.getData().isAfter(oggi.minusMonths(1))) {
                                        vociEffettivi.add(v);
                                    }

                                    break;
                                }
                                case "Ultimo Anno": {
                                    if (v.getData().isBefore(oggi.plusDays(1)) && v.getData().isAfter(oggi.minusYears(1))) {
                                        vociEffettivi.add(v);
                                    }
                                    break;
                                }
                                case "Altro": {
                                    if (v.getData().isBefore(df.plusDays(1)) && v.getData().isAfter(di.minusDays(1))) {
                                        vociEffettivi.add(v);
                                    }
                                    break;
                                }
                                default: {
                                }
                            }
                            vt.fireTableDataChanged();
                            aggiornaSomma();
                        } catch (NullPointerException e) {
                            System.err.println(e);
                        }
                    } catch (NullPointerException e) {
                        System.err.println(e);
                    }
                } catch (NullPointerException e) {
                    System.err.println(e);
                }
            }
        });

        /**
         * Cliccando il JButton "cancella" si cancellano le righe selezionate
         */
        cancella.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                int selezionati[] = t.getSelectedRows();
                for (int i = 0; i < selezionati.length; i++) {
                    if (selezionati[i] < vociEffettivi.size()) {
                        vociTotali.remove(selezionati[i]);
                        vociEffettivi.remove(selezionati[i]);
                        for (int a = i; a < selezionati.length; a++) {
                            selezionati[a]--;
                        }
                    }
                }
                vt.fireTableDataChanged();
                aggiornaSomma();
            }
        });

        /**
         * Cliccando il JButton "salva" si salva la tabella(con tutte le sue
         * voci) in file scelto dall'utente
         */
        salva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String nome;
                /**
                 * Parametri richiesti: nome del file
                 */
                try {
                    do {
                        nome = (String) JOptionPane.showInputDialog(null, "Inserire il nome", "Salva", JOptionPane.QUESTION_MESSAGE);
                    } while (nome.isEmpty());

                    File f = new File("Files" + File.separator + nome);
                    FileOutputStream fout = null;
                    DataOutputStream outData = null; //Filtro per scrivere stringhe
                    try {
                        fout = new FileOutputStream(f);
                        outData = new DataOutputStream(fout);
                        for (int i = 0; i < vociTotali.size() - 1; i++) {
                            outData.writeBytes(vociTotali.get(i).toString(" ") + "\n");
                        }
                        if (vociTotali.size() > 0) {
                            outData.writeBytes(vociTotali.get(vociTotali.size() - 1).toString(" ").replace("\n", ""));
                        }
                        fout.close();
                        outData.close();
                        JOptionPane.showMessageDialog(null, "Salvataggio eseguito con successo");
                    } catch (IOException ex) {
                        System.err.println(ex);
                        JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NullPointerException e) {
                    System.err.println(e);
                }
            }
        });

        /**
         * Cliccando il JButton "carica" si carica le voci presenti in file
         * scelto dall'utente
         */
        carica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String nome;
                /**
                 * Parametri richiesti: nome del file
                 */
                try {
                    do {
                        nome = (String) JOptionPane.showInputDialog(null, "Inserire il nome", "Carica", JOptionPane.QUESTION_MESSAGE);
                    } while (nome.isEmpty());

                    File f = new File("Files" + File.separator + nome);

                    if (nome.contains("xlsx")) {//File Excel
                        FileInputStream fin = null;

                        try {
                            fin = new FileInputStream(f);
                            vociTotali.clear();
                        } catch (FileNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            Workbook workbook = WorkbookFactory.create(fin);
                            Sheet sheet = workbook.getSheetAt(0);
                            Row row;
                            Iterator<Row> iterator = sheet.iterator();//Interfaccia che fa parte del framework Collection in java(Utilizzata per scorrere una raccolta di oggetti)
                            if (iterator.hasNext()) {//Riga 0 dove sono presenti i nomi delle colonne
                                iterator.next();
                            }
                            Voce v;
                            LocalDate d;
                            Cell c;
                            String temp;
                            while (iterator.hasNext()) {
                                row = iterator.next();

                                c = row.getCell(0);
                                d = LocalDate.parse(c.getStringCellValue());

                                c = row.getCell(1);
                                temp = c.getStringCellValue();
                                c = row.getCell(2);

                                if (c.getNumericCellValue() > 0) {
                                    v = new Entrata();
                                } else {
                                    v = new Uscita();
                                }

                                v.setData(d);
                                v.setDescrizione(temp);
                                v.setAmmontare(c.getNumericCellValue());

                                vociTotali.add(v);
                            }
                            fin.close();
                            workbook.close();
                            aggiornaTabella(false);
                            JOptionPane.showMessageDialog(null, "Caricamento eseguito con successo");
                        } catch (IOException ex) {
                            System.err.println(ex);
                            JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        try {
                            Scanner sc = new Scanner(f);
                            vociTotali.clear();
                            Voce v;
                            String temp;
                            String ammontare;
                            LocalDate data = null;

                            if (nome.contains("csv")) {//File CSV
                                sc.useDelimiter(",|\\n");
                            }

                            while (sc.hasNextLine()) {
                                temp = sc.next();
                                data = LocalDate.parse(temp);
                                temp = sc.next();
                                ammontare = sc.next();
                                if (Double.parseDouble(ammontare) > 0) {
                                    v = new Entrata();
                                } else {
                                    v = new Uscita();
                                }
                                v.setData(data);
                                v.setDescrizione(temp);
                                v.setAmmontare(Double.parseDouble(ammontare));
                                vociTotali.add(v);
                            }
                            sc.close();
                            aggiornaTabella(false);
                            JOptionPane.showMessageDialog(null, "Caricamento eseguito con successo");
                        } catch (FileNotFoundException ex) {
                            System.err.println(ex);
                            JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NullPointerException e) {
                    System.err.println(e);
                }
            }
        });

        /**
         * Cliccando il JCombo è possibile scegliere l'intervallo di tempo
         */
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                aggiornaTabella(true);
            }
        });

        /**
         * Cliccando il JButton "esporta" si può esportare la tabella in tre
         * formati: CSV, Testo ed Excel
         */
        esporta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String scelta = (String) combo2.getSelectedItem();
                String separatore = null;
                File f = null;
                FileOutputStream fout = null;
                DataOutputStream outData = null; //Filtro per scrivere stringhe
                switch (scelta) {
                    case "CSV": {
                        separatore = ",";
                        f = new File("Files" + File.separator + "bilancio.csv");
                        break;
                    }
                    case "Testo": {
                        separatore = " ";
                        f = new File("Files" + File.separator + "bilancio.txt");
                        break;
                    }
                    case "Excel": {
                        f = new File("Files" + File.separator + "bilancio.xlsx");

                        Workbook workbook = new XSSFWorkbook();
                        Sheet sheet = workbook.createSheet();
                        Row row;
                        Cell cell;

                        row = sheet.createRow(0);

                        for (int i = 0; i < vt.getColumnCount(); i++) {
                            cell = row.createCell(i);
                            cell.setCellValue(vt.getColumnName(i));
                        }

                        for (int r = 1; r <= vociTotali.size(); r++) {
                            row = sheet.createRow(r);
                            for (int c = 0; c < vt.getColumnCount(); c++) {
                                cell = row.createCell(c);
                                Voce v = vociTotali.get(r - 1);
                                switch (c) {
                                    case 0: {
                                        cell.setCellValue(v.getData().toString());
                                        break;
                                    }
                                    case 1: {
                                        cell.setCellValue(v.getDescrizione());
                                        break;
                                    }
                                    case 2: {
                                        cell.setCellValue(v.getAmmontare());
                                        break;
                                    }
                                    default: {
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < vt.getColumnCount(); i++) {
                            sheet.autoSizeColumn(i);
                        }
                        try {
                            fout = new FileOutputStream(f);
                            workbook.write(fout);
                            fout.close();
                            workbook.close();
                            JOptionPane.showMessageDialog(null, "Salvataggio eseguito con successo");
                        } catch (IOException ex) {
                            System.err.println(ex);
                            JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                    default: {
                    }
                }
                if (!scelta.equals("Excel")) {
                    try {
                        fout = new FileOutputStream(f);
                        outData = new DataOutputStream(fout);
                        for (int i = 0; i < vociTotali.size() - 1; i++) {
                            outData.writeBytes(vociTotali.get(i).toString(separatore) + "\n");
                        }
                        if (vociTotali.size() > 0) {
                            outData.writeBytes(vociTotali.get(vociTotali.size() - 1).toString(separatore).replace("\n", ""));
                        }
                        fout.close();
                        JOptionPane.showMessageDialog(null, "Esportato con successo");
                    } catch (IOException ex) {
                        System.err.println(ex);
                        JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        /**
         * Cliccando il JButton "ricercaButton" si cerca nelle voci, se è
         * presente la stringa scritta
         */
        ricercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String contenuto = ricerca.getText();
                boolean trovato = false;
                if (!contenuto.isEmpty()) {
                    if ((!contenuto.equals(temp))) {
                        vociSelezionati.clear();
                    }
                    if (vociSelezionati.size() > 0) {
                        t.setRowSelectionInterval(vociSelezionati.get(0), vociSelezionati.get(0));
                        vociSelezionati.remove(0);
                    } else {

                        for (Voce v : vociEffettivi) {
                            if (v.getData().toString().contains(contenuto) || v.getDescrizione().contains(contenuto) || String.valueOf(v.getAmmontare()).contains(contenuto)) {
                                if (trovato != true) {
                                    t.setRowSelectionInterval(vociEffettivi.indexOf(v), vociEffettivi.indexOf(v));
                                    trovato = true;
                                } else {
                                    vociSelezionati.add(vociEffettivi.indexOf(v));
                                }
                            }
                        }
                        if (trovato == false) {
                            t.clearSelection();
                            JOptionPane.showMessageDialog(null, "Nessuna voce trovata");
                        }
                    }
                    temp = contenuto;
                } else {
                    JOptionPane.showMessageDialog(null, "Inserire prima un valore");
                }
            }
        });

        /**
         * Funzione che tiene aggiornato la somma(Entrate - Uscite) ad ogni
         * modifica della tabella
         */
        vt.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tme) {
                aggiornaSomma();
            }
        });
    }

    /**
     * Funzione che aggiorna l'intervallo di date
     *
     * @param d1 data fine
     * @param d2 data inizio
     */
    protected void aggiornaDate(String d1, String d2) {
        inizioData.setText(d2);
        fineData.setText(d1);
    }

    /**
     * Funzione per verificare la d2(data finale) sia dopo a d1(data inizio) od
     * oguale
     *
     * @param d1 data inizio
     * @param d2 data fine
     * @return true se d2(data finale) è dopo a d1(data inizio) od oguale
     */
    protected boolean confrontoDate(String d1, String d2) {
        LocalDate di = LocalDate.parse(d1);
        LocalDate df = LocalDate.parse(d2);
        if (df.isAfter(di) || df.equals(di)) {
            return true;
        }
        return false;
    }

    /**
     * Funzione utilizzata per aggiornare le voci presenti nella tabella in base
     * all'opzione selezionata
     *
     * @param altro utilizzato per sapere se dopo aver caricato un file nella
     * tabella è ancora presente l'opzione "Altro"
     */
    protected void aggiornaTabella(boolean altro) {
        String scelta = (String) combo.getSelectedItem();
        LocalDate temp = LocalDate.now();
        vociEffettivi.clear();
        ris.setText("");
        Collections.sort(vociTotali, Comparator.comparing((v) -> v.getData())); //Algoritmo utilizzato per l'ordinamento in base alla data(Collections)
        switch (scelta) {
            case "Oggi": {
                aggiornaDate(temp.toString(), temp.toString());
                for (Voce v : vociTotali) {
                    if (v.getData().equals(temp)) {
                        vociEffettivi.add(v);
                    }
                }
                break;
            }
            case "Ultima Settimana": {
                aggiornaDate(temp.toString(), temp.minusWeeks(1).plusDays(1).toString());
                for (Voce v : vociTotali) {
                    if (v.getData().isBefore(temp.plusDays(1)) && v.getData().isAfter(temp.minusWeeks(1))) {
                        vociEffettivi.add(v);
                    }
                }
                break;
            }
            case "Ultimo Mese": {
                aggiornaDate(temp.toString(), temp.minusMonths(1).plusDays(1).toString());
                for (Voce v : vociTotali) {
                    if (v.getData().isBefore(temp.plusDays(1)) && v.getData().isAfter(temp.minusMonths(1))) {
                        vociEffettivi.add(v);
                    }
                }
                break;
            }
            case "Ultimo Anno": {
                aggiornaDate(temp.toString(), temp.minusYears(1).plusDays(1).toString());
                for (Voce v : vociTotali) {
                    if (v.getData().isBefore(temp.plusDays(1)) && v.getData().isAfter(temp.minusYears(1))) {
                        vociEffettivi.add(v);
                    }
                }
                break;
            }
            /**
             * Si richiede l'intervallo di date
             */
            case "Altro": {
                if (altro) {
                    String dataInizio = null;
                    String dataFine = null;
                    LocalDate oggi = LocalDate.now();
                    /**
                     * Parametri richiesti: data inizio e data finale
                     */
                    try {
                        do {
                            dataInizio = oggi.toString();
                            dataInizio = (String) JOptionPane.showInputDialog(null, "Inserire la data iniziale", "Data Inizio", JOptionPane.QUESTION_MESSAGE, null, null, dataInizio);
                        } while (dataInizio.isEmpty() || controlloData(dataInizio) == false);
                        try {
                            do {
                                dataFine = oggi.toString();
                                dataFine = (String) JOptionPane.showInputDialog(null, "Inserire la data finale", "Data Finale", JOptionPane.QUESTION_MESSAGE, null, null, dataFine);
                            } while (dataFine.isEmpty() || controlloData(dataFine) == false || confrontoDate(dataInizio, dataFine) == false);
                            di = LocalDate.parse(dataInizio);
                            df = LocalDate.parse(dataFine);
                            aggiornaDate(df.toString(), di.toString());
                            for (Voce v : vociTotali) {
                                if (v.getData().isBefore(df.plusDays(1)) && v.getData().isAfter(di.minusDays(1))) {
                                    vociEffettivi.add(v);
                                }
                            }
                        } catch (NullPointerException e) {
                            inizioData.setText("");
                            fineData.setText("");
                            System.err.println(e);
                        }
                    } catch (NullPointerException e) {
                        inizioData.setText("");
                        fineData.setText("");
                        System.err.println(e);
                    }
                }
                break;
            }
            default: {
            }
        }

        //Collections.reverse(vociEffettivi);
        vt.fireTableDataChanged();
        aggiornaSomma();

    }

    /**
     * Funzione che aggiorna la somma, ovvero (Entrate - Uscite)
     */
    protected void aggiornaSomma() {
        double somma = 0;
        for (Voce v : vociEffettivi) {
            somma = somma + v.getAmmontare();
        }
        if (somma != 0) {
            ris.setText("   " + somma);
        }
    }

    /**
     * Funzione utilizzata per controllare che la stringa inserita quando si
     * aggiunge una riga è possibile convertirla in LocalDate
     *
     * @param s stringa da controllare
     * @return true se la conversione è possibile
     */
    protected boolean controlloData(String s) {
        try {
            LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Funzione utilizzata per controllare che il valore inserito quando si
     * aggiunge una riga è possibile convertirlo in double
     *
     * @param s stringa da controllare
     * @return true se la conversione è possibile
     */
    protected boolean controlloAmmontare(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Funzione utilizzata per salvare ogni minuto le voci della tabella in un
     * file temporaneo ogni minuto
     */
    @Override
    public void run() {
        File f = null;
        String tempDir = System.getProperty("java.io.tmpdir");//Appdata --> Local --> Temp / echo $TMPDIR
        //System.out.println(tempDir);
        FileOutputStream fout = null;
        DataOutputStream outData = null; //Filtro per scrivere stringhe
        while (true) {
            try {
                try {
                    f = File.createTempFile(tempDir + "temporaneo", ".txt");
                } catch (IOException ex) {
                    System.err.println(ex);
                    JOptionPane.showMessageDialog(null, ex, "Errore File Temporaneo", JOptionPane.ERROR_MESSAGE);
                }
                fout = new FileOutputStream(f);
                outData = new DataOutputStream(fout);
                for (int i = 0; i < vociTotali.size() - 1; i++) {
                    outData.writeBytes(vociTotali.get(i).toString(" ") + "\n");
                }
                if (vociTotali.size() > 0) {
                    outData.writeBytes(vociTotali.get(vociTotali.size() - 1).toString(" ").replace("\n", ""));
                }
                fout.close();
                outData.close();
            } catch (IOException ex) {
                System.err.println(ex);
                JOptionPane.showMessageDialog(null, ex, "Errore File Temporaneo", JOptionPane.ERROR_MESSAGE);
            }
            try {
                Thread.sleep(60 * 1000);//Ogni minuto crea un file temporaneo
                f.delete();
            } catch (InterruptedException ex) {
                System.err.println(ex);
                JOptionPane.showMessageDialog(null, ex, "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
