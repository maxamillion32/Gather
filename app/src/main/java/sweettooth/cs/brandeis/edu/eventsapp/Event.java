package sweettooth.cs.brandeis.edu.eventsapp;


/**
 * Event object class
 */

public class Event {

    DateTime dateTime;
    String title;
    String description;
    int checks;

    public Event(DateTime dt, String t, String d) {
        dateTime = dt;
        title = t;
        description = d;
        checks = 0;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getChecks() {
        return checks;
    }

    public  void addCheck() {
        checks++;
    }
}
