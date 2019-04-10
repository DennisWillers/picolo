package de.willers.controller.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import de.willers.controller.handlers.WiederholeIntentHandler;
import de.willers.controller.picolo.Challenge;
import de.willers.controller.picolo.ChallengeMaster;
import de.willers.model.Context;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;
import de.willers.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class Hilfslogik extends de.willers.controller.game.Response {
    public Map<String, Object> getSessionAttributes(HandlerInput input) {
        Map<String, Object> sessionAttribute = new HashMap<>();
        Map<String, Object> inputSessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute.putAll(inputSessionAttribute);
        return sessionAttribute;
    }

    String[] readPlayers(Map<String, Object> sessionAttribute) {
        //Spielernamen auslesen, konfigurieren und in Array speichern
        Object spielerNamen2 = sessionAttribute.get(Parameter.SPIELER_NAMEN);
        String convertSpielerNamen = spielerNamen2.toString();
        convertSpielerNamen = convertSpielerNamen.replace("[", "");
        convertSpielerNamen = convertSpielerNamen.replace("]", "");
        return convertSpielerNamen.split(", ");
    }

    HandlerInput initialSessionAttribute(HandlerInput input, Intent requestIntent, Map<String, Object> sessionAttribute) {
        int players = parseStringToInt(requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue());
        sessionAttribute.put(Parameter.ANZAHL_SPIELER, players);
        String[] spielerNamen = new String[players];
        sessionAttribute.put(Parameter.ANZAHL_SPIELER, players);
        sessionAttribute.put(Parameter.SPIELER_NAMEN, spielerNamen);
        sessionAttribute.put(Parameter.VALID_SPIELERNAME,"null");
        sessionAttribute.put(Parameter.WIEDERGEGEBENE_NACHRICHTEN,"null");
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    HandlerInput changeContext (HandlerInput input, String context){
        Map<String, Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute.replace(Parameter.CONTEXT,context);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    HandlerInput changeSessionParameter (HandlerInput input, String parameter, String value){
        Map<String, Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute.replace(parameter,value);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    int platzDesNeuenSpielersErmitteln(String[] spielerNamen) {
        int zaehleSpieler = 0;
        for (String s :
                spielerNamen) {
            if (!s.equals("null")) {
                zaehleSpieler++;
            } else
                break;
        }
        return zaehleSpieler;
    }

    Optional<Response> getAktuelleZuFragendeSpielerposition(HandlerInput input, Intent requestIntent) {
        Map<String, Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();
        String[] spielerNamen = readPlayers(sessionAttribute);
        int neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);
        return frageSpielerNamen(input,neuerSpieler+1,requestIntent);
    }

    int zaehleSpieler(String[] spielerNamen) {
        int zaehleSpieler = 0;
        for (String s :
                spielerNamen) {
            if (!s.equals("null")) {
                zaehleSpieler++;
            }
        }
        return zaehleSpieler;
    }

    private String generateSessionOutput(String[] array) {
        String output = "[";
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                output = output + array[i];
            } else {
                output = output + array[i] + ", ";
            }
        }
        output = output + "]";
        return output;
    }

    HandlerInput neuenSpielerHinzufuegen(String[] spielerNamen, Map<String, Object> sessionAttribute, String neuerSpielerName, HandlerInput input) {
        int i = 0;
        while (i < spielerNamen.length) {
            if (spielerNamen[i].equals("null")) {
                spielerNamen[i] = neuerSpielerName;
                break;
            }
            i++;
        }
        String output = generateSessionOutput(spielerNamen);
        sessionAttribute.replace(Parameter.SPIELER_NAMEN, output);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    Map<String, Object> setSpielcounter(Map<String, Object> sessionAttribute) {
        if (sessionAttribute.get(Parameter.SPIELCOUNTER) == null) {
            sessionAttribute.put(Parameter.SPIELCOUNTER, 0);
        } else {
            Object value = sessionAttribute.get(Parameter.SPIELCOUNTER);
            int counter = (int) value + 1;
            sessionAttribute.replace(Parameter.SPIELCOUNTER, counter);
        }
        return sessionAttribute;
    }

    private int getSpielcounter(Map<String, Object> sessionAttribute) {
        return (int) sessionAttribute.get(Parameter.SPIELCOUNTER);
    }

    HandlerInput speichereNaechsteAktion(HandlerInput input, String nachricht) {
        Map<String, Object> sessionAttribute = getSessionAttributes(input);
        if (sessionAttribute.get(Parameter.LETZTE_NACHRICHT) == null) {
            sessionAttribute.put(Parameter.LETZTE_NACHRICHT, nachricht);
        } else {
            sessionAttribute.replace(Parameter.LETZTE_NACHRICHT, nachricht);
        }
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    HandlerInput speichereNaechsteAktion(HandlerInput input, int neuerSpieler) {
        String nachricht = askPlayerName() + neuerSpieler;
        return speichereNaechsteAktion(input,nachricht);
    }

    Optional<Response> ermittleNaechsteAktion(HandlerInput input, Map<String, Object> sessionAttribute) {
        try {
            ChallengeMaster master = ChallengeMaster.loadFromFile("blueprint.json");
            String[] players = readPlayers(input.getAttributesManager().getSessionAttributes());
            Challenge challenge = master.getChallenge(players, (String) sessionAttribute.get(Parameter.WIEDERGEGEBENE_NACHRICHTEN));
            input = aktualisiereWiedergegebeneNachrichten(input,challenge);
            String card = challenge.toString();
            System.out.println("CHALLENGE: "+challenge.toString());
            String antwort = "<prosody rate=\"slow\">" + challenge.toString() + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "<break time=\"10s\"/> " + "</prosody>";
            String start = "";
            if (getSpielcounter(sessionAttribute) == 0) {
                start = Text.ERSTE_ANWEISUNG;
            }
            input = speichereNaechsteAktion(input, start + antwort);
            return neueChallenge(input, start + antwort, start + card);
        } catch (Exception e) {
            e.printStackTrace();
            return fehlerNachricht(input);
        }
    }

    private HandlerInput aktualisiereWiedergegebeneNachrichten(HandlerInput input, Challenge newChallenge){
        Map<String,Object> sessionAttribute = input.getAttributesManager().getSessionAttributes();
        int neueZahl = newChallenge.getBlueprint().getId();
        String wiedergegebeneNachrichten = (String) sessionAttribute.get(Parameter.WIEDERGEGEBENE_NACHRICHTEN);
        if(wiedergegebeneNachrichten.equals("null")){
            wiedergegebeneNachrichten = neueZahl+"";
        } else {
            String[] nachrichten = wiedergegebeneNachrichten.split(",");
            if(pruefeNeueChallengeAufBereitsGesagt(nachrichten,neueZahl)){
                wiedergegebeneNachrichten = neueZahl+"";
            } else {
                wiedergegebeneNachrichten = wiedergegebeneNachrichten + "," + neueZahl;
            }
        }
        sessionAttribute.replace(Parameter.WIEDERGEGEBENE_NACHRICHTEN,wiedergegebeneNachrichten);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    private boolean pruefeNeueChallengeAufBereitsGesagt(String[] nachrichten, int neueZahl){
        for (String nachricht:nachrichten)
        {
            if(Integer.parseInt(nachricht) == neueZahl){
                return true;
            }
        }
        return false;
    }

    public Optional<Response> sinnhaftigkeitDesEinsatzesVonJaNeinAnpassen(HandlerInput input){
        Spiellogik spiellogik = new Spiellogik();
        String context = (String) input.getAttributesManager().getSessionAttributes().get(Parameter.CONTEXT);
        switch (context){
            case Context.START:
            case Context.PLAYER_NAME_CONFIRM:
                return spiellogik.pruefeContext(input);
            default:
                return spiellogik.pruefeContext(input);
        }
    }

    int parseStringToInt(String text){
        try {
            Double kommaZahl = Double.parseDouble(text);
            int zahl = (int) Math.round(kommaZahl);
            return zahl;
        } catch (Exception e){
            return -1;
        }
    }
}
