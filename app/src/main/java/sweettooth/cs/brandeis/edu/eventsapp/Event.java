package sweettooth.cs.brandeis.edu.eventsapp;


/**
 * Event object class
 */

public class Event implements Comparable<Event> {

    public String category;
    public int checks;
    public DateTime dateTime;
    public String description;
    public String title;



    public Event(String cat, int chcks, DateTime dt, String d, String t) {
        category = cat;
        dateTime = dt;
        title = t;
        description = d;
        checks = chcks;
    }

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChecks(int checks) {
        this.checks = checks;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int compareTo(Event otherEvent) {
        return this.getDateTime().compareTo(otherEvent.getDateTime());
    }
}
