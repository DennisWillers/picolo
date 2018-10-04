package main.java.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.model.Intentnamen;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CancelandStopIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.STOPINTENT).or(intentName(Intentnamen.CANCELINTENT)));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Tschüüs";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Beat the Musicmaster", speechText)
                .build();
    }
}
