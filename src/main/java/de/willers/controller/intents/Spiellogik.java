package de.willers.controller.intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import de.willers.controller.picolo.Challenge;
import de.willers.controller.picolo.ChallengeMaster;
import de.willers.model.Parameter;
import de.willers.view.Card;
import de.willers.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Von Dennis Willers (A13A316) am 28.01.2019 erstellt
 */
public class Spiellogik {
    //***********
    //SPIEL-LOGIK
    //***********

    public Optional<Response> pruefeAnzahlDerSpielerGegebenResponse(HandlerInput input){
        //Get Session und Request Attribute
        Map<String,Object> sessionAttribute = getSessionAttributes(input);
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        //Frage nach der Anzahl der Spieler
        if((requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue() == null || requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue().equals("?")) && sessionAttribute.get(Parameter.ANZAHL_SPIELER) == null){
            System.out.println("Ermittle Anzahl Spieler");
            return frageAnzahlSpieler(input,requestIntent);
        }
        else {
            return pruefeSessionAttributSpielerNamenVorhandenResponse(input,sessionAttribute,requestIntent);
        }
    }

    private Optional<Response> pruefeSessionAttributSpielerNamenVorhandenResponse(HandlerInput input,  Map<String,Object> sessionAttribute, Intent requestIntent){
        System.out.println("SessionAttribute Zweig");
        //Frage nach den Namen der Spieler

        //Prüfe das sessionAttribut nicht leer ist
        if(sessionAttribute.get(Parameter.SPIELER_NAMEN) != null){
            return pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input,sessionAttribute,requestIntent);
        }
        else {
            System.out.println("Session Attribute leer Zweig");
            //Wenn sessionAttribute leer sind
            initialSessionAttribute(input,requestIntent,sessionAttribute);
            return frageSpielerNamen(input,1,requestIntent);
        }
    }

    public Optional<Response> pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(HandlerInput input, Map<String,Object> sessionAttribute, Intent requestIntent){
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

        //Prüfe ob der Spielername verstanden wurde
        boolean spielernameVerstanden = (boolean) sessionAttribute.get(Parameter.SPIELERNAME_VERSTANDEN);
        if(spielernameVerstanden){
            sessionAttribute = changeSpielernameVerstanden(sessionAttribute);
            input.getAttributesManager().setSessionAttributes(sessionAttribute);
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
            else if(zaehleSpieler==players && sessionAttribute.get(Parameter.SPIELCOUNTER) == null){
                System.out.println("ELSE Zweig Stringarray: "+sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
                input = neuenSpielerHinzufuegen(spielerNamen,sessionAttribute,neuerSpielerName,input);
                sessionAttribute = input.getAttributesManager().getSessionAttributes();
                sessionAttribute = setSpielcounter(sessionAttribute);
                input.getAttributesManager().setSessionAttributes(sessionAttribute);
                return ermittleNaechsteAktion(input,sessionAttribute);
            } else {
                sessionAttribute = setSpielcounter(sessionAttribute);
                return ermittleNaechsteAktion(input,sessionAttribute);
            }
        } else {
            String verstandenerSpielername = requestIntent.getSlots().get(Parameter.NEUER_SPIELER_NAME).getValue();
            return frageObSpielernameRichtigVerstandenWurde(input,verstandenerSpielername);
        }
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
        System.out.println("ConvertSpielerNamen: "+convertSpielerNamen);
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
        sessionAttribute.put(Parameter.SPIELERNAME_VERSTANDEN,false);
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

    private String generateSessionOutput(String[] array){
        String output = "[";
        for (int i = 0; i < array.length; i++) {
            if(i == array.length-1){
                output = output + array[i];
            } else {
                output  = output + array[i] + ", ";
            }
        }
        output = output + "]";
        System.out.println("Output: "+output);
        return output;
    }

    private HandlerInput neuenSpielerHinzufuegen(String[] spielerNamen, Map<String,Object> sessionAttribute, String neuerSpielerName, HandlerInput input){
        System.out.println("neuenSpielerHinzufuegen SessionAttribute1: "+sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        int i = 0;
        while(i < spielerNamen.length){
            System.out.println("ForSchleife Spielername[i] = "+spielerNamen[i]);
            if(spielerNamen[i].equals("null")){
                System.out.println("SpielerNamen[i] = "+spielerNamen[i]+" | neuerSpielername = "+neuerSpielerName);
                spielerNamen[i] = neuerSpielerName;
                break;
            }
            i++;
        }

        System.out.println("Replace SessionAttribute vorher: "+sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        System.out.println("Länge: "+spielerNamen.length+" | inhalt: "+spielerNamen[i]+" | 0te Stelle: "+spielerNamen[0]);
        String output = generateSessionOutput(spielerNamen);
        sessionAttribute.replace(Parameter.SPIELER_NAMEN,output);
        System.out.println("Replace SessionAttribute nacher: "+sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        System.out.println("Replace SessionAttribute ohne ToString nacher: "+sessionAttribute.get(Parameter.SPIELER_NAMEN));
        input.getAttributesManager().setSessionAttributes(sessionAttribute);


        System.out.println("neuenSpielerHinzufuegen SessionAttribute2: "+sessionAttribute.get(Parameter.SPIELER_NAMEN).toString());
        System.out.println("neuenSpielerHinzufuegen Input1: "+input.getAttributesManager().getSessionAttributes().get(Parameter.SPIELER_NAMEN).toString());
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

    public Map<String,Object> changeSpielernameVerstanden (Map<String,Object> sessionAttribute){
        boolean spielernameVerstanden = (boolean) sessionAttribute.get(Parameter.SPIELERNAME_VERSTANDEN);
        if (spielernameVerstanden) {
            sessionAttribute.replace(Parameter.SPIELERNAME_VERSTANDEN, false);
        } else {
            sessionAttribute.replace(Parameter.SPIELERNAME_VERSTANDEN, true);
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
            String[] players = readPlayers(input.getAttributesManager().getSessionAttributes());
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

    private Optional<Response> frageObSpielernameRichtigVerstandenWurde(HandlerInput input, String spielername){
        return input.getResponseBuilder()
                .withSpeech(spielername + Text.FRAGE_SPIELERNAME_RICHTIG_VERSTANDEN)
                .withReprompt(spielername + Text.FRAGE_SPIELERNAME_RICHTIG_VERSTANDEN)
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
