package app.mediczy_com.delivery_form;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.utility.CountryDetails;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.DeliveryFormReq;
import app.mediczy_com.webservicemodel.response.CommonResponse;

public class DeliveryFormActivity extends BaseActivity implements ResponseListener {

    Context context;
    private Animation animationShake;
    private Button submitBtn;
    private TextView tvDate_time;
    private Spinner spState, spCoutry;
    private EditText edCity, edName, edAddress, edPhone, edZip;
    private ProgressDialog pDialog = null;
    private Calendar date;
    private DeliveryFormReq deliveryFormReq;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mhour;
    private int mminute;
    static final int TIME_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID = 0;
    private int mRequestType = 0;
    private String str_country_code, str_CountryName, str_state, strDateTime="", type, adsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_form);
        context = DeliveryFormActivity.this;
        setToolBar();
        init();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        tvDate_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        spCoutry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_CountryName = parent.getItemAtPosition(position).toString();
                str_country_code = CountryDetails.code[position].toString();
                MLog.e("str_CountryName:", "" + str_CountryName);
                MLog.e("str_country_code:", "" + str_country_code);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_state = parent.getItemAtPosition(position).toString();
                MLog.e("str_state:", "" + str_state);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar_delivery_from);
        setSupportActionBar(toolBar);
 //       getSupportActionBar().setTitle("Delivery Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            type = bundle.getString("type");
            adsId = bundle.getString("id");
        }
        MLog.e("type, adsId", "" + type+" , "+adsId);

        if (type.equalsIgnoreCase("enquiry")) {
            getSupportActionBar().setTitle("Enquiry");
        }else if(type.equalsIgnoreCase("sample")) {
            getSupportActionBar().setTitle("Get Sample");
        }
    }

    private void init() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mhour = c.get(Calendar.HOUR_OF_DAY);
        mminute = c.get(Calendar.MINUTE);

        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        submitBtn = (Button) findViewById(R.id.submit_del_from);
        tvDate_time = (TextView) findViewById(R.id.textView_del_from_date_time_value);
        spState = (Spinner) findViewById(R.id.spinner_del_from_state);
        spCoutry = (Spinner) findViewById(R.id.spinner_del_from_country);
        edCity = (EditText) findViewById(R.id.editText_del_from_city);
        edName = (EditText) findViewById(R.id.editText_del_from_name);
        edAddress = (EditText) findViewById(R.id.editText_del_from_address);
        edPhone = (EditText) findViewById(R.id.editText_del_from_phone);
        edZip = (EditText) findViewById(R.id.editText_del_from_zip);

        ArrayAdapter<String> dataAdapterState = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.state_array));
        dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(dataAdapterState);

        ArrayAdapter<String> dataAdapterCountry = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                CountryDetails.country);
        dataAdapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCoutry.setAdapter(dataAdapterCountry);
    }

    private void validate() {
        String strCity = edCity.getText().toString();
        String strName =  edName.getText().toString();
        String strAddress = edAddress.getText().toString();
        String strPhone = edPhone.getText().toString();
        String strZip = edZip.getText().toString();

        if (strName.equals("")) {
            edName.startAnimation(animationShake);
            edName.setError("Enter Name");
        }else if(strCity.equals("")) {
            edCity.startAnimation(animationShake);
            edCity.setError("Enter City");
        }else if(strAddress.equals("")) {
            edAddress.startAnimation(animationShake);
            edAddress.setError("Enter Address");
        }else if(strPhone.equals("")) {
            edPhone.startAnimation(animationShake);
            edPhone.setError("Enter Phone Number");
        }else if(strZip.equals("")) {
            edZip.startAnimation(animationShake);
            edZip.setError("Enter ZipCode");
        }else if(strDateTime.equals("")) {
            Utility.getInstance().showToast("Date and Time is not selected", context);
        }else {
            mRequestType = IConstant_WebService.WSR_DeliveryFromReq;
            onRequest();
        }
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            String strCity = edCity.getText().toString();
            String strName =  edName.getText().toString();
            String strAddress = edAddress.getText().toString();
            String strPhone = edPhone.getText().toString();
            String strZip = edZip.getText().toString();

            deliveryFormReq = new DeliveryFormReq();
            deliveryFormReq.Name = strName;
            deliveryFormReq.Address = strAddress;
            deliveryFormReq.Phone = strPhone;
            deliveryFormReq.Zip = strZip;
            deliveryFormReq.City = strCity;
            deliveryFormReq.type = type;
            deliveryFormReq.state = str_state;
            deliveryFormReq.country = str_CountryName;
            deliveryFormReq.date = strDateTime;
            deliveryFormReq.time = strDateTime;
            deliveryFormReq.adsId = adsId;
            RequestManager.DeliveryFormRequest(this, null, deliveryFormReq, mRequestType);
        }else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        MLog.e("responseObj", "" + responseObj);
        if (requestType == IConstant_WebService.WSR_DeliveryFromReq) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                String responseSuccess = req.status;
                String msg = req.msg;
                if (responseSuccess != null) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setData(req);
                    } else if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    } else {
                        AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                    }
                } else {
                    AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                }
            }
        }
    }

    private void setData(CommonResponse req) {
        try {
            Utility.getInstance().showToast(req.msg.toString(), DeliveryFormActivity.this);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
 //                     The chosen one Sun Oct 08 11:04:52 GMT+05:30 2017
                        MLog.e("showDateTimePicker", "The chosen one " + date.getTime());
                        strDateTime = Utility.getInstance().getDateAndTime(date.getTime());
                        tvDate_time.setText(strDateTime);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}
