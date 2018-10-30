package de.willers.controller.picolo;

import java.text.MessageFormat;

/**
 * @author Simon
 * Created on 28.10.2018.
 */
public class Blueprint {
    private MessageFormat format;
    private ChallengeType type;

    public Blueprint(String text, ChallengeType type) {
        this.format = new MessageFormat(text);
        this.type = type;
    }

    public int minPlayers() {
        return this.format.getFormats().length;
    }

    public ChallengeType getType() {
        return this.type;
    }

    public String format(String[] players) {
        return this.format.format(players);
    }
}
