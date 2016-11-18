package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * Home Fragment
 */

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String text) {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

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
