package sweettooth.cs.brandeis.edu.eventsapp;

/**
 * Created by Tyler on 11/13/16.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class Explorer extends AppCompatActivity {
    //fragments
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        // caldroid fragment
        caldroidFragment = new CaldroidFragment();
        // use custom fragment by uncommenting below and commenting above line
        // caldroidFragment = new CaldroidSampleCustomFragment();
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH)+1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            // use compact mode by uncommenting below
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
            // use dark theme by uncommenting below
            // args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
            caldroidFragment.setArguments(args);
        }
        // attach fragment to activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fragToActivity, caldroidFragment);
        t.commit();

        // create listener
        final CaldroidListener listener = new CaldroidListener() {
            // NEED TO ADD: functionality when clicking date to see events for the date in list form
            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), dateFormat.format(date), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChangeMonth(int month, int year) {
                Toast.makeText(getApplicationContext(),"month: " + month + " year: " + year, Toast.LENGTH_SHORT).show();
            }
        };
        // set listener
        caldroidFragment.setCaldroidListener(listener);

        // dialog button
        // NEED TO CHANGE: what is displayed when clicking dialog
        Button dialogButton = (Button) findViewById(R.id.dialog);
        final Bundle instanceState = savedInstanceState;

        // create and set button listener
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog setup
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(listener);

                if (instanceState != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getSupportFragmentManager(), instanceState, "DIALOG_CALDROID_SAVED_STATE", "CALDROID_DIALOG_FRAGMENT");
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    dialogCaldroidFragment.setArguments(bundle);
                }
                dialogCaldroidFragment.show(getSupportFragmentManager(), "CALDROID_DIALOG_FRAGMENT");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState, "DIALOG_CALDROID_SAVED_STATE");
        }
    }
}
