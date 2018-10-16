package main.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.controller.intents.AlleGeboteController;
import main.java.controller.intents.DefiniertesGebotController;
import main.java.model.Intentnamen;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class AlleGeboteIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.ALLE_GEBOTE_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        AlleGeboteController alleGeboteController = new AlleGeboteController();
        return input.getResponseBuilder()
                .withSpeech(alleGeboteController.getGebotTextSSML())
                .withSimpleCard(alleGeboteController.getGebotTitel(), alleGeboteController.getGebotText())
                .build();
    }

}
