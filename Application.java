/*
 * LISTA DELLA SPESA - VERSIONE CON COMMENTI DETTAGLIATI
 * 
 * Questo file contiene commenti estesi che spiegano:
 * - PERCHÉ usiamo ogni tipo di dato
 * - PERCHÉ organizziamo il codice così
 * - COME funziona ogni parte
 * - COSA succede quando
 * 
 * OBIETTIVO: Capire NON SOLO il codice, ma la LOGICA dietro
 */

// ============================================
// IMPORT - Importiamo le librerie che servono
// ============================================

// Files: Serve per leggere/scrivere file CSV
// Lo usiamo per salvare la lista della spesa su disco
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ============================================
// CLASSE PRINCIPALE
// ============================================

public class Application {
    
    // ============================================
    // ATTRIBUTI DELLA CLASSE
    // ============================================
    
    /*
     * ATTRIBUTO 1: articoli
     * 
     * Tipo: List<Map<String, Object>>
     * 
     * COSA È?
     * - Una LISTA (ordine importante)
     * - Dove ogni elemento è una MAPPA
     * - Ogni mappa ha: chiavi (String) e valori (Object)
     * 
     * PERCHÉ List?
     * - Perché l'ordine importa
     * - L'utente vuole: "Articolo 1, Articolo 2, ecc."
     * - Se usassimo Set, perderemmo l'ordine
     * 
     * PERCHÉ Map (HashMap)?
     * - Perché ogni articolo ha più informazioni:
     *   * Nome
     *   * Categoria
     *   * Prezzo
     *   * Quantità
     *   * Acquistato (sì/no)
     * - Con HashMap, usiamo nomi significativi ("nome", "prezzo")
     * - Se usassimo array, avremmo indici: articolo[0], articolo[1], ecc.
     * - È più leggibile: articolo.get("nome")
     * 
     * PERCHÉ <Map<String, Object>>?
     * - <Map> = usiamo Map generics per type-safety
     * - <String> = le chiavi sono stringhe ("nome", "prezzo", ecc.)
     * - <Object> = i valori possono essere di qualsiasi tipo
     *   * "nome" → String ("Pane")
     *   * "prezzo" → Double (1.50)
     *   * "quantita" → Integer (2)
     *   * "acquistato" → Boolean (true/false)
     * 
     * QUANDO si INIZIALIZZA?
     * - Nel COSTRUTTORE (vedi sotto)
     * - Non qui, perché non è una costante
     */
    private List<Map<String, Object>> articoli;
    
    /*
     * ATTRIBUTO 2: scanner
     * 
     * Tipo: Scanner
     * 
     * COSA È?
     * - Un oggetto che legge input da tastiera
     * - System.in = standard input = tastiera
     * 
     * PERCHÉ Scanner?
     * - Perché vogliamo leggere dati dall'utente
     * - Scanner ha metodi comodi:
     *   * nextInt() - legge un numero intero
     *   * nextDouble() - legge un numero decimale
     *   * nextLine() - legge una linea di testo
     * 
     * PERCHÉ attributo (non variabile locale)?
     * - Perché TUTTE le funzioni lo usano
     * - Se fosse locale (inside una funzione), non potrebbero accedervi
     * - Scrivendo private Scanner scanner, lo rendo accessibile a TUTTA la classe
     * 
     * QUANDO si INIZIALIZZA?
     * - Nel COSTRUTTORE
     */
    private final Scanner scanner;
    
    // ============================================
    // COSTRUTTORE
    // ============================================
    
