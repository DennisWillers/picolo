package de.willers.view;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public interface Text {

    String HELP          = "Sage 'Alexa, weiter', um die nächste Anweisung zu erhalten. ";
    String STOP          = "Lasst euch weiterhin feiern! ";
    String FALLBACK      = "Leider konnte der Herr dich nicht verstehen ";

    String PICOLO_START = "Willkommen bei Picolo! Möchtest du ein neues Spiel starten? ";
    String PICOLO_SPIELSTAND_LADEN = "Willkommen zurück bei Picolo! Möchtest du das letzte Spiel fortsetzen? ";
    String[] ANZAHL_SPIELER = {
            "Wie viele Spieler spielen mit?",
            "Nenne mir die Anzahl der teilnehmenden Spieler",
            "Wie viele Spieler gibt es heute?"
            };
    String ANZAHL_EIN_SPIELER = "Leider unterstützen wir nicht grundloses Selbstbetrinken. Bitte suche dir hierfür weitere Mitspieler. ";
    String[] SPIELER_NAME_FRAGEN = {
            "Nenne mir deinen Namen Spieler ",
            "Bitte sage mir deinen Namen Spieler ",
            "Sag mir bitte deinen Namen Spieler ",
            "Ich benötige deinen namen Spieler ",
            "Bitte nenne mir deinen Namen Spieler "
    };
    String[] FRAGE_SPIELERNAME_RICHTIG_VERSTANDEN = {
            ", habe ich deinen Namen richtig verstanden?",
            ", heißt du wirklich so?",
            ", willst du im weiteren Spielverlauf so genannt werden?",
            ", ist der Name richtig verstanden worden?"
    };
    String NAECHSTE_AUFGABE = "Wollt ihr die nächste Aufgabe Wissen? ";
    String ERSTE_ANWEISUNG = "Alles klar, Los geht´s! ";
    String WIEDERHOLEN_FEHLER = "Das Spiel wurde noch nicht gestartet. Deswegen kann ich nichts wiederholen. Bitte starte vorher das Spiel. ";
    String HALLO[] = {
            "Hallo ",
            "Servus ",
            "Grüß dich ",
            "Naaa ",
            "Na wen haben wir denn hier? ",
            "Moin ",
            "Hi ",
            "Juten Tag "
    };

    //SSML Aussprache

    /**TODO
     * SSML Text bearbeiten
     */
    String HELP_SSML          = "Sage 'Alexa, weiter', um die nächste Anweisung zu erhalten. ";
    String STOP_SSML          = "Lasst euch weiterhin feiern! ";
    String FALLBACK_SSML      = "Ich habe dich leider nicht verstanden. ";
}
