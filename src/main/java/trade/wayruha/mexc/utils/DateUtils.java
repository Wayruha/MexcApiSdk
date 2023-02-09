package trade.wayruha.mexc.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class DateUtils {
    public static final String DATE_TIME_PRETTY_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter PRETTY_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PRETTY_FORMATTER);

    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    public static LocalDateTime parse(String dateTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String currentDate(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    public static String currentDatePretty() {
        return LocalDateTime.now().format(PRETTY_FORMATTER);
    }

    public static String getUnixTime() {
        StringBuilder nowStr = new StringBuilder(Instant.now().toString());

        return new Date(System.currentTimeMillis()).toInstant().toString();
    }
}
