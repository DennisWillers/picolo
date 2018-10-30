package main.de.willers.java.view;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public interface Text {

    String HELP          = "Die Gebooooote sind alles was zählt. Erfrage alle 10 Gebote und erleuchte dein Wissen! ";
    String STOP          = "Gott ist mit dir! ";
    String FALLBACK      = "Leider konnte der Herr dich nicht verstehen ";

    String PICOLO_START = "Willkommen bei Picolo! Möchtest du ein neues Spiel starten?";
    String[] ANZAHL_SPIELER = {
            "Wie viele Spieler spielen mit?",
            "Nenne mir die Anzahl der teilnehmenden Spieler",
            "Wie viele Spieler gibt es heute?"
            };
    String[] SPIELER_NAME_FRAGEN = {
            "Nenne mir deinen Namen Spieler ",
            "Wie heißt du, Spieler ",
            "Sag mir bitte deinen Namen Spieler "
    };

    //SSML Aussprache

    /**TODO
     * SSML Text bearbeiten
     */
    String HELP_SSML          = "ZU BEARBEITEN";
    String STOP_SSML          = "ZU BEARBEITEN";
    String FALLBACK_SSML      = "ZU BEARBEITEN";
}
