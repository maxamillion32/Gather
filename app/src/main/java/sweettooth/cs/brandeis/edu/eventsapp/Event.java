package sweettooth.cs.brandeis.edu.eventsapp;


import java.io.Serializable;

/**
 * Event object class
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

    public String[] getDetailArray () {
        String[] eventDetails = new String[5];
        eventDetails[0] = "Title: " + getTitle();
        eventDetails[1] = "Time: " + getDateTime().formatSimpleDate();
        eventDetails[2] = "Description: " + getDescription();
        eventDetails[3] = "Category: " + getCategory();
        eventDetails[4] = "Location: " + getLocation();
        return eventDetails;
    }

    public int compareTo(Event otherEvent) {
        return this.getDateTime().compareTo(otherEvent.getDateTime());
    }
}
