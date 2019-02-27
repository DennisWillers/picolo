package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import de.willers.controller.game.Hilfslogik;
import de.willers.model.Context;
import de.willers.model.Parameter;
import de.willers.view.Text;


import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttribute = new Hilfslogik().getSessionAttributes(input);
        sessionAttribute.put(Parameter.CONTEXT, Context.START);
        input.getAttributesManager().setSessionAttributes(sessionAttribute);

        return input.getResponseBuilder()
                .withSpeech(Text.PICOLO_START)
                .withReprompt(Text.PICOLO_START)
                .build();
    }

}