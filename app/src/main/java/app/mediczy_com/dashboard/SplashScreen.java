package app.mediczy_com.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONObject;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.Splash_Side.MoveActivity;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.video_call.updated.IncomingCallScreenActivityVideoCall;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3250;
    ImageView fullscreen_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        fullscreen_content = (ImageView) findViewById(R.id.fullscreen_content);

        try {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(fullscreen_content);
            Glide.with(SplashScreen.this).load(R.raw.chat_bot_medi).into(imageViewTarget);
        } catch (Exception e) {
            MLog.e("Exception->",e.toString());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String isKeyAvailable = LocalStore.getInstance().getUserID(SplashScreen.this);
                MLog.e("isKeyAvailable->",isKeyAvailable);
                if (isKeyAvailable.equals("A")){
                    Intent i = new Intent(SplashScreen.this, Register.class);
                    startActivity(i);
                    finish();
                } else{
                    String newNotification="";
                    Bundle bundle = getIntent().getExtras();
                    if (bundle!=null)
                        newNotification = bundle.getString("newNotification");
                    Intent i = new Intent(SplashScreen.this, HomeNavigation.class);
                    i.putExtra("newNotification", newNotification);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String isKeyAvailable = LocalStore.getInstance().getUserID(SplashScreen.this);
//                if (!isKeyAvailable.equals("A")) {
//                    String newNotification = "";
//                    Bundle bundle = getIntent().getExtras();
//                    if (bundle != null)
//                        newNotification = bundle.getString("newNotification");
//                    Intent i = new Intent(SplashScreen.this, HomeNavigation.class);
//                    i.putExtra("newNotification", newNotification);
//                    startActivity(i);
//                    finish();
//                } else {
//                    Intent intent = new Intent(SplashScreen.this, MoveActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                    finish();
//
//                }
//            }
//        }, SPLASH_TIME_OUT);
    }

    private void notificationVideoCall() {
        try {
            if (getIntent().getExtras()!=null) {
                Object obj = null;
                String json = null;
                Bundle extras = getIntent().getExtras();
                if (extras != null){
                    MLog.d("EXTRAS", extras.toString());
                }

                for (String key : getIntent().getExtras().keySet()) {
                    Object value = getIntent().getExtras().get(key);
                    MLog.e("Notification_Value", "" +"Key : " + key +" Value : "+value);
                    if (key.equals("data")) {
                        obj = getIntent().getExtras().get(key);
                        MLog.e("obj-->", "" + obj.toString());
                    }
                }
                if (obj!=null) {
                    JSONObject jObject = new JSONObject(obj.toString());
                    String id = jObject.getString("id");
                    if (id!=null) {
                        Intent intent = new Intent(SplashScreen.this,
                                IncomingCallScreenActivityVideoCall.class);
                        intent.putExtra("CALL_ID", id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MLog.e("Exception-->", "" + e.toString());
        }
    }

    private void ShortcutIcon(){
        boolean status = LocalStore.getInstance().getAppShortcut(SplashScreen.this);
        MLog.e("AppShortcut-->", ""+status);
        if (!status) {
            Intent shortcutIntent = new Intent(getApplicationContext(), SplashScreen.class);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            addIntent.putExtra("duplicate", false); // Just create once
            getApplicationContext().sendBroadcast(addIntent);
            LocalStore.getInstance().setAppShortcut(SplashScreen.this, true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
