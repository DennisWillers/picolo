package main.java.view;

/**
 * Von Dennis Willers (A13A316) am 04.10.2018 erstellt
 */
public interface Text {
    String[] ZEHN_GEBOTE = {"1. Gebot: Ich bin der Herr, dein Gott. Du sollst nicht andere Götter haben neben mir. ",
                            "2. Gebot: Du sollst den Namen des Herrn, deines Gottes, nicht unnütz gebrauchen; denn der Herr wird den nicht ungestraft lassen, der seinen Namen mißbraucht. ",
                            "3. Gebot: Du sollst den Feiertag heiligen. ",
                            "4. Gebot: Du sollst deinen Vater und deine Mutter ehren, auf daß dir's wohlgehe und du lange lebest auf Erden. ",
                            "5. Gebot: Du sollst nicht töten. ",
                            "6. Gebot: Du sollst nicht ehebrechen. ",
                            "7. Gebot: Du sollst nicht stehlen. ",
                            "8. Gebot: Du sollst nicht falsch Zeugnis reden wider deinen Nächsten. ",
                            "9. Gebot: Du sollst nicht begehren deines Nächsten Haus. ",
                            "10. Gebot: Du sollst nicht begehren deines Nächsten Weib, Knecht, Magd, Vieh noch alles, was sein ist. "};

    String WELCHES_GEBOT = "Welches Gebot möchtest du Wissen? Nenne mir eine Zahl! ";

    String HELP          = "Die Gebooooote sind alles was zählt. Erfrage alle 10 Gebote und erleuchte dein Wissen! ";
    String STOP          = "Gott ist mit dir! ";
    String FALLBACK      = "Leider konnte der Herr dich nicht verstehen ";
}
