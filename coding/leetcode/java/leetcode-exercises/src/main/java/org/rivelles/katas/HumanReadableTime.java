package org.rivelles.katas;

public class HumanReadableTime {
    public static String makeReadable(int seconds) {
        var hours = seconds / 3600;
        seconds = seconds % 3600;
        var minutes = seconds / 60;
        seconds = seconds % 60;

        return String.join(":", asString(hours), asString(minutes), asString(seconds));
    }

    private static String asString(Integer time) {
        var numberAsString = time.toString();
        if (time < 10) numberAsString = "0"+numberAsString;
        return numberAsString;
    }
}
