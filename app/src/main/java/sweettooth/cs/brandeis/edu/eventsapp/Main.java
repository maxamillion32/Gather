// Names:      William Edgecomb, Chelsi Hu,
//             Tyler Lichten, Christine Kim
// Team Name:  Sweet Tooth
// Course:     COSI 153A
// Assignment: Final Project
/* App:        Gather--crowdsourced calendar
               app that helps users find and
               share local public events*/
// Semester:   Fall 2016


package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;


public class Main extends AppCompatActivity {

    //allows references to bottom bar and fragments through main
    protected static BottomBar bottomBar;
    protected static HomeFragment homeFrag;
    protected static MyEventsFragment myEventsFrag;
    protected static ExploreFragment exploreFrag;
    protected static SettingsFragment settingsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEventsFrag = MyEventsFragment.newInstance("Content for my events.");
        homeFrag = HomeFragment.newInstance("Content for home.");
        exploreFrag = ExploreFragment.newInstance("Content for explore.");
        settingsFrag = SettingsFragment.newInstance("Content for settings.");

        //settings fragment to be location where userID is stored and referenced
        if(SettingsFragment.getUserID() != null){
            SettingsFragment.userID = SettingsFragment.getUserID();
        }

        /*BottomBar is a third-party object that connects fragments via a clickable
          horizontal menu that shows at the bottom of the screen*/
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setFragmentItems(getFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(homeFrag, R.drawable.ic_home, "HOME"),
                new BottomBarFragment(myEventsFrag, R.drawable.ic_my_events, "MY EVENTS"),
                new BottomBarFragment(exploreFrag, R.drawable.ic_explore, "EXPLORE"),
                new BottomBarFragment(settingsFrag, R.drawable.ic_settings, "SETTINGS")
        );

        // Setting colors for different tabs when there's more than three of them.
        bottomBar.mapColorForTab(0, "#61B902");
        bottomBar.mapColorForTab(1, "#00796B");
        bottomBar.mapColorForTab(2, "#7B1FA2");
        bottomBar.mapColorForTab(3, "#FF5252");

        //jump to settings fragment if no one is logged in
        if (SettingsFragment.userID == null) {
            bottomBar.selectTabAtPosition(3, false);
            bottomBar.hide();
        }
    }

    //menu is for directing to activity in which one ca
    //add an event to public calendar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addEvent:
                startActivity(new Intent("sweettooth.cs.brandeis.edu.eventsapp.AddEvent"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
