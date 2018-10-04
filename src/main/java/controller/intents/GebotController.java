package main.java.controller.intents;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public class GebotController {
    private int gebotNr;
    private String gebotText;
    private String gebotTitel;

    public GebotController(){

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
}
