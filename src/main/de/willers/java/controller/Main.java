package main.de.willers.java.controller;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;
import main.de.willers.java.controller.handlers.*;
import main.de.willers.java.model.Parameter;

public class Main extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                .withSkillId(Parameter.SKILL_ID)
                .build();
    }

    public Main() {
        super(getSkill());
    }

}