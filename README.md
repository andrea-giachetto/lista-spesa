# Lista della Spesa

Applicazione Java per la gestione di una lista della spesa con funzionalità complete di aggiunta, rimozione, ricerca e persistenza dei dati.

## Descrizione

Questa applicazione console permette di gestire una lista della spesa in modo interattivo. Gli articoli possono essere aggiunti con informazioni dettagliate (nome, categoria, prezzo, quantità), marcati come acquistati, e salvati/caricati da file CSV per mantenere i dati tra le sessioni.

## Funzionalità

1. **Aggiungi articolo**: Inserisci nuovi articoli con nome, categoria, prezzo unitario e quantità
2. **Visualizza lista**: Mostra tutti gli articoli con checkbox per lo stato di acquisto e riepilogo spesa
3. **Rimuovi articolo**: Elimina articoli dalla lista
4. **Cerca articolo**: Trova articoli per nome o categoria
5. **Marca come acquistato**: Segna/deseleziona articoli come acquistati
6. **Calcola totale spesa**: Visualizza statistiche dettagliate sulla spesa totale, acquisiti e rimanente
7. **Salva su file**: Esporta la lista in formato CSV (spesa.csv)
8. **Carica da file**: Importa la lista da file CSV esistente
9. **Svuota lista**: Elimina tutti gli articoli dalla lista e pulisce il file CSV (richiede conferma)
10. **Esci**: Chiude l'applicazione con opzione di salvataggio

## Requisiti

- Java 11 o superiore
- JDK installato per la compilazione

## Compilazione

```bash
javac Application.java
```

## Esecuzione

```bash
java Application
```

## Struttura Dati

L'applicazione utilizza:
- `List<Map<String, Object>>` per memorizzare gli articoli
- Ogni articolo è rappresentato come una `HashMap` con le seguenti chiavi:
  - `nome` (String): nome dell'articolo
  - `categoria` (String): categoria di appartenenza
  - `prezzo` (Double): prezzo unitario
  - `quantita` (Integer): quantità da acquistare
  - `acquistato` (Boolean): stato di acquisto

## Formato File CSV

Il file `spesa.csv` viene salvato nella directory corrente con il seguente formato:

```csv
Nome,Categoria,Prezzo,Quantita,Acquistato
Pane,Panetteria,1.5,2,false
Latte,Latticini,1.2,1,true
```

## Esempio di Utilizzo

```
╔═══════════════════════════════════════╗
║    GESTIONE LISTA DELLA SPESA        ║
╠═══════════════════════════════════════╣
║ 1. Aggiungi articolo                 ║
║ 2. Visualizza lista                  ║
║ 3. Rimuovi articolo                  ║
║ 4. Cerca articolo                    ║
║ 5. Marca come acquistato             ║
║ 6. Calcola totale spesa              ║
║ 7. Salva su file                     ║
║ 8. Carica da file                    ║
║ 9. Svuota lista                      ║
║ 10. Esci                             ║
╚═══════════════════════════════════════╝
Scegli (1-10):
```

### Aggiunta di un articolo

```
=== AGGIUNGI ARTICOLO ===
Nome articolo: Pane
Categoria: Panetteria
Prezzo unitario: 1.5
Quantità: 2
✓ Articolo aggiunto!
```

### Visualizzazione della lista

```
═══════════════════════════════════════
    LISTA DELLA SPESA
═══════════════════════════════════════

[ ] 1. Pane - Panetteria
       €1.50 x 2 = €3.00
[✓] 2. Latte - Latticini
       €1.20 x 1 = €1.20
═══════════════════════════════════════
Totale articoli: 2
Non acquistati: 1
Acquistati: 1
Spesa totale: €4.20
═══════════════════════════════════════
```

### Svuotamento della lista

```
=== SVUOTA LISTA ===
⚠ Sei sicuro di voler svuotare la lista? (s/n): s
✓ Lista svuotata con successo!
(2 articoli eliminati)
```

## Caratteristiche Tecniche

### Validazione Input
- Controllo su nomi e categorie vuote
- Validazione di prezzi e quantità positive
- Gestione errori per input non numerici con try-catch

### Gestione Errori
- Try-catch per la lettura di input non validi
- Gestione eccezioni per operazioni su file
- Messaggi di errore chiari e informativi

### User Experience
- Menu interattivo con interfaccia grafica ASCII
- Checkbox visivi per lo stato di acquisto (✓)
- Formattazione prezzi con 2 cifre decimali
- Conferma prima di uscire con opzione salvataggio

## Struttura del Codice

```
Application.java
├── Attributi
│   ├── articoli: List<Map<String, Object>>
│   └── scanner: Scanner
├── Costruttore
│   └── inizializzazione attributi
├── Metodi Menu
│   ├── mostraMenu()
│   └── elaboraScelta(int)
├── Operazioni Articoli
│   ├── aggiungiArticolo()
│   ├── visualizzaLista()
│   ├── rimuoviArticolo()
│   ├── cercaArticolo()
│   ├── marcaAcquistato()
│   └── calcolaTotale()
├── Gestione File
│   ├── salvasuFile()
│   └── caricaDaFile()
├── Utilità
│   ├── stampaArticolo()
│   └── visualizzaListaSemplice()
└── Main
    ├── main(String[] args)
    └── esegui()
```

## Note Implementative

### Perché List<Map<String, Object>>?
- **List**: mantiene l'ordine di inserimento degli articoli
- **Map**: permette di usare chiavi significative invece di indici numerici
- **Object**: permette di memorizzare tipi diversi (String, Double, Integer, Boolean)

### Gestione Scanner
- Il Scanner è un attributo di classe per essere accessibile da tutti i metodi
- `scanner.nextLine()` dopo `nextInt()`/`nextDouble()` per pulire il buffer

### Persistenza Dati
- Formato CSV per la compatibilità con altre applicazioni
- File salvato nella directory corrente (spesa.csv)

## Possibili Miglioramenti Futuri

- Organizzazione articoli per categoria
- Supporto per più liste separate
- Export in formati diversi (JSON, XML)
- Interfaccia grafica (GUI)
- Database per la persistenza
- Filtri avanzati e ordinamento
- Storico delle spese
- Budget e limiti di spesa

## Autore

Progetto didattico con commenti dettagliati per l'apprendimento di Java.