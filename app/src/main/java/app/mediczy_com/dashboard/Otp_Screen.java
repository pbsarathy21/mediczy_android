package app.mediczy_com.dashboard;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.mediczy_com.BaseActivity;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi Raj on 09-12-2015.
 */
public class Otp_Screen extends BaseActivity implements ResponseListener {

    private TextView Ed_Otp;
    private Button btn_Resend;
    private FloatingActionButton fb_tick;
    private Animation animationShake;
    BroadcastReceiver updateUIReciver;
    CountDownTimer countDownTimer;
    private String device_id, strPhoneNumber, responseUserID="", responseOtp, responsePhoneNumber,
            responseType, redId, responseStatus, responseMsg, ReqType="";

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_scree);
        setToolbar();
        init();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.Package_Service);
        updateUIReciver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String state = intent.getExtras().getString("msg");
                MLog.e("state", "" + state);
                if (state.contains(responseOtp)){
                    Ed_Otp.setText(responseOtp);
                    fb_tick.setVisibility(View.GONE);
                    ReqType="Otp";
                    mRequestType = IConstant_WebService.WSR_Otp_Screen;
                    onRequest(ReqType);
                    CancelTimer();
                }else {
                    showToast("Invalid otp");
                }
            }
        };
        registerReceiver(updateUIReciver, filter);

        btn_Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.getInstance().isConnectingToInternet(Otp_Screen.this)) {
                    Ed_Otp.setText("");
                    fb_tick.setVisibility(View.GONE);
                    ResendCount();
                    ReqType="Otp_Resend";
                    mRequestType = IConstant_WebService.WSR_Otp_Screen;
                    onRequest(ReqType);
                } else {
                    AlertDialogFinish.Show(Otp_Screen.this, Constant.Alart_Internet, true);
                }
            }
        });
    }

    private void CancelTimer() {
        try {
            if (countDownTimer!=null)
                countDownTimer.cancel();
            btn_Resend.setEnabled(true);
            btn_Resend.setBackgroundColor(Otp_Screen.this.getResources().getColor(R.color.blue_1));
            btn_Resend.setText("RESEND");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(updateUIReciver);
    }

    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
//        getSupportActionBar().setTitle("FAQ");
        getSupportActionBar().setTitle("Verify your number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        Ed_Otp = (TextView)findViewById(R.id.editText_otp_screen);
        fb_tick = (FloatingActionButton)findViewById(R.id.fab_otp_screen);
        btn_Resend = (Button)findViewById(R.id.button_otp_resend);
        device_id = Utility.getInstance().getDeviceID(Otp_Screen.this);

        Bundle bundle = getIntent().getExtras();
        responseUserID = bundle.getString("responseUserID");
        responseOtp = bundle.getString("responseOtp");
        responsePhoneNumber = bundle.getString("responsePhoneNumber");
        responseType = bundle.getString("responseType");
        redId = bundle.getString("fcm_id");
        MLog.e("responseUserID", "" + responseUserID);
        MLog.e("responseOtp", "" + responseOtp);
        ResendCount();
    }

    private void ResendCount() {
        countDownTimer = new CountDownTimer(50000, 1000) {
            public void onTick(long millisUntilFinished) {
                Ed_Otp.setText("Wait a while to receive otp message...");
                btn_Resend.setBackgroundColor(Otp_Screen.this.getResources().getColor(R.color.blue_light));
                btn_Resend.setTextColor(Otp_Screen.this.getResources().getColor(R.color.white));
                btn_Resend.setText(""+String.valueOf(millisUntilFinished / 1000));
                btn_Resend.setEnabled(false);
            }
            public void onFinish() {
                Ed_Otp.setText("Wait a while to receive otp message...");
                btn_Resend.setEnabled(true);
                btn_Resend.setBackgroundColor(Otp_Screen.this.getResources().getColor(R.color.blue_1));
                btn_Resend.setText("RESEND");
            }
        }.start();
    }

    private void moveNavigationPage() {
        Intent i = new Intent(Otp_Screen.this, HomeNavigation.class);
        startActivity(i);
        finish();
    }

    private void showToast(String msg){
        Toast.makeText(Otp_Screen.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void onRequest(String reqType1) {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            String ed_msg = Ed_Otp.getText().toString().trim();
            requestCommon = new RequestCommon();
            requestCommon.mobile_number = responsePhoneNumber;
            requestCommon.user_id  = responseUserID;
            requestCommon.otp = ed_msg;
            if (reqType1.equals("Otp")) {
                String requestURL = IConstant_WebService.check_user_otp;
                RequestManager.Otp_Screen(this, null, requestCommon, mRequestType, requestURL);
            }else if (reqType1.equals("Otp_Resend")){
                String requestURL = IConstant_WebService.send_user_otp;
                RequestManager.Otp_Resend(this, null, requestCommon, mRequestType, requestURL);
            }
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_Otp_Screen) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                setData(req);
            }else {
                CancelTimer();
                AlertDialogFinish.Show(Otp_Screen.this, Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(CommonResponse req) {
        responseStatus = req.status;
        responseMsg = req.msg;
        MLog.e("responseSuccess", "" + responseStatus);
        MLog.e("msg", "" + responseMsg);
        if (responseStatus != null) {
            if (ReqType.equals("Otp")) {
                if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                    showToast("Welcome To Mediczy");
                    LocalStore.getInstance().setUserId(Otp_Screen.this, responseUserID);
                    LocalStore.getInstance().setOtp(Otp_Screen.this, responseOtp);
                    Intent myIntent = new Intent(Register.ACTION_CLOSE);
                    sendBroadcast(myIntent);
                    moveNavigationPage();
                }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                    AlertDialogFinish.Show(Otp_Screen.this, responseMsg, true);
                } else {
                    AlertDialogFinish.Show(Otp_Screen.this, Constant.Alart_Status500, true);
                }
            }
            if (ReqType.equals("Otp_Resend")) {
                if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                    if (req.patientRegisterResponse!=null) {
                        responseOtp = req.patientRegisterResponse.otp;
                        btn_Resend.setEnabled(true);
                        btn_Resend.setBackgroundColor(Otp_Screen.this.getResources().getColor(R.color.blue_1));
                        btn_Resend.setText("RESEND");
                    } else {
                        AlertDialogFinish.Show(Otp_Screen.this, Constant.Alart_Status500, true);
                    }
                }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                    AlertDialogFinish.Show(Otp_Screen.this, responseMsg, true);
                } else {
                    AlertDialogFinish.Show(Otp_Screen.this, Constant.Alart_Status500, true);
                }
            }
        }else {
            CancelTimer();
            AlertDialogFinish.Show(Otp_Screen.this, Constant.Alart_Status500, true);
        }
    }
}
