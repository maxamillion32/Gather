package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

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
    private ViewFlipper viewFlipper;
    private float lastY;


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
                // pull up event activity
                Intent intent = new Intent("sweettooth.cs.brandeis.edu.eventsapp.CompleteEvent");
                Bundle bundle = new Bundle();
                bundle.putSerializable("KEY",event);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });








        final DatabaseUtility dbUtil = new DatabaseUtility();


        final List<TextView> texts = new ArrayList<>();
        texts.add((TextView) homeFragmentView.findViewById(R.id.event1));
        texts.add((TextView) homeFragmentView.findViewById(R.id.event2));
        texts.add((TextView) homeFragmentView.findViewById(R.id.event3));
        texts.add((TextView) homeFragmentView.findViewById(R.id.event4));
        texts.add((TextView) homeFragmentView.findViewById(R.id.event5));

        final List<ImageView> images = new ArrayList<>();
        images.add((ImageView) homeFragmentView.findViewById(R.id.image1));
        images.add((ImageView) homeFragmentView.findViewById(R.id.image2));
        images.add((ImageView) homeFragmentView.findViewById(R.id.image3));
        images.add((ImageView) homeFragmentView.findViewById(R.id.image4));
        images.add((ImageView) homeFragmentView.findViewById(R.id.image5));



        //Pull top events from database
        new AsyncTask<Void, Void, Boolean>() {

            protected Boolean doInBackground(Void... params) {
                DatabaseReference topEventsRef = databaseRef.child("Events");

                topEventsRef.orderByChild("checks");

                ValueEventListener userEventsListener = new ValueEventListener() {
                    //called when attached to reference and when node or its children is modified
                    //dataSnapshot is an object representing database contents

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();

                            int i = 0;
                            for (DataSnapshot child : childSnapshots) {

                                Event event = child.getValue(Event.class);
                                texts.get(i).setText(event.getTitle() + "\n" + event.getDescription() + "\n" + event.getDateTime().formatSimpleDate());
                                if(event.getCategory().equals("Business")){
                                    images.get(i).setImageResource(R.drawable.discoverbuisness);
                                }
                                if(event.getCategory().equals("Community")){
                                    images.get(i).setImageResource(R.drawable.discovercommunity);
                                }
                                if(event.getCategory().equals("Music")){
                                    images.get(i).setImageResource(R.drawable.discovermusic);
                                }
                                if(event.getCategory().equals("Other")){
                                    images.get(i).setImageResource(R.drawable.discoverother);
                                }
                                if(event.getCategory().equals("Sports")){
                                    images.get(i).setImageResource(R.drawable.discoversport);
                                }
                                i++;
                                if(i == 5){
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //error w/ onDataChange
                        Log.d("DummyTag", "getUsersEventIDs:onCancelled", databaseError.toException());
                    }
                };
                //attaches listener to reference
                topEventsRef.addValueEventListener(userEventsListener);
                return null;
            }
        }.execute();




        viewFlipper = (ViewFlipper) homeFragmentView.findViewById(R.id.discoverFlip);

        //viewFlipper.setInAnimation(getActivity(), R.anim.right_in);
        //viewFlipper.setOutAnimation(getActivity(), R.anim.left_out);

        //viewFlipper.setFlipInterval(4000);
        //viewFlipper.startFlipping();

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float currentY = event.getY();

                        // Handling left to right screen swap.
                        if (lastY > currentY) {

                            // If there aren't any other children, just break.
                            if (viewFlipper.getDisplayedChild() == 0)
                                break;

                            // Next screen comes in from right.
                            viewFlipper.setInAnimation(getContext(), R.anim.right_in);
                            // Current screen goes out from left.
                            viewFlipper.setOutAnimation(getContext(), R.anim.left_out);

                            // Display next screen.

                            viewFlipper.showNext();



                        }

                        // Handling right to left screen swap.
                        if (lastY < currentY) {

                            // If there is a child (to the left), kust break.
                            if (viewFlipper.getDisplayedChild() == 1)
                                break;

                            // Next screen comes in from left.
                            viewFlipper.setInAnimation(getContext(), R.anim.left_in);
                            // Current screen goes out from right.
                            viewFlipper.setOutAnimation(getContext(), R.anim.right_out);



                            // Display previous screen.

                            viewFlipper.showPrevious();

                        }
                        break;
                }




                return true;
            }
        });

        return homeFragmentView;

    }




}