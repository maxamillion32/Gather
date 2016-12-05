package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    //protected static String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    protected static BottomBar bottomBar;
    protected static HomeFragment homeFrag;
    protected static MyEventsFragment myEventsFrag;
    protected static ExploreFragment exploreFrag;
    protected static SettingsFragment settingsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("ON CREATE MAIN");

        homeFrag = HomeFragment.newInstance("Content for home.");
        myEventsFrag = MyEventsFragment.newInstance("Content for my events.");
        exploreFrag = ExploreFragment.newInstance("Content for explore.");
        settingsFrag = SettingsFragment.newInstance("Content for settings.");

        //does basic test of database functionality--perhaps uncomment when we turn in on Tues

        //dbUtil.test();

        //starts activity to test database functionality--nothing implemented right now
        //startActivity(new Intent("sweettooth.cs.brandeis.edu.eventsapp.DatabaseTester"));

        //bottom navigation bar

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

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            bottomBar.selectTabAtPosition(3, false);
            bottomBar.hide();
        }


    }

    //@Override
    //public void onResume() {
      //  super.onResume();
        //if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //enteredWithCredentials = false;
            //bottomBar.selectTabAtPosition(3, true);
            //bottomBar.hide();
        //}
    //}

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
