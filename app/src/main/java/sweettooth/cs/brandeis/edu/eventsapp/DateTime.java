package sweettooth.cs.brandeis.edu.eventsapp;

/**
 * DateTime class
 */

public class DateTime {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public DateTime (int y, int m, int d, int h, int min) {
        year = y;
        month = m;
        day = d;
        hour = h;
        minute = min;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
