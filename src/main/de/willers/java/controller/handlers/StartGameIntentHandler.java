package main.de.willers.java.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import main.de.willers.java.model.Intentnamen;
import main.de.willers.java.model.Parameter;
import main.de.willers.java.view.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Von Dennis Willers (A13A316) am 28.10.2018 erstellt
 */
public class StartGameIntentHandler implements RequestHandler
{
    @Override
    public boolean canHandle(HandlerInput input) {
    return input.matches(intentName(Intentnamen.STARTGAMEINTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> slots = new HashMap<>();
        slots.putAll(input.getAttributesManager().getRequestAttributes());
        Intent intent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        if (intent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue() == null || intent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue().equals("?")) {
            return input.getResponseBuilder()
                    .withSpeech(randomPlayerAnswer())
                    .addElicitSlotDirective(Parameter.ANZAHL_SPIELER, intent)
                    .build();
        } else {
            String value = intent.getSlots().get(Parameter.ANZAHL_SPIELER).getValue();
            int gebotNr = Integer.parseInt(value);
            return input.getResponseBuilder()
                    .withSpeech(gebotNr + "")
                    .withSimpleCard(gebotNr + "", gebotNr + "")
                    .build();
        }
    }

    private String randomPlayerAnswer(){
            String[] text = Text.ANZAHL_SPIELER;
            return text[(int) Math.floor(Math.random() * (Text.ANZAHL_SPIELER.length))];
    }
}
