package de.willers.controller.picolo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Simon
 * Created on 28.10.2018.
 */
public class ChallengeMaster {
    private Blueprint[] blueprints;
    private Random rand;

    public ChallengeMaster(Blueprint[] blueprints) {
        this.rand = new Random();
        this.blueprints = blueprints;
    }

    public static ChallengeMaster loadFromFile(String file) throws Exception {
        System.out.println("Parser");
        JSONParser parser = new JSONParser();
        System.out.println("ReadTest");
        System.out.println("JsonArray FileReader");
        JSONArray a = (JSONArray) parser.parse(new FileReader(file));

        System.out.println("Blueprint");
        Blueprint[] blueprints = new Blueprint[a.size()];
        Iterator<JSONObject> iterator = a.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            JSONObject obj = iterator.next();
            String template = obj.get("template").toString();
            ChallengeType type = ChallengeType.valueOf(obj.get("type").toString());
            blueprints[i] = new Blueprint(template, type);
        }

        return new ChallengeMaster(blueprints);
    }

    public Challenge getChallenge(String[] players) {
        // filter out blueprints, that need more players than the amount of given players
        Object[] possibleBlueprints = Arrays.stream(blueprints).filter(b -> b.minPlayers() <= players.length).toArray();
        Blueprint blueprint = (Blueprint) possibleBlueprints[this.rand.nextInt(possibleBlueprints.length)];

        // randomly shuffle players
        String[] shuffled = Arrays.copyOf(players, players.length);
        shuffleArray(shuffled);
        return new Challenge(blueprint, shuffled);
    }

    static void shuffleArray(String[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    // just a simple test
    /*public static void main(String[] args) {
        try {
            ChallengeMaster master = loadFromFile("./src/main/resources/blueprint.json");
            for (int i = 0; i < 10; i++)
                System.out.println(master.getChallenge(new String[]{"Simon", "Dennis", "Daniel", "Lucie"}).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}
