package main.java.controller;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;
import main.java.controller.handlers.*;

public class Main extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new DailySongIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                .withSkillId("amzn1.ask.skill.08b3ba96-965d-4a81-8a13-e7c8fe03a322")
                .build();
    }

    public Main() {
        super(getSkill());
    }

}
