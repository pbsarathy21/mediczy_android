package app.mediczy_com.video_call.updated;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.doctor_prescribed.ADD_Doctor_Prescribed;
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
 * Created by Prithivi on 01-07-2018.
 */

public class CallScreenActivityVideoCall extends VideoCallBaseActivity implements ResponseListener {

    static final String TAG = CallScreenActivityVideoCall.class.getSimpleName();
    static final String ADDED_LISTENER = "addedListener";

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    private String mCallId, mCallName;
    private boolean mAddedListener = false;
    private boolean mLocalVideoViewAdded = false;
    private boolean mRemoteVideoViewAdded = false;
    private boolean isLoudSpeakerEnabled = false;

    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    private ImageView iVCallSpeakerState;

    private String patient_id, doctorId;
    private boolean isOpen =true;
    private RequestCommon requestCommon;
    private int mRequestType = 0;
    private ProgressDialog pDialog = null;

    private class UpdateCallDurationTask extends TimerTask {
        @Override
        public void run() {
            CallScreenActivityVideoCall.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_call_callscreen);

        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        iVCallSpeakerState = (ImageView) findViewById(R.id.imageView_sinch_speaker_state);
        iVCallSpeakerState.setVisibility(View.GONE);
        Button endCallButton = (Button) findViewById(R.id.hangupButton);

        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });

        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        mCallName = getIntent().getStringExtra(SinchService.CALL_NAME);
        MLog.e("name", "" + mCallName);

        iVCallSpeakerState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoudSpeakerEnabled) {
                    isLoudSpeakerEnabled = false;
                    iVCallSpeakerState.setImageDrawable(ContextCompat.getDrawable(CallScreenActivityVideoCall.this,
                            R.drawable.call_phone));
                    AudioController audioController = getSinchServiceInterface().getAudioController();
                    audioController.disableSpeaker();
                }else {
                    isLoudSpeakerEnabled = true;
                    iVCallSpeakerState.setImageDrawable(ContextCompat.getDrawable(CallScreenActivityVideoCall.this,
                            R.drawable.call_phone_loud));
                    AudioController audioController = getSinchServiceInterface().getAudioController();
                    audioController.enableSpeaker();
                }
            }
        });
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (!mAddedListener) {
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }
        } else {
            MLog.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }

        updateUI();
    }

    private void updateUI() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mCallerName.setText(call.getRemoteUserId());
            mCallState.setText(call.getState().toString());
            if (call.getDetails().isVideoOffered()) {
                addLocalView();
                if (call.getState() == CallState.ESTABLISHED) {
                    addRemoteView();
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDurationTask.cancel();
        mTimer.cancel();
        removeVideoViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
        updateUI();
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
            String remoteUserId = call.getRemoteUserId();
            LocalStore.getInstance().setPatientVideoCallId(this, remoteUserId);
            MLog.e("call_remoteUserId", ""+remoteUserId);
        }
        finish();
        String Type = LocalStore.getInstance().getType(this);
        if (isOpen) {
            isOpen = false;
            if (Type.equalsIgnoreCase("Doctor")) {
                doctorPrescriptionActivity();
//                requestCallCompletedApi();
            }
        }
    }

    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }

    private void addLocalView() {
        if (mLocalVideoViewAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localVideo);
            localView.addView(vc.getLocalView());
            localView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    vc.toggleCaptureDevicePosition();
                }
            });
            mLocalVideoViewAdded = true;
        }
    }
    private void addRemoteView() {
        if (mRemoteVideoViewAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            LinearLayout view = (LinearLayout) findViewById(R.id.remoteVideo);
            view.addView(vc.getRemoteView());
            mRemoteVideoViewAdded = true;
        }
    }

    private void removeVideoViews() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            LinearLayout view = (LinearLayout) findViewById(R.id.remoteVideo);
            view.removeView(vc.getRemoteView());

            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localVideo);
            localView.removeView(vc.getLocalView());
            mLocalVideoViewAdded = false;
            mRemoteVideoViewAdded = false;
        }
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            MLog.e(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
//            Utility.getInstance().showToast(endMsg, CallScreenActivityVideoCall.this);
            MLog.e(TAG, "Call ended: " + endMsg.toString());
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            MLog.e(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            audioController.enableSpeaker();
            isLoudSpeakerEnabled = true;
            iVCallSpeakerState.setVisibility(View.VISIBLE);
            MLog.e(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        @Override
        public void onCallProgressing(Call call) {
            MLog.e(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // No need to implement if you use managed push
            MLog.e(TAG, "onShouldSendPushNotification");
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            MLog.e(TAG, "Video track added");
            addRemoteView();
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }
    private void doctorPrescriptionActivity() {
        Intent myIntent = new Intent(this, ADD_Doctor_Prescribed.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
    }

    private void requestCallCompletedApi() {
        patient_id = LocalStore.getInstance().getPatientVideoCallId(this);
        doctorId = LocalStore.getInstance().getUserID(this);
        MLog.e("patient_id", ""+patient_id);
        MLog.e("doctorId", ""+doctorId);
        mRequestType = IConstant_WebService.WSR_Call_Completed;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.doctor_id = doctorId;
            requestCommon.patient_id = patient_id;
            RequestManager.Doctor_Call_Completed(this, null, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_Call_Completed) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                String responseSuccess = req.status;
                String msg = req.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("msg", "" + msg);
                if (responseSuccess != null) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setData(req);
                    } else if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }
        }
    }

    private void setData(CommonResponse req) {
        try {
            Utility.getInstance().showToast(req.msg, this);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }
}
