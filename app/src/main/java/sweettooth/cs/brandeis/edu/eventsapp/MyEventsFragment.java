package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * MyEventsFragment--shows a clickable listview of events
 * user has indicated interest in*/

public class MyEventsFragment extends Fragment {

    private static final String logTag = "MyEventsFragment";
    protected ListView myEventsListView;
    protected View myEventsFragmentView;
    //object for iterating through database and populating listview
    private DatabaseUtility databaseUtil;

    //default contructor--important for fragments
    public MyEventsFragment() {
    }

    public static MyEventsFragment newInstance(String text) {
        return new MyEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(logTag, "In onCreate()");

        myEventsFragmentView = inflater.inflate(R.layout.fragment_my_events, container, false);

        myEventsListView = (ListView) myEventsFragmentView.findViewById(R.id.my_events_ListView);

        //if user logged in, populate listview using DatabaseUtility
        String userID;
        if ((userID = SettingsFragment.userID) != null) {
            databaseUtil = new DatabaseUtility(logTag, userID, myEventsFragmentView, myEventsListView,
                    this, new MyEventsHomeTrackerAdapter());
            databaseUtil.accessUserEvents(DatabaseUtility.Tab.MYEVENTS);
        }
        return myEventsFragmentView;
    }
}
