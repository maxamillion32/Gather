package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Home Fragment
 */

public class HomeFragment extends Fragment {

    //allow access to Firebase database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();
    //private static final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String userID = "WaUzsjdZwcN0og4vTu00JHPhWW32";
    private static final String logTag = "HomeFragment";

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String text) {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        Log.d(logTag, "In onCreate()");
        super.onCreateView(inflater, container, savedInstanceState);
        View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        //updates reference to database automatically when database is modified
        databaseRef.keepSynced(true);

        final ListView myEventListView = (ListView) homeFragmentView.findViewById(R.id.homeListView);

        //accesses user's subsribed events. This code would be better suited in an activity
        //String userID = user.getUid();

        DatabaseReference usersEventsRef = databaseRef.child("UserToEvents").child(userID);

        //listener for changes to a node in database
        ValueEventListener userEventsListener = new ValueEventListener() {

            //called when attached to reference and when node or its children is modified
            //dataSnapshot is an object representing database contents
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(logTag, "In onDataChange() for user's events");
                if (dataSnapshot.hasChildren()) {

                    Date currentDate = new Date();
                    final DateTime currentDateTime = new DateTime(currentDate.getYear(),
                                    currentDate.getMonth(), currentDate.getDay(), 0, 0);

                    final MyEventsHomeTrackerAdapter adapter = new MyEventsHomeTrackerAdapter();

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
                            Log.d(logTag, "In onDataChange() for all events, within onDataChange() for user's events");
                            //DataSnapshot eventsSnapshot = dataSnapshot.child("Events");
                            for (String eventID : eventIDs) {
                                System.out.println(eventID);
                                DataSnapshot childSnapshot = dataSnapshot.child(eventID);
                                Event event = childSnapshot.getValue(Event.class);
                                System.out.println(event.category);
                                if (event.getDateTime().compareTo(currentDateTime) < 0) {
                                    Log.d(logTag, "Adding event to set...");
                                    usersEvents.add(event);
                                }
                                adapter.populateEventsList(usersEvents);
                                myEventListView.setAdapter(adapter);
                                Log.d(logTag, "ListView adapter should be set!");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(logTag, "onCancelled : listener for all events, within listener for user's events",
                                            databaseError.toException());
                        }
                    };
                    eventsRef.addListenerForSingleValueEvent(eventsListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error w/ onDataChange
                Log.d(logTag, "onCancelled : listener for user's events ", databaseError.toException());
            }
        };
        //attaches listener to reference
        usersEventsRef.addValueEventListener(userEventsListener);



        ViewFlipper flip = (ViewFlipper) homeFragmentView.findViewById(R.id.discoverFlip);

        TextView event = (TextView) homeFragmentView.findViewById(R.id.event1);
        TextView event2 = (TextView) homeFragmentView.findViewById(R.id.event1);
        DatabaseUtility dbUtil = new DatabaseUtility();
        //List<String> eventlist = dbUtil.getTopEvents();
        //event.setText(eventlist.get(0));
        //event2.setText(eventlist.get(1));

        flip.setInAnimation(getActivity(), R.anim.right_enter);
        flip.setOutAnimation(getActivity(), R.anim.left_out);

        flip.setFlipInterval(3000); //Currently flips by time interval, for testing. Will try to add flip by touch later
        flip.startFlipping();

        return homeFragmentView;
    }
}
