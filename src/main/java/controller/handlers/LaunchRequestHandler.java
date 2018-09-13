package main.java.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;


import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Willkommen beim Skillmaster. Wie kann ich deinen Tag versüßen?";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Beat the Musicmaster", speechText)
                .withReprompt(speechText)
                .build();
    }

}