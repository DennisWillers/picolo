package de.willers.model;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public interface Intentnamen {
    //Standard Namen
    String HELPINTENT = "AMAZON.HelpIntent";
    String STOPINTENT = "AMAZON.StopIntent";
    String CANCELINTENT = "AMAZON.CancelIntent";
    String FALLBACKINTENT = "AMAZON.FallbackIntent";

    //Ja Nein
    String YESINTENT = "AMAZON.YesIntent";
    String NOINTENT = "AMAZON.NoIntent";

    //test
    String TEST = "test";

    //Definierte Intents Namen
    String STARTGAMEINTENT = "StartGameIntent";
    String WIEDERHOLEINTENT = "WiederholeIntent";
}
