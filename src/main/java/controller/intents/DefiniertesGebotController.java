package main.java.controller.intents;

import main.java.controller.service.FindGebotController;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public class DefiniertesGebotController {
    private int gebotNr;
    private String gebotText;
    private String gebotTextSSML;
    private String gebotTitel;

    public DefiniertesGebotController(int gebotNr){
        FindGebotController controller = new FindGebotController();
        this.gebotNr = gebotNr;
        this.gebotText = controller.getGebotText(this.gebotNr);
        this.gebotTextSSML = controller.getGebotTextSSML(this.gebotNr);
        this.gebotTitel = ++this.gebotNr + ". Gebot";
    }

    public int getGebotNr() {
        return gebotNr;
    }

    public void setGebotNr(int gebotNr) {
        this.gebotNr = gebotNr;
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

    public String getGebotTextSSML() {
        return gebotTextSSML;
    }

    public void setGebotTextSSML(String gebotTextSSML) {
        this.gebotTextSSML = gebotTextSSML;
    }
}
