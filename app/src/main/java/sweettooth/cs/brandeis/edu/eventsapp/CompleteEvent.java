package sweettooth.cs.brandeis.edu.eventsapp;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CompleteEvent extends AppCompatActivity {

    private Event event;
    private TextView title;
    private TextView description;
    private TextView datetime;
    private TextView category;
    private TextView location;
    private TextView checks;
    private Button interested;
    private final String yesinterested = "I'm interested!";
    private final String notinterested = "I'm no longer interested.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        event = (Event)getIntent().getSerializableExtra("KEY");

        title = (TextView)findViewById(R.id.title);
        description = (TextView)findViewById(R.id.description);
        datetime = (TextView)findViewById(R.id.datetime);
        category = (TextView)findViewById(R.id.category);
        location = (TextView)findViewById(R.id.location);
        checks = (TextView)findViewById(R.id.checks);
        interested = (Button)findViewById(R.id.interested);

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        datetime.setText(event.getDateTime().formatSimpleDate());
        category.setText(event.getCategory());
        location.setText(event.getLocation());
        String interest = event.getChecks()+" users are interested in this event!";
        checks.setText(interest);
        // put in an if check to see if user is interested in this event
        // if not, set text to notinterested
        interested.setText(yesinterested);
    }

    @Override
    public void onStart() {
        super.onStart();
        interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add event to user
                if (interested.getText().equals(yesinterested)) {
                    databaseRef.child("UserToEvents").child(userID).child(eventID).setValue(true);
                    // increase number of checks
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("checks", event.getChecks()+1);
                    databaseRef.child("Events").child(eventID).updateChildren(childUpdates);
                } else {
                    // delete event from user
                    databaseRef.child("UserToEvents").child(userID).child(eventID).removeValue();
                    // decrease number of checks
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("checks", event.getChecks()-1);
                    databaseRef.child("Events").child(eventID).updateChildren(childUpdates);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}