    /*
     * COSA È UN COSTRUTTORE?
     * - Una funzione speciale che viene eseguita quando crei un oggetto
     * - Serve per INIZIALIZZARE gli attributi
     * 
     * ESEMPIO di uso:
     * ListaDellaSpesa lista = new ListaDellaSpesa();
     *                              ↑
     *                    QUI viene chiamato il costruttore!
     */
    public Application() {
        
        /*
         * LINEA 1: this.articoli = new ArrayList<>();
         * 
         * COSA SUCCEDE?
         * - Creiamo un nuovo ArrayList VUOTO
         * - Lo assegniamo all'attributo "articoli"
         * 
         * PERCHÉ new ArrayList<>()?
         * - "new" = crea un nuovo oggetto in memoria
         * - ArrayList<> = lista dinamica (cresce/riduce automaticamente)
         * - () = costruttore di ArrayList (senza parametri = lista vuota)
         * 
         * PERCHÉ VUOTO?
         * - All'inizio non c'è nessun articolo
         * - L'utente li aggiungerà uno per uno, o verranno caricati dal file .csv
         * 
         * COSA SIGNIFICA <>?
         * - È il "generic type" di Java
         * - Dice: "questa lista contiene Map<String, Object>"
         * - Senza <>, Java darebbe warning
         * - Con <>, Java sa che tipo contiene la lista
         * 
         * PERCHÉ this.articoli?
         * - "this" = l'oggetto corrente
         * - "this.articoli" = l'attributo articoli di QUESTO oggetto
         * - Potremmo scrivere solo "articoli = ...", ma è meno chiaro
         * - È meglio essere espliciti
         */
        this.articoli = new ArrayList<>();
        
        /*
         * LINEA 2: this.scanner = new Scanner(System.in);
         * 
         * COSA SUCCEDE?
         * - Creiamo un nuovo Scanner che legge da System.in
         * - System.in = tastiera
         * - Lo assegniamo all'attributo "scanner"
         * 
         * PERCHÉ new Scanner(System.in)?
         * - "new" = crea un nuovo Scanner
         * - "System.in" = dice a Scanner di leggere dalla tastiera
         * 
         * ALTERNATIVA (NON USARE):
         * Scanner scanner = new Scanner(new File("dati.txt"));
         * Questo leggerebbe da un file, non dalla tastiera
         * 
         * PERCHÉ System.in?
         * - Perché vogliamo che l'utente digiti cose
         * - Questo è il modo standard in Java di leggere da tastiera
         */
        this.scanner = new Scanner(System.in);
    }
    
    // ============================================
    // METODI DI MENU
    // ============================================
    
    /*
     * METODO: mostraMenu()
     * 
     * COSA FA?
     * - Stampa il menu principale
     * - Mostra all'utente le opzioni (1-9)
     * 
     * PERCHÉ è un metodo separato?
     * - Code organization: separare la "UI" dal "logic"
     * - Riusabilità: potremmo mostrare il menu più volte
     * - Leggibilità: il main() è più pulito
     * 
     * PERCHÉ private?
     * - Perché è un dettaglio implementativo
     * - L'utente NON deve vederlo, usa main()
     * - È "internal", non "public API"
     */
    private void mostraMenu() {
        
        // System.out.println() stampa una riga
        // Le righe con box-drawing characters (╔ ║ ╚) creano un box carino
        // Non è necessario per il funzionamento, ma migliora la UX
        
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║    GESTIONE LISTA DELLA SPESA        ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1. Aggiungi articolo                 ║");
        System.out.println("║ 2. Visualizza lista                  ║");
        System.out.println("║ 3. Rimuovi articolo                  ║");
        System.out.println("║ 4. Cerca articolo                    ║");
        System.out.println("║ 5. Marca come acquistato             ║");
        System.out.println("║ 6. Calcola totale spesa              ║");
        System.out.println("║ 7. Salva su file                     ║");
        System.out.println("║ 8. Carica da file                    ║");
        System.out.println("║ 9. Svuota lista                      ║");
        System.out.println("║ 10. Esci                             ║");
        System.out.println("╚═══════════════════════════════════════╝");

        // System.out.print() (senza "ln") stampa SENZA andare a capo
        // Così il cursore rimane sulla stessa riga
        System.out.print("Scegli (1-10): ");
    }
    
