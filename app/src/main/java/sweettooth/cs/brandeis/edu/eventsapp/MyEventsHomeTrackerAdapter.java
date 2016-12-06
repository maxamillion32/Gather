package sweettooth.cs.brandeis.edu.eventsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MyEventsHomeTrackerAdapter--custom adapter for populating
 * listview or gridview of user's events*/

public class MyEventsHomeTrackerAdapter extends BaseAdapter {

    //list of user's upcoming events
    private ArrayList<Event> myEvents;

    //gets list size
    public int getCount() {
        return myEvents.size();
    }

    //gets item at index
    public Object getItem(int index) {
        return myEvents.get(index);
    }

    //simply returns argument
    public long getItemId(int index) {
        return index;
    }

    //called iteratively to inflate each event entry and set text fields--reuses view
    public View getView(int index, View view, ViewGroup parent) {
        //if parent is a gridview...
        if (parent.getId() == R.id.home_gridview) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.event_entry_home, parent, false);
            }

            Object data = this.getItem(index);
            if (data instanceof Event) {
                Event event = (Event) data;
                ((TextView) view.findViewById(R.id.event_entry_date_time)).setText(event.getDateTime().formatSimpleDate());
                ((TextView) view.findViewById(R.id.event_entry_title)).setText(event.getTitle());
                ((TextView) view.findViewById(R.id.event_entry_category)).setText(event.category);
            } else {
                throw new RuntimeException("item should be of type Event");
            }
        //parent should be listview
        } else {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.event_entry_my_events, parent, false);
            }

            Object data = this.getItem(index);
            if (data instanceof Event) {
                Event event = (Event) data;
                ((TextView) view.findViewById(R.id.my_events_title)).setText(event.getTitle());
                ((TextView) view.findViewById(R.id.my_events_datetime)).setText(event.getDateTime().formatSimpleDate());
                ((TextView) view.findViewById(R.id.my_events_description)).setText(event.getDescription());
                ((TextView) view.findViewById(R.id.my_events_category)).setText(event.category);
                ((TextView) view.findViewById(R.id.my_events_location)).setText(event.getLocation());
            } else {
                throw new RuntimeException("item should be of type Event");
            }
        }
        return view;
    }

    //populates adapter's list given list of events
    public void populateEventsList (Collection<Event> events) {
        this.myEvents = new ArrayList<Event>(events);
    }
}
