package com.ustglobal.myustapp.Notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ustglobal.myustapp.Common.USTSharedPreferences;

/**
 * Created by Shubham Singhal.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    private USTSharedPreferences sp = null;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        sp = new USTSharedPreferences(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sp.setPrefrenceStringdata(USTSharedPreferences.IE.firebase_token,refreshedToken);

        String id = FirebaseInstanceId.getInstance().getId();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}