package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DatabaseUtility--has one principal method that iterates
 * through database and populates events into a view. This
 * class is used both to populate the gridview on the home
 * fragment and the listview on the my events fragment
 */

public class DatabaseUtility {
    //allow access to Firebase database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();
    //indicated tab/fragment, home or my events
    protected enum Tab {HOME, MYEVENTS};

    //final needed for use of variables in inner classes
    private final String userID;
    private final View fragView;
    //gridview or listview
    private final View subView;
    private final Fragment frag;
    private final MyEventsHomeTrackerAdapter adapter;
    private final String logTag;

    protected DatabaseUtility(String logTag, String userID, View fragView, View subView,
                              Fragment frag, MyEventsHomeTrackerAdapter adapter) {
        this.userID = userID;
        this.fragView = fragView;
        this.subView = subView;
        this.frag = frag;
        this.adapter = adapter;
        this.logTag = logTag + ": in DButil";
    }

    //iterates through firebase database using listeners and populates subview
    protected void accessUserEvents(final Tab tabEnum) {

        Log.d(logTag, "In accessUserEvents()");

        DatabaseReference usersEventsRef = databaseRef.child("UserToEvents").child(userID);
        final GridView subViewGrid;
        final ListView subViewList;

        //displayed if there are no events to show--button goes to explore fragment
        final TextView noEvents;
        final Button exploreBttn;

        if (tabEnum == Tab.HOME) {
            subViewGrid = (GridView) subView;
            subViewList = null;
            exploreBttn = (Button) fragView.findViewById(R.id.button_to_explore);
            noEvents = (TextView) fragView.findViewById(R.id.no_events);
        } else if (tabEnum == Tab.MYEVENTS) {
            subViewList = (ListView) subView;
            subViewGrid = null;
            exploreBttn = (Button) fragView.findViewById(R.id.button_to_explore2);
            noEvents = (TextView) fragView.findViewById(R.id.no_events2);
        } else {
            exploreBttn = null;
            noEvents = null;
            subViewGrid = null;
            subViewList = null;
            throw new RuntimeException("enum must be HOME or MYEVENTS");
        }

        noEvents.setVisibility(View.GONE);
        exploreBttn.setVisibility(View.GONE);

        //creates click listener for button
        View.OnClickListener buttnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Main.bottomBar.selectTabAtPosition(2, true);
            }
        };

        exploreBttn.setOnClickListener(buttnListener);

        /*listener for changes to a database node (including changes to children) in
          database--will be assigned to userEventsRef*/
        ValueEventListener userEventsListener = new ValueEventListener() {

            //called when attached to reference and when node or its children is modified
            //dataSnapshot is an object representing database contents
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(logTag, "In onDataChange() for user's events");
                if (dataSnapshot.hasChildren()) {

                    //gets DateTime of current time, for purpose of filtering out passed events
                    final Date currentDate = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentDate);
                    System.out.println(cal.toString());
                    final DateTime currentDateTime = new DateTime(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0);

                    //set of ids for events user is interested in
                    final Set<String> eventIDs = new HashSet<String>();
                    //list of events interested in
                    final List<Event> usersEvents = new ArrayList<Event>();

                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : childSnapshots) {
                        System.out.println(child.getKey());
                        eventIDs.add(child.getKey());
                    }

                    //reference to all events
                    DatabaseReference eventsRef = dataSnapshot.getRef().getRoot().child("Events");

                    //nested listener for all events--however, onDataChange will only be called
                    //upon attachment since we will use addListenerForSingleValueEvent method
                    //(see below). Attaching a listener is the only means to access the values
                    //in database, so we do it this way even though the fragment does not need
                    //to listen for any change to database under events node
                    ValueEventListener eventsListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(logTag, "In onDataChange() for all events, within onDataChange() for user's events");
                            //DataSnapshot eventsSnapshot = dataSnapshot.child("Events");
                            for (String eventID : eventIDs) {
                                //gets snapshot of an event
                                DataSnapshot childSnapshot = dataSnapshot.child(eventID);
                                //reconstitutes stored event object
                                Event event = childSnapshot.getValue(Event.class);
                                //only adds events of today or in the future
                                if (event.getDateTime().compareTo(currentDateTime) <= 0) {
                                    Log.d(logTag, "Adding event to set...");
                                    usersEvents.add(event);
                                }
                                //sorts events by date
                                Collections.sort(usersEvents);
                                Collections.reverse(usersEvents);

                                //for populateing subView...
                                adapter.populateEventsList(usersEvents);
                                if (tabEnum == Tab.HOME) {
                                    subViewGrid.setAdapter(adapter);
                                } else {
                                    subViewList.setAdapter(adapter);
                                }
                                Log.d(logTag, "subView should be set");
                            }
                            if (usersEvents.size() == 0) {
                                //No events to show!
                                noEvents.setText(fragView.getResources().getString(R.string.noEvents));
                                exploreBttn.setText(fragView.getResources().getString(R.string.toExplore));
                                noEvents.setVisibility(View.VISIBLE);
                                exploreBttn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //error w/ onDataChange
                            Log.d(logTag, "onCancelled : listener for all events, within listener for user's events",
                                    databaseError.toException());
                        }
                    };
                    //onDataChange only called upon attachment
                    eventsRef.addListenerForSingleValueEvent(eventsListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error w/ onDataChange
                Log.d(logTag, "onCancelled : listener for user's events ", databaseError.toException());
            }
        };
        //attaches listener to reference--onDataChange called whenever database reference has changes
        usersEventsRef.addValueEventListener(userEventsListener);

        //sets click listener for subView-goes to full-screen activity of event on click
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Event event = (Event) adapter.getItem(position);
                // pull up event activity
                Intent intent = new Intent("sweettooth.cs.brandeis.edu.eventsapp.CompleteEvent");
                Bundle bundle = new Bundle();
                bundle.putSerializable("KEY", event);
                intent.putExtras(bundle);
                frag.startActivity(intent);
            }
        };

        if (tabEnum == Tab.HOME) {
            subViewGrid.setOnItemClickListener(listener);
        } else {
            subViewList.setOnItemClickListener(listener);
        }
    }
}