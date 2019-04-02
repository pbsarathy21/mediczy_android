package app.mediczy_com.service_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi on 04-12-2016.
 */

public class TelephonyReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context arg0, Intent intent) {
        // TODO Auto-generated method stub
        context = arg0;
        try {
            if (intent != null && intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                //Toast.makeText(context, "Outgoign call", 1000).show();
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                showToast(number);
            } else {
                //get the phone state
                String newPhoneState = intent.hasExtra(TelephonyManager.EXTRA_STATE) ? intent.getStringExtra(TelephonyManager.EXTRA_STATE) : null;
                Bundle bundle = intent.getExtras();

                if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    //read the incoming call number
                    String phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    MLog.i("PHONE RECEIVER", "Telephone is now ringing " + phoneNumber);

                } else if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    //Once the call ends, phone will become idle
                    MLog.i("PHONE RECEIVER", "Telephone is now idle");
                    showToast("PHONE RECEIVER");
                } else if (newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    //Once you receive call, phone is busy
                    MLog.i("PHONE RECEIVER", "Telephone is now busy");
                }
            }

        } catch (Exception ee) {
            MLog.i("Telephony receiver", ee.getMessage());
        }
    }
    private void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
