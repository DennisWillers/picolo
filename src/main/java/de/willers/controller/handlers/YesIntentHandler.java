package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import de.willers.controller.game.Hilfslogik;
import de.willers.controller.game.Spiellogik;
import de.willers.model.Context;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 28.01.2019 erstellt
 */
public class YesIntentHandler implements RequestHandler {

    Spiellogik spiellogik = new Spiellogik();
    Hilfslogik hilfslogik = new Hilfslogik();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.YESINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttribute = hilfslogik.getSessionAttributes(input);
        String context = (String) sessionAttribute.get(Parameter.CONTEXT);
        if (context == null){
            sessionAttribute.put(Parameter.CONTEXT, Context.START);
            input.getAttributesManager().setSessionAttributes(sessionAttribute);
            return spiellogik.pruefeContext(input);
        } else {
            return spiellogik.pruefeContext(input);
            /*return input.getResponseBuilder()
                    .withSpeech("Yes-Intent-Called")
                    .withReprompt("Yes-Intent-Called")
                    .build();*/
        }
        /*Spiellogik spiellogik = new Spiellogik();
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        Map<String, Object> newSessionAttributes = spiellogik.changeSpielernameVerstanden(sessionAttributes);
        input.getAttributesManager().setSessionAttributes(newSessionAttributes);
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        return spiellogik.pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input,sessionAttributes,requestIntent);*/
    }
}
