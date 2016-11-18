package sweettooth.cs.brandeis.edu.eventsapp;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* This rudimentaty class tests the functionality of our Firebase Database--performs
*  very basic database operations. It utilizes JSON database from Firebase account.
*  See bottom of class for how JSON file looks*/

public class DatabaseUtility {
    //allow access to Firebase database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();

    /* adds a hardcoded event to events, subscribes a user to an event,
       accesses a user's events, and accesses a category's events*/
    protected void test () {
        //create new event and add to database
        DateTime dt = new DateTime(2017, 3, 8, 14, 0);
        String desc = "Donna The Buffalo playing at Crane Beach";
        Event event = new Event ("Music", dt, "Concert at the Beach", desc);
        String eventID = addEventToDB(event);

        //unique auto-generated user-id, hardcoded
        String chelsiID = "hFmYEO0pCFf3uWSMzDGLnWdBrqg2";
        //subscribes user to event
        subscribeToEvent(chelsiID, eventID);

        //accesses user's subscribed events
        accessUsersEventIDs(chelsiID);

        //accesses a category's events
        accessEventIDsFromCategory("Music");

    }



    //adds event to database and returns event ID
    protected String addEventToDB (Event event) {
        //creates child of event node and gets reference
        DatabaseReference eventRef = databaseRef.child("Events").push();
        //sets child's value
        eventRef.setValue(event);
        //unique auto-generated event ID
        String eventID = eventRef.getKey();
        //adds event ID to database under its category
        databaseRef.child("CategoriesToEvents").child(event.category).setValue(eventID);
        return eventID;
    }

    //subscribes user to event
    protected void subscribeToEvent(String userID, String eventID) {
        databaseRef.child("CategoriesToEvents").child(userID).child(eventID).setValue("true");
    }

    //Get current user's ID
    protected String getUserID(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth != null){
            return mAuth.getCurrentUser().getUid();
        }
        else return null;
    }

    protected List<String> getTopEvents(){

        DatabaseReference userEventsRef = databaseRef.child("Events");


        ValueEventListener userEventsListener = new ValueEventListener() {
            //called when attached to reference and when node or its children is modified
            //dataSnapshot is an object representing database contents

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : childSnapshots) {
                        HashMap<String, Object> map= (HashMap)child.getValue();
                        Log.d("DATA",""+  map.get("title"));

                        //eventIDs.add(child.getKey());
                    }
                    //do something with eventIDs, such as populate list
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error w/ onDataChange
                Log.d("DummyTag", "getUsersEventIDs:onCancelled", databaseError.toException());
            }
        };
        //attaches listener to reference
        userEventsRef.addValueEventListener(userEventsListener);
        return null;

    }

    //accesses user's subsribed events. This code would be better suited in an activity
    protected void accessUsersEventIDs (String userID) {
        final List<String> eventIDs = new ArrayList<>();
        //String userID = user.getUid();

        //reference to user's events
        DatabaseReference userEventsRef = databaseRef.child("UserToEvents").child(userID);

        //listener for changes to a node in database
        ValueEventListener userEventsListener = new ValueEventListener() {
            //called when attached to reference and when node or its children is modified
            //dataSnapshot is an object representing database contents
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : childSnapshots) {
                        System.out.println(child.getKey());
                        eventIDs.add(child.getKey());
                    }
                    //do something with eventIDs, such as populate list
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error w/ onDataChange
                Log.d("DummyTag", "getUsersEventIDs:onCancelled", databaseError.toException());
            }
        };
        //attaches listener to reference
        userEventsRef.addValueEventListener(userEventsListener);
    }

    //accesses event IDs associated with category
    protected void accessEventIDsFromCategory (String category) {
        final List<String> eventIDs = new ArrayList<>();
        DatabaseReference  categoryEventsRef = databaseRef.child(category);

        //listener
        ValueEventListener categoryEventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> childSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : childSnapshots) {
                        eventIDs.add(child.getKey());
                    }
                    //do something with eventIDs, such as display corresponding events
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error
                Log.d("DummyTag", "getUsersEventIDs:onCancelled", databaseError.toException());
            }
        };
        categoryEventsRef.addValueEventListener(categoryEventsListener);
    }
}

/* Here is example of JSON file so far:

{
  "CategoriesToEvents" : {
    "Sports" : "-KWV2yPiqNA4IgAbWUl7"
  },
  "Events" : {
    "-KWV2yPiqNA4IgAbWUl7" : {
      "category" : "Sports",
      "checks" : 0,
      "dateTime" : {
        "day" : 25,
        "hour" : 22,
        "minute" : 30,
        "month" : 11,
        "year" : 2016
      },
      "description" : "Bruins take on Rangers in the playoffs",
      "title" : "Bruins Game"
    }
  },
  "Sports" : {
    "-KWV2yPiqNA4IgAbWUl7" : true
  },
  "UserToEvents" : {
    "hFmYEO0pCFf3uWSMzDGLnWdBrqg2" : {
      "-KWV2yPiqNA4IgAbWUl7" : true
    }
  }
}

 */