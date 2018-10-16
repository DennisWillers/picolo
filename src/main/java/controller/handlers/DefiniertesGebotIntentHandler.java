package main.java.controller.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import main.java.controller.intents.DefiniertesGebotController;
import main.java.model.Intentnamen;
import main.java.model.Parameter;
import main.java.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DefiniertesGebotIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intentnamen.DEFINIERTES_GEBOT_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> slots = new HashMap<>();
        slots.putAll(input.getAttributesManager().getRequestAttributes());
        Intent intent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        if(intent.getSlots().get(Parameter.GEBOTSZAHL).getValue() == null || intent.getSlots().get(Parameter.GEBOTSZAHL).getValue().equals("?")){
            return input.getResponseBuilder()
                    .withSpeech(Text.WELCHES_GEBOT)
                    .addElicitSlotDirective(Parameter.GEBOTSZAHL, intent)
                    .build();
        }
        else{
            String value = intent.getSlots().get(Parameter.GEBOTSZAHL).getValue();
            int gebotNr = Integer.parseInt(value);
            DefiniertesGebotController definiertesGebotController = new DefiniertesGebotController(--gebotNr);
            return input.getResponseBuilder()
                    .withSpeech(definiertesGebotController.getGebotText())
                    .withSimpleCard(definiertesGebotController.getGebotTitel(), definiertesGebotController.getGebotText())
                    .build();
        }
    }
}
