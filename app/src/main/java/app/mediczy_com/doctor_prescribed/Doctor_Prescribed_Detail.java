package app.mediczy_com.doctor_prescribed;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import app.mediczy_com.BaseActivity;
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
import app.mediczy_com.webservicemodel.response.PrescriptionDetail;

public class Doctor_Prescribed_Detail extends BaseActivity implements ResponseListener {

    private Toolbar toolBar;
    private TextView Tv_Date, Tv_Patient_Name, Tv_Patient_phone, Tv_Patient_date,
            Tv_Description, Tv_DoctorName, Tv_Doctor_RegNo, Tv_Prescribed_By;
    private ImageView Iv_Sign;
    private String device_id, redId, Id = "", Name = "", ReqType, resSign;
    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_prescribed__detail);

        init();
    }

    private void init() {
        toolBar = (Toolbar) findViewById(R.id.toolbar_doctor_prescribed_detail);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Prescribed Detail");
        Tv_Prescribed_By = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_doctor_name);
        Tv_Date = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_date);
        Tv_Patient_Name = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_patient_name);
        Tv_Patient_phone = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_phone);
        Tv_Patient_date = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_date1);
        Tv_Description = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_desc);
        Tv_DoctorName = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_doctor_degree);
        Tv_Doctor_RegNo = (TextView) findViewById(R.id.textView_doctor_prescribed_detail_doctor_reg_no);
        Iv_Sign = (ImageView) findViewById(R.id.imageView_prescribed_detail_sign);

        Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("id");
        device_id = Utility.getInstance().getDeviceID(this);
        redId = LocalStore.getInstance().getGcmId(this);

        mRequestType = IConstant_WebService.WSR_Doctor_Prescribed_Detail;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.prescription_id = Id;
            RequestManager.Doctor_Prescribed_Detail(this, null, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    private void setSign(String image) {
        MLog.e("SignatureUrl", "" + image);
        Picasso.with(this).load(image).
                into(Iv_Sign, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        Iv_Sign.setVisibility(View.GONE);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_Doctor_Prescribed_Detail) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                String responseSuccess = req.status;
                String msg = req.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("msg", "" + msg);
                if (responseSuccess != null) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setData(req.prescription_details);
                    }else if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }
        }
    }

    private void setData(PrescriptionDetail prescription_details) {
        try {
            Tv_Prescribed_By.setText("Prescribed By,");
            Tv_Date.setText(prescription_details.created_at);
            Tv_Description.setText(prescription_details.prescription);
            Tv_Patient_Name.setText(""+prescription_details.user.firstname+" "+
                    prescription_details.user.lastname);
            Tv_Patient_date.setText(""+prescription_details.user.dob);
            Tv_Patient_phone.setText(prescription_details.user.mobile);
            Tv_DoctorName.setText(prescription_details.doctor.firstname + ", " +
                    prescription_details.doctor.degree);
            Tv_Doctor_RegNo.setText(prescription_details.doctor.registration_licence);
            resSign = prescription_details.doctor.signature;
            setSign(IConstant_WebService.imageUrl_Signature + resSign);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(Doctor_Prescribed_Detail.this, e.toString(), true);
        }
    }
}
