/*
 * SOLUZIONE COMPLETA: LISTA DELLA SPESA
 * 
 * Implementa tutte le funzionalità:
 * - Aggiungi articolo
 * - Visualizza lista
 * - Rimuovi articolo
 * - Cerca articolo
 * - Marca come acquistato
 * - Calcola totale
 * - Salva/Carica da file
 * - Menu principale
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ListaDellaSpesa {
    
    // Attributi
    private List<Map<String, Object>> articoli;
    private Scanner scanner;
    
    // Costruttore
    public ListaDellaSpesa() {
        this.articoli = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    
    // ============================================
    // MENU
    // ============================================
    
    private void mostraMenu() {
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
        System.out.println("║ 9. Esci                              ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print("Scegli (1-9): ");
    }
    
    private void elaboraScelta(int scelta) {
        switch (scelta) {
            case 1:
                aggiungiArticolo();
                break;
            case 2:
                visualizzaLista();
                break;
            case 3:
                rimuoviArticolo();
                break;
            case 4:
                cercaArticolo();
                break;
            case 5:
                marcaAcquistato();
                break;
            case 6:
                calcolaTotale();
                break;
            case 7:
                salvasuFile();
                break;
            case 8:
                caricaDaFile();
                break;
            case 9:
                System.out.print("\nVuoi salvare prima di uscire? (s/n): ");
                String risposta = scanner.nextLine();
                if (risposta.equalsIgnoreCase("s")) {
                    salvasuFile();
                }
                System.out.println("\n✓ Grazie per aver usato il gestionale!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("❌ Scelta non valida!");
        }
    }
    
    // ============================================
    // OPERAZIONI ARTICOLI
    // ============================================
    
    private void aggiungiArticolo() {
        System.out.println("\n=== AGGIUNGI ARTICOLO ===");
        
        System.out.print("Nome articolo: ");
        String nome = scanner.nextLine();
        
        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();
        
        System.out.print("Prezzo unitario: ");
        double prezzo = scanner.nextDouble();
        
        System.out.print("Quantità: ");
        int quantita = scanner.nextInt();
        scanner.nextLine();  // Cancella buffer
        
        // Validazione
        if (nome.isEmpty() || categoria.isEmpty() || prezzo <= 0 || quantita <= 0) {
            System.out.println("❌ Dati non validi!");
            return;
        }
        
        // Crea articolo
        Map<String, Object> articolo = new HashMap<>();
        articolo.put("nome", nome);
        articolo.put("categoria", categoria);
        articolo.put("prezzo", prezzo);
        articolo.put("quantita", quantita);
        articolo.put("acquistato", false);
        
        articoli.add(articolo);
        System.out.println("✓ Articolo aggiunto!");
    }
    
    private void visualizzaLista() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("    LISTA DELLA SPESA");
        System.out.println("═══════════════════════════════════════\n");
        
        if (articoli.isEmpty()) {
            System.out.println("La lista è vuota");
            System.out.println("═══════════════════════════════════════");
            return;
        }
        
        // Stampa articoli
        int numero = 1;
        int acquistati = 0;
        double spesaTotale = 0;
        
        for (Map<String, Object> articolo : articoli) {
            stampaArticolo(numero, articolo);
            
            if ((boolean) articolo.get("acquistato")) {
                acquistati++;
            }
            
            double prezzo = (double) articolo.get("prezzo");
            int quantita = (int) articolo.get("quantita");
            spesaTotale += prezzo * quantita;
            
            numero++;
        }
        
        // Riepilogo
        System.out.println("═══════════════════════════════════════");
        System.out.println("Totale articoli: " + articoli.size());
        System.out.println("Non acquistati: " + (articoli.size() - acquistati));
        System.out.println("Acquistati: " + acquistati);
        System.out.printf("Spesa totale: €%.2f\n", spesaTotale);
        System.out.println("═══════════════════════════════════════");
    }
    
    private void stampaArticolo(int numero, Map<String, Object> articolo) {
        boolean acquistato = (boolean) articolo.get("acquistato");
        String checkbox = acquistato ? "[✓]" : "[ ]";
        
        String nome = (String) articolo.get("nome");
        String categoria = (String) articolo.get("categoria");
        double prezzo = (double) articolo.get("prezzo");
        int quantita = (int) articolo.get("quantita");
        double subtotale = prezzo * quantita;
        
        System.out.println(checkbox + " " + numero + ". " + nome + " - " + categoria);
        System.out.printf("       €%.2f x %d = €%.2f\n", prezzo, quantita, subtotale);
    }
    
    private void rimuoviArticolo() {
        System.out.println("\n=== RIMUOVI ARTICOLO ===");
        
        if (articoli.isEmpty()) {
            System.out.println("La lista è vuota");
            return;
        }
        
        visualizzaListaSemplice();
        
        System.out.print("Quale articolo rimuovere? (numero): ");
        try {
            int numero = scanner.nextInt();
            scanner.nextLine();
            
            if (numero > 0 && numero <= articoli.size()) {
                Map<String, Object> articolo = articoli.get(numero - 1);
                String nome = (String) articolo.get("nome");
                articoli.remove(numero - 1);
                System.out.println("✓ \"" + nome + "\" rimosso dalla lista");
            } else {
                System.out.println("❌ Numero non valido!");
            }
        } catch (Exception e) {
            System.out.println("❌ Errore input!");
            scanner.nextLine();
        }
    }
    
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
    
    // ============================================
    // CALCOLI
    // ============================================
    
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
    
    // ============================================
    // FILE
    // ============================================
    
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
            
            // Svuota lista attuale
            articoli.clear();
            
            // Salta header
            for (int i = 1; i < linee.size(); i++) {
                String linea = linee.get(i);
                String[] campi = linea.split(",");
                
                String nome = campi[0];
                String categoria = campi[1];
                double prezzo = Double.parseDouble(campi[2]);
                int quantita = Integer.parseInt(campi[3]);
                boolean acquistato = Boolean.parseBoolean(campi[4]);
                
                // Crea articolo
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
    
    // ============================================
    // UTILITY
    // ============================================
    
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
    // MAIN
    // ============================================
    
    public void esegui() {
        while (true) {
            mostraMenu();
            
            try {
                int scelta = scanner.nextInt();
                scanner.nextLine();  // Cancella buffer
                elaboraScelta(scelta);
            } catch (Exception e) {
                System.out.println("❌ Errore input!");
                scanner.nextLine();
            }
        }
    }
    
    public static void main(String[] args) {
        ListaDellaSpesa lista = new ListaDellaSpesa();
        lista.esegui();
    }
}