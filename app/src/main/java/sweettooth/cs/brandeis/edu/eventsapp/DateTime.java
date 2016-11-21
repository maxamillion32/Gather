package sweettooth.cs.brandeis.edu.eventsapp;

/**
 * DateTime class
 */

public class DateTime implements Comparable<DateTime> {

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

    //most recent first
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

    public String formatSimpleDate() {
        int monthNum = this.month;
        int hourMilitary = this.hour;
        String hourCivilian = hourMilitary <= 12 ? hourMilitary + " AM" : hourMilitary - 12 + " PM";

        String month;
        switch (monthNum) {
            case 1:
                month = "JAN";
                break;
            case 2:
                month = "FEB";
                break;
            case 3:
                month = "MAR";
                break;
            case 4:
                month = "APR";
                break;
            case 5:
                month = "MAY";
                break;
            case 6:
                month = "JUN";
                break;
            case 7:
                month = "JUL";
                break;
            case 8:
                month = "AUG";
                break;
            case 9:
                month = "SEPT";
                break;
            case 10:
                month = "OCT";
                break;
            case 11:
                month = "NOV";
                break;
            case 12:
                month = "DEC";
                break;
            default:
                throw new RuntimeException("invalid month");
        }
        return month + " " + this.day + ", " + hourCivilian;
    }
}
