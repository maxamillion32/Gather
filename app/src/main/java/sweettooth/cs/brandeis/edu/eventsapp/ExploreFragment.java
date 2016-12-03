package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import android.app.Activity;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Explore Fragment
 */

public class ExploreFragment extends Fragment {

    public ExploreFragment() {
    }

    public static ExploreFragment newInstance(String text) {
        return new ExploreFragment();
    }

    //for fragments
    private CaldroidFragment caldroidFragment;
    private FragmentActivity fragAct;
    //for displaying dialog list of events on date clicked in explorer
    private static HashMap<String,String> mapOfEvents;

    @Override
    public void onAttach(Activity activity) {
        fragAct = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View exploreFragmentView = inflater.inflate(R.layout.fragment_explore, container, false);
            //date shown for toasts
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            // caldroid fragment
            caldroidFragment = new CaldroidFragment();
            if (savedInstanceState != null) {
                caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
            } else {
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH)+1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
                args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
                caldroidFragment.setArguments(args);
            }
            FragmentManager fragManager = fragAct.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction trans = fragManager.beginTransaction();
            trans.replace(R.id.calFrag, caldroidFragment);
            trans.commit();

            // create listener for selecting dates and changing months
            final CaldroidListener listener = new CaldroidListener() {
                @Override
                public void onSelectDate(final Date date, View view) {
                    //date toast
                    //Toast.makeText(getActivity().getApplicationContext(), dateFormat.format(date), Toast.LENGTH_SHORT).show();
                    //map for storing each event as <event key, event title>
                    mapOfEvents = new HashMap<>();
                    //clicked date
                    final String dateClickFormatted = new SimpleDateFormat("d M yyyy").format(date);
                    //date displayed in dialog
                    final String dateDialog = new SimpleDateFormat("MM/dd/yyyy").format(date);
                    //get events from db
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
                    Query query = ref;
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check each event to see if it matches the date clicked
                            Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();
                            for (DataSnapshot s : snaps) {
                                //event key to store in map
                                String key = s.getKey();
                                //to get event data
                                DataSnapshot childSnapshot = dataSnapshot.child(key);
                                //event object
                                Event event = childSnapshot.getValue(Event.class);

                                /*
                                //test prints
                                System.out.println(event.category);
                                System.out.println(event.title);
                                System.out.println("Event Date: " + event.getDateTime().formatCalendarDateForMatching());
                                System.out.println(dateClickFormatted);
                                System.out.println(event.checks);
                                */

                                //check if date clicked matches date in db
                                if (event.getDateTime().formatCalendarDateForMatching().equals(dateClickFormatted)) {
                                    //add event to hash map
                                    mapOfEvents.put(key, event.title + ": " + event.checks + " interested");
                                }
                            }
                            //for event list
                            ListView listOfEvents = new ListView(fragAct);
                            final ArrayAdapter arrayAdapter;
                            //only show list of events if the date clicked has events in db
                            if (mapOfEvents.size() == 0) {
                                //no events on date
                                String[] noEvents = new String[1];
                                noEvents[0] = "No events on " + dateDialog;
                                arrayAdapter = new ArrayAdapter<>(fragAct, R.layout.daily_event_list, R.id.listTxtView, noEvents);
                            } else {
                                //list title of each event
                                Object[] titles = mapOfEvents.values().toArray();
                                String[] arrayOfTitles = Arrays.asList(titles).toArray(new String[mapOfEvents.size()]);
                                arrayAdapter = new ArrayAdapter<>(fragAct, R.layout.daily_event_list, R.id.listTxtView, arrayOfTitles);
                            }
                            listOfEvents.setAdapter(arrayAdapter);
                            //display dialog list of events
                            Dialog dialog = new Dialog(fragAct);
                            dialog.setContentView(listOfEvents);
                            dialog.show();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                @Override
                public void onChangeMonth(int month, int year) {
                    //month toast
                    Toast.makeText(getActivity().getApplicationContext(),month+"/"+year, Toast.LENGTH_SHORT).show();
                }
            };
            // set listener
            caldroidFragment.setCaldroidListener(listener);
            //return view
            return exploreFragmentView;
        }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
}
