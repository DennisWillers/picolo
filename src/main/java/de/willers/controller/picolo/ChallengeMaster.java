package de.willers.controller.picolo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import static java.lang.Math.toIntExact;

/**
 * @author Simon
 * Created on 28.10.2018.
 */
public class ChallengeMaster {
    private Blueprint[] blueprints;
    private Random rand;
    private int randomNumber;

    public ChallengeMaster(Blueprint[] blueprints) {
        this.rand = new Random();
        this.blueprints = blueprints;
    }

    public static ChallengeMaster loadFromFile(String file) throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader(file));

        System.out.println("Blueprint");
        Blueprint[] blueprints = new Blueprint[a.size()];
        Iterator<JSONObject> iterator = a.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            JSONObject obj = iterator.next();
            int id = toIntExact((Long) obj.get("id"));
            String template = obj.get("template").toString();
            ChallengeType type = ChallengeType.valueOf(obj.get("type").toString());
            blueprints[i] = new Blueprint(id,template, type);
        }

        return new ChallengeMaster(blueprints);
    }

    public Challenge getChallenge(String[] players, String returnedMessages) {
        System.out.println("getChallege String[] = "+players);
        System.out.println("getChallege String[0] = "+players[0]);
        System.out.println("getChallege String[1] = "+players[1]);
        // filter out blueprints, that need more players than the amount of given players
        Object[] possibleBlueprints = Arrays.stream(blueprints).filter(b -> b.minPlayers() <= players.length).toArray();
        randomNumber = this.rand.nextInt(possibleBlueprints.length);
        Blueprint blueprint = (Blueprint) possibleBlueprints[randomNumber];
        System.out.println("Zufallsblueprint: "+blueprint.getId());
        blueprint = getBlueprint(possibleBlueprints,blueprint,returnedMessages);

        // randomly shuffle players
        String[] shuffled = Arrays.copyOf(players, players.length);
        if (players.length>2){
            System.out.println("Players > 2");
            shuffleArray(shuffled);
            System.out.println(">2 Shuffled[0] = "+shuffled[0]);
            System.out.println(">2 Shuffled[1] = "+shuffled[1]);
        } else if (players.length == 2){
            System.out.println("Players == 2");
            Random random = new Random();
            int zufallsZahl = random.nextInt(2);
            if(zufallsZahl == 0){
                System.out.println("==2");
                String temp = shuffled[0];
                shuffled[0] = shuffled[1];
                shuffled[1] = temp;
            }
            System.out.println("Shuffled[0] = "+shuffled[0]);
            System.out.println("Shuffled[1] = "+shuffled[1]);
        }
        return new Challenge(blueprint, shuffled);
    }

    private Blueprint getBlueprint(Object[] possibleBlueprints, Blueprint blueprint, String returnedMessages){
        System.out.println("getBlueprint: "+blueprint.getId());
        if(returnedMessages.equals("null")) {
            return blueprint;
        }
        String[] messages = returnedMessages.split(",");
        System.out.println("PossibleBlueprints: "+possibleBlueprints.length+" | MessagesLength: "+messages.length);
        if(possibleBlueprints.length <= messages.length){
            return blueprint;
        }
        int blueprintNumber = blueprint.getId();
        for(int i = 0; i < messages.length; i++){
            System.out.println("Message:" +messages[i]+ " | BlueprintNumber:" +blueprintNumber);
            if(Integer.parseInt(messages[i]) == blueprintNumber){
                Blueprint newBlueprint = getNextBlueprint(possibleBlueprints);
                System.out.println("BlueprintNumber: "+blueprintNumber);
                return getBlueprint(possibleBlueprints,newBlueprint,returnedMessages);
            }
        }
        return blueprint;
    }

    private Blueprint getNextBlueprint(Object[] possibleBlueprints){
        if(this.randomNumber == possibleBlueprints.length-1) {
        //if(blueprintNumber == possibleBlueprint.getId()){
            randomNumber = -1;
        }
        randomNumber++;
        System.out.println("GetNextBlueprintNumber: "+randomNumber);
        return (Blueprint) possibleBlueprints[randomNumber];
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
