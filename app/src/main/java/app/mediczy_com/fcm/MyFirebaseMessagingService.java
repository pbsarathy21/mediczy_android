package app.mediczy_com.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import app.mediczy_com.utility.MLog;

/**
 * Created by aprithiviraj on 12/11/2016.
 */

/*public class MyFirebaseMessagingService  {
}*/

public class MyFirebaseMessagingService  extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MLog.e("refreshedToken-->", refreshedToken);
        if(refreshedToken!=null && !refreshedToken.equals(""))
            sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(FCMConfig.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        // sending gcm token to server
        MLog.e("sendRegistrationToServer :", "" + token);
    }
}
