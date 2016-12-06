package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
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
import android.content.Intent;
import android.widget.AdapterView;
import android.graphics.drawable.ColorDrawable;

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
    //for collecting key in firebase mapped to event object
    private static HashMap<String,Event> mapOfEvents;
    //for coloring calendar
    private static HashMap<String,Event> mapForColoring;
    //for preferences
    private static HashMap<String,String> mapForPref;
    //user preferences
    private static ArrayList<String> listOfTruePref;
    //for displaying list of events on date click
    private static ArrayList<String> eventList;
    //number of days in month
    private int daysInCurrentMonth;
    //month and year when fragment inflated
    private int inflatedMonth;
    private int inflatedYear;
    //for avoiding dialog when changing interest data in CompleteEvent activity and returning to calendar
    private boolean showDialog;

    @Override
    public void onAttach(Activity activity) {
        fragAct = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        //fixes edge case where data changes in another fragment
        showDialog = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //needed to override onCreate so that data from firebase populates into hash map before onCreateView for coloring purposes
        super.onCreate(savedInstanceState);
        //collect events for coloring calendar
        mapForColoring = new HashMap<>();
        //preferences to dates
        mapForPref = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        Query query = ref;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ExploreFragment", "In onDataChange() in explore fragment getting dates");
                //put each event into hash map for coloring
                Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();
                for (DataSnapshot s : snaps) {
                    //event key to store in map
                    String key = s.getKey();
                    //to get event data
                    DataSnapshot childSnapshot = dataSnapshot.child(key);
                    //event object
                    Event event = childSnapshot.getValue(Event.class);
                    //add event to hash map
                    mapForColoring.put(key, event);
                    mapForPref.put(key,event.dateTime.formatCalendarDateForMatching());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error
                Log.d("ExploreFragment", "In onCancelled()", databaseError.toException());
            }
        });
        //collect the users preferences
        listOfTruePref = new ArrayList();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("UserToCategories").child(SettingsFragment.userID);
        Query query2 = ref2;
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ExploreFragment", "In onDataChange() in explore fragment getting user preferences");
                //add user preferences to list
                Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();
                for (DataSnapshot s : snaps) {
                    //all preferences in firebase db are true
                    listOfTruePref.add(s.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error
                Log.d("ExploreFragment", "In onCancelled()", databaseError.toException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
            //month when inflated
            this.inflatedMonth = cal.get(Calendar.MONTH) + 1;
            //year when inflated
            this.inflatedYear = cal.get(Calendar.YEAR);
            //get number of days in month when inflating the explore fragment
            this.daysInCurrentMonth = getMaxDaysInMonth(inflatedMonth, inflatedYear);
        }
        //avoid inadvertently showing dialog list
        showDialog = false;
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
                //only show dialog if a date was clicked
                showDialog = true;
                //map for storing each event as <firebase key string, event object>
                mapOfEvents = new HashMap<>();
                //clicked date
                final String dateClickFormatted = new SimpleDateFormat("d M yyyy").format(date);
                //date displayed in dialog
                final String dateDialog = new SimpleDateFormat("MM/dd/yyyy").format(date);
                //get events from firebase again to make sure most recently updated data
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
                Query query = ref;
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("ExploreFragment", "In onDataChange() in explore fragment after onClick");
                        //check each event to see if it matches the date clicked
                        Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();
                        for (DataSnapshot s : snaps) {
                            //event key to store in map
                            String key = s.getKey();
                            //to get event data
                            DataSnapshot childSnapshot = dataSnapshot.child(key);
                            //event object
                            Event event = childSnapshot.getValue(Event.class);
                            //check if date clicked matches date in db
                            if (event.getDateTime().formatCalendarDateForMatching().equals(dateClickFormatted)) {
                                //check if users preferences allow to be shown
                                if (listOfTruePref.contains(event.getCategory())) {
                                    //add event to hash map
                                    mapOfEvents.put(key, event);
                                }
                            }
                            //keep colors updated
                            mapForColoring.put(key, event);
                        }
                        //check if dialog should show
                        if (showDialog) {
                            //becomes true again only when intent returned from completeEvent activity
                            showDialog = false;
                            //for event list
                            ListView listOfEvents = new ListView(fragAct);
                            final ArrayAdapter arrayAdapter;
                            //only show list of events if the date clicked has events in db
                            if (mapOfEvents.size() == 0) {
                                //no events on date or none meeting user preferences
                                String[] noEvents = new String[1];
                                //doesn't meet preferences but there are events
                                if (mapForPref.containsValue(dateClickFormatted)){
                                    noEvents[0] = "Change preferences to see event(s)";
                                //no events exist on date
                                } else {
                                    noEvents[0] = "No events on " + dateDialog;
                                }
                                arrayAdapter = new ArrayAdapter<>(fragAct, R.layout.daily_event_list, R.id.listTxtView, noEvents);
                            } else {
                                eventList = new ArrayList<>();
                                Set set = mapOfEvents.entrySet();
                                Iterator i = set.iterator();
                                while (i.hasNext()) {
                                    Map.Entry entry = (Map.Entry) i.next();
                                    Event event = (Event) entry.getValue();
                                    eventList.add(event.checks + " interested: " + event.title);
                                }
                                //sort event list by most popular
                                Collections.sort(eventList, Collections.<String>reverseOrder());
                                //put date first in array
                                String[] eventListArray = new String[eventList.size()+1];
                                eventListArray[0] = "Events on " + dateDialog;
                                //add each event title to array starting at index 1
                                for (int j = 0; j < eventList.size(); j++) {
                                    eventListArray[j+1] = eventList.get(j);
                                }
                                arrayAdapter = new ArrayAdapter<>(fragAct, R.layout.daily_event_list, R.id.listTxtView, eventListArray);
                            }
                            listOfEvents.setAdapter(arrayAdapter);
                            //display dialog list of events
                            final Dialog dialog = new Dialog(fragAct);
                            dialog.setContentView(listOfEvents);
                            dialog.show();
                            //only start CompleteEvent activity if there is at least one event on the date clicked
                            if (!mapOfEvents.isEmpty()) {
                                listOfEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //text view data clicked as string
                                        String title = (String) arrayAdapter.getItem(position);
                                        //check if user clicked the Events on Date item
                                        if (title.contains("Events on " + dateDialog)) {
                                            //do nothing then because it is not an event
                                        } else {
                                            //only extract title from text view
                                            String[] toGetTitle = title.split("interested: ");
                                            String titleFromTextView = toGetTitle[1];
                                            //pull up event activity
                                            Intent intent = new Intent("sweettooth.cs.brandeis.edu.eventsapp.CompleteEvent");
                                            Bundle bundle = new Bundle();
                                            //find the event that matches the text view clicked
                                            Set set = mapOfEvents.entrySet();
                                            Iterator i = set.iterator();
                                            while (i.hasNext()) {
                                                Map.Entry entry = (Map.Entry) i.next();
                                                Event event = (Event) entry.getValue();
                                                //check if event clicked in the dialog list is the same as the event in the database
                                                if (event.title.equals(titleFromTextView) && event.dateTime.formatCalendarDateForMatching().equals(dateClickFormatted)) {
                                                    //dismiss dialog to avoid WindowsLeaked exception before starting activity
                                                    dialog.dismiss();
                                                    //start CompleteEvent activity and return boolean for showDialog
                                                    bundle.putSerializable("KEY", event);
                                                    intent.putExtras(bundle);
                                                    startActivityForResult(intent,1);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //error
                        Log.d("ExploreFragment","In onCancelled()", databaseError.toException());
                    }
                });
            }
            @Override
            public void onChangeMonth(int month, int year) {
                //month toast
                //Toast.makeText(getActivity().getApplicationContext(),month+"/"+year, Toast.LENGTH_SHORT).show();
                //get number of days in month
                daysInCurrentMonth = getMaxDaysInMonth(month,year);
                //for calendar cells with events and preference
                ColorDrawable lightBlue = new ColorDrawable(getResources().getColor(R.color.caldroid_sky_blue));
                ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
                ColorDrawable darkBlue = new ColorDrawable(getResources().getColor(R.color.caldroid_holo_blue_dark));
                ColorDrawable fiveFiveFive = new ColorDrawable(getResources().getColor(R.color.caldroid_555));
                ColorDrawable primary = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
                //new calendar instance
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,month-1);
                cal.set(Calendar.YEAR,year);
                //build list of events with Date,Category,Checks
                Set colorSet = mapForColoring.entrySet();
                ArrayList<String> colorMe = new ArrayList<>();
                Iterator itr = colorSet.iterator();
                while (itr.hasNext()) {
                    Map.Entry entry = (Map.Entry) itr.next();
                    Event event = (Event) entry.getValue();
                    colorMe.add(event.dateTime.getDay() + " " + event.dateTime.getMonth() + " " + event.dateTime.getYear());
                    colorMe.add(event.getCategory());
                    colorMe.add(String.valueOf(event.getChecks()));
                }
                //each day
                for (int i = 1; i <= daysInCurrentMonth; i++) {
                    //check each day
                    cal.set(Calendar.DAY_OF_MONTH,i);
                    //comparing popularity
                    String popularCat = "";
                    int popularity = 0;
                    int counter = 0;
                    //check if there is an event on the date
                    if (colorMe.contains(String.valueOf(i) + " " + month + " " + year)) {
                        //go through list of events and find most popular category
                        for (int x = 0; x < colorMe.size(); x++) {
                            //same date?
                            if (colorMe.get(x).equals(String.valueOf(i) + " " + month + " " + year)) {
                                //first entry on this date is most popular until a more popular one is found
                                if (counter == 0) {
                                    //event category
                                    popularCat = colorMe.get(x + 1);
                                    //event checks
                                    popularity = Integer.parseInt(colorMe.get(x + 2));
                                    counter++;
                                } else {
                                    //check if more popular
                                    if (Integer.parseInt(colorMe.get(x + 2)) > popularity) {
                                        //it's more popular!!
                                        popularCat = colorMe.get(x + 1);
                                        popularity = Integer.parseInt(colorMe.get(x + 2));
                                    }
                                }
                            }
                        }
                        //color event category most popular for that day
                        if (popularCat.equals("Sports")) {
                            //color day blue since there is an event
                            caldroidFragment.setBackgroundDrawableForDate(lightBlue, cal.getTime());
                        } else if (popularCat.equals("Business")) {
                            //color day blue since there is an event
                            caldroidFragment.setBackgroundDrawableForDate(red, cal.getTime());
                        } else if (popularCat.equals("Other")) {
                            //color day blue since there is an event
                            caldroidFragment.setBackgroundDrawableForDate(darkBlue, cal.getTime());
                        } else if (popularCat.equals("Music")) {
                            //color day blue since there is an event
                            caldroidFragment.setBackgroundDrawableForDate(fiveFiveFive, cal.getTime());
                        } else if (popularCat.equals("Community")) {
                            //color day blue since there is an event
                            caldroidFragment.setBackgroundDrawableForDate(primary, cal.getTime());
                        }
                    }
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //true showDialog boolean will now show dialog after event interest changed
                //change in event interest will fire onDataChange() and then dialog can once again be shown
                showDialog = data.getBooleanExtra("showDialog", true);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("ExploreFragment", "onActivityResult - RESULT_CANCELLED");
            }
        }
    }

    //return number of days in month
    public static int getMaxDaysInMonth(int month, int year) {
        //dummy value for days
        int days = 0;
        //get number of days in the month
        switch (month) {
            case 1: days = 31;
                break;
            case 2:
                //account for leap years
                if (year % 4 == 0) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
            case 3: days = 31;
                break;
            case 4: days = 30;
                break;
            case 5: days = 31;
                break;
            case 6: days = 30;
                break;
            case 7: days = 31;
                break;
            case 8: days = 31;
                break;
            case 9: days = 30;
                break;
            case 10: days = 31;
                break;
            case 11: days = 30;
                break;
            case 12: days = 31;
                break;
        }
        return days;
    }
}
