package main.java.controller.intents;

import main.java.controller.service.FindGebotController;
import main.java.view.Card;

/**
 * Von Dennis Willers (A13A316) am 08.10.2018 erstellt
 */
public class AlleGeboteController {
    private String gebotText;
    private String gebotTitel;

    public AlleGeboteController(){
        FindGebotController controller = new FindGebotController();
        this.gebotText = controller.getAlleGebote();
        this.gebotTitel = Card.TITEL;
    }

    public String getGebotText() {
        return gebotText;
    }

    public void setGebotText(String gebotText) {
        this.gebotText = gebotText;
    }

    public String getGebotTitel() {
        return gebotTitel;
    }

    public void setGebotTitel(String gebotTitel) {
        this.gebotTitel = gebotTitel;
    }
}
