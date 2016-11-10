package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView hello = (TextView) homeFragmentView.findViewById(R.id.hellohome);

        return homeFragmentView;
    }
}
