package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import de.willers.controller.intents.Spiellogik;
import de.willers.model.Intentnamen;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 28.01.2019 erstellt
 */
public class YesIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.YESINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        /*return input.getResponseBuilder()
                .withSpeech("Yes-Intent-Called")
                .withReprompt("Yes-Intent-Called")
                .build();*/
        Spiellogik spiellogik = new Spiellogik();
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        Map<String, Object> newSessionAttributes = spiellogik.changeSpielernameVerstanden(sessionAttributes);
        input.getAttributesManager().setSessionAttributes(newSessionAttributes);
        Intent requestIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        return spiellogik.pruefeObWeitereSpielernamenGeprueftWerdenMuessenResponse(input,sessionAttributes,requestIntent);
    }
}
