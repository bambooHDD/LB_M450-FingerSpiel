# 1. Anforderungsanalyse

| **ID** | **Typ**       | **Kategorie**          | **Beschreibung**                                              | **Priorität** | **Messgrösse/Zielwert** | **Akzeptanzkriterium**     | **Auswirkung**       |
| ------ | ------------- | ---------------------- | ------------------------------------------------------------- | ------------- | ----------------------- | -------------------------- | -------------------- |
| F1     | Funktional    | Spieler-Management     | Implementierung von MenschSpieler mit manueller Eingabe       | Hoch          | -                       | Spieler kann Züge eingeben | -                    |
| F2     | Funktional    | Spieler-Management     | Implementierung von AISpieler mit automatischer Zugberechnung | Hoch          | -                       | KI macht gültige Züge      | -                    |
| F3     | Funktional    | Spiel-Initialisierung  | Erstellung von zwei Spielern                                  | Hoch          | -                       | Zwei Spieler vorhanden     | -                    |
| F4     | Funktional    | Spiel-Initialisierung  | Standard-Fingeranzahl (1 pro Hand)                            | Hoch          | -                       | Korrekte Initialisierung   | -                    |
| F5     | Funktional    | Spielzüge              | Gültige Quellhand (L/R)                                       | Hoch          | -                       | Nur L/R erlaubt            | -                    |
| F6     | Funktional    | Spielzüge              | Gültige Zielspieler (1/2)                                     | Hoch          | -                       | Nur 1/2 erlaubt            | -                    |
| F4     | Funktional    | Spielzüge              | Gültige Zielhand (L/R)                                        | Hoch          | -                       | Nur L/R erlaubt            | -                    |
| F8     | Funktional    | Spielregeln            | Addition der Finger bei Zug                                   | Hoch          | -                       | Korrekte Berechnung        | -                    |
| F9     | Funktional    | Spielregeln            | Kein Angriff auf leere Hände                                  | Hoch          | -                       | Validierung implementiert  | -                    |
| F10    | Funktional    | Spielregeln            | Kein Selbst-Angriff                                           | Hoch          | -                       | Validierung implementiert  | -                    |
| F11    | Funktional    | Spielregeln            | Finger-Teilung bei gerader Zahl                               | Hoch          | -                       | Korrekte Teilungslogik     | -                    |
| F12    | Funktional    | Match-System           | Best-of-System                                                | Hoch          | -                       | Match-Ende korrekt erkannt | -                    |
| Q1     | Qualität      | Benutzerfreundlichkeit | Fehlermeldungen                                               | Hoch          | Klar und hilfreich      | -                          | -                    |
| Q2     | Qualität      | Benutzerfreundlichkeit | Spielzug-Eingabe                                              | Hoch          | Einfach zu verstehen    | -                          | -                    |
| Q3     | Qualität      | Benutzerfreundlichkeit | Spielstand-Anzeige                                            | Mittel        | Klar strukturiert       | -                          | -                    |
| Q4     | Qualität      | Robustheit             | Zug-Validierung                                               | Hoch          | 100%                    | -                          | -                    |
| Q5     | Qualität      | Testbarkeit            | Code Coverage                                                 | Hoch          | 80%                     | -                          | -                    |
| R1     | Randbedingung | Kulturell              | Englisch als Sprache                                          | -             | -                       | -                          | UI und Dokumentation |

# 2. Testumfeld
## 2.1 Technische Umgebung
### 2.1.1 Entwicklungsumgebung
- Java Development Kit (JDK) 17
- Maven als Build-Tool
- IntelliJ IDEA als IDE
- Git für Versionskontrolle
- Windows 10 Betriebssystem
### 2.1.2 Testumgebung
- JUnit 5 für Unit Tests
- Mockito für Mocking
- Build In CodeCoverage Tool von IntelliJ
## 2.2 Testwerkzeug und -frameworks
### 2.2.1 Unit Testing
- JUnit 5: Haupttestframework
- AssertJ: Fluent Assertions
### 2.2.3 Mocking
- Mockito: Mock-Erstellung und -Verifikation
### 2.2.4 Code Quality
- Build In CodeCoverage Tool von IntelliJ

# 3. Testfallspezifikation
## Testfall 1: MenschSpieler Eingabe
**Anforderungsnummer:** F1

**Voraussetzungen:**
- Spiel ist initialisiert
- MenschSpieler ist am Zug

**Eingabe:**
1. MenschSpieler gibt Zug "L1R" ein
3. MenschSpieler gibt Zug "R2L" ein

**Ausgabe:**
2. Zug wird korrekt verarbeitet
3. Zug wird korrekt verarbeitet

## Testfall 2: AISpieler Logik
**Anforderungsnummer:** F2

**Voraussetzungen:**
- Spiel ist initialisiert
- AISpieler ist am Zug
- Gegner hat mindestens eine Hand mit Fingern

**Eingabe:**
1. AISpieler wird aufgefordert einen Zug zu machen

**Ausgabe:**
2. AISpieler macht einen gültigen Zug im Format `[LR][1][LR]`

## Testfall 3: Spiel-Initialisierung mit zwei Spielern
**Anforderungsnummer:** F3

**Voraussetzungen:**
- Keine Spieler existieren

**Eingabe:**
1. Erstelle Spiel mit zwei Spielern

**Ausgabe:**
2. Spiel wird mit zwei Spielern initialisiert

## Testfall 4: Standard-Fingeranzahl
**Anforderungsnummer:** F4

**Voraussetzungen:**
- Spiel ist initialisiert
- Zwei Spieler existieren

**Eingabe:**
1. Überprüfe Fingeranzahl beider Spieler

