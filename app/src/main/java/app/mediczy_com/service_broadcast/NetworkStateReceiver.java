package app.mediczy_com.service_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import app.mediczy_com.utility.MLog;

/**
 * Created by prithivi on 2-Jan-16.
 */
    public class NetworkStateReceiver extends BroadcastReceiver{

        private static final String LOG_TAG = "NetworkChangeReceiver";
        private boolean isConnected = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            MLog.e(LOG_TAG, "Received notification about network status");
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                MLog.e(LOG_TAG, "Now you are connected to Internet!");
                                isConnected = true;

                                Intent msgIntent = new Intent(context, ServiceOnlineStatus.class);
                                msgIntent.putExtra(ServiceOnlineStatus.SERVICE_IN_MSG, "Show_Toast");
                                context.startService(msgIntent);
                            }
                            return true;
                        }
                    }
                }
            }
            MLog.e(LOG_TAG, "You are not connected to Internet!");
            isConnected = false;
            return false;
        }
    }