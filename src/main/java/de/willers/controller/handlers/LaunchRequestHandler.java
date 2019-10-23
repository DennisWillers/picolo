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
        Hilfslogik hilfslogik = new Hilfslogik();
        Map<String, Object> sessionAttribute = hilfslogik.getSessionAttributes(input);
        String speech;
        if(hilfslogik.existiertSpielstand(input)){
            sessionAttribute.put(Parameter.CONTEXT, Context.LOAD_GAME);
            speech = Text.PICOLO_SPIELSTAND_LADEN;
        } else {
            sessionAttribute.put(Parameter.CONTEXT, Context.START);
            speech = Text.PICOLO_START;
        }
        input.getAttributesManager().setSessionAttributes(sessionAttribute);

        return input.getResponseBuilder()
                .withSpeech(speech)
                .withReprompt(speech)
                .build();
    }

}
