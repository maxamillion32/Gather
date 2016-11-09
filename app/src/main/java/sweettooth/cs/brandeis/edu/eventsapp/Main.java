package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

public class Main extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //starts tester for database functions. comment out to ignore
        //startActivity(new Intent("sweettooth.cs.brandeis.edu.eventsapp.DatabaseTester"));

        //Sign In that doesn't really do anything and probably half broken, but now we have button for signin yay
        Intent intent = new Intent(Main.this, GoogleAuth.class);
        startActivity(intent);

        //bottom navigation bar
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.home_item:
                        Snackbar.make(coordinatorLayout, "Home Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.explore_item:
                        Snackbar.make(coordinatorLayout, "Explore Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.settings_item:
                        Snackbar.make(coordinatorLayout, "Settings Selected", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
}
