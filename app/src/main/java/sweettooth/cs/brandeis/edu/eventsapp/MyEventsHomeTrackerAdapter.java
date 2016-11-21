package sweettooth.cs.brandeis.edu.eventsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Will_Masha on 11/20/2016.
 */

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
        //TODO: should return database-set ID
        return index;
    }

    //called iteratively to inflate each log entry and set text fields--reuses view
    public View getView(int index, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.event_entry_home, parent, false);
        }

        Object data = this.getItem(index);
        if (data instanceof Event) {
            Event event = (Event)data;
            ((TextView) view.findViewById(R.id.event_entry_date_time)).setText(event.getDateTime().formatSimpleDate());
            ((TextView) view.findViewById(R.id.event_entry_title)).setText(event.getTitle());
            ((TextView) view.findViewById(R.id.event_entry_category)).setText(event.category);
        } else {
            throw new RuntimeException("item should be of type Event");
        }
        return view;
    }

    //given descrption and notes, constructs single data entry and adds it to list
    public void populateEventsList (Collection<Event> events) {
        this.myEvents = new ArrayList<Event>(events);
    }

}
