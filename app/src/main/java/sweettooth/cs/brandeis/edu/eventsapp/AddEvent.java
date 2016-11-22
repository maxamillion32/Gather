package sweettooth.cs.brandeis.edu.eventsapp;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private String title;
    private String description;
    private String category;
    private DateTime dateTime;
    private Spinner categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // populate categories spinner list
        categories = (Spinner) findViewById(R.id.event_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: verify input
                EditText etTitle = (EditText) findViewById(R.id.event_title);
                title = etTitle.getText().toString();
                EditText etDescription = (EditText) findViewById(R.id.event_description);
                description = etDescription.getText().toString();
                category = categories.getSelectedItem().toString();
                Event newEvent = new Event(category,0,null,description,title);
                // ADD EVENT TO DATABASE HERE
                NavUtils.navigateUpFromSameTask(AddEvent.this);
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
