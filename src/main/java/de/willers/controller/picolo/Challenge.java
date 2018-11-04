package de.willers.controller.picolo;

/**
 * @author Simon
 * Created on 28.10.2018.
 */
public class Challenge {
    private Blueprint blueprint;
    private String[] players;

    public Challenge(Blueprint blueprint, String[] players) {
        this.blueprint = blueprint;
        this.players = players;
    }

    public String toString() {
        return blueprint.toString(players);
    }
}