    /*
     * METODO: elaboraScelta(int scelta)
     * 
     * COSA FA?
     * - Prende il numero che l'utente ha digitato
     * - Esegue l'azione corrispondente
     * 
     * PERCHÉ switch instead of if/else?
     * - if/else: if (scelta == 1) { ... } else if (scelta == 2) { ... }
     * - switch: switch(scelta) { case 1: ... }
     * - Switch è più leggibile quando hai TANTE opzioni
     * - Anche più veloce (saltella direttamente al case)
     * 
     * PARAMETRO: int scelta
     * - Il numero che l'utente ha scelto (1, 2, 3, etc.)
     * - Passato da main() quando chiama elaboraScelta(scelta)
     */
    private void elaboraScelta(int scelta) {
        
        // switch (variabile) { case valore: ... }
        // Java controlla il valore di "scelta" e va al case corrispondente
        switch (scelta) {
            
            // Se scelta == 1, esegui questo
            case 1:
                // Chiama il metodo per aggiungere un articolo
                aggiungiArticolo();
                // break: esce dal switch (senza break, continuerebbe al case 2!)
                break;
            
            case 2:
                // Mostra la lista
                visualizzaLista();
                break;
            
            case 3:
                // Rimuove un articolo
                rimuoviArticolo();
                break;
            
            case 4:
                // Cerca un articolo
                cercaArticolo();
                break;
            
            case 5:
                // Marca come acquistato
                marcaAcquistato();
                break;
            
            case 6:
                // Calcola il totale
                calcolaTotale();
                break;
            
            case 7:
                // Salva su file
                salvasuFile();
                break;
            
            case 8:
                // Carica da file
                caricaDaFile();
                break;

            case 9:
                // Svuota la lista
                svuotaLista();
                break;

            case 10:
                // Esci dal programma

                // Chiedi conferma all'utente
                System.out.print("\nVuoi salvare prima di uscire? (s/n): ");

                // Leggi la risposta
                String risposta = scanner.nextLine();

                // Se risposta è "s" (case-insensitive), salva
                if (risposta.equalsIgnoreCase("s")) {
                    salvasuFile();
                }

                // Stampa messaggio di bye
                System.out.println("\n✓ Grazie per aver usato il gestionale!");

                // Chiudi lo scanner (libera le risorse)
                scanner.close();

                // Esci dal programma
                // 0 = tutto ok
                // non-zero = errore
                System.exit(0);
                break;
            
            // Se nessun case corrisponde
            default:
                System.out.println("❌ Scelta non valida!");
        }
    }
    
    // ============================================
    // OPERAZIONI ARTICOLI
    // ============================================
    
    /*
     * METODO: aggiungiArticolo()
     * 
     * COSA FA?
     * 1. Chiede all'utente i dati dell'articolo
     * 2. Valida i dati
     * 3. Crea un HashMap con i dati
     * 4. Lo aggiunge alla lista
     */
    private void aggiungiArticolo() {
        
        // Titolo della sezione
        System.out.println("\n=== AGGIUNGI ARTICOLO ===");
        
        // STEP 1: Leggi il nome
        // System.out.print() (senza ln) stampa il prompt sulla stessa riga
        System.out.print("Nome articolo: ");
        
        // scanner.nextLine() legge una LINEA intera di testo
        // Ritorna una String
        String nome = scanner.nextLine();
        
        // STEP 2: Leggi la categoria
        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();
        
        // STEP 3: Leggi il prezzo
        System.out.print("Prezzo unitario: ");
        
        // scanner.nextDouble() legge un numero decimale
        // Se l'utente digita "abc", Scanner si infuria e lancia Exception
        // (Affrontiamo questo in un'altra lezione!)
        double prezzo = scanner.nextDouble();
        
        // STEP 4: Leggi la quantità
        System.out.print("Quantità: ");
        
        // scanner.nextInt() legge un numero intero
        int quantita = scanner.nextInt();
        
        /*
         * PERCHÉ scanner.nextLine() dopo nextInt()?
         * 
         * Problema: nextInt() legge il numero ma NON il newline
         * Esempio:
         * - Utente digita: "2" e preme ENTER
         * - nextInt() legge il "2"
         * - Ma il newline rimane nel buffer!
         * - Linea successiva: nextLine() leggerebbe il newline (stringa vuota)
         * 
         * Soluzione:
         * scanner.nextLine();  // Leggi il newline rimasto nel buffer
         * 
         * Questo "cancella" il buffer
         */
        scanner.nextLine();  // Cancella buffer
        
        // STEP 5: Valida i dati
        // Controlla che i dati siano sensati
        
        /*
         * PERCHÉ validare?
         * - Nome vuoto? Non ha senso
         * - Prezzo negativo? Non ha senso
         * - Quantità zero? Non ha senso
         * 
         * Se i dati non sono validi, fermati qui e torna al menu
         */
        if (nome.isEmpty() || categoria.isEmpty() || prezzo <= 0 || quantita <= 0) {
            System.out.println("❌ Dati non validi!");
            
            // return: esce dalla funzione
            // Il resto del codice NON viene eseguito
            return;
        }
        
        // STEP 6: Crea un HashMap per l'articolo
        // HashMap<K, V>: chiavi (K) e valori (V)
        // Qui: chiavi = String, valori = Object
        
        /*
         * PERCHÉ HashMap?
         * - Perché ogni articolo ha TANTI dati
         * - Con HashMap, usiamo nomi: "nome", "prezzo", ecc.
         * - È più leggibile che array[0], array[1], etc.
         */
        Map<String, Object> articolo = new HashMap<>();
        
        // Aggiungi i dati al HashMap
        // put(chiave, valore)
        
        /*
         * PERCHÉ put()?
         * - put() = "metti questa coppia chiave-valore nella mappa"
         * - Se la chiave esiste già, il valore viene sovrascritto
         * - Se non esiste, viene aggiunta
         * 
         * Esempi:
         * articolo.put("nome", "Pane");
         * - Chiave: "nome" (String)
         * - Valore: "Pane" (String)
         * 
         * articolo.put("prezzo", 1.50);
         * - Chiave: "prezzo" (String)
         * - Valore: 1.50 (Double)
         */
        articolo.put("nome", nome);
        articolo.put("categoria", categoria);
        articolo.put("prezzo", prezzo);
        articolo.put("quantita", quantita);
        
        /*
         * articolo.put("acquistato", false);
         * 
         * PERCHÉ false?
         * - Quando aggiungiamo un articolo, NON è ancora acquistato
         * - false = articolo NON ancora comprato
         * - L'utente lo markerà come acquistato quando lo compra
         */
        articolo.put("acquistato", false);
        
        // STEP 7: Aggiungi il HashMap alla lista
        // articoli.add() = "aggiungi questo elemento alla lista"
        
        /*
         * PERCHÉ add()?
         * - add() = metodo di ArrayList
         * - Aggiunge l'elemento in fondo alla lista
         * - Incrementa automaticamente la size
         * 
         * Esempio:
         * - Prima: articoli ha 2 elementi
         * - articoli.add(nuovoArticolo);
         * - Dopo: articoli ha 3 elementi
         */
        articoli.add(articolo);
        
        // Stampa messaggio di successo
        System.out.println("✓ Articolo aggiunto!");
    }
    
