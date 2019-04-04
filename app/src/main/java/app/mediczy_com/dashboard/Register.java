package app.mediczy_com.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.iid.FirebaseInstanceId;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.Retrofit.ListDetails;
import app.mediczy_com.Retrofit.RetrofitInterface;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.fcm.MyFirebaseMessagingService;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.payment.PaymentFreeCodeObserver;
import app.mediczy_com.payment.PaymentByDoctor;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CountryDetails;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RegisterRequest;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Prithivi Raj on 26-11-2015.
 */
public class Register extends AppCompatActivity implements View.OnClickListener,
        PaymentFreeCodeObserver, ResponseListener {

    public static final String ACTION_CLOSE = "app.mediczy.ACTION_CLOSE";
    public static Register register;
    private FirstReceiver firstReceiver;
    private Animation animationShake, animAlpha2;
    private CardView cardView;
    private Spinner Sp_Country, Sp_Gender, Sp_Month, Sp_Day, Sp_Year, Sp_CountryCode;
    private Button Btn_Register, Btn_Patent, Btn_Doctor, Btn_DoctorLogin;
    private TextView Tv_CountryCode, Tv_Radio_Patient, Tv_Radio_Doctor, Tv_Terms;
    private EditText Ed_FirstName, Ed_LastName, Ed_Number, Ed_Doc_UserName, Ed_Doc_Psd, Freecode, Ed_Ref_code;
    private TextInputLayout Ed_inputLayoutPost_Ref_code;
    private RelativeLayout Rl_Doctor, Rl_Patient, Rl_Doctorpayment, Rl_Paynow, Rl_Freecode, viewRegisterBG,
            viewRegisterPatinet, viewRegisterDoctor, viewRegisterDoctorLogin;
    private CheckBox ChBox_Terms, ChBox_Ref_code;
    private RadioButton Rb_Patient, Rb_Doctor;
    private String responseStatus="", responseUserID, responseOtp, responsePhoneNumber, responseFirstName, responseFreeCode,
            responseBalance="", responseProfileImage="", str_country_code="", responseLastName, responseGender, responseDob,
            responseCountryCode, responseType, responseMsg, responseRefId="", health_card_avail="", responsePending_amount;
    private static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    static final String TAG = "Register Activity";
    private String regId="", str_country, str_gender, str_month, str_day,
            str_year, str_Type, device_id, resFreecode;

    ProgressDialog pDialog = null;
    RegisterRequest requestCommon;
    private int mRequestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        register = this;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        Log.i(TAG, "hash -> "+ appSignatureHashHelper.getAppSignatures().get(0));

        sendHashtoServer(appSignatureHashHelper.getAppSignatures().get(0));


        init();

        Sp_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_country = parent.getItemAtPosition(position).toString();
                str_country_code = CountryDetails.code[position].toString();
                MLog.e("str_CountryName:", "" + str_country);
                MLog.e("str_country_code:", "" + str_country_code);
//                ShowToast(str_country);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Sp_Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_gender = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Sp_Month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_month = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Sp_Day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_day = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Sp_Year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_year = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Sp_CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str_CountryName = parent.getItemAtPosition(position).toString();
                str_country_code = CountryDetails.code[position].toString();
                MLog.e("str_CountryName:", "" + str_CountryName);
                MLog.e("str_country_code:", "" + str_country_code);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void sendHashtoServer(String s) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<ListDetails> call = retrofitInterface.hashKeyApi(s);

        call.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null)
                {
                    Log.i(TAG, "hash status -> "+response.body().status);
                    Log.i(TAG, "hash message -> "+response.body().msg);
                }
                else {
                    Log.e(TAG, "hash status -> error");
                    Log.e(TAG, "hash message -> null");
                }

            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Log.e(TAG, "hash onFailure -> "+t);

            }
        });
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        animAlpha2 = AnimationUtils.loadAnimation(this, R.anim.on_click);
        cardView = (CardView)findViewById(R.id.card_relative_layout_register);
        viewRegisterBG = (RelativeLayout)findViewById(R.id.register_login_bg);
        viewRegisterPatinet = (RelativeLayout)findViewById(R.id.register_patient_layout);
        viewRegisterDoctor = (RelativeLayout)findViewById(R.id.register_doctor_layout);
        viewRegisterDoctorLogin = (RelativeLayout)findViewById(R.id.register_doctor_login_layout);

        Btn_Patent = (Button) viewRegisterBG.findViewById(R.id.button_register_patent);
        Btn_Doctor = (Button) viewRegisterBG.findViewById(R.id.button_register_doctor);
        Sp_Country = (Spinner)viewRegisterPatinet.findViewById(R.id.spinner_register_country_name);
        Sp_Gender = (Spinner)viewRegisterPatinet.findViewById(R.id.spinner_register_gender);
        Sp_Month = (Spinner)viewRegisterPatinet.findViewById(R.id.spinner_register_dob_month);
        Sp_Day = (Spinner)viewRegisterPatinet.findViewById(R.id.spinner_register_dob_day);
        Sp_Year = (Spinner)viewRegisterPatinet.findViewById(R.id.spinner_register_dob_year);
        Sp_CountryCode = (Spinner)viewRegisterPatinet.findViewById(R.id.spinner_register_country_code);
        Ed_FirstName = (EditText)viewRegisterPatinet.findViewById(R.id.editText_register_first_name);
        Ed_LastName = (EditText)viewRegisterPatinet.findViewById(R.id.editText_register_last_name);
        Ed_Number = (EditText)viewRegisterPatinet.findViewById(R.id.editText_register_mobile_number);
        Btn_Register = (Button)viewRegisterPatinet.findViewById(R.id.button_register_continue);
        Btn_DoctorLogin = (Button)viewRegisterDoctorLogin.findViewById(R.id.button_register_doctor_login);

        Tv_CountryCode = (TextView)viewRegisterPatinet.findViewById(R.id.textView_register_country_code);
        ChBox_Terms = (CheckBox)viewRegisterPatinet.findViewById(R.id.checkBox_register_terms);
        ChBox_Ref_code = (CheckBox)viewRegisterPatinet.findViewById(R.id.checkBox_register_ref_code);
        TextView refCode = (TextView)viewRegisterPatinet.findViewById(R.id.textView_register_ref_code);
        Ed_Ref_code = (EditText)viewRegisterPatinet.findViewById(R.id.editText_register__ref_code1);
        Ed_inputLayoutPost_Ref_code = (TextInputLayout) findViewById(R.id.editText_register_ref_code);

        Rb_Patient = (RadioButton)findViewById(R.id.radioButton_register);
        Rb_Doctor = (RadioButton)findViewById(R.id.radioButton_register_doctor);
        Tv_Radio_Patient = (TextView)findViewById(R.id.textView_register_patient);
        Tv_Radio_Doctor = (TextView)findViewById(R.id.textView_register_doctor);
        Ed_Doc_UserName = (EditText)viewRegisterDoctorLogin.findViewById(R.id.editText_register_doctor_unsernme);
        Ed_Doc_Psd = (EditText)viewRegisterDoctorLogin.findViewById(R.id.editText_register_doctor_psd);
        Rl_Doctor = (RelativeLayout)viewRegisterDoctorLogin.findViewById(R.id.relative_register_first_doctor);
        Rl_Patient = (RelativeLayout)viewRegisterPatinet.findViewById(R.id.relative_register_first_al);
        Tv_Terms = (TextView)viewRegisterPatinet.findViewById(R.id.textView_register_terms_condition);
        RelativeLayout Rl_Ref = (RelativeLayout)viewRegisterPatinet.findViewById(R.id.relative_register_ref_code);
        Rl_Ref.setVisibility(View.GONE);
        Rl_Doctorpayment = (RelativeLayout) viewRegisterDoctor.findViewById(R.id.doctor_payment);
        Rl_Paynow = (RelativeLayout) viewRegisterDoctor.findViewById(R.id.button_vd_pay);
        Rl_Freecode = (RelativeLayout) viewRegisterDoctor.findViewById(R.id.button_coupon);
        Freecode = (EditText) viewRegisterDoctor.findViewById(R.id.editText_view_detail_coupon_code);

        Btn_DoctorLogin.setOnClickListener(this);
        Btn_Patent.setOnClickListener(this);
        Btn_Doctor.setOnClickListener(this);
        Tv_Terms.setOnClickListener(this);
        ChBox_Terms.setOnClickListener(this);
        ChBox_Ref_code.setOnClickListener(this);
        Rb_Patient.setOnClickListener(this);
        Rb_Doctor.setOnClickListener(this);
        Tv_Radio_Patient.setOnClickListener(this);
        Tv_Radio_Doctor.setOnClickListener(this);
        Btn_Register.setOnClickListener(this);
        Rl_Paynow.setOnClickListener(this);
        Rl_Freecode.setOnClickListener(this);

        ChBox_Ref_code.setChecked(false);
        Ed_inputLayoutPost_Ref_code.setVisibility(View.GONE);
        Ed_FirstName.clearFocus();
        Ed_LastName.clearFocus();
        Ed_Number.clearFocus();
        Ed_Ref_code.clearFocus();

