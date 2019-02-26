package de.willers.controller.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import de.willers.controller.picolo.Challenge;
import de.willers.controller.picolo.ChallengeMaster;
import de.willers.model.Context;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;
import de.willers.view.Card;
import de.willers.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 28.01.2019 erstellt
 */
public class Spiellogik extends Hilfslogik {
    //***********
    //SPIEL-LOGIK
    //***********

    public Optional<Response> pruefeContext(HandlerInput input){
        String context = (String) input.getAttributesManager().getSessionAttributes().get(Parameter.CONTEXT);
        switch (context){
            case Context.START:
                System.out.println("Ermittle Anzahl Spieler");
                Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
                if(input.matches(intentName(Intentnamen.YESINTENT)))
                    return frageAnzahlSpielerYesIntent(input,requestIntent);
                else
                    return frageAnzahlSpieler(input, requestIntent);
            default:
                return pruefeAnzahlDerSpielerGegebenResponse(input);
        }
    }

    public Optional<Response> pruefeAnzahlDerSpielerGegebenResponse(HandlerInput input) {
        //Get Session und Request Attribute
        Map<String, Object> sessionAttribute = getSessionAttributes(input);
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        //Frage nach der Anzahl der Spieler
        if ((requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue() == null || requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue().equals("?")) && sessionAttribute.get(Parameter.ANZAHL_SPIELER) == null) {
            System.out.println("Ermittle Anzahl Spieler");
            return frageAnzahlSpieler(input, requestIntent);
        } else {
            return pruefeSessionAttributSpielerNamenVorhandenResponse(input, sessionAttribute, requestIntent);
        }
    }

    private Optional<Response> pruefeSessionAttributSpielerNamenVorhandenResponse(HandlerInput input, Map<String, Object> sessionAttribute, Intent requestIntent) {
        System.out.println("SessionAttribute Zweig");
        //Frage nach den Namen der Spieler

        //Prüfe das sessionAttribut nicht leer ist
        if (sessionAttribute.get(Parameter.SPIELER_NAMEN) != null) {
            return pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input, sessionAttribute, requestIntent);
        } else {
            System.out.println("Session Attribute leer Zweig");
            //Wenn sessionAttribute leer sind
            initialSessionAttribute(input, requestIntent, sessionAttribute);
            return frageSpielerNamen(input, 1, requestIntent);
        }
    }

    public Optional<Response> pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(HandlerInput input, Map<String, Object> sessionAttribute, Intent requestIntent) {
        System.out.println("Session Attribute nicht leer Zweig");
        //Attribute auslesen
        String neuerSpielerName = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getValue();
        String spielerNamen[] = readPlayers(sessionAttribute);
        int players = (int) sessionAttribute.get(Parameter.ANZAHL_SPIELER);
        System.out.println("NeuerSpielerName: " + neuerSpielerName);

        //Ermittle die Anzahl der ermittelnden Namen
        int zaehleSpieler = zaehleSpieler(spielerNamen);
        System.out.println("ZaehleSpieler = " + zaehleSpieler);
        System.out.println("players = " + players);

        //Prüfe ob der Spielername verstanden wurde
        boolean spielernameVerstanden = (boolean) sessionAttribute.get(Parameter.SPIELERNAME_VERSTANDEN);
        if (spielernameVerstanden) {
            sessionAttribute = changeSpielernameVerstanden(sessionAttribute);
            input.getAttributesManager().setSessionAttributes(sessionAttribute);
            //Prüfe ob es so viele Namen wie Spieler gibt
            if (++zaehleSpieler < players) {
                System.out.println("ZahleSpieler != Players Zweig");
                //Nummer des neuen Spielers ermitteln
                int neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);
                System.out.println("NeuerSpielerInt: " + neuerSpieler);

                //Neuen Spieler an ersten zu findenden null Stelle hinzufügen
                input = neuenSpielerHinzufuegen(spielerNamen, sessionAttribute, neuerSpielerName, input);
                return frageSpielerNamen(input, neuerSpieler + 2, requestIntent);
            } else if (zaehleSpieler == players && sessionAttribute.get(Parameter.SPIELCOUNTER) == null) {
                System.out.println("ELSE Zweig Stringarray: " + sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
                input = neuenSpielerHinzufuegen(spielerNamen, sessionAttribute, neuerSpielerName, input);
                sessionAttribute = input.getAttributesManager().getSessionAttributes();
                sessionAttribute = setSpielcounter(sessionAttribute);
                input.getAttributesManager().setSessionAttributes(sessionAttribute);
                return ermittleNaechsteAktion(input, sessionAttribute);
            } else {
                sessionAttribute = setSpielcounter(sessionAttribute);
                return ermittleNaechsteAktion(input, sessionAttribute);
            }
        } else {
            String verstandenerSpielername = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getValue();
            return frageObSpielernameRichtigVerstandenWurde(input, verstandenerSpielername);
        }
    }
}
