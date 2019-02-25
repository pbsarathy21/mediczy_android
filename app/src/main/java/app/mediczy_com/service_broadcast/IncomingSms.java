package app.mediczy_com.service_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 10-12-2015.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            MLog.e("SmsReceiver", "" + "SmsReceiver");

            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Intent local = new Intent();
                    local.setAction(Constant.Package_Service);
                    local.putExtra("msg", message);
                    context.sendBroadcast(local);

                    MLog.e("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
/*                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();*/
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            MLog.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }
}
