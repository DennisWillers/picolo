package de.willers.view;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public interface Text {

    String HELP          = "Sage 'Alexa, weiter', um die nächste Anweisung zu erhalten. ";
    String STOP          = "Lasst euch weiterhin feiern! ";
    String FALLBACK      = "Leider konnte der Herr dich nicht verstehen ";

    String PICOLO_START = "Willkommen bei Picolo! Möchtest du ein neues Spiel starten? ";
    String[] ANZAHL_SPIELER = {
            "Wie viele Spieler spielen mit?",
            "Nenne mir die Anzahl der teilnehmenden Spieler",
            "Wie viele Spieler gibt es heute?"
            };
    String[] SPIELER_NAME_FRAGEN = {
            "Nenne mir deinen Namen Spieler ",
            "Bitte sage mir deinen Namen Spieler ",
            "Sag mir bitte deinen Namen Spieler "
    };
    String NAECHSTE_AUFGABE = "Wollt ihr die nächste Aufgabe Wissen? ";
    String ERSTE_ANWEISUNG = "Alles klar, Los geht´s! ";

    //SSML Aussprache

    /**TODO
     * SSML Text bearbeiten
     */
    String HELP_SSML          = "Sage 'Alexa, weiter', um die nächste Anweisung zu erhalten. ";
    String STOP_SSML          = "Lasst euch weiterhin feiern! ";
    String FALLBACK_SSML      = "Leider konnte der Herr dich nicht verstehen ";
}
