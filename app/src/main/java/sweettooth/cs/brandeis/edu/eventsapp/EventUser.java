package sweettooth.cs.brandeis.edu.eventsapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.api.model.GetTokenResponse;

import java.util.List;

/**
 * Created by Chelsi Hu on 11/10/2016.
 */

public class EventUser extends FirebaseUser {
    @NonNull
    @Override
    public String getUid() {
        return null;
    }

    @NonNull
    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Nullable
    @Override
    public List<String> getProviders() {
        return null;
    }

    @NonNull
    @Override
    public List<? extends UserInfo> getProviderData() {
        return null;
    }

    @NonNull
    @Override
    public FirebaseUser zzaq(@NonNull List<? extends UserInfo> list) {
        return null;
    }

    @Override
    public FirebaseUser zzcu(boolean b) {
        return null;
    }

    @NonNull
    @Override
    public FirebaseApp zzcow() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return null;
    }

    @Nullable
    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @NonNull
    @Override
    public GetTokenResponse zzcox() {
        return null;
    }

    @Override
    public void zza(@NonNull GetTokenResponse getTokenResponse) {

    }

    @NonNull
    @Override
    public String zzcoy() {
        return null;
    }

    @NonNull
    @Override
    public String zzcoz() {
        return null;
    }
}
