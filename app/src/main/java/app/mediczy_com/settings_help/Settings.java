package app.mediczy_com.settings_help;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import app.mediczy_com.R;
import app.mediczy_com.dashboard.SplashScreen;
import app.mediczy_com.health_tube.HealthTube_List;
import app.mediczy_com.profile.Profile_Edit;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private CardView CV_Notification, CV_Profile, CV_Notification_Tone, CV_Help,
            Cv_Payment, Cv_Logout;
    private TextView Tv_Rupee;
    private RadioButton Rb_Notification_Tone;
    private int notification_tone=1;
    private String Type="", balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CV_Notification = (CardView)findViewById(R.id.relative_setting_notification);
        CV_Profile = (CardView)findViewById(R.id.relative_setting_profile);
        CV_Notification_Tone = (CardView)findViewById(R.id.relative_setting_notifi_tone);
        CV_Help = (CardView)findViewById(R.id.relative_setting_help);
        Cv_Payment = (CardView) findViewById(R.id.relative_setting_payment);
        Cv_Logout = (CardView) findViewById(R.id.relative_setting_notifi_logout);
        Tv_Rupee = (TextView) findViewById(R.id.textView_setting_payment_rupee);
        Rb_Notification_Tone = (RadioButton) findViewById(R.id.radio_button_setting_notification_tone);
        Type = LocalStore.getInstance().getType(this);
        balance = LocalStore.getInstance().getDoctorBalance(this);
        MLog.e("Type", "" + Type);
        MLog.e("balance", "" + balance);
        Tv_Rupee.setText("Rs." + balance);
        Cv_Logout.setVisibility(View.GONE);

        Cv_Payment.setOnClickListener(this);
        CV_Notification.setOnClickListener(this);
        CV_Profile.setOnClickListener(this);
        CV_Notification_Tone.setOnClickListener(this);
        CV_Help.setOnClickListener(this);
        Cv_Logout.setOnClickListener(this);
        Rb_Notification_Tone.setOnClickListener(this);

        notification_tone = Integer.valueOf(LocalStore.getInstance().getNotificationTone(this));
        MLog.e("notification", "" + notification_tone);
        if (notification_tone==1) {
            Rb_Notification_Tone.setChecked(true);
        }else if (notification_tone==0) {
            Rb_Notification_Tone.setChecked(false);
        }

        if (Type.equals("Patient")){
            CV_Profile.setVisibility(View.GONE);
            Cv_Payment.setVisibility(View.GONE);
            CV_Notification.setVisibility(View.VISIBLE);
        }else{
            CV_Notification.setVisibility(View.GONE);
            Cv_Payment.setVisibility(View.VISIBLE);
            CV_Profile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_setting_notification:
                Intent notifi = new Intent(Settings.this, HealthTube_List.class);
                startActivity(notifi);
                overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
                break;
            case R.id.relative_setting_profile:
                Intent profile = new Intent(Settings.this, Profile_Edit.class);
                startActivity(profile);
                overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
                break;
            case R.id.relative_setting_notifi_tone:
                MLog.e("notification", "" + notification_tone);
//                LocalStore localStore = new LocalStore();
                if (notification_tone==1) {
                    notification_tone=0;
                    Rb_Notification_Tone.setChecked(false);
                    LocalStore.getInstance().setNotificationTone(this, String.valueOf(notification_tone));
                }else if (notification_tone==0) {
                    notification_tone=1;
                    Rb_Notification_Tone.setChecked(true);
                    LocalStore.getInstance().setNotificationTone(this, String.valueOf(notification_tone));
                }
                break;
            case R.id.radio_button_setting_notification_tone:
                MLog.e("notification", "" + notification_tone);
//                LocalStore localStore1 = new LocalStore();
                if (notification_tone==1) {
                    notification_tone=0;
                    Rb_Notification_Tone.setChecked(false);
                    LocalStore.getInstance().setNotificationTone(this, String.valueOf(notification_tone));
                }else if (notification_tone==0) {
                    notification_tone=1;
                    Rb_Notification_Tone.setChecked(true);
                    LocalStore.getInstance().setNotificationTone(this, String.valueOf(notification_tone));
                }
                break;
            case R.id.relative_setting_help:
                Intent i = new Intent(Settings.this, Help_Activity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
                break;
            case R.id.relative_setting_notifi_logout:
                alert_msg_logout();
                break;
        }
    }

    private void alert_msg_logout()
    {
        new AlertDialog.Builder(Settings.this)
                .setMessage("Are you sure you want to sign out")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try {
                            LocalStore.getInstance().ClearData(Settings.this);
                            Intent i = new Intent(Settings.this, SplashScreen.class);
                            startActivity(i);
                            finish();
                       //     HomePage.homePage.finish();
                            overridePendingTransition(R.anim.fade_in_1, R.anim.fade_out_1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).show();
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finish();
            overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
            return true;
        }
        return false;
    }
}
