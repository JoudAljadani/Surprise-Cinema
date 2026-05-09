import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class BookingManager {
    private static final Random random = new Random();

    private static final String[] CINEMAS = {
            "AMC Cinema", "VOX Cinema",
            "Muvi Cinema", "Empire Cinema"
    };


    public static String getRandomCinema() {
        int index = random.nextInt(CINEMAS.length);
        return CINEMAS[index];
    }

    public static String generateHall() {
        int hallNumber = random.nextInt(3) + 1;
        return "Hall " + hallNumber;
    }

    public static String generateDate() {
        return LocalDate.now().toString();
    }

    public static String generateSeat() {
        char row = (char) ('A' + random.nextInt(5));
        int number = random.nextInt(15) + 1;

        return row + String.valueOf(number);
    }

    public static String generateRandomTime(String range) {
        String[] parts = range.split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        LocalTime start = LocalTime.parse(parts[0], formatter);
        LocalTime end = LocalTime.parse(parts[1], formatter);

        int startMinutes = start.getHour() * 60 + start.getMinute();

        int endMinutes = end.getHour() * 60 + end.getMinute();

        // Handle range that crosses midnight
        if (endMinutes < startMinutes) {
            endMinutes += 24 * 60;
        }
        int randomMinutes = startMinutes + random.nextInt(endMinutes - startMinutes + 1);
        randomMinutes = randomMinutes % (24 * 60);
        LocalTime randomTime = LocalTime.of(randomMinutes / 60, randomMinutes % 60);
        return randomTime.format(formatter);
    }
}
