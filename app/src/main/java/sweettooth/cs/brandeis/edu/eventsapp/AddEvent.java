package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddEvent extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String title;
    private String description;
    private String category;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
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

        // save and return to Home
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
                dateTime = new DateTime(year,month,day,hour,minutes);
                Event newEvent = new Event(category,0,dateTime,description,title);
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

    // date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int y, int m, int d) {
        year = y;
        month = m;
        day = d;
        TextView showDate = (TextView) findViewById(R.id.show_date);
        showDate.setText(""+month+"/"+day+"/"+year);
    }

    // time picker
    public void showTimePickerDialog(View v) {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    public void onTimeSet(TimePicker view, int h, int m) {
        hour = h;
        minutes = m;
        TextView showTime = (TextView) findViewById(R.id.show_time);
        String time;
        if (hour <= 12) {
            time = hour + ":" + minutes + " AM";
        } else {
            time = (hour-12) + ":" + minutes + " PM";
        }
        showTime.setText(time);
    }
}
