package main.java.controller.service;

import main.java.view.Text;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public class FindGebotController {

    public String getGebotText(int index){
        return Text.ZEHN_GEBOTE[index];
    }

    public String getZufallsGebot() {
        int index = (int) Math.round(Math.random() * 10);
        if(index == 10) index = 9;
        return Text.ZEHN_GEBOTE[index];
    }
}
