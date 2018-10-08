package main.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.controller.intents.DefiniertesGebotController;
import main.java.controller.intents.ZufallsGebotController;
import main.java.model.Intentnamen;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ZufallsGebotIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.ZUFALLS_GEBOT_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ZufallsGebotController zufallsGebotController = new ZufallsGebotController();
        return input.getResponseBuilder()
                .withSpeech(zufallsGebotController.getGebotText())
                .withSimpleCard(zufallsGebotController.getGebotTitel(), zufallsGebotController.getGebotText())
                .build();
    }

}
