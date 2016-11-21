package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import android.app.Activity;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.util.Log;

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
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getActivity().getApplicationContext(), dateFormat.format(date), Toast.LENGTH_SHORT).show();

                //testing dialog display with hard-coded array
                //TODO: build query for getting each date's info from db and adding it to list view
                String dateTest = dateFormat.format(date);
                Log.d("DATE", dateTest);
                String arrayTest[];
                if (dateTest.contains("24") && dateTest.contains("Nov")) {
                    arrayTest = new String[] {"Thanksgiving Event 1", "Thanksgiving Event 2", "Thanksgiving Event 3"};
                } else {
                    arrayTest = new String[] {"Not Thanksgiving Event 1", "Not Thanksgiving Event 2", "Not Thanksgiving Event 3"};
                }
                ListView lstView = new ListView(fragAct);
                lstView.setAdapter(new ArrayAdapter<>(fragAct, R.layout.test_event_list, R.id.listTxtView, arrayTest));
                Dialog dialog = new Dialog(fragAct);
                dialog.setContentView(lstView);
                dialog.show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
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
