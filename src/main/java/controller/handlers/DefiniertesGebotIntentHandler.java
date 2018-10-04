package main.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.model.Intentnamen;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DefiniertesGebotIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.DEFINIERTES_GEBOT_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Es gibt noch kein Gebot";
       return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Beat the Musicmaster", speechText)
                .build();
    }
}
