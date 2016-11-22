package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //does basic test of database functionality--perhaps uncomment when we turn in on Tues
        DatabaseUtility dbUtil = new DatabaseUtility();
        //dbUtil.test();



        //starts activity to test database functionality--nothing implemented right now
        //startActivity(new Intent("sweettooth.cs.brandeis.edu.eventsapp.DatabaseTester"));

        //bottom navigation bar
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setFragmentItems(getFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(HomeFragment.newInstance("Content for home."), R.drawable.ic_home, "HOME"),
                new BottomBarFragment(MyEventsFragment.newInstance("Content for my events."), R.drawable.ic_my_events, "MY EVENTS"),
                new BottomBarFragment(ExploreFragment.newInstance("Content for explore."), R.drawable.ic_explore, "EXPLORE"),
                new BottomBarFragment(SettingsFragment.newInstance("Content for settings."), R.drawable.ic_settings, "SETTINGS")
        );

        // Setting colors for different tabs when there's more than three of them.
        bottomBar.mapColorForTab(0, "#3B494C");
        bottomBar.mapColorForTab(1, "#00796B");
        bottomBar.mapColorForTab(2, "#7B1FA2");
        bottomBar.mapColorForTab(3, "#FF5252");
    }
}
