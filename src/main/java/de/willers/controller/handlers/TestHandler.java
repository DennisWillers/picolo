package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import de.willers.model.Intentnamen;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class TestHandler implements RequestHandler
{

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.TEST));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder().withSpeech("TEST").withReprompt("REPROMPT-TEST").build();
    }
}
