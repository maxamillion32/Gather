package sweettooth.cs.brandeis.edu.eventsapp;


/**
 * Event object class
 */

public class Event {

    String category;
    int checks;
    DateTime dateTime;
    String description;
    String title;



    public Event(String cat, DateTime dt, String t, String d) {
        category = cat;
        dateTime = dt;
        title = t;
        description = d;
        checks = 0;
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
}
