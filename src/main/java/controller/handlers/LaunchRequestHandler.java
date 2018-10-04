package main.java.controller.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import main.java.controller.intents.GebotController;


import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        GebotController gebotController = new GebotController();
        return input.getResponseBuilder()
                .withSpeech(gebotController.getGebotText())
                .withSimpleCard(gebotController.getGebotTitel(), gebotController.getGebotText())
                .build();
    }

}