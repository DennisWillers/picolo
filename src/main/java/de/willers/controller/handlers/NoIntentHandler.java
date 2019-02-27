package de.willers.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import de.willers.controller.game.Spiellogik;
import de.willers.model.Intentnamen;

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
        Spiellogik spiellogik = new Spiellogik();
        return spiellogik.pruefeContext(input);
    }
}
