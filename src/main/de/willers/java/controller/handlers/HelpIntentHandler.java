package main.de.willers.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.de.willers.java.model.Intentnamen;
import main.de.willers.java.view.Card;
import main.de.willers.java.view.Text;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.HELPINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSpeech(Text.HELP_SSML)
                .withSimpleCard(Card.TITEL, Text.HELP)
                .withReprompt(Text.HELP_SSML)
                .build();
    }
}
