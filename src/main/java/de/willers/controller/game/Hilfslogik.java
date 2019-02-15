package de.willers.controller.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import de.willers.controller.picolo.Challenge;
import de.willers.controller.picolo.ChallengeMaster;
import de.willers.model.Parameter;
import de.willers.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Hilfslogik extends de.willers.controller.game.Response {
    Map<String, Object> getSessionAttributes(HandlerInput input) {
        Map<String, Object> sessionAttribute = new HashMap<>();
        Map<String, Object> inputSessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute.putAll(inputSessionAttribute);
        return sessionAttribute;
    }

    String[] readPlayers(Map<String, Object> sessionAttribute) {
        //Spielernamen auslesen, konfigurieren und in Array speichern
        Object spielerNamen2 = sessionAttribute.get(Parameter.SPIELER_NAMEN);
        System.out.println(spielerNamen2.toString());
        String convertSpielerNamen = spielerNamen2.toString();
        convertSpielerNamen = convertSpielerNamen.replace("[", "");
        convertSpielerNamen = convertSpielerNamen.replace("]", "");
        System.out.println("ConvertSpielerNamen: " + convertSpielerNamen);
        return convertSpielerNamen.split(", ");
    }

    HandlerInput initialSessionAttribute(HandlerInput input, Intent requestIntent, Map<String, Object> sessionAttribute) {
        int players = Integer.parseInt(requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue());
        sessionAttribute.put(Parameter.ANZAHL_SPIELER, players);
        String[] spielerNamen = new String[players];
        sessionAttribute.put(Parameter.ANZAHL_SPIELER, players);
        sessionAttribute.put(Parameter.SPIELER_NAMEN, spielerNamen);
        sessionAttribute.put(Parameter.SPIELERNAME_VERSTANDEN, false);
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

    int zaehleSpieler(String[] spielerNamen) {
        int zaehleSpieler = 0;
        for (String s :
                spielerNamen) {
            if (!s.equals("null")) {
                zaehleSpieler++;
            }
            System.out.println("s = " + s);
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
        System.out.println("Output: " + output);
        return output;
    }

    HandlerInput neuenSpielerHinzufuegen(String[] spielerNamen, Map<String, Object> sessionAttribute, String neuerSpielerName, HandlerInput input) {
        System.out.println("neuenSpielerHinzufuegen SessionAttribute1: " + sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        int i = 0;
        while (i < spielerNamen.length) {
            System.out.println("ForSchleife Spielername[i] = " + spielerNamen[i]);
            if (spielerNamen[i].equals("null")) {
                System.out.println("SpielerNamen[i] = " + spielerNamen[i] + " | neuerSpielername = " + neuerSpielerName);
                spielerNamen[i] = neuerSpielerName;
                break;
            }
            i++;
        }

        System.out.println("Replace SessionAttribute vorher: " + sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        System.out.println("Länge: " + spielerNamen.length + " | inhalt: " + spielerNamen[i] + " | 0te Stelle: " + spielerNamen[0]);
        String output = generateSessionOutput(spielerNamen);
        sessionAttribute.replace(Parameter.SPIELER_NAMEN, output);
        System.out.println("Replace SessionAttribute nacher: " + sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        System.out.println("Replace SessionAttribute ohne ToString nacher: " + sessionAttribute.get(Parameter.SPIELER_NAMEN));
        input.getAttributesManager().setSessionAttributes(sessionAttribute);


        System.out.println("neuenSpielerHinzufuegen SessionAttribute2: " + sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        System.out.println("neuenSpielerHinzufuegen Input1: " + input.getAttributesManager().getSessionAttributes().get(Parameter.SPIELER_NAMEN).toString());
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

    public Map<String, Object> changeSpielernameVerstanden(Map<String, Object> sessionAttribute) {
        boolean spielernameVerstanden = (boolean) sessionAttribute.get(Parameter.SPIELERNAME_VERSTANDEN);
        if (spielernameVerstanden) {
            sessionAttribute.replace(Parameter.SPIELERNAME_VERSTANDEN, false);
        } else {
            sessionAttribute.replace(Parameter.SPIELERNAME_VERSTANDEN, true);
        }
        return sessionAttribute;
    }

    private int getSpielcounter(Map<String, Object> sessionAttribute) {
        return (int) sessionAttribute.get(Parameter.SPIELCOUNTER);
    }

    private HandlerInput speichereNaechsteAktion(HandlerInput input, String nachricht) {
        Map<String, Object> sessionAttribute = getSessionAttributes(input);
        if (sessionAttribute.get(Parameter.LETZTE_NACHRICHT) == null) {
            sessionAttribute.put(Parameter.LETZTE_NACHRICHT, nachricht);
        } else {
            sessionAttribute.replace(Parameter.LETZTE_NACHRICHT, nachricht);
        }
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    Optional<Response> ermittleNaechsteAktion(HandlerInput input, Map<String, Object> sessionAttribute) {
        try {
            System.out.println("ChallengeMaster init");
            ChallengeMaster master = ChallengeMaster.loadFromFile("blueprint.json");
            System.out.println("String players init");
            String[] players = readPlayers(input.getAttributesManager().getSessionAttributes());
            System.out.println("Get Challenge");
            Challenge challenge = master.getChallenge(players);
            String card = challenge.toString();
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
}
