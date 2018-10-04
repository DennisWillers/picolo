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
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler(),

                        new AlleGeboteIntentHandler(),
                        new DefiniertesGebotIntentHandler(),
                        new ZufallsGebotIntentHandler())
                .withSkillId("amzn1.ask.skill.60f322c8-0387-4c10-9b39-4418747771c1")
                .build();
    }

    public Main() {
        super(getSkill());
    }

}