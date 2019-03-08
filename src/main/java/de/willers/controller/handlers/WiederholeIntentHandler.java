package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;
import de.willers.view.Card;
import de.willers.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 04.11.2018 erstellt
 */
public class WiederholeIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.WIEDERHOLEINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttribute = new HashMap<>();
        Map<String, Object> inputSessionAttribute = input.getAttributesManager().getSessionAttributes();
        sessionAttribute.putAll(inputSessionAttribute);

        if (sessionAttribute.get(Parameter.LETZTE_NACHRICHT) == null || sessionAttribute.get(Parameter.LETZTE_NACHRICHT).equals("?")){
            return spielNichtGestartetAntwort(input);
        }
        else {
            String wiederholen = (String) sessionAttribute.get(Parameter.LETZTE_NACHRICHT);
            wiederholen = "<prosody rate=\"slow\">"+wiederholen+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> "+"<break time=\"10s\"/> " + "</prosody>";
            return wiederholeNachricht(input,wiederholen);
        }
    }

    private Optional<Response> spielNichtGestartetAntwort(HandlerInput input){
        return input.getResponseBuilder()
                .withSpeech(Text.WIEDERHOLEN_FEHLER)
                .withSimpleCard(Card.TITEL,Text.WIEDERHOLEN_FEHLER)
                .build();
    }

    private Optional<Response> wiederholeNachricht(HandlerInput input, String nachricht){
        return input.getResponseBuilder()
                .withSpeech(nachricht)
                .withReprompt(nachricht)
                .build();
    }
}
