package sweettooth.cs.brandeis.edu.eventsapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Settings Fragment
 */

public class SettingsFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener  {

    TextView user;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ImageView i;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String text) {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View settingsFragmentView = inflater.inflate(R.layout.login_screen, container, false);

        user = (TextView) settingsFragmentView.findViewById(R.id.currentUser);
        i = (ImageView) settingsFragmentView.findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            user.setText(mAuth.getCurrentUser().getDisplayName());
        }
        else{
            user.setText("Please sign in.");
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("GoogleActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("GoogleActivity", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        return settingsFragmentView;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        SignInButton inbutton = (SignInButton) v.findViewById(R.id.sign_in_button);
        Button outbutton = (Button) v.findViewById(R.id.sign_out_button);
        user = (TextView) v.findViewById(R.id.currentUser);





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage((FragmentActivity)getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(AppIndex.API).build();
        }






        inbutton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                //Sends description and notes back to main to create an entry
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        Log.d("BUTTONBUTTONBUTTON", "sign in button click");

                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, 1);


                        if(mAuth.getCurrentUser() != null){
                            user.setText(mAuth.getCurrentUser().getDisplayName());
                        }
                        else{
                            user.setText("Please sign in.");
                        }
                        break;
                    // ...
                }


            }

        });

        outbutton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                //Sends description and notes back to main to create an entry
                switch (view.getId()) {
                    case R.id.sign_out_button:
                        signOut();
                        user.setText("Please sign in.");
                        break;
                    // ...
                }


            }

        });


        
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("TESTSUCCESS", "" + result.getSignInAccount());
            Log.d("TESTGOOGLE", ""+ result);
            Log.d("TESTSTATUS", "" + result.getStatus());
            Log.d("TESTSUCCESS", "" + result.isSuccess());

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }

        }

    }


    // [END signin]

    void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        //Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GoogleActivity", "firebaseAuthWithGoogle: " + acct.getDisplayName());
        Log.d("GoogleActivity", "IDTOKEN: " + acct.getIdToken());



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        final Uri u = acct.getPhotoUrl();

        Log.d("GoogleActivity", "Cred: " + credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("GoogleActivity", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(mAuth.getCurrentUser() != null){

                            user.setText(mAuth.getCurrentUser().getDisplayName());

                            //Displays profile image in imageview, but broken
                            /*try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), u);
                                i.setImageBitmap(bitmap);
                            }catch (IOException e){
                                Log.d("GoogleActivity", "Error setting profile image.");
                            }*/

                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("GoogleAuth", "signInWithCredential", task.getException());

                        }
                        // ...
                    }
                });


    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GoogleAuth Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        //mGoogleApiClient.connect();
        //AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());



    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        //mGoogleApiClient.disconnect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
