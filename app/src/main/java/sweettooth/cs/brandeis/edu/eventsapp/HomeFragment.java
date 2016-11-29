package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Home Fragment
 */

public class HomeFragment extends Fragment {

    //allow access to Firebase database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();
    final static MyEventsHomeTrackerAdapter adapter = new MyEventsHomeTrackerAdapter();
    protected MyEventsFragment myEventsFrag;
    private static FragmentActivity fragAct;

    //alternate user IDs

    //private static final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Chelsi Brandeis
    //private static final String userID = "N4c9T5KP9RTbJk5dwPODdpqTpwC3";
    //myApp
    //private static final String userID = "qJIO0lDbqlNTgUn4zSflkUv51js1";
    //Chelsi Gmail
    //private static final String userID = "xViHGKUkMbdlMa4iRomERmyqjIy1";
    //Chelsi Gmail - cai
    //private static final String userID = "o2JrPnMLYcMoQIB2B55kXzaxdv03";
    //Tyler

    private static final String userID = "WYAaQnXSh0dnVohaz2jVH1PTNcC2";

    private static final String logTag = "HomeFragment";

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String text) {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        fragAct = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        Log.d(logTag, "In onCreate()");
        super.onCreateView(inflater, container, savedInstanceState);
        final View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);


        //updates reference to database automatically when database is modified
        databaseRef.keepSynced(true);

        final GridView myEventsGridView = (GridView) homeFragmentView.findViewById(R.id.home_gridview);

        final MyEventsFragment frag = this.myEventsFrag;

        DatabaseReference usersEventsRef = databaseRef.child("UserToEvents").child(userID);

        //listener for changes to a node in database
        ValueEventListener userEventsListener = new ValueEventListener() {

            //called when attached to reference and when node or its children is modified
            //dataSnapshot is an object representing database contents
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(logTag, "In onDataChange() for user's events");
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
                            Log.d(logTag, "In onDataChange() for all events, within onDataChange() for user's events");
                            //DataSnapshot eventsSnapshot = dataSnapshot.child("Events");
                            for (String eventID : eventIDs) {
                                System.out.println(eventID);
                                DataSnapshot childSnapshot = dataSnapshot.child(eventID);
                                Event event = childSnapshot.getValue(Event.class);
                                System.out.println(event.category);
                                System.out.println("Event Date: " + event.getDateTime().formatSimpleDate());
                                System.out.println("Current Date: " + currentDateTime.formatSimpleDate());
                                if (event.getDateTime().compareTo(currentDateTime) < 0) {
                                    Log.d(logTag, "Adding event to set...");
                                    usersEvents.add(event);
                                }
                                adapter.populateEventsList(usersEvents);
                                myEventsGridView.setAdapter(adapter);
                                Log.d(logTag, "about to set adapter to my events");

                                MyEventsFragment.homeTrackerAdapter = adapter;
                                //frag.setListAdapter(adapter);
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

        //sets click listener for ListView-entry deleted upon click
        myEventsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Event event = (Event) adapter.getItem(position);
                String[] eventDetails = event.getDetailArray();
                ListView listOfEvents = new ListView(fragAct);
                listOfEvents.setAdapter(new ArrayAdapter<String>(fragAct, R.layout.test_event_list, R.id.listTxtView, eventDetails));

                //dialog of events
                Dialog dialog = new Dialog(fragAct);
                dialog.setContentView(listOfEvents);
                dialog.show();
            }
        });

        ViewFlipper flip = (ViewFlipper) homeFragmentView.findViewById(R.id.discoverFlip);

        TextView event = (TextView) homeFragmentView.findViewById(R.id.event1);
        TextView event2 = (TextView) homeFragmentView.findViewById(R.id.event1);

        flip.setInAnimation(getActivity(), R.anim.right_enter);
        flip.setOutAnimation(getActivity(), R.anim.left_out);

        flip.setFlipInterval(3000); //Currently flips by time interval, for testing. Will try to add flip by touch later
        flip.startFlipping();

        return homeFragmentView;
    }
}