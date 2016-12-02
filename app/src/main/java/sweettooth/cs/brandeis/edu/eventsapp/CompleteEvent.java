package sweettooth.cs.brandeis.edu.eventsapp;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CompleteEvent extends AppCompatActivity {

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();
    private static final String yesinterested = "I'm interested!";
    private static final String notinterested = "I'm no longer interested.";

    private Event event;
    private TextView title;
    private TextView description;
    private TextView datetime;
    private TextView category;
    private TextView location;
    private TextView checks;
    private Button interested;
    private String eventID;
    private String userID = "WYAaQnXSh0dnVohaz2jVH1PTNcC2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        event = (Event)getIntent().getSerializableExtra("KEY");
        Log.wtf("CTK","create thing");

        // find event ID for this event
        DatabaseReference  allEventsRef = databaseRef.child("Events");
        ValueEventListener allEventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : childSnapshots) {
                        Event dbEvent = child.getValue(Event.class);
                        String dbTitle =  dbEvent.getTitle();
                        if (dbTitle.equals(event.getTitle())){
                            eventID = child.getKey();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error
                Log.d("DummyTag", "getUsersEventIDs:onCancelled", databaseError.toException());
            }
        };
        allEventsRef.addValueEventListener(allEventsListener);

        // determine if user is subscribed to this event
        DatabaseReference  userEventsRef = databaseRef.child("UserToEvents").child(userID);
        ValueEventListener userEventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    interested.setText(yesinterested);
                    for (DataSnapshot child : childSnapshots) {
                        String eID = child.getKey();
                        if (eID.equals(eventID)){
                            interested.setText(notinterested);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error
                Log.d("DummyTag", "getUsersEventIDs:onCancelled", databaseError.toException());
            }
        };
        userEventsRef.addValueEventListener(userEventsListener);

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