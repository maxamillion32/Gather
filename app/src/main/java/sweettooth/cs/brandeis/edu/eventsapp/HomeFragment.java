package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Home Fragment--displays gridview of events user is interested in (events
 * not with full detail) and an viewflipper of events to discover new events
 */

public class HomeFragment extends Fragment {

    private static final String logTag = "HomeFragment";

    //references to access the Firebase database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();
    //reference to node of database representing user's events interested in
    protected DatabaseReference usersEventsRef;

    //displays events user has indicated interest in
    protected GridView myEventsGridView;
    //adapter for populating gridview
    protected final static MyEventsHomeTrackerAdapter adapter = new MyEventsHomeTrackerAdapter();
    //object for iterating through databases entries and populating gridview
    private DatabaseUtility databaseUtil;

    View homeFragmentView;
    private static String userID;

    //list of events used for viewflipper
    private List<Event> events = new ArrayList<>();
    private ViewFlipper viewFlipper;
    private float lastX;

    //no-argument constructor--important for classes that extend Fragment
    public HomeFragment() {
    }

    //newInstance method
    public static HomeFragment newInstance(String text) {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(logTag, "In onCreate()");
        super.onCreateView(inflater, container, savedInstanceState);

        homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        //updates reference to database automatically when database is modified
        databaseRef.keepSynced(true);

        myEventsGridView = (GridView) homeFragmentView.findViewById(R.id.home_gridview);

        if ((userID = SettingsFragment.userID) != null) {
            //safe to populate gridview using DatabaseUtility custom class
            databaseUtil = new DatabaseUtility(logTag, userID, homeFragmentView, myEventsGridView, this, adapter);
            databaseUtil.accessUserEvents(DatabaseUtility.Tab.HOME);
        }

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

        final List<ImageButton> buttons = new ArrayList<>();
        buttons.add((ImageButton) homeFragmentView.findViewById(R.id.button1));
        buttons.add((ImageButton) homeFragmentView.findViewById(R.id.button2));
        buttons.add((ImageButton) homeFragmentView.findViewById(R.id.button3));
        buttons.add((ImageButton) homeFragmentView.findViewById(R.id.button4));
        buttons.add((ImageButton) homeFragmentView.findViewById(R.id.button5));


        for(int j = 0; j < 5; j++){

            buttons.get(j).setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    // pull up event activity
                    Intent intent = new Intent("sweettooth.cs.brandeis.edu.eventsapp.CompleteEvent");
                    Bundle bundle = new Bundle();

                    switch(view.getId()) {
                        case R.id.button1:
                            bundle.putSerializable("KEY", events.get(0));
                            break;
                        case R.id.button2:
                            bundle.putSerializable("KEY", events.get(1));
                            break;
                        case R.id.button3:
                            bundle.putSerializable("KEY", events.get(2));
                            break;
                        case R.id.button4:
                            bundle.putSerializable("KEY", events.get(3));
                            break;
                        case R.id.button5:
                            bundle.putSerializable("KEY", events.get(4));
                            break;
                        default:
                            throw new RuntimeException("Unknow button ID");

                    }
                    //
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }


        //Pull top events from database
        new AsyncTask<Void, Void, Boolean>() {

            protected Boolean doInBackground(Void... params) {
                DatabaseReference topEventsRef = databaseRef.child("Events");


                ValueEventListener userEventsListener = new ValueEventListener() {
                    //called when attached to reference and when node or its children is modified
                    //dataSnapshot is an object representing database contents

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                            List<Event> allevents = new ArrayList<Event>();

                            for (DataSnapshot child : childSnapshots) {

                                Event event = child.getValue(Event.class);
                                if(allevents.size() == 0) {
                                    allevents.add(event);
                                }
                                else {
                                    boolean inserted = false;
                                    for(int i = 0; i < allevents.size(); i++){
                                        if(event.getChecks() > allevents.get(i).getChecks()){
                                            allevents.add(i, event);
                                            inserted = true;
                                            break;
                                        }
                                    }
                                    if(!inserted){
                                        allevents.add(event);
                                    }

                                }

                            }
                            events = new ArrayList<Event>();
                            for(int i = 0; i < 5; i++){

                                Event event = allevents.get(i);


                                events.add(event);
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

        viewFlipper.setInAnimation(getActivity(), R.anim.right_in);
        viewFlipper.setOutAnimation(getActivity(), R.anim.left_out);

        viewFlipper.setFlipInterval(8000);
        viewFlipper.startFlipping();

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float currentX = event.getX();

                        // Handling left to right screen swap.
                        if (lastX > currentX) {



                            // Next screen comes in from right.
                            viewFlipper.setInAnimation(getActivity(), R.anim.right_in);
                            // Current screen goes out from left.
                            viewFlipper.setOutAnimation(getActivity(), R.anim.left_out);

                            // Display next screen.
                            viewFlipper.showNext();
                        }

                        // Handling right to left screen swap.
                        if (lastX < currentX) {





                            // Next screen comes in from left.
                            viewFlipper.setInAnimation(getActivity(), R.anim.left_in);
                            // Current screen goes out from right.
                            viewFlipper.setOutAnimation(getActivity(), R.anim.right_out);

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