package app.mediczy_com.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.SinchHelpers;
import com.sinch.android.rtc.calling.CallNotificationResult;

import org.json.JSONObject;

import java.util.Map;

import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.chat.chat_new.NotificationObserver;
import app.mediczy_com.dashboard.SplashScreen;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.NotificationUtils;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.video_call.updated.IncomingCallScreenActivityVideoCall;
import app.mediczy_com.video_call.updated.SinchService;

/**
 * Created by prithiviraj on 12/11/2016.
 */

/*public class FCMMessageReceiverService  {

}*/

public class FCMMessageReceiverService extends FirebaseMessagingService implements
        SinchService.StartFailedListener, ServiceConnection {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    private NotificationObserver notificationObserver;
    private SinchService.SinchServiceInterface mSinchServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.e(TAG, "onCreate->" + "onCreate");
        sinchService();
    }

//    public Intent zzH(Intent intent) {
//        MLog.e(TAG, "Intent->" + "Intent");
////       notificationVideoCall(intent);
//        return super.zzae(intent);
//    }

//    @Override
//    protected Intent zzae(Intent intent) {
//        MLog.e(TAG, "Intent->" + "Intent");
////       notificationVideoCall(intent);
//        return super.zzae(intent);
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map data = remoteMessage.getData();
        if (SinchHelpers.isSinchPushPayload(data)) {
            new ServiceConnection() {
                private Map payload;
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    if (payload != null) {
                        MLog.e(TAG, "Sinch Payload->" + payload.toString());
                        turnOnDisplay();
                        SinchService.SinchServiceInterface sinchService = (SinchService.SinchServiceInterface) service;
                        if (sinchService != null) {
                            NotificationResult result = sinchService.relayRemotePushNotificationPayload(payload);
                            // handle result, e.g. show a notification or similar
                            // here is example for notifying user about missed/canceled call:
                            if (result.isValid() && result.isCall()) {
                                CallNotificationResult callResult = result.getCallResult();
                                if (callResult.isCallCanceled()) {
                                    createNotification(callResult.getRemoteUserId());
                                }
                            }
                        }
                    }
                    payload = null;
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    MLog.e(TAG, "Sinch onServiceDisconnected->" + "onServiceDisconnected");
                }

                public void relayMessageData(Map<String, String> data) {
                    MLog.e(TAG, "Sinch Relay MessageData->" + "relayMessageData");
                    payload = data;
                    getApplicationContext().bindService(new Intent(getApplicationContext(), SinchService.class), this, BIND_AUTO_CREATE);
                }
            }.relayMessageData(data);
        } else {
            MLog.e(TAG, "onMessageReceived->" + remoteMessage);
            MLog.e(TAG, "Data Payload->" + remoteMessage.getData().toString());
            MLog.e(TAG, "Notification Body->" + remoteMessage.getNotification().getBody());
            MLog.e(TAG, "Notification From->" + remoteMessage.getFrom());
            if (remoteMessage == null)
                return;
            Utility.getInstance().showToast("onMessageReceived", this);
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                MLog.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
                //           handleDataMessage(remoteMessage.getNotification().getBody().toString());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                MLog.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                try {
                    String mData = remoteMessage.getData().toString();
                    mData = mData.replace("{data=", "");
                    mData = mData.replace(", title=Mediczy}", "");
                    handleDataMessage(mData);
                } catch (Exception e) {
                    MLog.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }
    }

    private void turnOnDisplay() {
        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        MLog.e("Screen On->", ""+isScreenOn);
        if(isScreenOn==false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
            wl_cpu.acquire(10000);
        }
    }

    private void createNotification(String userId) {
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SplashScreen.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.app_logo)
                        .setContentTitle("Missed call from:")
                        .setContentText(userId);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void handleDataMessage(String json) {
        MLog.e(TAG, "push_json: " + json.toString());
        try {
            JSONObject jsonObj = new JSONObject(json.toString());

            if (jsonObj.has("chat_list")) {
                JSONObject data = jsonObj.getJSONObject("chat_list");
                String message = data.getString("message").toString();
                String created_at = data.getString("created_at").toString();
                String sender_id = data.getString("sender_id").toString();
                String image_path = data.getString("image_path").toString();
                String receiver_id = data.getString("receiver_id").toString();
                String type = data.getString("type").toString();
                String user_type = data.getString("user_type").toString();
                String message_id = data.getString("message_id").toString();

                MLog.e(TAG, "message: " + message);
                MLog.e(TAG, "created_at: " + created_at);
                MLog.e(TAG, "sender_id: " + sender_id);
                MLog.e(TAG, "image_path: " + image_path);
                MLog.e(TAG, "type: " + type);
                MLog.e(TAG, "receiver_id: " + receiver_id);
                MLog.e(TAG, "user_type: " + user_type);
                MLog.e(TAG, "message_id: " + message_id);

                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in background, show the notification in notification tray
                    Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
                    resultIntent.putExtra("newNotification", message);
                    // check for image attachment
                    if (TextUtils.isEmpty(image_path)) {
                        showNotificationMessage(getApplicationContext(), message, message, created_at, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), message, message, created_at, resultIntent, image_path);
                    }
                } else {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(FCMConfig.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("current_time", created_at);
                    pushNotification.putExtra("sender_id", sender_id);
                    pushNotification.putExtra("image", image_path);
                    pushNotification.putExtra("receiver_id", receiver_id);
                    pushNotification.putExtra("type", type);
                    pushNotification.putExtra("user_type", user_type);
                    pushNotification.putExtra("message_id", message_id);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();

                    notificationObserver = (NotificationObserver) HomeNavigation.homeNavigation;
                    notificationObserver.onNotificationReceived(true);
                }
            } else if (jsonObj.has("video_call")) {
                Utility.getInstance().showToast(jsonObj.toString(), this);
                JSONObject data = jsonObj.getJSONObject("video_call");
                final String doctor_id = data.getString("doctor_id").toString();
                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Utility.getInstance().showToast(jsonObj.toString(), this);
                    sinchService();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(FCMMessageReceiverService.this,
                                    IncomingCallScreenActivityVideoCall.class);
                            intent.putExtra("CALL_ID", doctor_id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, 200);
                }
            }
        } catch (Exception e) {
            MLog.e(TAG, "Exception: " + e.getMessage());
            Utility.getInstance().showToast(e.getMessage().toString(), this);
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(FCMConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void sendNotification(RemoteMessage.Notification notification) {

        int color = getResources().getColor(R.color.notification_color);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app_logo)
                .setColor(color)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void sinchService() {
        getApplicationContext().bindService(new Intent(this, SinchService.class), this,
                BIND_AUTO_CREATE);
        try {
            final String userId = LocalStore.getInstance().getUserID(this);
            MLog.e("TAG", "sinchService" + userId);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getSinchServiceInterface() != null) {
                        if (!getSinchServiceInterface().isStarted()) {
                            getSinchServiceInterface().startClient(userId);
                        }
                    }
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

    @Override
    public void onStartFailed(SinchError error) {
        Utility.getInstance().showToast(error.toString(), this);
        //       dismissDialog();
    }

    @Override
    public void onStarted() {
//        dismissDialog();
    }

    private void playSoundForXSeconds(int seconds) {
        final MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(FCMMessageReceiverService.this,
                    Uri.parse("android.resource://" + FCMMessageReceiverService.this.getPackageName() + "/" + R.raw.video_call_ringtone));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    mp.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, seconds * 1000);
    }

    private void notificationVideoCall(final Intent intent) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (intent.getExtras()!=null) {
                    for (String key : intent.getExtras().keySet()) {
                        Object value = intent.getExtras().get(key);
                        MLog.e("Notification_Value", "" +"Key : " + key +" Value : "+value);
                        if (intent.getExtras().get(key).equals("data")) {
                            Object obj = intent.getExtras().get(key);
                            MLog.e("obj-->", "" +obj);
                            Gson gson = new Gson();
                            NotificationResponse notificationData
                                    = gson.fromJson(obj.toString(), NotificationResponse.class);
                            MLog.e("notificationData-->", "" +notificationData);
                            if (notificationData!=null) {
                                if (notificationData.id!=null) {
                                    playSoundForXSeconds(30);
                                    Intent intent1 = new Intent(FCMMessageReceiverService.this,
                                            IncomingCallScreenActivityVideoCall.class);
                                    intent1.putExtra("CALL_ID", notificationData.id);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent1);
                                }
                            }
                        }
                    }
                }
            }
        }, 200);
    }
}
