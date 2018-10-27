package main.de.willers.java.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import main.de.willers.java.view.Text;


import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        /*Map<String, Object> slots = new HashMap<>();
        slots.putAll(input.getAttributesManager().getSessionAttributes());*/


        return input.getResponseBuilder()
                .withSpeech(Text.PICOLO_START)
                .withReprompt(Text.PICOLO_START)
                .build();
    }

}