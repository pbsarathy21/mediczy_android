package app.mediczy_com.patient_problem_form;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.TouchImageView;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.PatientFromDetail_Req;
import app.mediczy_com.webservicemodel.response.Appointment_Details;
import app.mediczy_com.webservicemodel.response.CommonResponse;

public class PatientFormDetail extends BaseActivity implements ResponseListener {

    private Toolbar toolBar;
    TextView tv_patient_name, tv_patient_mobile_number, tv_catergory, tv_appointment_for, tv_issue, tv_gender,
            tv_age, tv_height, tv_weight, tv_city, tv_previous_diagnosed, tv_medications, tv_allergies,
            tv_transaction_status, tv_doctor_reply;
    private ImageView reportImage;
    private RelativeLayout rlImage;
    ProgressDialog pDialog = null;
    PatientFromDetail_Req patientFromDetailReq;
    private int mRequestType = 0;
    String doctor_phone_number, appointment_id, doctor_id;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form_detail);
        imageLoader=new ImageLoader(PatientFormDetail.this);
        init();
        reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) reportImage.getDrawable();
                    if (drawable != null) {
                        Bitmap bitmap = drawable.getBitmap();
                        showImage(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {
        toolBar = (Toolbar) findViewById(R.id.toolbar_patient_form_detail);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Patient Form Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tv_patient_name = (TextView) findViewById(R.id.tv_form_detail_patient_name_value);
        tv_patient_mobile_number = (TextView) findViewById(R.id.tv_form_detail_patient_mobile_number_value);
        tv_catergory = (TextView) findViewById(R.id.tv_form_detail_tv_form_detail_catergory_value);
        tv_appointment_for = (TextView) findViewById(R.id.tv_form_detail_tv_appointment_for_value);
        tv_issue = (TextView) findViewById(R.id.tv_form_detail_issue_value);
        tv_gender = (TextView) findViewById(R.id.tv_form_detail_gender_value);
        tv_age = (TextView) findViewById(R.id.tv_form_detail_age_value);
        tv_height = (TextView) findViewById(R.id.tv_form_detail_height_value);
        tv_weight = (TextView) findViewById(R.id.tv_form_detail_weight_value);
        tv_city = (TextView) findViewById(R.id.tv_form_detail_city_value);
        tv_previous_diagnosed = (TextView) findViewById(R.id.tv_form_detail_previous_diagnosed_value);
        tv_medications = (TextView) findViewById(R.id.tv_form_detail_medications_value);
        tv_allergies = (TextView) findViewById(R.id.tv_form_detail_allergies_value);
        tv_transaction_status = (TextView) findViewById(R.id.tv_form_detail_transaction_status_value);
        tv_doctor_reply = (TextView) findViewById(R.id.tv_form_detail_doctor_reply_value);
        reportImage = (ImageView) findViewById(R.id.tv_form_detail_image_value);
        rlImage = (RelativeLayout) findViewById(R.id.rl_form_detail_image);
        rlImage.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        appointment_id = bundle.getString("appointment_id");
        doctor_id = LocalStore.getInstance().getUserID(this);
        doctor_phone_number = LocalStore.getInstance().getPhoneNumber(this);

        mRequestType = IConstant_WebService.WSR_PatientFormDetail;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            patientFromDetailReq = new PatientFromDetail_Req();
            patientFromDetailReq.appointment_id = appointment_id;
            RequestManager.PatientRequest(this, null, patientFromDetailReq, mRequestType);
        }else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_PatientFormDetail) {
            CommonResponse patientFromDetailResponse = (CommonResponse) responseObj;
            if (patientFromDetailResponse!=null) {
                MLog.e("patientFromDetailResponse :", "" + responseObj);
                String responseSuccess = patientFromDetailResponse.status;
                String msg = patientFromDetailResponse.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("msg", "" + msg);
                if (responseSuccess!=null){
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setData(patientFromDetailResponse.appointment_details);
                    }if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }
        }
    }

    private void setData(Appointment_Details extndRes) {
        try {
            tv_patient_name.setText(extndRes.patient_name);
            tv_patient_mobile_number.setText(extndRes.patient_mobile_number);
            tv_catergory.setText(extndRes.catergory);
            tv_appointment_for.setText(extndRes.appointment_for);
            tv_issue.setText(extndRes.issue);
            tv_gender.setText(extndRes.gender);
            tv_age.setText(extndRes.age);
            tv_height.setText(extndRes.height);
            tv_weight.setText(extndRes.weight);
            tv_city.setText(extndRes.city);
            tv_previous_diagnosed.setText(extndRes.previous_diagnosed);
            tv_medications.setText(extndRes.medications);
            tv_allergies.setText(extndRes.allergies);
            tv_transaction_status.setText(extndRes.transaction_status);
            tv_doctor_reply.setText(extndRes.doctor_reply);
            tv_patient_name.setText(extndRes.patient_name);
            MLog.e("Image->", "" + extndRes.image);
            if (extndRes.image!=null) {
                imageLoader.DisplayImage(extndRes.image, reportImage, 4);
                rlImage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    public void showImage(Bitmap bitmap) {
        int width = 20;
        int height = 40;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog.dismiss();
            }
        });
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        screenWidth = screenWidth - width;
        screenHeight = screenHeight - height;
        MLog.d("ScreenResoluction->",+ screenWidth+","+screenHeight);
        final TouchImageView imageView = new TouchImageView(this);
        imageView.setImageBitmap(bitmap);
//	    builder.addContentView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.addContentView(imageView, new RelativeLayout.LayoutParams(screenWidth, screenHeight));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
            }
        });
        dialog.show();
    }
}
