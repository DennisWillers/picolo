package de.willers.controller.picolo;

/**
 * @author Simon
 * Created on 28.10.2018.
 */
public enum ChallengeType {
    GAME("Spiel"),
    CHALLENGE(""),
    EX("Auf Ex");

    private final String type;

    private ChallengeType(String type) {
        this.type = type;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return type.equals(otherName);
    }

    public String toString() {
        return this.type;
    }

}
