package sweettooth.cs.brandeis.edu.eventsapp;

import java.io.Serializable;

/**
 * DateTime--custom class representing date and time
 */

public class DateTime implements Comparable<DateTime>, Serializable {

    public int day;
    public int hour;
    public int minute;
    public int month;
    public int year;

    public DateTime () {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
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

    public void setYear(int year) { this.year = year; }

    public void setMonth(int month) { this.month = month; }

    public void setDay(int day) { this.day = day; }

    public void setHour(int hour) { this.hour = hour; }

    public void setMinute(int minute) { this.minute = minute; }


    //allows chronological sorting
    public int compareTo(DateTime otherDateTime) {
        //int year = dateTime.getYear();
        int otherYear = otherDateTime.getYear();
        //int month = dateTime.getMonth();
        int otherMonth = otherDateTime.getMonth();
        //int day = dateTime.getDay();
        int otherDay = otherDateTime.getDay();
        if (year < otherYear) {
            return 1;
        } else if (year > otherYear) {
            return -1;
        } else {
            if (month < otherMonth) {
                return 1;
            } else if (month > otherMonth) {
                return -1;
            } else {
                if (day < otherDay) {
                    return 1;
                } else if (day > otherDay) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public String formatCalendarDateForMatching () {
        return this.day + " " + this.month + " " + this.year;
    }

    //formats simple string representing date
    public String formatSimpleDate() {
        int monthNum = this.month;
        int hourMilitary = this.hour;
        String hourCivilian = hourMilitary <= 12 ? hourMilitary + " AM" : hourMilitary - 12 + " PM";

        String month;
        switch (monthNum) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sept";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;
            default:
                throw new RuntimeException("'" + this.month + "'" + " invalid month");
        }
        return month + " " + this.day + ", " + hourCivilian;
    }
}