    /*
     * METODO: visualizzaLista()
     * 
     * COSA FA?
     * - Stampa TUTTI gli articoli con i loro dettagli
     * - Mostra il totale di articoli, acquistati, spesa totale
     * 
     * COMPLESSO? Sì, ma vediamo passo per passo...
     */
    private void visualizzaLista() {
        
        // Stampa titolo con decorazioni
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("    LISTA DELLA SPESA");
        System.out.println("═══════════════════════════════════════\n");
        
        // Controlla se la lista è vuota
        // articoli.isEmpty() ritorna true se non c'è nessun elemento
        
        /*
         * PERCHÉ controllare?
         * - Se la lista è vuota, non c'è niente da mostrare
         * - Meglio dire "La lista è vuota" che mostrare niente
         */
        if (articoli.isEmpty()) {
            System.out.println("La lista è vuota");
            System.out.println("═══════════════════════════════════════");
            
            // Torna al menu
            return;
        }
        
        // Inizializza variabili per il riepilogo
        
        /*
         * int numero = 1;
         * - Contatore per mostrare "1. Pane", "2. Latte", etc.
         * - Inizia da 1 (non da 0, come gli umani contano)
         */
        int numero = 1;
        
        /*
         * int acquistati = 0;
         * - Contatore di quanti articoli sono già acquistati
         */
        int acquistati = 0;
        
        /*
         * double spesaTotale = 0;
         * - Somma di tutti i subtotali (prezzo × quantità)
         */
        double spesaTotale = 0;
        
        // LOOP: For-each su tutti gli articoli
        
        /*
         * for (Map<String, Object> articolo : articoli)
         * 
         * COSA SIGNIFICA?
         * - articoli = la lista che vogliamo iterare
         * - Map<String, Object> = il tipo di ogni elemento
         * - articolo = nome della variabile di loop (l'elemento corrente)
         * - : = "in"
         * 
         * TRADUZIONE NATURALE:
         * "Per ogni articolo NELLA lista articoli"
         * 
         * COME FUNZIONA?
         * - Iterazione 1: articolo = primo elemento, numero = 1
         * - Iterazione 2: articolo = secondo elemento, numero = 2
         * - ... e così via
         */
        for (Map<String, Object> articolo : articoli) {
            
            // Stampa l'articolo (vedi metodo stampaArticolo sotto)
            stampaArticolo(numero, articolo);
            
            // Controlla se questo articolo è acquistato
            // (boolean) = cast (conversione di tipo)
            
            /*
             * PERCHÉ cast?
             * - articolo.get("acquistato") ritorna Object
             * - Ma sappiamo che è un Boolean
             * - (boolean) dice: "converti questo Object a boolean"
             * 
             * Se non mettessimo il cast:
             * boolean acquistato = articolo.get("acquistato");  // ERRORE!
             * Object non è boolean!
             * 
             * Con il cast:
             * boolean acquistato = (boolean) articolo.get("acquistato");  // OK!
             */
            if ((boolean) articolo.get("acquistato")) {
                
                // Se è acquistato, incrementa il contatore
                acquistati++;
            }
            
            // Calcola il subtotale di questo articolo
            double prezzo = (double) articolo.get("prezzo");
            int quantita = (int) articolo.get("quantita");
            double subtotale = prezzo * quantita;
            
            // Aggiungi al totale
            spesaTotale += subtotale;
            
            // Incrementa il numero per il prossimo articolo
            numero++;
        }
        
        // Stampa riepilogo
        System.out.println("═══════════════════════════════════════");
        System.out.println("Totale articoli: " + articoli.size());
        
        // .size() = numero di elementi nella lista
        System.out.println("Non acquistati: " + (articoli.size() - acquistati));
        System.out.println("Acquistati: " + acquistati);
        
        // printf() permette di formattare numeri
        // %.2f = numero decimale con 2 cifre dopo la virgola
        System.out.printf("Spesa totale: €%.2f\n", spesaTotale);
        System.out.println("═══════════════════════════════════════");
    }
    
