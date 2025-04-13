# Fingerspiel (Chopsticks)

## Spielregeln

Fingerspiel (auch bekannt als Chopsticks) ist ein Handspiel mit folgenden Regeln:

1. **Ausgangssituation**: Jeder Spieler beginnt mit 1 ausgestreckten Finger an jeder Hand.

2. **Spielablauf**: Die Spieler ziehen abwechselnd und haben zwei mögliche Züge:
    - **Angriff**: Ein Spieler nutzt eine seiner Hände, um eine Hand des Gegners anzugreifen. Die Anzahl der Finger an der gegnerischen Hand erhöht sich um die Anzahl der Finger an der angreifenden Hand.
    - **Teilen**: Ein Spieler verteilt seine Finger gleichmässig auf beide Hände. Dies ist nur erlaubt, wenn:
        - Die Gesamtanzahl der Finger gerade ist
        - Die Verteilung zu einer Änderung führt (nicht bereits ausgeglichen)
        - Mindestens eine Hand Finger hat

3. **Handstatus**:
    - Wenn eine Hand genau 5 Finger erreicht, "stirbt" sie (wird zu 0 Fingern)
    - Eine Hand mit 0 Fingern kann weder angreifen noch angegriffen werden

4. **Spielende**: Ein Spieler verliert, wenn beide Hände "tot" sind (0 Finger).

## Spielsteuerung

Um einen Zug auszuführen, verwende folgende Befehle:
- **Angriff**: `[Deine Hand][Gegner Nummer][Gegner Hand]`
    - Beispiel: `l2r` - Greife die rechte Hand von Gegner 2 mit deiner linken Hand an
    - Handindikatoren: `l` für links, `r` für rechts
- **Teilen**: `s` - Verteile deine Finger gleichmässig auf beide Hände

## Einrichtung und Ausführung des Spiels

### Voraussetzungen
- Java 17 oder höher
- Maven

### Ausführen in einer IDE (IntelliJ IDEA)

1. **Projekt öffnen**:
    - IntelliJ IDEA starten
    - "Öffnen" oder "Projekt importieren" auswählen
    - Zum Projektverzeichnis navigieren und die `pom.xml` Datei auswählen
    - "Als Projekt öffnen" wählen

2. **Projekt bauen**:
    - Warten, bis IntelliJ die Maven-Abhängigkeiten importiert hat
    - Projekt über `Build > Build Project` bauen

3. **Spiel starten**:
    - Die Hauptklasse (enthält eine `main`-Methode) finden
    - Rechtsklick auf die Hauptklasse und "Ausführen" wählen

### Ausführen über die Kommandozeile

1. **Projekt bauen**:
   ```
   mvn clean package
   ```

2. **Spiel starten**:
   ```
   java -jar target/LB_450_FingerSpiel-1.0-SNAPSHOT.jar
   ```

## Spieltipps

- Versuche beim Angriff, die Summe der Finger deines Gegners auf Vielfache von 5 zu bringen
- Eine frühe Teilung kann dir manchmal einen strategischen Vorteil verschaffen
- Behalte eine Hand mit einem Wert, der deinen Gegner effektiv angreifen kann
- Denke daran, dass eine Hand mit 4 Fingern "stirbt", wenn sie mit nur 1 Finger getroffen wird
- Achte auf Möglichkeiten, deinen Gegner in Situationen zu zwingen, in denen er keine effektiven Züge hat

