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



    //SSML Aussprache
    String[] ZEHN_GEBOTE_SSML = {"<emphasis level=\"strong\"><p>Erstes Gebot:</p></emphasis> <amazon:effect name=\"whispered\">Ich bin der Herr, dein Gott. Du sollst nicht andere Götter haben neben mir.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Zweites Gebot:</p></emphasis>  <amazon:effect name=\"whispered\">Du sollst den Namen des Herrn, deines Gottes, nicht unnütz gebrauchen; denn der Herr wird den nicht ungestraft lassen, der seinen Namen mißbraucht.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Drittes Gebot:</p></emphasis>  <amazon:effect name=\"whispered\">Du sollst den Feiertag heiligen.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Viertes Gebot:</p></emphasis>  <amazon:effect name=\"whispered\">Du sollst deinen Vater und deine Mutter ehren, auf daß dir's wohlgehe und du lange lebest auf Erden.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Fünftes Gebot:</p></emphasis>  <amazon:effect name=\"whispered\">Du sollst nicht töten.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Sechstes Gebot:</p></emphasis> <amazon:effect name=\"whispered\">Du sollst nicht ehebrechen.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Siebtes Gebot:</p></emphasis>  <amazon:effect name=\"whispered\">Du sollst nicht stehlen.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Achtes Gebot:</p></emphasis>  <amazon:effect name=\"whispered\">Du sollst nicht falsch Zeugnis reden wider deinen Nächsten.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Neuntes Gebot:</p></emphasis> <amazon:effect name=\"whispered\">Du sollst nicht begehren deines Nächsten Haus.</amazon:effect> ",
            "<emphasis level=\"strong\"><p>Zehntes Gebot:</p></emphasis> <amazon:effect name=\"whispered\">Du sollst nicht begehren deines Nächsten Weib, Knecht, Magd, Vieh noch alles, was sein ist.</amazon:effect> "};

    String WELCHES_GEBOT_SSML = "<emphasis level=\"reduced\"><s>Welches Gebot möchtest du Wissen? </s>Nenne mir eine Zahl!</emphasis> ";

    String HELP_SSML          = "<amazon:effect name=\"whispered\">Die Gebooooote sind alles was zählt. Erfrage alle 10 Gebote und erleuchte dein Wissen!</amazon:effect> ";
    String STOP_SSML          = "<emphasis level=\"strong\">Gott ist mit dir!</emphasis> ";
    String FALLBACK_SSML      = "<emphasis level=\"reduced\">Leider konnte der Herr dich nicht verstehen</emphasis>";
}