//        String[] recourseList = this.getResources().getStringArray(R.array.CountryCodes);
        ArrayAdapter<String> dataAdapter_country = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CountryDetails.country);
        dataAdapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_Country.setAdapter(dataAdapter_country);

        ArrayAdapter<String> dataAdapter_gender = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender_list));
        dataAdapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_Gender.setAdapter(dataAdapter_gender);

        ArrayAdapter<String> dataAdapter_month = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.month_list));
        dataAdapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_Month.setAdapter(dataAdapter_month);

        ArrayAdapter<String> dataAdapter_day = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,  getResources().getStringArray(R.array.day_list));
        dataAdapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_Day.setAdapter(dataAdapter_day);

        ArrayAdapter<String> dataAdapter_year = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.year_list));
        dataAdapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_Year.setAdapter(dataAdapter_year);

        ArrayAdapter<String> dataAdapter_countryCode = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CountryDetails.country);
        dataAdapter_countryCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_CountryCode.setAdapter(dataAdapter_countryCode);

        str_Type = "Patient";
        mRequestType = IConstant_WebService.WSR_RegisterPatient;
        viewRegisterPatinet.setVisibility(View.VISIBLE);
        viewRegisterDoctorLogin.setVisibility(View.GONE);
        Rb_Patient.setChecked(true);
        Rb_Doctor.setChecked(false);

        device_id = Utility.getInstance().getDeviceID(Register.this);
        IntentFilter filter = new IntentFilter(ACTION_CLOSE);
        firstReceiver = new FirstReceiver();
        registerReceiver(firstReceiver, filter);
        String terms = "I agree the Terms and Conditions";
        SpannableString content = new SpannableString(terms);
        content.setSpan(new UnderlineSpan(), 0, terms.length(), 0);
        Tv_Terms.setText(content);
        gcmRegister();
    }

    private String gcmRegister() {
/*        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().checkPlayServices(Register.this);
            if (TextUtils.isEmpty(regId)) {
                regId = registerGCM();
                MLog.e("GCM_RegId:", "" + regId);
            }
        }
        MLog.e("GCM Server->", "" + regId);*/

        if (Utility.getInstance().isConnectingToInternet(this)) {
            startService(new Intent(this, MyFirebaseMessagingService.class));
            try {
                regId = FirebaseInstanceId.getInstance().getToken();
                MLog.e("FireBase id login", "Refreshed token: " + regId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return regId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(firstReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_register_doctor:
                AnimateControl(v);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Doctor";
                        mRequestType = IConstant_WebService.WSR_RegisterDoctor;
                        selectionType();
                    }
                }, 300);
                break;
            case R.id.button_register_patent:
                AnimateControl(v);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Patient";
                        mRequestType = IConstant_WebService.WSR_RegisterPatient;
                        selectionType();
                    }
                }, 300);
                break;
            case R.id.textView_register_patient:
                AnimateControl(v);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Patient";
                        mRequestType = IConstant_WebService.WSR_RegisterPatient;
                        selectionType();
                    }
                }, 300);
                break;
            case R.id.radioButton_register:
                AnimateControl(v);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Patient";
                        mRequestType = IConstant_WebService.WSR_RegisterPatient;
                        selectionType();
                    }
                }, 300);
                break;
            case R.id.textView_register_doctor:
                AnimateControl(v);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Doctor";
                        mRequestType = IConstant_WebService.WSR_RegisterDoctor;
                        selectionType();

                    }
                }, 300);
                break;
            case R.id.radioButton_register_doctor:
                AnimateControl(v);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Doctor";
                        mRequestType = IConstant_WebService.WSR_RegisterDoctor;
                        selectionType();
                    }
                }, 300);
                break;
            case R.id.checkBox_register_terms:
                ChBox_Terms.setError(null);
                break;
            case R.id.checkBox_register_ref_code:
                if (ChBox_Ref_code.isChecked()){
                    Ed_inputLayoutPost_Ref_code.setVisibility(View.VISIBLE);
                }else {
                    Ed_Ref_code.setText("");
                    Ed_Ref_code.setError(null);
                    Ed_inputLayoutPost_Ref_code.setVisibility(View.GONE);
                }
                break;
            case R.id.textView_register_terms_condition:
                custom_dialog_Terms();
                break;
            case R.id.button_vd_pay:
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_Type = "Doctor";
                        mRequestType = IConstant_WebService.WSR_RegisterDoctor;
                        Intent i = new Intent(Register.this, PaymentByDoctor.class);
                        i.putExtra("Id", "100");
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }

                }, 300);
                break;
            case R.id.button_coupon:
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resFreecode=Freecode.getText().toString();
                        if (!resFreecode.equals("")) {
                            str_Type = "FreeCode";
                            mRequestType = IConstant_WebService.WSR_RegisterFreeCode;
                     //       getFreeCode();
                            onRequest();
                        } else {
                            Freecode.setError("Please enter the code");
                            Freecode.startAnimation(animationShake);
                        }
                    }

                }, 300);
                break;
            case R.id.button_register_doctor_login:
                str_Type = "Doctor";
                mRequestType = IConstant_WebService.WSR_RegisterDoctor;
                PatentORDoctorLogin();
                break;
            case R.id.button_register_continue:
                str_Type = "Patient";
                mRequestType = IConstant_WebService.WSR_RegisterPatient;
                PatentORDoctorLogin();
                break;
        }
    }

    private void PatentORDoctorLogin() {
        if (str_Type.equals("Doctor")) {
            String str_psd = Ed_Doc_Psd.getText().toString().trim();
            String str_user = Ed_Doc_UserName.getText().toString().trim();
            if (str_user.equals("")){
                Ed_Doc_UserName.setError("Enter UserName");
                Ed_Doc_UserName.startAnimation(animationShake);
            }else if (str_psd.equals("")){
                Ed_Doc_Psd.startAnimation(animationShake);
                Ed_Doc_Psd.setError("Enter Password");
            }else {
                onRequest();
            }
        }if (str_Type.equals("Patient")) {
            String str_ed_first_name = Ed_FirstName.getText().toString().trim();
            String str_ed_last_name = Ed_LastName.getText().toString().trim();
            String str_ed_number = Ed_Number.getText().toString().trim();
            String str_ed_ref_code = Ed_Ref_code.getText().toString().trim();
            String dob = str_day+"/"+str_month+"/"+str_year;
            MLog.e("dob", "" + dob);
/*            else if (str_ed_last_name.equals("")){
                Ed_LastName.startAnimation(animationShake);
                Ed_LastName.setError("Enter Last Name");
            }*/
            if (str_ed_first_name.equals("")){
                Ed_FirstName.setError("Enter First Name");
                Ed_FirstName.startAnimation(animationShake);
            }else if (str_ed_number.equals("")){
                Ed_Number.startAnimation(animationShake);
                Ed_Number.setError("Enter Number");
            }else if (!ChBox_Terms.isChecked()){
                ChBox_Terms.setError("Accept The Terms & Conditions");
                Tv_Terms.startAnimation(animationShake);
            }else if (ChBox_Ref_code.isChecked() && str_ed_ref_code.equals("")){
                Ed_Ref_code.startAnimation(animationShake);
                Ed_Ref_code.setError("Enter your reference code");
            }else{
                isStoragePermissionGranted();
            }
        }
    }

    private void selectionType() {
        viewRegisterBG.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        if (str_Type.equals("Patient")) {
            Rb_Doctor.setChecked(false);
            Rb_Patient.setChecked(true);
            viewRegisterDoctorLogin.setVisibility(View.GONE);
            Btn_Register.setVisibility(View.VISIBLE);
            viewRegisterPatinet.setVisibility(View.VISIBLE);
            Btn_Register.setText("CONTINUE");
            viewRegisterDoctor.setVisibility(View.GONE);
            Rl_Freecode.setVisibility(View.GONE);
        } if (str_Type.equals("Doctor")){
            Rb_Doctor.setChecked(true);
            Rb_Patient.setChecked(false);
            viewRegisterPatinet.setVisibility(View.GONE);
 //           checkPaymentOK();
            viewRegisterDoctor.setVisibility(View.GONE);
            Rl_Freecode.setVisibility(View.GONE);
            viewRegisterDoctorLogin.setVisibility(View.VISIBLE);
        }
    }

    private void checkPaymentOK() {
        if(LocalStore.getInstance().get_paymentok(Register.this).equals("0")){
            viewRegisterDoctor.setVisibility(View.VISIBLE);
            Rl_Freecode.setVisibility(View.VISIBLE);
            viewRegisterDoctorLogin.setVisibility(View.GONE);
        }else{
            viewRegisterDoctor.setVisibility(View.GONE);
            Rl_Freecode.setVisibility(View.GONE);
            viewRegisterDoctorLogin.setVisibility(View.VISIBLE);
        }
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            String regId = gcmRegister();
            if (regId==null || regId.equals("")) {
                Utility.getInstance().showToast("Google play service error in your device check and try again",
                        Register.this);
            } else {
                Utility.getInstance().showLoading(pDialog, this);
                requestCommon = new RegisterRequest();
                requestCommon.type = str_Type;
                requestCommon.fcm_id = regId;
                requestCommon.device_id = device_id;
                String doctorFreeCode = LocalStore.getInstance().get_DoctorFreeCode(this);
                if (doctorFreeCode.equals("0")) {
                    doctorFreeCode = null;
                }
                if (str_Type.equals("Patient")) {
      //            String str_country_code = Tv_CountryCode.getText().toString().trim();
                    String str_ed_first_name = Ed_FirstName.getText().toString().trim();
                    String str_ed_last_name = Ed_LastName.getText().toString().trim();
                    String str_ed_number = Ed_Number.getText().toString().trim();
                    String str_ed_ref_id = Ed_Ref_code.getText().toString().trim();
                    String dob = str_day+"/"+str_month+"/"+str_year;
                    MLog.e("dob", "" + dob);
                    requestCommon.firstname = str_ed_first_name;
                    requestCommon.lastname = str_ed_last_name;
                    requestCommon.number = str_ed_number;
                    requestCommon.country_code = str_country_code;
                    requestCommon.gender = str_gender;
                    requestCommon.dob = dob;
                    requestCommon.ref_id = str_ed_ref_id;
                    RequestManager.Register(this, null, requestCommon, mRequestType);
                } else if (str_Type.equals("Doctor")) {
                    String str_ed_user_name = Ed_Doc_UserName.getText().toString().trim();
                    String str_ed_psd = Ed_Doc_Psd.getText().toString().trim();
                    requestCommon.username = str_ed_user_name;
                    requestCommon.password = str_ed_psd;
                    requestCommon.free_code = doctorFreeCode;
                    RequestManager.DoctorLogin(this, null, requestCommon, mRequestType);
   //                 setDoctorHardCodedValue();
                }else if (str_Type.equals("FreeCode")) {
                    String strFreeCode = Freecode.getText().toString().trim();
                    requestCommon.free_code = strFreeCode;
                    RequestManager.RegisterFreeCode(this, null, requestCommon, mRequestType);
                }
            }
        } else {
            AlertDialogFinish.Show(Register.this, Constant.Alart_Internet, true);
        }
    }

    private void setDoctorHardCodedValue() {
        LocalStore.getInstance().setDoctorBalance(Register.this, "30");
        LocalStore.getInstance().setType(Register.this, "Doctor");
        LocalStore.getInstance().setUserId(Register.this, "101");
        LocalStore.getInstance().setGcmId(Register.this, regId);
        LocalStore.getInstance().setFirstName(Register.this, "Prithivi");
        LocalStore.getInstance().setPhoneNumber(Register.this, "9790545214");
        LocalStore.getInstance().setDoctorImage(Register.this, "");
        LocalStore.getInstance().setRefId(Register.this, "123");
        Intent i = new Intent(Register.this, HomeNavigation.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onPaymentReceived(boolean paymentStatus) {
        if (paymentStatus) {
            checkPaymentOK();
        }else{
            Utility.getInstance().showToast("Payment failed !!!", Register.this);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        CommonResponse req = (CommonResponse) responseObj;
        if (req != null) {
            setData(req, requestType);
        }else {
            AlertDialogFinish.Show(Register.this, Constant.Alart_Status500, true);
        }
    }

    private void setData(CommonResponse req, int requestType) {
        responseStatus = req.status;
        responseMsg = req.msg;
        responseType = req.type;
        health_card_avail = req.health_card_avail;
        if (requestType == IConstant_WebService.WSR_RegisterPatient) {
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                if (req.patientRegisterResponse!=null) {
                    responseUserID = req.patientRegisterResponse.user_id;
                    responseOtp = req.patientRegisterResponse.otp;
                    responsePhoneNumber = req.patientRegisterResponse.mobile;
                    responseFirstName = req.patientRegisterResponse.firstname;
                    responseLastName = req.patientRegisterResponse.lastname;
                    responseGender = req.patientRegisterResponse.gender;
                    responseDob = req.patientRegisterResponse.dob;
                    responseCountryCode = req.patientRegisterResponse.countrycode;
                    responseRefId = req.patientRegisterResponse.referral_code;
                    responseType ="patient";
                    LocalStore.getInstance().setGcmId(Register.this, regId);
                    LocalStore.getInstance().setPhoneNumber(Register.this, responsePhoneNumber);
                    LocalStore.getInstance().setFirstName(Register.this, responseFirstName);
                    LocalStore.getInstance().setLastName(Register.this, responseLastName);
                    LocalStore.getInstance().setGender(Register.this, responseGender);
                    LocalStore.getInstance().setDob(Register.this, responseDob);
                    LocalStore.getInstance().setCountryCode(Register.this, responseCountryCode);
                    LocalStore.getInstance().setType(Register.this, responseType);
                    LocalStore.getInstance().setRefId(Register.this, responseRefId);
                    LocalStore.getInstance().setHealthCard_Available(Register.this, health_card_avail);

                    if (str_country_code.equalsIgnoreCase("+91")) {
                        Intent i = new Intent(Register.this, Otp_Screen.class);
                        i.putExtra("responseUserID", responseUserID);
                        i.putExtra("responseOtp", responseOtp);
                        i.putExtra("responsePhoneNumber", responsePhoneNumber);
                        i.putExtra("responseType", responseType);
                        i.putExtra("fcm_id", regId);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else {
                        LocalStore.getInstance().setUserId(Register.this, responseUserID);
                        LocalStore.getInstance().setOtp(Register.this, responseOtp);

                        Intent i = new Intent(Register.this, HomeNavigation.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        Utility.getInstance().showToast("Welcome To Mediczy", Register.this);
                    }
                }
            }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                AlertDialogFinish.Show(Register.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(Register.this, Constant.Alart_Status500, true);
            }
        } else if (requestType == IConstant_WebService.WSR_RegisterDoctor) {
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                if (req.doctorRegisterResponse!=null) {
                    responsePending_amount = req.pending_amount;
                    responseUserID = req.doctorRegisterResponse.doctor_id;
                    responseFirstName = req.doctorRegisterResponse.firstname;
                    responsePhoneNumber = req.doctorRegisterResponse.mobile;
                    responseBalance = req.doctorRegisterResponse.price;
                    responseProfileImage = req.doctorRegisterResponse.image_path;
                    responseRefId = req.doctorRegisterResponse.registration_licence;
                    responseType ="doctor";
                    LocalStore.getInstance().setDoctorBalance(Register.this, responsePending_amount);
                    LocalStore.getInstance().setType(Register.this, responseType);
                    LocalStore.getInstance().setUserId(Register.this, responseUserID);
                    LocalStore.getInstance().setGcmId(Register.this, regId);
                    LocalStore.getInstance().setFirstName(Register.this, responseFirstName);
                    LocalStore.getInstance().setPhoneNumber(Register.this, responsePhoneNumber);
                    LocalStore.getInstance().setDoctorImage(Register.this, responseProfileImage);
                    LocalStore.getInstance().setRefId(Register.this, responseRefId);

                    Intent i = new Intent(Register.this, HomeNavigation.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            } else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                AlertDialogFinish.Show(Register.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(Register.this, Constant.Alart_Status500, true);
            }
        }else if (requestType == IConstant_WebService.WSR_RegisterFreeCode) {
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                responseFreeCode = req.freecode;
                LocalStore.getInstance().set_DoctorFreeCode(Register.this,responseFreeCode);
                LocalStore.getInstance().set_paymentok(Register.this,"paid");
                checkPaymentOK();
            } else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                AlertDialogFinish.Show(Register.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(Register.this, Constant.Alart_Status500, true);
            }
        }
    }

    class FirstReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MLog.e("FirstReceiver", "FirstReceiver");
            if (intent.getAction().equals(ACTION_CLOSE)) {
                Register.this.finish();
            }
        }
    }

    private void custom_dialog_Terms() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_terms_condition);
        TextView tvHeading = (TextView) dialog.findViewById(R.id.textView_terms_value);
        dialog.show();
    }


    public boolean isStoragePermissionGranted() {
            onRequest();
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            onRequest();
        }
    }

    private void AnimateControl(View v) {
        v.startAnimation(animAlpha2);
        //      viewRegisterBG.startAnimation(animAlpha2);
        //      cardView.startAnimation(animAlpha2);
        Ed_FirstName.startAnimation(animAlpha2);
        Ed_Doc_UserName.startAnimation(animAlpha2);
        Ed_Doc_Psd.startAnimation(animAlpha2);
        Tv_CountryCode.startAnimation(animAlpha2);
        Ed_FirstName.startAnimation(animAlpha2);
        Ed_LastName.startAnimation(animAlpha2);
        Ed_Number.startAnimation(animAlpha2);
        Ed_Ref_code.startAnimation(animAlpha2);
        Sp_Country.startAnimation(animAlpha2);
        Sp_Day.startAnimation(animAlpha2);
        Sp_Gender.startAnimation(animAlpha2);
        Sp_Month.startAnimation(animAlpha2);
        Sp_Year.startAnimation(animAlpha2);
        Sp_CountryCode.startAnimation(animAlpha2);
        ChBox_Terms.startAnimation(animAlpha2);

        Rl_Paynow.startAnimation(animAlpha2);
        Rl_Freecode.startAnimation(animAlpha2);
        Freecode.startAnimation(animAlpha2);
    }
}
