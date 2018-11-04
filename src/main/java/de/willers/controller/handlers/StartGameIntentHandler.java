package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import de.willers.controller.picolo.Challenge;
import de.willers.controller.picolo.ChallengeMaster;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;
import de.willers.view.Card;
import de.willers.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 28.10.2018 erstellt
 */
public class StartGameIntentHandler implements RequestHandler
{
    @Override
    public boolean canHandle(HandlerInput input) {
    return input.matches(intentName(Intentnamen.STARTGAMEINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        //Get Session und Request Attribute
        Map<String,Object> sessionAttribute = getSessionAttributes(input);
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        //Frage nach der Anzahl der Spieler
        if((requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue() == null || requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue().equals("?")) && sessionAttribute.get(Parameter.ANZAHL_SPIELER) == null){
        //if (requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue() == null || requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue().equals("?")) {
            System.out.println("Ermittle Anzahl Spieler");
            return frageAnzahlSpieler(input,requestIntent);
        }
        else {
            System.out.println("SessionAttribute Zweig");
            //Frage nach den Namen der Spieler

            //Prüfe das sessionAttribut nicht leer ist
            if(sessionAttribute.get(Parameter.SPIELER_NAMEN) != null){
                System.out.println("Session Attribute nicht leer Zweig");
                //Attribute auslesen
                String neuerSpielerName = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getValue();
                String spielerNamen[] = readPlayers(sessionAttribute);
                int players = (int) sessionAttribute.get(Parameter.ANZAHL_SPIELER);
                System.out.println("NeuerSpielerName: "+neuerSpielerName);

                //Ermittle die Anzahl der ermittelnden Namen
                int zaehleSpieler = zaehleSpieler(spielerNamen);
                System.out.println("ZaehleSpieler = "+zaehleSpieler);
                System.out.println("players = "+players);

                //Prüfe ob es so viele Namen wie Spieler gibt
                if (++zaehleSpieler < players){
                    System.out.println("ZahleSpieler != Players Zweig");
                    //Nummer des neuen Spielers ermitteln
                    int neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);
                    System.out.println("NeuerSpielerInt: "+neuerSpieler);

                    //Neuen Spieler an ersten zu findenden null Stelle hinzufügen
                    input = neuenSpielerHinzufuegen(spielerNamen,sessionAttribute,neuerSpielerName,input);
                    return frageSpielerNamen(input,neuerSpieler+2,requestIntent);
                }
                else if(zaehleSpieler==players){
                    System.out.println("ELSE Zweig ZahleSpieler != Players Zweig");
                    input = neuenSpielerHinzufuegen(spielerNamen,sessionAttribute,neuerSpielerName,input);
                }
            }
            else {
                System.out.println("Session Attribute leer Zweig");
                //Wenn sessionAttribute leer sind
                initialSessionAttribute(input,requestIntent,sessionAttribute);
                return frageSpielerNamen(input,1,requestIntent);
            }
        }
        sessionAttribute = setSpielcounter(sessionAttribute);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return ermittleNaechsteAktion(input,sessionAttribute);
    }

    //*************
    //HILFSMETHODEN
    //*************

    private Map<String,Object> getSessionAttributes(HandlerInput input){
        Map<String, Object> sessionAttribute = new HashMap<>();
        Map<String, Object> inputSessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute.putAll(inputSessionAttribute);
        return  sessionAttribute;
    }

    private String[] readPlayers(Map<String,Object> sessionAttribute){
        //Spielernamen auslesen, konfigurieren und in Array speichern
        Object spielerNamen2 = sessionAttribute.get(Parameter.SPIELER_NAMEN);
        System.out.println(spielerNamen2.toString());
        String convertSpielerNamen = spielerNamen2.toString();
        convertSpielerNamen = convertSpielerNamen.replace("[", "");
        convertSpielerNamen = convertSpielerNamen.replace("]", "");
        return convertSpielerNamen.split(", ");
    }

    private String randomPlayerAnswer(){
        String[] text = Text.ANZAHL_SPIELER;
        return text[(int) Math.floor(Math.random() * (Text.ANZAHL_SPIELER.length))];
    }

    private String askPlayerName(){
        String[] text = Text.SPIELER_NAME_FRAGEN;
        return text[(int) Math.floor(Math.random() * (Text.SPIELER_NAME_FRAGEN.length))];
    }

    private HandlerInput initialSessionAttribute(HandlerInput input, Intent requestIntent, Map<String,Object> sessionAttribute){
        int players = Integer.parseInt(requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue());
        sessionAttribute.put(Parameter.ANZAHL_SPIELER,players);
        String[] spielerNamen = new String[players];
        sessionAttribute.put(Parameter.ANZAHL_SPIELER,players);
        sessionAttribute.put(Parameter.SPIELER_NAMEN,spielerNamen);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    private int platzDesNeuenSpielersErmitteln(String[] spielerNamen){
        int zaehleSpieler = 0;
        for (String s:
                spielerNamen) {
            if (!s.equals("null")) {
                zaehleSpieler++;
            }
            else
                break;
        }
        return zaehleSpieler;
    }

    private int zaehleSpieler(String[] spielerNamen){
        int zaehleSpieler = 0;
        for (String s:
                spielerNamen) {
            if (!s.equals("null")) {
                zaehleSpieler++;
            }
            System.out.println("s = "+s);
        }
        return zaehleSpieler;
    }

    private HandlerInput neuenSpielerHinzufuegen(String[] spielerNamen, Map<String,Object> sessionAttribute, String neuerSpielerName, HandlerInput input){
        for(int i = 0; i < spielerNamen.length; i++){
            if(spielerNamen[i].equals("null")){
                spielerNamen[i] = neuerSpielerName;
                System.out.println("Spielernamen: "+spielerNamen);
                sessionAttribute.remove(Parameter.SPIELER_NAMEN);
                sessionAttribute.put(Parameter.SPIELER_NAMEN,spielerNamen);
                input.getAttributesManager().setSessionAttributes(sessionAttribute);
                break;
            }
        }
        return input;
    }

    private Map<String,Object> setSpielcounter(Map<String,Object> sessionAttribute){
        if (sessionAttribute.get(Parameter.SPIELCOUNTER) == null) {
            sessionAttribute.put(Parameter.SPIELCOUNTER, 0);
        } else {
            Object value = sessionAttribute.get(Parameter.SPIELCOUNTER);
            int counter = (int) value+1;
            sessionAttribute.replace(Parameter.SPIELCOUNTER, counter);
        }
        return  sessionAttribute;
    }

    private int getSpielcounter (Map<String,Object> sessionAttribute){
        return (int) sessionAttribute.get(Parameter.SPIELCOUNTER);
    }

    private HandlerInput speichereNaechsteAktion(HandlerInput input, String nachricht){
        Map<String,Object> sessionAttribute = getSessionAttributes(input);
        if (sessionAttribute.get(Parameter.LETZTE_NACHRICHT) == null) {
            sessionAttribute.put(Parameter.LETZTE_NACHRICHT, nachricht);
        } else {
            sessionAttribute.replace(Parameter.LETZTE_NACHRICHT, nachricht);
        }
        input.getAttributesManager().setSessionAttributes(sessionAttribute);
        return input;
    }

    private Optional<Response> ermittleNaechsteAktion(HandlerInput input, Map<String,Object> sessionAttribute){
        try {
            System.out.println("ChallengeMaster init");
            ChallengeMaster master = ChallengeMaster.loadFromFile("blueprint.json");
            System.out.println("String players init");
            String[] players = readPlayers(sessionAttribute);
            System.out.println("Get Challenge");
            Challenge challenge = master.getChallenge(players);
            String card = challenge.toString();
            String antwort = "<prosody rate=\"slow\">"+challenge.toString()+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> " + "</prosody>";
            String start = "";
            if(getSpielcounter(sessionAttribute) == 0){
                start = Text.ERSTE_ANWEISUNG ;
            }
            input = speichereNaechsteAktion(input,start+antwort);
            return neueChallenge(input,start+antwort,start+card);
        } catch (Exception e) {
            e.printStackTrace();
            return fehlerNachricht(input);
        }
    }

    /*
     * RESPONSE MÖGLICHKEITEN
     */

    private Optional<Response> frageAnzahlSpieler(HandlerInput input, Intent requestIntent){
        return input.getResponseBuilder()
                .withSpeech(randomPlayerAnswer())
                .addElicitSlotDirective(Parameter.ANZAHL_SPIELER, requestIntent)
                .build();
    }

    private Optional<Response> frageSpielerNamen(HandlerInput input, int spieler, Intent requestIntent){
        return input.getResponseBuilder()
                .withSpeech(askPlayerName()+spieler)
                .addElicitSlotDirective(Parameter.NEUER_SPIELER_NAME, requestIntent)
                .build();
    }


    private Optional<Response> neueChallenge(HandlerInput input, String antwort, String card){
        return input.getResponseBuilder()
                .withSpeech(antwort)
                .withSimpleCard(Card.CHALLENGE, card)
                .withReprompt(Text.NAECHSTE_AUFGABE)
                .build();
    }

    private Optional<Response> fehlerNachricht (HandlerInput input){
        String fehler = "Es ist ein Fehler passiert";
        return input.getResponseBuilder()
                .withSpeech(fehler)
                .withSimpleCard("Fehler", fehler)
                .build();
    }
}