package app.mediczy_com.settings_help;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.dashboard.Register;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.health_tube.HealthTube_List;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.profile.Profile_Edit;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.AsyncRequest;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi on 23-10-2016.
 */

public class Setting_Fragment extends Fragment implements View.OnClickListener, ResponseListener {

    Context context;
    private Toolbar toolbar;
    private CardView CV_Notification, CV_Profile, CV_Notification_Tone, CV_Help,
            Cv_Payment, Cv_Logout, Cv_Version;
    private TextView Tv_Rupee, tvVersion;
    private RadioButton Rb_Notification_Tone;
    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;
    private int notification_tone=1;
    private String Type="", pending_amount, responseStatus="", doctor_id,
            responseMsg="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        context = getActivity();
        //      Actionbar_Menu(rootView);
        init(rootView);

        Cv_Version.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Utility.getInstance().showToast(getResources().getString(R.string.buildType), context);
                return false;
            }
        });

        return rootView;
    }


    private void init(View rootView) {
 /*       toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        CV_Notification = (CardView)rootView.findViewById(R.id.relative_setting_notification);
        CV_Profile = (CardView)rootView.findViewById(R.id.relative_setting_profile);
        CV_Notification_Tone = (CardView)rootView.findViewById(R.id.relative_setting_notifi_tone);
        CV_Help = (CardView)rootView.findViewById(R.id.relative_setting_help);
        Cv_Payment = (CardView) rootView.findViewById(R.id.relative_setting_payment);
        Cv_Logout = (CardView) rootView.findViewById(R.id.relative_setting_notifi_logout);
        Cv_Version = (CardView) rootView.findViewById(R.id.relative_setting_version);

        Tv_Rupee = (TextView) rootView.findViewById(R.id.textView_setting_payment_rupee);
        tvVersion = (TextView) rootView.findViewById(R.id.textView_setting_version);
        Rb_Notification_Tone = (RadioButton) rootView.findViewById(R.id.radio_button_setting_notification_tone);
        Type = LocalStore.getInstance().getType(context);
        pending_amount = LocalStore.getInstance().getDoctorBalance(context);
        doctor_id = LocalStore.getInstance().getUserID(context);
        MLog.e("balance", "" + pending_amount);
        MLog.e("Type", "" + Type);
        MLog.e("doctor_id", "" + doctor_id);
//        Cv_Logout.setVisibility(View.GONE);

        Cv_Payment.setOnClickListener(this);
        CV_Notification.setOnClickListener(this);
        CV_Profile.setOnClickListener(this);
        CV_Notification_Tone.setOnClickListener(this);
        CV_Help.setOnClickListener(this);
        Cv_Logout.setOnClickListener(this);
        Rb_Notification_Tone.setOnClickListener(this);

//        tvVersion.setText("Version "+ getResources().getString(R.string.versionName));
        notification_tone = Integer.valueOf(LocalStore.getInstance().getNotificationTone(context));
        MLog.e("notification", "" + notification_tone);
        if (notification_tone==1) {
            Rb_Notification_Tone.setChecked(true);
        }else if (notification_tone==0) {
            Rb_Notification_Tone.setChecked(false);
        }

        if (Type.equalsIgnoreCase("Patient")){
            CV_Profile.setVisibility(View.GONE);
            Cv_Payment.setVisibility(View.GONE);
            CV_Notification.setVisibility(View.VISIBLE);
        }else {
            CV_Notification.setVisibility(View.GONE);
            Cv_Payment.setVisibility(View.VISIBLE);
            CV_Profile.setVisibility(View.VISIBLE);

            mRequestType = IConstant_WebService.WSR_DoctorBalance;
            onRequest();
        }
        CV_Notification.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_setting_payment:
                break;
            case R.id.relative_setting_notification:
                Intent notifi = new Intent(context, HealthTube_List.class);
                startActivity(notifi);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.relative_setting_profile:
                Intent profile = new Intent(context, Profile_Edit.class);
                startActivity(profile);
                getActivity().overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
                break;
            case R.id.relative_setting_notifi_tone:
                MLog.e("notification", "" + notification_tone);
//                LocalStore localStore = new LocalStore();
                if (notification_tone==1) {
                    notification_tone=0;
                    Rb_Notification_Tone.setChecked(false);
                    LocalStore.getInstance().setNotificationTone(context, String.valueOf(notification_tone));
                }else if (notification_tone==0) {
                    notification_tone=1;
                    Rb_Notification_Tone.setChecked(true);
                    LocalStore.getInstance().setNotificationTone(context, String.valueOf(notification_tone));
                }
                break;
            case R.id.radio_button_setting_notification_tone:
                MLog.e("notification", "" + notification_tone);
  //              LocalStore localStore1 = new LocalStore();
                if (notification_tone==1) {
                    notification_tone=0;
                    Rb_Notification_Tone.setChecked(false);
                    LocalStore.getInstance().setNotificationTone(context, String.valueOf(notification_tone));
                }else if (notification_tone==0) {
                    notification_tone=1;
                    Rb_Notification_Tone.setChecked(true);
                    LocalStore.getInstance().setNotificationTone(context, String.valueOf(notification_tone));
                }
                break;
            case R.id.relative_setting_help:
                Intent i = new Intent(context, Help_Activity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.relative_setting_notifi_logout:
                alert_msg_logout();
                break;
        }
    }

    private void alert_msg_logout()
    {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to sign out")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try {
                            LocalStore.getInstance().ClearData(context);
                            Intent i = new Intent(context, Register.class);
                            startActivity(i);
                      //      finish();
                            HomeNavigation.homeNavigation.finish();
                            getActivity().overridePendingTransition(R.anim.fade_in_1, R.anim.fade_out_1);
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

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            requestCommon.doctor_id = doctor_id;
            String requestURL;
            requestURL = IConstant_WebService.doctor_admin_balance;
            RequestManager.DoctorAdminBalance(context, this, requestCommon, mRequestType, requestURL);
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_DoctorBalance) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                setData(res);
            }else {
                AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(CommonResponse res) {
        try {
            responseStatus = res.status;
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)) {
                pending_amount = res.pending_amount;
                Tv_Rupee.setText("Rs." + pending_amount);
            } else if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)) {
                responseMsg = res.msg;
                AlertDialogFinish.Show(getActivity(), responseMsg, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(getActivity(), e.toString(), true);
        }
    }
}
