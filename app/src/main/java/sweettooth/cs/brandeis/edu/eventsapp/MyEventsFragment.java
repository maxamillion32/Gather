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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * MyEvents Fragment
 */

public class MyEventsFragment extends Fragment {

    protected ListView myEventsListView;
    private View myEventsFragmentView;
    protected static MyEventsHomeTrackerAdapter homeTrackerAdapter;
    boolean listSet = false;

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

        myEventsListView = (ListView) myEventsFragmentView.findViewById(R.id.my_events_ListView);
        if (homeTrackerAdapter != null) {
            myEventsListView.setAdapter(homeTrackerAdapter);
        }
        listSet = true;

        myEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) homeTrackerAdapter.getItem(position);
                // pull up event activity
                Intent intent = new Intent("sweettooth.cs.brandeis.edu.eventsapp.CompleteEvent");
                Bundle bundle = new Bundle();
                bundle.putSerializable("KEY",event);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
