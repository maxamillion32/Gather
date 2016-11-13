package sweettooth.cs.brandeis.edu.eventsapp;


/**
 * Event object class
 */

public class Event {

    String category;
    DateTime dateTime;
    String title;
    String description;
    int checks;


    public Event(String category, DateTime dt, String t, String d) {
        dateTime = dt;
        title = t;
        description = d;
        checks = 0;
    }

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
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