    /*
     * METODO: stampaArticolo(int numero, Map<String, Object> articolo)
     * 
     * COSA FA?
     * - Stampa UN singolo articolo con il suo checkbox
     * 
     * PARAMETRI:
     * - numero: il numero sequenziale (1, 2, 3, ...)
     * - articolo: l'articolo da stampare
     * 
     * PERCHÉ metodo separato?
     * - Code reuse: visualizzaLista() lo chiama
     * - Ma lo chiama anche cercaArticolo()
     * - Se non fosse separato, avremmo codice duplicato
     * - DRY principle: Don't Repeat Yourself
     */
    private void stampaArticolo(int numero, Map<String, Object> articolo) {
        
        // Leggi lo stato di acquisto
        boolean acquistato = (boolean) articolo.get("acquistato");
        
        // Crea il checkbox
        // ternary operator: condizione ? valore_se_true : valore_se_false
        
        /*
         * String checkbox = acquistato ? "[✓]" : "[ ]";
         * 
         * COSA SIGNIFICA?
         * - Se acquistato == true: usa "[✓]"
         * - Se acquistato == false: usa "[ ]"
         * 
         * ALTERNATIVA (if/else):
         * String checkbox;
         * if (acquistato) {
         *     checkbox = "[✓]";
         * } else {
         *     checkbox = "[ ]";
         * }
         * 
         * Ma il ternary operator è più conciso
         */
        String checkbox = acquistato ? "[✓]" : "[ ]";
        
        // Estrai tutti i dati dall'articolo
        String nome = (String) articolo.get("nome");
        String categoria = (String) articolo.get("categoria");
        double prezzo = (double) articolo.get("prezzo");
        int quantita = (int) articolo.get("quantita");
        
        // Calcola il subtotale
        double subtotale = prezzo * quantita;
        
        // Stampa la riga dell'articolo
        System.out.println(checkbox + " " + numero + ". " + nome + " - " + categoria);
        
        // Stampa prezzo e quantità
        // printf() = formatted print
        // %.2f = numero decimale con 2 cifre dopo la virgola
        System.out.printf("       €%.2f x %d = €%.2f\n", prezzo, quantita, subtotale);
    }
    
