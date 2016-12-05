package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MyEvents Fragment
 */

public class MyEventsFragment extends Fragment {

    protected ListView myEventsListView;
    protected View myEventsFragmentView;
    protected static MyEventsHomeTrackerAdapter homeTrackerAdapter;
    boolean listSet = false;
    protected TextView noEvents;
    protected Button exploreBttn;
    protected boolean viewsReady = false;
    private DatabaseUtility databaseUtil;

    private static final String logTag = "MyEventsFragment";

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

        TextView hello = (TextView) myEventsFragmentView.findViewById(R.id.helloevents);

        noEvents = (TextView) myEventsFragmentView.findViewById(R.id.no_events2);
        exploreBttn = (Button) myEventsFragmentView.findViewById(R.id.button_to_explore2);

        myEventsListView = (ListView) myEventsFragmentView.findViewById(R.id.my_events_ListView);

        FirebaseUser user;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseUtil = new DatabaseUtility(userID, myEventsFragmentView, myEventsListView,
                    this, new MyEventsHomeTrackerAdapter());
            databaseUtil.accessUserEvents(DatabaseUtility.Tab.MYEVENTS);
        }

        return myEventsFragmentView;
    }

    /*protected void setListAdapter(MyEventsHomeTrackerAdapter adapter) {
        if (myEventsFragmentView == null) {
            homeTrackerAdapter = adapter;
        } else {
            myEventsListView = (ListView) myEventsFragmentView.findViewById(R.id.my_events_ListView);
            myEventsListView.setAdapter(adapter);
            listSet = true;
        }
    }*/
}