**Ausgabe:**
2. Beide Spieler haben FingerLinks=1 und FingerRechts=1

## Testfall 5: Gültige Quellhand
**Anforderungsnummer:** F5

**Voraussetzungen:**
- Spiel ist initialisiert
- Zwei Spieler existieren

**Eingabe:**
1. Zug "L1R"
3. Zug "R2L"
5. Zug "X1R"

**Ausgabe:**
2. Zug wird akzeptiert
4. Zug wird akzeptiert
6. Invalid Move

## Testfall 6: Gültiger Zielspieler
**Anforderungsnummer:** F6

**Voraussetzungen:**
- Spiel ist initialisiert
- Zwei Spieler existieren

**Eingabe:**
1. Zug "L1R"
3. Zug "L2R"
5. Zug "L3R"

**Ausgabe:**
2. Zug wird akzeptiert
4. Zug wird akzeptiert
6. Invalid Move

## Testfall 7: Gültige Zielhand
**Anforderungsnummer:** F7

**Voraussetzungen:**
- Spiel ist initialisiert
- Zwei Spieler existieren

**Eingabe:**
1. Zug "L1R"
3. Zug "L1L"
5. Zug "L1X"
 
**Ausgabe:**
2. Zug wird akzeptiert
4. Zug wird akzeptiert
6. Invalid Move

## Testfall 8: Finger-Addition
**Anforderungsnummer:** F8

**Voraussetzungen:**
- Spiel ist initialisiert
- Spieler 1 hat 2 Finger auf der linken Hand
- Spieler 2 hat 2 Finger auf der rechten Hand

**Eingabe:**
1. Spieler 1 macht Zug "L2R"

**Ausgabe:**
2. Spieler 2 hat nun 4 Finger auf der rechten Hand

## Testfall 9: Kein Angriff auf leere Hände
**Anforderungsnummer:** F9

**Voraussetzungen:**
- Spiel ist initialisiert
- Spieler 2 hat 0 Finger auf der rechten Hand

**Eingabe:**
1. Spieler 1 macht Zug "L2R"

**Ausgabe:**
2. Invalid Move

## Testfall 10: Kein Selbst-Angriff
**Anforderungsnummer:** F10

**Voraussetzungen:**
- Spiel ist initialisiert
- Spieler 1 ist am Zug

**Eingabe:**
1.  Spieler 1 macht Zug "L1L"

**Ausgabe:**
2. Invalid Move

## Testfall 11: Finger-Teilung
**Anforderungsnummer:** F11

**Voraussetzungen:**
- Spiel ist initialisiert
- Zwei Spieler existieren
- Spieler hat 4 Finger auf einer Hand

**Eingabe:**
1.  Spieler wählt Teilung der Hand

**Ausgabe:**
2.  Hand wird in zwei Hände mit je 2 Fingern aufgeteilt

## Testfall 12: Match-System Best-of
**Anforderungsnummer:** F12

**Voraussetzungen:**
- Match-System ist initialisiert (Best-of-3)

**Eingabe:**
1.  Spieler 1 gewinnt 2 Spiele
2. Spieler 2 gewinnt 1 Spiele

**Ausgabe:**
3. Match ist beendet, Spieler 1 ist Gewinner

## Testfall 13: Fehlermeldungen
**Anforderungsnummer:** Q1

**Voraussetzungen:**
- Spiel ist initialisiert

**Eingabe:**
1. Ungültiger Zug "X1R"
3. Ungültiger Zug "L3R"
5. Selbst-Angriff "L1L"

**Ausgabe:**
2. Klare Fehlermeldung: "Ungültige Quellhand. Nur L oder R erlaubt."
4. Klare Fehlermeldung: "Ungültiger Zielspieler. Nur 1 oder 2 erlaubt."
6. Klare Fehlermeldung: "Selbst-Angriff nicht erlaubt."

## Testfall 14: Spielzug-Eingabe
**Anforderungsnummer:** Q2

**Voraussetzungen:**
- Spiel ist initialisiert
- MenschSpieler ist am Zug

**Eingabe:**
1. MenschSpieler gibt Zug ein

**Ausgabe:**
2.  Eingabeaufforderung ist klar und verständlich
3. Format-Hinweis wird angezeigt

## Testfall 15: Spielstand-Anzeige
**Anforderungsnummer:** Q3

**Voraussetzungen:**
- Spiel ist initialisiert
- Einige Züge wurden gemacht

**Eingabe:**
1.  Anzeige des aktuellen Spielstands

**Ausgabe:**
2.  Klare Darstellung der Fingeranzahl beider Spieler
3.  Übersichtliche Anzeige des aktuellen Spielers

## Testfall 16: Zug-Validierung
**Anforderungsnummer:** Q4

**Voraussetzungen:**
- Spiel ist initialisiert
- Alle möglichen ungültigen Züge werden getestet

**Eingabe:**
1. Teste alle ungültigen Zug-Kombinationen

**Ausgabe:**
2.  100% der ungültigen Züge werden korrekt abgefangen

## Testfall 17: Code Coverage
**Anforderungsnummer:** Q5

**Voraussetzungen:**
- Alle Testfälle wurden ausgeführt

**Eingabe:**
1.  Führe IntelliJ Code Coverage aus

**Ausgabe:**
2. Code Coverage von mindestens 80%

## Testfall 18: Englische Sprache
**Anforderungsnummer:** R1

**Voraussetzungen:**
- Spiel ist initialisiert

**Eingabe:**
1.  Starte Spiel
2. Mache ungültigen Zug

**Ausgabe:**
3. Alle Meldungen sind auf Englisch
