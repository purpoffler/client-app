package utlis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateCalculator {
    public static String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
