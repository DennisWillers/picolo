package main.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.controller.intents.DefiniertesGebotController;
import main.java.model.Intentnamen;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DefiniertesGebotIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.DEFINIERTES_GEBOT_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        /*if(input.getRequestEnvelope().getSession().getAttributes().get(Slot.GEBOTSZAHL) == null){

            return input.getResponseBuilder()
                    .addElicitSlotDirective(Slot.GEBOTSZAHL, ),
        }
        else{*/
            DefiniertesGebotController definiertesGebotController = new DefiniertesGebotController();
            return input.getResponseBuilder()
                    .withSpeech(definiertesGebotController.getGebotText())
                    .withSimpleCard(definiertesGebotController.getGebotTitel(), definiertesGebotController.getGebotText())
                    .build();
        //}
    }
}
