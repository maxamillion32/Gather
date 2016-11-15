package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

/**
 * Explore Fragment
 */

public class ExploreFragment extends Fragment {

    public ExploreFragment() {
    }

    public static ExploreFragment newInstance(String text) {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View exploreFragmentView = inflater.inflate(R.layout.fragment_explore, container, false);

        //added button (not ideal) because our current BottomBar works off fragments and Explorer is an Activity
        //thus when Explorer activity is created we lose our BottomBar when in the activity
        Button cal = (Button) exploreFragmentView.findViewById(R.id.goToExplorer);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Explorer.class);
                startActivity(intent);
            }
        });

        return exploreFragmentView;
    }
}
