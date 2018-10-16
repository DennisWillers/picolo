package main.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.model.Intentnamen;
import main.java.view.Card;
import main.java.view.Text;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.HELPINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = Text.HELP;
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(Card.TITEL, speechText)
                .withReprompt(speechText)
                .build();
    }
}
