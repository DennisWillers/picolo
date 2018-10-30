package main.de.willers.java.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import main.de.willers.java.model.Intentnamen;
import main.de.willers.java.model.Parameter;
import main.de.willers.java.view.Text;

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
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        //Frage nach der Anzahl der Spieler
        if (requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue() == null || requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue().equals("?")) {
            System.out.println("Ermittle Anzahl Spieler");
            return frageAnzahlSpieler(input,requestIntent);
        }
        else {
            System.out.println("SessionAttribute Zweig");
            //Get Session Attribute
            Map<String,Object> sessionAttribute = getSessionAttributes(input);
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
                if (++zaehleSpieler != players){
                    System.out.println("ZahleSpieler != Players Zweig");
                    //Nummer des neuen Spielers ermitteln
                    int neuerSpieler = platzDesNeuenSpielersErmitteln(spielerNamen);
                    System.out.println("NeuerSpielerInt: "+neuerSpieler);

                    //Neuen Spieler an ersten zu findenden null Stelle hinzufügen
                    input = neuenSpielerHinzufuegen(spielerNamen,sessionAttribute,neuerSpielerName,input);
                    return frageSpielerNamen(input,neuerSpieler+2,requestIntent);
                }
                else{
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
        return ermittleNaechsteAktion(input,requestIntent);
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

    private Optional<Response> frageAnzahlSpieler(HandlerInput input, Intent requestIntent){
        return input.getResponseBuilder()
                .withSpeech(randomPlayerAnswer())
                .addElicitSlotDirective(Parameter.ANZAHL_SPIELER, requestIntent)
                .build();
    }

    private Optional<Response> frageSpielerNamen(HandlerInput input, int spieler, Intent requestIntent){
        return input.getResponseBuilder()
                .withSpeech(askPlayerName()+spieler+"?")
                .addElicitSlotDirective(Parameter.NEUER_SPIELER_NAME, requestIntent)
                .build();
    }

    private Optional<Response> ermittleNaechsteAktion(HandlerInput input, Intent requestIntent){
        String value = requestIntent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue();
        int gebotNr = Integer.parseInt(value);
        return input.getResponseBuilder()
                .withSpeech(gebotNr + "")
                .withSimpleCard(gebotNr + "", gebotNr + "")
                .build();
    }
}
