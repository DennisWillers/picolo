package main.java.model;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public interface Intentnamen {
    //Standard Namen
    String HELPINTENT = "AMAZON.HelpIntent";
    String STOPINTENT = "AMAZON.StopIntent";
    String CANCELINTENT = "AMAZON.CancelIntent";
    String FALLBACKINTENT = "AMAZON.FallbackIntent";

    //Definierte Intents Namen
    String ALLE_GEBOTE_INTENT = "AlleGebote";
    String DEFINIERTES_GEBOT_INTENT = "DefiniertesGebot";
    String ZUFALLS_GEBOT_INTENT = "ZufallsGebot";
}
