package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //starts tester for database functions. comment out to ignore
        startActivity(new Intent("sweettooth.cs.brandeis.edu.eventsapp.DatabaseTester"));
    }
}
