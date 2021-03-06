package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import de.willers.controller.game.Hilfslogik;
import de.willers.controller.game.Spiellogik;
import de.willers.model.Context;
import de.willers.model.Intentnamen;
import de.willers.model.Parameter;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 28.01.2019 erstellt
 */
public class NoIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.NOINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String context = (String) input.getAttributesManager().getSessionAttributes().get(Parameter.CONTEXT);
        if(context.equals(Context.START) || context.equals(Context.GAME)){
            return new CancelandStopIntentHandler().handle(input);
        } else {
            return new Hilfslogik().sinnhaftigkeitDesEinsatzesVonJaNeinAnpassen(input);
        }
    }
}
