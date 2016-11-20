package sweettooth.cs.brandeis.edu.eventsapp;

/**
 * DateTime class
 */

public class DateTime {

    public int day;
    public int hour;
    public int minute;
    public int month;
    public int year;



    public DateTime () {

    }


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
