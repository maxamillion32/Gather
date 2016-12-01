package sweettooth.cs.brandeis.edu.eventsapp;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        // determine if event is listed under the user or not


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