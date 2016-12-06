package sweettooth.cs.brandeis.edu.eventsapp;

import java.io.Serializable;

/**
 * Event--represents a single Gather event
 */

public class Event implements Comparable<Event>, Serializable {

    public String category;
    public int checks;
    public DateTime dateTime;
    public String description;
    public String title;
    public String location;

    public Event(String cat, int chcks, DateTime dt, String d, String t, String loc) {
        category = cat;
        dateTime = dt;
        title = t;
        description = d;
        checks = chcks;
        location = loc;
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

    public void setLocation(String location) { this.location = location; }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() { return category; }

    public int getChecks() {
        return checks;
    }

    public String getLocation() { return this.location; }

    public  void addCheck() {
        checks++;
    }

    //allows sorting of events by date
    public int compareTo(Event otherEvent) {
        return this.getDateTime().compareTo(otherEvent.getDateTime());
    }
}