    /*
     * METODO: rimuoviArticolo()
     * 
     * COSA FA?
     * 1. Mostra la lista
     * 2. Chiede quale articolo rimuovere (numero)
     * 3. Rimuove dalla lista
     * 
     * LOGICA:
     * - L'utente dice: "Rimuovi articolo 2"
     * - Noi andiamo all'indice 2-1 = 1 (perché gli array partono da 0)
     * - Lo rimuoviamo
     */
    private void rimuoviArticolo() {
        
        System.out.println("\n=== RIMUOVI ARTICOLO ===");
        
        // Controlla se la lista è vuota
        if (articoli.isEmpty()) {
            System.out.println("La lista è vuota");
            return;
        }
        
        // Mostra la lista semplice (solo nomi)
        visualizzaListaSemplice();
        
        // Chiedi quale articolo rimuovere
        System.out.print("Quale articolo rimuovere? (numero): ");
        
        // try-catch: gestisce errori
        
        /*
         * COSA POTREBBE ANDARE MALE?
         * - L'utente digita "abc" invece di un numero
         * - nextInt() si infuria e lancia NumberFormatException
         * 
         * SENZA try-catch:
         * - L'app crasha
         * 
         * CON try-catch:
         * - Catturiamo l'eccezione
         * - Stampiamo un messaggio d'errore
         * - Tornaimo al menu (senza crashare)
         */
        try {
            
            // Leggi il numero
            int numero = scanner.nextInt();
            scanner.nextLine();  // Cancella buffer
            
            // Controlla se il numero è valido
            
            /*
             * numero > 0: il numero deve essere positivo
             * numero <= articoli.size(): il numero non può essere > della lista
             * 
             * Esempi:
             * - Se la lista ha 3 articoli:
             *   * numero 1, 2, 3 = OK
             *   * numero 0, 4, -1 = NO
             */
            if (numero > 0 && numero <= articoli.size()) {
                
                // Ottieni l'articolo in posizione numero-1
                // Perché numero-1? Perché l'utente conta da 1, ma gli array da 0
                
                /*
                 * L'utente dice: "Rimuovi articolo 1"
                 * Ma internamente è l'indice 0!
                 * Perciò: numero - 1 = 1 - 1 = 0
                 */
                Map<String, Object> articolo = articoli.get(numero - 1);
                
                // Leggi il nome (per stamparlo nel messaggio)
                String nome = (String) articolo.get("nome");
                
                // Rimuovi dalla lista
                articoli.remove(numero - 1);
                
                // Stampa messaggio di successo
                System.out.println("✓ \"" + nome + "\" rimosso dalla lista");
                
            } else {
                
                // Il numero non è valido
                System.out.println("❌ Numero non valido!");
            }
            
        } catch (Exception e) {
            
            // Se c'è un'eccezione (es. l'utente digita "abc"):
            System.out.println("❌ Errore input!");
            
            // Cancella il buffer (l'input invalido è rimasto lì)
            scanner.nextLine();
        }
    }
    
    // ... (i metodi rimanenti: cercaArticolo, marcaAcquistato, calcolaTotale, 
    //      salvasuFile, caricaDaFile seguono la stessa logica)
    // ... vedi il file originale per i dettagli di questi metodi ...
    
    /*
     * PER BREVITÀ, ho omesso i commenti dettagliati per gli altri metodi
     * (cercaArticolo, marcaAcquistato, calcolaTotale, salvasuFile, caricaDaFile)
     * 
     * Ma la logica è la STESSA:
     * 1. Leggi dati
     * 2. Valida
     * 3. Processa
     * 4. Stampa risultato
     */
    
    private void cercaArticolo() {
        System.out.println("\n=== CERCA ARTICOLO ===");
        System.out.print("Cosa cerchi? ");
        String ricerca = scanner.nextLine().toLowerCase();
        
        boolean trovato = false;
        int numero = 1;
        
        for (Map<String, Object> articolo : articoli) {
            String nome = ((String) articolo.get("nome")).toLowerCase();
            String categoria = ((String) articolo.get("categoria")).toLowerCase();
            
            if (nome.contains(ricerca) || categoria.contains(ricerca)) {
                if (!trovato) {
                    System.out.println("\n✓ Trovato!");
                    trovato = true;
                }
                stampaArticolo(numero, articolo);
                System.out.println();
            }
            numero++;
        }
        
        if (!trovato) {
            System.out.println("❌ Nessun articolo trovato");
        }
    }
    
    private void marcaAcquistato() {
        System.out.println("\n=== MARCA COME ACQUISTATO ===");
        
        if (articoli.isEmpty()) {
            System.out.println("La lista è vuota");
            return;
        }
        
        visualizzaListaSemplice();
        System.out.print("Quale articolo hai acquistato? (numero): ");
        try {
            int numero = scanner.nextInt();
            scanner.nextLine();
            
            if (numero > 0 && numero <= articoli.size()) {
                Map<String, Object> articolo = articoli.get(numero - 1);
                String nome = (String) articolo.get("nome");
                boolean acquistato = (boolean) articolo.get("acquistato");
                articolo.put("acquistato", !acquistato);
                
                String stato = !acquistato ? "acquistato" : "non acquistato";
                System.out.println("✓ \"" + nome + "\" segnato come " + stato);
            } else {
                System.out.println("❌ Numero non valido!");
            }
        } catch (Exception e) {
            System.out.println("❌ Errore input!");
            scanner.nextLine();
        }
    }
    
