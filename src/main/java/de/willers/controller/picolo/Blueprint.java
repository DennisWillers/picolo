package de.willers.controller.picolo;

import java.text.MessageFormat;

/**
 * @author Simon
 * Created on 28.10.2018.
 */
public class Blueprint {
    private MessageFormat format;
    private ChallengeType type;
    private int id;

    public Blueprint(int id, String text, ChallengeType type) {
        this.id = id;
        this.format = new MessageFormat(text);
        this.type = type;
    }

    public int minPlayers() {
        return this.format.getFormats().length;
    }

    public ChallengeType getType() {
        return this.type;
    }

    public String toString(String[] players) {
        return this.format.format(players);
    }

    public int getId() {
        return this.id;
    }
}

