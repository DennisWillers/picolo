package de.willers.controller.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import de.willers.model.Parameter;
import de.willers.view.Card;
import de.willers.view.Text;

import java.util.Optional;

public class Response {
    /*
     * RESPONSE MÖGLICHKEITEN
     */
    Optional<com.amazon.ask.model.Response> frageAnzahlSpieler(HandlerInput input, Intent requestIntent) {
        return input.getResponseBuilder()
                .withSpeech(randomPlayerAnswer())
                .addElicitSlotDirective(Parameter.ANZAHL_SPIELER, requestIntent)
                .build();
    }

    Optional<com.amazon.ask.model.Response> frageAnzahlSpielerYesIntent(HandlerInput input, Intent requestIntent) {
        return input.getResponseBuilder()
                .withSpeech(randomPlayerAnswer())
                .withReprompt(randomPlayerAnswer())
                .build();
    }

    Optional<com.amazon.ask.model.Response> frageSpielerNamen(HandlerInput input, int spieler, Intent requestIntent) {
        return input.getResponseBuilder()
                .withSpeech(askPlayerName() + spieler)
                .addElicitSlotDirective(Parameter.NEUER_SPIELER_NAME, requestIntent)
                .build();
    }

    Optional<com.amazon.ask.model.Response> frageSpielerNamenYesOrNoIntent(HandlerInput input, int spieler, Intent requestIntent) {
        return input.getResponseBuilder()
                .withSpeech(askPlayerName() + spieler)
                .withReprompt(askPlayerName() + spieler)
                .build();
    }

    Optional<com.amazon.ask.model.Response> frageObSpielernameRichtigVerstandenWurde(HandlerInput input, String spielername) {
        return input.getResponseBuilder()
                .withSpeech(spielername + Text.FRAGE_SPIELERNAME_RICHTIG_VERSTANDEN)
                .withReprompt(spielername + Text.FRAGE_SPIELERNAME_RICHTIG_VERSTANDEN)
                .build();
    }


    Optional<com.amazon.ask.model.Response> neueChallenge(HandlerInput input, String antwort, String card) {
        return input.getResponseBuilder()
                .withSpeech(antwort)
                .withSimpleCard(Card.CHALLENGE, card)
                .withReprompt(Text.NAECHSTE_AUFGABE)
                .build();
    }

    Optional<com.amazon.ask.model.Response> fehlerNachricht(HandlerInput input) {
        String fehler = "Es ist ein Fehler passiert";
        return input.getResponseBuilder()
                .withSpeech(fehler)
                .withSimpleCard("Fehler", fehler)
                .build();
    }

    //HILFSMETHODEN

    private String randomPlayerAnswer() {
        String[] text = Text.ANZAHL_SPIELER;
        return text[(int) Math.floor(Math.random() * (Text.ANZAHL_SPIELER.length))];
    }

    private String askPlayerName() {
        String[] text = Text.SPIELER_NAME_FRAGEN;
        return text[(int) Math.floor(Math.random() * (Text.SPIELER_NAME_FRAGEN.length))];
    }
}
