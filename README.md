
# Gestione Bilancio

Il presente progetto si propone di descrivere e sviluppare una applicazione Java che abbia le seguenti 
funzionalità: 
- Gestione del bilancio; 
- Salvataggio e caricamento del bilancio; 
- Ricerca di informazioni nel bilancio; 
- Esportazione del bilancio in diversi formati.

La gestione del bilancio prevede la visualizzazione di una tabella delle voci di bilancio con le seguenti 
informazioni: 
- Data 
- Descrizione 
- Ammontare 

Ogni voce può rappresentare una entrata o una spesa, le quali l'utente potrà:

- aggiungere
- modificare
- cancellare

## Visualizzazione delle voci

Quando l’utente aggiunge una voce al bilancio, l’applicazione deve proporre come data di default quella odierna, che l’utente può comunque cambiare. 

L’utente può decidere se visualizzare le 
informazioni di un singolo giorno, di una settimana, di un mese, oppure di un anno. 

In ogni caso, la tabella deve mostrare alla fine la somma algebrica delle voci (somma delle entrate meno somma delle uscite).  

Infine, si deve dare anche la possibilità all’utente di selezionare un periodo arbitrario di tempo, specificando il giorno di inizio e quello di fine.

## Salvataggio e caricamento del bilancio

L’utente deve avere la possibilità di salvare il bilancio su un file, specificandone il nome, e di ricaricare il bilancio specificando il nome del file salvato in precedenza. 

Nel caso in cui si tenti di salvare il bilancio in un file che esiste già, deve essere chiesto 
all’utente se desidera sovrascrivere il file esistente.

Inoltre, si implementa un meccanismo di salvataggio automatico basato su un thread che periodicamente salva le informazioni in un file temporaneo.
## Ricerca di informazioni nel bilancio

L’utente deve avere la possibilità di effettuare delle ricerche nel bilancio. La ricerca si basa su testo libero 
che può essere una parte del testo contenuto in una voce. 

La ricerca deve evidenziare la prima cella/riga che contiene il testo cercato; l’applicazione deve permettere 
all’utente di continuare la ricerca per evidenziare man mano le celle/righe successive che rispondono ai 
requisiti (ad esempio, tramite un bottone “successivo”, o usando lo stesso bottone della ricerca). 
