package app.mediczy_com.doctor_prescribed;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ADD_Doctor_Prescribed extends AppCompatActivity implements ResponseListener {

    String strPrescriptionMsg, strPrescriptionSubject,
            strPrescription, doctorId, patient_id;
    Dialog dialog;
    RequestCommon requestCommon;
    private int mRequestType = 0;
    ProgressDialog pDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doctor_prescribed);

        patient_id = LocalStore.getInstance().getPatientVideoCallId(this);
        doctorId = LocalStore.getInstance().getUserID(this);

        MLog.e("patient_id", ""+patient_id);
        MLog.e("doctorId", ""+doctorId);
        custom_dialog_prescription();
    }

    private void custom_dialog_prescription() {
        final Animation animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_prescription);
        dialog.setCancelable(false);
        final EditText Ed_Subject = (EditText) dialog.findViewById(R.id.editText_popup__add_prescription_subject);
        final EditText Ed_PopUp = (EditText) dialog.findViewById(R.id.editText_popup__add_prescription);
        final CheckBox toggleButton = (CheckBox) dialog.findViewById(R.id.toggleButton_send_prescription);
        final TextView tvSend = (TextView) dialog.findViewById(R.id.textView_pres_send);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.textView_pres_cancel);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Ed_PopUp, InputMethodManager.SHOW_IMPLICIT);
 //       toggleButton.setChecked(true);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String str_ed = Ed_PopUp.getText().toString();
                String str_ed_Subject = Ed_Subject.getText().toString();
                if (str_ed_Subject.equals("")) {
                    Ed_Subject.startAnimation(animationShake);
                } else if (str_ed.equals("")) {
                    Ed_PopUp.startAnimation(animationShake);
                }else {
                    strPrescriptionMsg= "";
                    strPrescriptionSubject="";
                    strPrescriptionMsg = str_ed;
                    strPrescriptionSubject = str_ed_Subject;
                    mRequestType = IConstant_WebService.WSR_Doctor_Prescribed;
                    onRequest();
                }
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strPrescription = "Yes";
                    toggleButton.setChecked(true);
                } else {
                    strPrescription = "No";
                    toggleButton.setChecked(false);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });
        dialog.show();
    }

    private void finishActivity() {
        dialog.dismiss();
        LocalStore.getInstance().Clear(ADD_Doctor_Prescribed.this);
        finish();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.doctor_id = doctorId;
            requestCommon.patient_id = patient_id;
            requestCommon.prescription_Msg = strPrescriptionMsg;
            requestCommon.prescription_Subject = strPrescriptionSubject;
            requestCommon.prescription_CodeStatus = strPrescription;
            RequestManager.Doctor_Prescribed(this, null, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_Doctor_Prescribed) {
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
            finishActivity();
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }
}