    private void calcolaTotale() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║     CALCOLO SPESA TOTALE             ║");
        System.out.println("╠═══════════════════════════════════════╣");
        
        if (articoli.isEmpty()) {
            System.out.println("║ La lista è vuota                      ║");
            System.out.println("╚═══════════════════════════════════════╝");
            return;
        }
        
        double totale = 0;
        double totalNonAcquistati = 0;
        double totalAcquistati = 0;
        int acquistati = 0;
        
        for (Map<String, Object> articolo : articoli) {
            double prezzo = (double) articolo.get("prezzo");
            int quantita = (int) articolo.get("quantita");
            double subtotale = prezzo * quantita;
            boolean acquistato = (boolean) articolo.get("acquistato");
            
            totale += subtotale;
            
            if (acquistato) {
                totalAcquistati += subtotale;
                acquistati++;
            } else {
                totalNonAcquistati += subtotale;
            }
        }
        
        System.out.printf("║ Articoli totali: %-23d║\n", articoli.size());
        System.out.printf("║ Non acquistati: %-24d║\n", articoli.size() - acquistati);
        System.out.printf("║ Acquistati: %-27d║\n", acquistati);
        System.out.println("║                                       ║");
        System.out.printf("║ Spesa totale: €%-29.2f║\n", totale);
        System.out.printf("║ Spesa acquisiti: €%-26.2f║\n", totalAcquistati);
        System.out.printf("║ Spesa rimanente: €%-26.2f║\n", totalNonAcquistati);
        System.out.println("╚═══════════════════════════════════════╝");
    }
    
    private void salvasuFile() {
        System.out.println("\n=== SALVA SU FILE ===");
        
        try {
            StringBuilder csv = new StringBuilder();
            csv.append("Nome,Categoria,Prezzo,Quantita,Acquistato\n");
            
            for (Map<String, Object> articolo : articoli) {
                csv.append((String) articolo.get("nome")).append(",");
                csv.append((String) articolo.get("categoria")).append(",");
                csv.append((double) articolo.get("prezzo")).append(",");
                csv.append((int) articolo.get("quantita")).append(",");
                csv.append((boolean) articolo.get("acquistato")).append("\n");
            }
            
            Files.writeString(Paths.get("spesa.csv"), csv.toString());
            System.out.println("✓ Lista salvata in \"spesa.csv\"!");
            System.out.println("(" + articoli.size() + " articoli salvati)");
            
        } catch (Exception e) {
            System.out.println("❌ Errore durante il salvataggio: " + e.getMessage());
        }
    }
    
    private void caricaDaFile() {
        System.out.println("\n=== CARICA DA FILE ===");
        
        try {
            List<String> linee = Files.readAllLines(Paths.get("spesa.csv"));
            
            articoli.clear();
            
            for (int i = 1; i < linee.size(); i++) {
                String linea = linee.get(i);
                String[] campi = linea.split(",");
                
                String nome = campi[0];
                String categoria = campi[1];
                double prezzo = Double.parseDouble(campi[2]);
                int quantita = Integer.parseInt(campi[3]);
                boolean acquistato = Boolean.parseBoolean(campi[4]);
                
                Map<String, Object> articolo = new HashMap<>();
                articolo.put("nome", nome);
                articolo.put("categoria", categoria);
                articolo.put("prezzo", prezzo);
                articolo.put("quantita", quantita);
                articolo.put("acquistato", acquistato);
                
                articoli.add(articolo);
            }
            
            System.out.println("✓ Lista caricata da \"spesa.csv\"!");
            System.out.println("(" + articoli.size() + " articoli caricati)");
            
        } catch (Exception e) {
            System.out.println("❌ Errore durante il caricamento: " + e.getMessage());
            System.out.println("(Il file potrebbe non esistere)");
        }
    }

    /*
     * METODO: svuotaLista()
     *
     * COSA FA?
     * - Svuota completamente la lista in memoria
     * - Pulisce il file spesa.csv
     *
     * PERCHÉ questo metodo?
     * - Permette di ricominciare da zero
     * - Utile quando si vuole creare una nuova lista da zero
     * - Chiede conferma per evitare cancellazioni accidentali
     */
    private void svuotaLista() {
        System.out.println("\n=== SVUOTA LISTA ===");

        // Controlla se la lista è già vuota
        if (articoli.isEmpty()) {
            System.out.println("⚠ La lista è già vuota!");
            return;
        }

        // Chiedi conferma all'utente
        System.out.print("⚠ Sei sicuro di voler svuotare la lista? (s/n): ");
        String conferma = scanner.nextLine();

        // Se l'utente conferma
        if (conferma.equalsIgnoreCase("s")) {

            // Conta quanti articoli verranno eliminati
            int numeroArticoli = articoli.size();

            // Svuota la lista in memoria
            articoli.clear();

            // Svuota o elimina il file CSV
            try {
                // Opzione 1: Sovrascrive il file con solo l'header
                StringBuilder csv = new StringBuilder();
                csv.append("Nome,Categoria,Prezzo,Quantita,Acquistato\n");
                Files.writeString(Paths.get("spesa.csv"), csv.toString());

                // Messaggio di successo
                System.out.println("✓ Lista svuotata con successo!");
                System.out.println("(" + numeroArticoli + " articoli eliminati)");

            } catch (Exception e) {
                // Se c'è un errore durante la pulizia del file
                System.out.println("✓ Lista in memoria svuotata!");
                System.out.println("⚠ Attenzione: errore durante la pulizia del file: " + e.getMessage());
            }

        } else {
            // L'utente ha annullato
            System.out.println("✗ Operazione annullata");
        }
    }

    private void visualizzaListaSemplice() {
        int numero = 1;
        for (Map<String, Object> articolo : articoli) {
            boolean acquistato = (boolean) articolo.get("acquistato");
            String checkbox = acquistato ? "[✓]" : "[ ]";
            System.out.println(checkbox + " " + numero + ". " + articolo.get("nome"));
            numero++;
        }
        System.out.println();
    }
    
    // ============================================
    // MAIN - PUNTO DI INGRESSO
    // ============================================
    
    /*
     * METODO: main(String[] args)
     * 
     * COSA SIGNIFICA?
     * - Il PUNTO DI INGRESSO del programma
     * - Quando avvii l'app, parte da qui
     * 
     * FIRMA:
     * - public: chiunque può chiamarla
     * - static: non serve un'istanza di Application
     * - void: non ritorna niente
     * - String[] args: argomenti da riga di comando (non usiamo)
     */
    public static void main(String[] args) {
        
        // Crea un'istanza di Application
        // new = crea un nuovo oggetto
        // Application() = chiama il costruttore
        
        /*
         * COSA SUCCEDE?
         * 1. Viene allocata memoria per il nuovo oggetto
         * 2. Viene chiamato il costruttore
         * 3. Nel costruttore:
         *    - articoli viene inizializzato come ArrayList
         *    - scanner viene inizializzato come Scanner(System.in)
         * 4. L'oggetto è pronto all'uso
         */
        Application app = new Application();
        
        // Chiama il metodo esegui()
        // Questo avvia il ciclo principale del programma
        app.esegui();
    }
    
    // ============================================
    // METODO PRINCIPALE DEL CICLO
    // ============================================
    
    /*
     * METODO: esegui()
     * 
     * COSA FA?
     * - Avvia il ciclo principale ("event loop")
     * - Mostra il menu, legge la scelta, esegue l'azione
     * - Ripete finché l'utente non sceglie "Esci"
     */
    public void esegui() {
        
        // while (true) = ciclo infinito
        // L'unico modo per uscire è con System.exit(0) (nel case 9)
        
        /*
         * PERCHÉ while(true)?
         * - Vogliamo che il programma continui a girare
         * - L'utente sceglie un'azione, l'app la fa, torna al menu
         * - Finché l'utente non sceglie "Esci"
         * 
         * ALTERNATIVA:
         * boolean continua = true;
         * while (continua) { ... continua = false; }
         * 
         * Ma while(true) è più semplice
         */
        while (true) {
            
            // Mostra il menu
            mostraMenu();
            
            // try-catch: gestisce errori nell'input
            try {
                
                // Leggi la scelta dell'utente
                // nextInt() legge un numero intero
                int scelta = scanner.nextInt();
                
                // Cancella il newline dal buffer
                scanner.nextLine();
                
                // Elabora la scelta
                // (vedi il metodo elaboraScelta)
                elaboraScelta(scelta);
                
            } catch (Exception e) {
                
                // Se c'è un errore (es. l'utente digita "abc"):
                System.out.println("❌ Errore input!");
                
                // Pulisci il buffer
                scanner.nextLine();
                
                // Il ciclo continua, torna a mostraMenu()
            }
        }
    }
}
