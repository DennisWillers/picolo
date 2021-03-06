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
            case Context.LOAD_GAME:
                return pruefeObSpielstandGeladenWerdenSoll(input);
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

    private Optional<Response> pruefeObSpielstandGeladenWerdenSoll (HandlerInput input) {
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        if (requestIntent.getName().equals(Intentnamen.YESINTENT)) {
            HandlerInput letzterSpielstand = new Hilfslogik().getSpielstand(input);
            input.getAttributesManager().setSessionAttributes(letzterSpielstand.getAttributesManager().getSessionAttributes());
            return pruefeContext(input);
        } else {
            input = changeContext(input, Context.PLAYER_COUNT);
            return frageAnzahlSpieler(input, requestIntent);
        }
    }

    private Optional<Response> pruefeAnzahlDerSpielerGegebenResponse(HandlerInput input) {
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        //Frage nach der Anzahl der Spieler
        String anzahlSpieler = requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue();
        if ((anzahlSpieler == null || anzahlSpieler.equals("?")) || parseStringToInt(anzahlSpieler) <= 0) {
            System.out.println("Ermittle Anzahl Spieler");
            return frageAnzahlSpieler(input, requestIntent);
        } else if (anzahlSpieler.equals("1")) {
            return anzahlSpielerIstEins(input, requestIntent);
        } else {
            return pruefeSessionAttributSpielerNamenVorhandenResponse(input, requestIntent);
        }
    }

    private Optional<Response> pruefeSessionAttributSpielerNamenVorhandenResponse(HandlerInput input, Intent requestIntent) {
        //Frage nach den Namen der Spieler

        Map<String, Object> sessionAttribute = getSessionAttributes(input);
        //Prüfe das sessionAttribut nicht leer ist
        if (sessionAttribute.get(Parameter.SPIELER_NAMEN) != null) {
            return pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input, requestIntent);
        } else {
            //Context ändern
            input = changeContext(input, Context.PLAYER_NAME_ASK);
            sessionAttribute = getSessionAttributes(input);
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
        Map<String, Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();

        //Attribute auslesen
        String neuerSpielerName = (String) sessionAttribute.get(Parameter.VALID_SPIELERNAME);
        String[] spielerNamen = readPlayers(sessionAttribute);
        int players = (int) sessionAttribute.get(Parameter.ANZAHL_SPIELER);
        System.out.println("NeuerSpielerName: " + neuerSpielerName);

        //Ermittle die Anzahl der ermittelnden Namen
        int zaehleSpieler = zaehleSpieler(spielerNamen);

        //Prüfe ob es so viele Namen wie Spieler gibt
        if (++zaehleSpieler < players) {
            //Ändere Context
            input = changeContext(input, Context.PLAYER_NAME_ASK);
            //Nummer des neuen Spielers ermitteln
            int neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);

            //Neuen Spieler an ersten zu findenden null Stelle hinzufügen
            input = neuenSpielerHinzufuegen(spielerNamen, sessionAttribute, neuerSpielerName, input);
            input = speichereNaechsteAktion(input, neuerSpieler + 2);
            return frageSpielerNamen(input, neuerSpieler + 2, requestIntent);
        } else /*if (zaehleSpieler == players && sessionAttribute.get(Parameter.SPIELCOUNTER) == null)*/ {
            //ändere Context
            input = changeContext(input, Context.GAME);
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
