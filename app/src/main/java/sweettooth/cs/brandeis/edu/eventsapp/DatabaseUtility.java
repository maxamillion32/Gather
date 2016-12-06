package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/* This rudimentaty class tests the functionality of our Firebase Database--performs
*  very basic database operations. It utilizes JSON database from Firebase account.
*  See bottom of class for how JSON file looks*/

public class DatabaseUtility {
    //allow access to Firebase database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();

    protected enum Tab {HOME, MYEVENTS}

    ;
    private final String userID;
    private final View fragView;
    private final View subView;
    private final Fragment frag;
    private final MyEventsHomeTrackerAdapter adapter;

    protected DatabaseUtility(String userID, View fragView, View subView, Fragment frag, MyEventsHomeTrackerAdapter adapter) {
        this.userID = userID;
        this.fragView = fragView;
        this.subView = subView;
        this.frag = frag;
        this.adapter = adapter;
    }

    protected void accessUserEvents(final Tab tabEnum) {

        Log.d("", "In accessUserEvents()");
        System.out.println("UID: " + userID);

        //if (tabEnum == Tab.HOME) {
          //  final GridView myEventsGridView = Main.homeFrag.myEventsGridView;
        //}

        DatabaseReference usersEventsRef = databaseRef.child("UserToEvents").child(userID);
        final GridView subViewGrid;
        final ListView subViewList;

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
            throw new RuntimeException("enum must be of HOME or MYEVENTS");
        }


        noEvents.setText("");
        exploreBttn.setVisibility(View.INVISIBLE);
        exploreBttn.setText("");

        //creates click listener for button
        View.OnClickListener buttnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Main.bottomBar.selectTabAtPosition(2, true);
            }
        };

        exploreBttn.setOnClickListener(buttnListener);
        //final MyEventsFragment myEventsFrag = Main.myEventsFrag;

        /*listener for changes to a database node (including changes to children) in
          database--will be assigned to userEventsRef*/
        ValueEventListener userEventsListener = new ValueEventListener() {

            //called when attached to reference and when node or its children is modified
            //dataSnapshot is an object representing database contents
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("", "In onDataChange() for user's events");
                if (dataSnapshot.hasChildren()) {

                    final Date currentDate = new Date();

                    //System.out.println("TOTAL DATE: " + currentDate.toString());
                    //System.out.println("YEAR: " + currentDate.getYear());
                    //System.out.println("MONTH: " + currentDate.getMonth());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentDate);
                    System.out.println(cal.toString());
                    final DateTime currentDateTime = new DateTime(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0);

                    final Set<String> eventIDs = new HashSet<String>();
                    final Set<Event> usersEvents = new TreeSet<Event>();

                    //DataSnapshot usersEventIDsSnapshot = dataSnapshot.child("UserToEvents").child(userID);
                    //Iterable<DataSnapshot> childSnapshots = usersEventIDsSnapshot.getChildren();
                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : childSnapshots) {
                        System.out.println(child.getKey());
                        eventIDs.add(child.getKey());
                    }

                    DatabaseReference eventsRef = dataSnapshot.getRef().getRoot().child("Events");

                    ValueEventListener eventsListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("", "In onDataChange() for all events, within onDataChange() for user's events");
                            //DataSnapshot eventsSnapshot = dataSnapshot.child("Events");
                            for (String eventID : eventIDs) {
                                System.out.println(eventID);
                                DataSnapshot childSnapshot = dataSnapshot.child(eventID);
                                Event event = childSnapshot.getValue(Event.class);
                                System.out.println(event.category);
                                System.out.println("Event Date: " + event.getDateTime().formatSimpleDate());
                                System.out.println("Current Date: " + currentDateTime.formatSimpleDate());
                                if (event.getDateTime().compareTo(currentDateTime) < 0) {
                                    Log.d("", "Adding event to set...");
                                    usersEvents.add(event);
                                }
                                if (usersEvents.size() == 0) {
                                    System.out.println("NO EVENTS DETECTED");
                                    noEvents.setText(frag.getResources().getString(R.string.noEvents));
                                    exploreBttn.setText(frag.getResources().getString(R.string.toExplore));
                                    exploreBttn.setVisibility(View.VISIBLE);
                                } else {
                                    noEvents.setVisibility(View.GONE);
                                    exploreBttn.setVisibility(View.GONE);
                                }

                                adapter.populateEventsList(usersEvents);

                                if (tabEnum == Tab.HOME) {
                                    subViewGrid.setAdapter(adapter);
                                } else {
                                    subViewList.setAdapter(adapter);
                                }
                                //Log.d("", "about to set adapter to my events");
                                //Log.d("", "ListView adapter should be set!");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("", "onCancelled : listener for all events, within listener for user's events",
                                    databaseError.toException());
                        }
                    };
                    eventsRef.addListenerForSingleValueEvent(eventsListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error w/ onDataChange
                Log.d("", "onCancelled : listener for user's events ", databaseError.toException());
            }
        };
        //attaches listener to reference
        usersEventsRef.addValueEventListener(userEventsListener);

        //sets click listener for ListView-entry deleted upon click
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

