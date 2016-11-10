package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * MyEvents Fragment
 */

public class MyEventsFragment extends Fragment {

    public MyEventsFragment() {
    }

    public static MyEventsFragment newInstance(String text) {
        return new MyEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View myEventsFragmentView = inflater.inflate(R.layout.fragment_my_events, container, false);

        TextView hello = (TextView) myEventsFragmentView.findViewById(R.id.helloevents);

        return myEventsFragmentView;
    }
}
