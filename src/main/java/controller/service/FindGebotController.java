package main.java.controller.service;

import main.java.view.Text;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public class FindGebotController {

    public String getGebotText(int index){
        return Text.ZEHN_GEBOTE[index];
    }

    public String getGebotTextSSML(int index){
        return Text.ZEHN_GEBOTE_SSML[index];
    }

    public int getZufallsZahl() {
        int index = (int) Math.round(Math.random() * 10);
        if(index == 10) index = 9;
        return index;
    }

    public String getAlleGebote() {
        StringBuilder sb = new StringBuilder();
        for (String s : Text.ZEHN_GEBOTE) {
            sb.append(s);
        }
        return sb.toString();
    }

    public String getAlleGeboteSSML() {
        StringBuilder sb = new StringBuilder();
        for (String s : Text.ZEHN_GEBOTE_SSML) {
            sb.append(s);
        }
        return sb.toString();
    }
}
