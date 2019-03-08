package de.willers.controller.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SlotConfirmationStatus;
import de.willers.model.Context;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;

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

    public Optional<Response> pruefeContext(HandlerInput input) {
        String context = (String) input.getAttributesManager().getSessionAttributes().get(Parameter.CONTEXT);
        context = context == null ? Context.PLAYER_COUNT : context;
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        switch (context) {
            case Context.START:
                //Definieren nächsten Context:
                input = changeContext(input, Context.PLAYER_COUNT);
                System.out.println("Ermittle Anzahl Spieler");
                return frageAnzahlSpieler(input, requestIntent);
            case Context.PLAYER_COUNT:
                return pruefeAnzahlDerSpielerGegebenResponse(input);
            case Context.PLAYER_NAME_ASK:
                return verstandenenNamenZurUeberpruefungResponse(input);
            case Context.PLAYER_NAME_CONFIRM:
                return pruefeVerstandenenSpielernamen(input);
            case Context.GAME:
                return naechsteGameAktion(input);
            default:
                return pruefeAnzahlDerSpielerGegebenResponse(input);
        }
    }

    private Optional<Response> pruefeAnzahlDerSpielerGegebenResponse(HandlerInput input) {
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        //Frage nach der Anzahl der Spieler
        String anzahlSpieler = requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue();
        if ((anzahlSpieler == null || anzahlSpieler.equals("?")) || Integer.parseInt(anzahlSpieler) <= 0) {
            System.out.println("Ermittle Anzahl Spieler");
            return frageAnzahlSpieler(input, requestIntent);
        } else if (anzahlSpieler.equals("1")) {
            return anzahlSpielerIstEins(input, requestIntent);
        } else {
            return pruefeSessionAttributSpielerNamenVorhandenResponse(input, requestIntent);
        }
    }

    private Optional<Response> pruefeSessionAttributSpielerNamenVorhandenResponse(HandlerInput input, Intent requestIntent) {
        System.out.println("SessionAttribute Zweig");
        //Frage nach den Namen der Spieler


        Map<String, Object> sessionAttribute = getSessionAttributes(input);
        //Prüfe das sessionAttribut nicht leer ist
        if (sessionAttribute.get(Parameter.SPIELER_NAMEN) != null) {
            return pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input, requestIntent);
        } else {
            //Context ändern
            input = changeContext(input, Context.PLAYER_NAME_ASK);
            sessionAttribute = getSessionAttributes(input);
            System.out.println("Session Attribute leer Zweig");
            //Wenn sessionAttribute leer sind
            initialSessionAttribute(input, requestIntent, sessionAttribute);
            input = speichereNaechsteAktion(input, 1);
            return frageSpielerNamen(input, 1, requestIntent);
        }
    }

    private Optional<Response> verstandenenNamenZurUeberpruefungResponse(HandlerInput input) {
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        String verstandenerSpielername = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getValue();
        System.out.println("ValidSpielername: " + input.getAttributesManager().getSessionAttributes().get(Parameter.VALID_SPIELERNAME));
        if(verstandenerSpielername == null){
            int neuerSpieler;
            if(input.getAttributesManager().getSessionAttributes().get(Parameter.SPIELER_NAMEN) == null){
                neuerSpieler = 0;
            } else {
                String[] spielerNamen = readPlayers(input.getAttributesManager().getSessionAttributes());
                neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);
            }
            return frageSpielerNamen(input,neuerSpieler+1,requestIntent);
        } else {
            input = changeSessionParameter(input, Parameter.VALID_SPIELERNAME, verstandenerSpielername);
            //Context ändern
            input = changeContext(input, Context.PLAYER_NAME_CONFIRM);
            return frageObSpielernameRichtigVerstandenWurde(input, verstandenerSpielername, requestIntent);
        }
    }

    private Optional<Response> pruefeVerstandenenSpielernamen(HandlerInput input) {
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        String spielername = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getName();
        SlotConfirmationStatus status = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getConfirmationStatus();
        if (status == SlotConfirmationStatus.CONFIRMED && !spielername.equals("null")){
            return pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input, requestIntent);
        } else if (status == SlotConfirmationStatus.NONE){
            String pruefname = (String) input.getAttributesManager().getSessionAttributes().get(Parameter.VALID_SPIELERNAME);
            return frageObSpielernameRichtigVerstandenWurde(input, pruefname,requestIntent);
        } else {
            //Change Context
            input = changeContext(input, Context.PLAYER_NAME_ASK);
            return getAktuelleZuFragendeSpielerposition(input, requestIntent);
        }
    }

    private Optional<Response> pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(HandlerInput input, Intent requestIntent) {
        System.out.println("Session Attribute nicht leer Zweig");

        Map<String, Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();

        //Attribute auslesen
        String neuerSpielerName = (String) sessionAttribute.get(Parameter.VALID_SPIELERNAME);
        String[] spielerNamen = readPlayers(sessionAttribute);
        int players = (int) sessionAttribute.get(Parameter.ANZAHL_SPIELER);
        System.out.println("NeuerSpielerName: " + neuerSpielerName);

        //Ermittle die Anzahl der ermittelnden Namen
        int zaehleSpieler = zaehleSpieler(spielerNamen);
        System.out.println("ZaehleSpieler = " + zaehleSpieler);
        System.out.println("players = " + players);

        //Prüfe ob es so viele Namen wie Spieler gibt
        if (++zaehleSpieler < players) {
            //Ändere Context
            input = changeContext(input, Context.PLAYER_NAME_ASK);

            System.out.println("ZahleSpieler != Players Zweig");
            //Nummer des neuen Spielers ermitteln
            int neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);
            System.out.println("NeuerSpielerInt: " + neuerSpieler);

            //Neuen Spieler an ersten zu findenden null Stelle hinzufügen
            input = neuenSpielerHinzufuegen(spielerNamen, sessionAttribute, neuerSpielerName, input);
            System.out.println("Frage neuen Spielernamen");
            input = speichereNaechsteAktion(input, neuerSpieler + 2);
            return frageSpielerNamen(input, neuerSpieler + 2, requestIntent);
        } else /*if (zaehleSpieler == players && sessionAttribute.get(Parameter.SPIELCOUNTER) == null)*/ {
            //ändere Context
            input = changeContext(input, Context.GAME);

            System.out.println("ELSE Zweig Stringarray: " + sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
            input = neuenSpielerHinzufuegen(spielerNamen, sessionAttribute, neuerSpielerName, input);
            sessionAttribute = input.getAttributesManager().getSessionAttributes();
            sessionAttribute = setSpielcounter(sessionAttribute);
            input.getAttributesManager().setSessionAttributes(sessionAttribute);
            return ermittleNaechsteAktion(input, sessionAttribute);
        }
    }

    private Optional<Response> naechsteGameAktion(HandlerInput input) {
        Map<String, Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute = setSpielcounter(sessionAttribute);
        return ermittleNaechsteAktion(input, sessionAttribute);
    }
}
