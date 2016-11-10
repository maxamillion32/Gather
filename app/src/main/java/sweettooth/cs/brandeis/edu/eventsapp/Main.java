package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnTabSelectedListener;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //starts tester for database functions. comment out to ignore
        //startActivity(new Intent("sweettooth.cs.brandeis.edu.eventsapp.DatabaseTester"));

        /* Temporarily commented out-- ask Christine for clarification
        //Sign In that doesn't really do anything and probably half broken, but now we have button for signin yay
        Intent intent = new Intent(Main.this, GoogleAuth.class);
        startActivity(intent);
        */

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
