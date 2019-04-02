package app.mediczy_com.filter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import app.mediczy_com.BaseActivity;
import app.mediczy_com.DoctorsList;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.FilterSearchRequest;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.FilterDetail;
import app.mediczy_com.webservicemodel.response.FilterResponse;

public class FilterActivity extends BaseActivity implements ResponseListener {

    private Spinner sp_Location, sp_Language, sp_Education,
            sp_DocFees, sp_Speciality, sp_AppAvailable, sp_Exp;
    private Button Btn_Search;

    private String strLocation, strLanguage, strEducation, strDoctorFees,
            strSpeciality, strAppAvailable, strExperience, Id, Name, latitude="", longitude="";

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    FilterSearchRequest filterSearchRequest;
    private int mRequestType = 0;
    FilterRefreshDoctorListObserver refreshDoctorListObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        refreshDoctorListObserver = (FilterRefreshDoctorListObserver) DoctorsList.doctorsList;
        setToolbar();
        init();

        sp_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strLocation = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_Language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strLanguage = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_Education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strEducation = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_DocFees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDoctorFees = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_Speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSpeciality = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_AppAvailable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strAppAvailable = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_Exp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strExperience = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestType = IConstant_WebService.WSR_Filter_Post;
                onRequest();
            }
        });
    }

    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar_filter);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Filter Your Doctors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
        sp_Location = (Spinner) findViewById(R.id.spinner_location_filter);
        sp_Language = (Spinner) findViewById(R.id.spinner_language_filter);
        sp_Education = (Spinner) findViewById(R.id.spinner_education_filter);
        sp_DocFees = (Spinner) findViewById(R.id.spinner_doc_fees_filter);
        sp_Speciality = (Spinner) findViewById(R.id.spinner_speciality_filter);
        sp_AppAvailable = (Spinner) findViewById(R.id.spinner_app_available_filter);
        sp_Exp = (Spinner) findViewById(R.id.spinner_exp_filter);
        Btn_Search = (Button) findViewById(R.id.button_filter_search);

        Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("id");
        Name = bundle.getString("name");
        latitude = bundle.getString("lat");
        longitude = bundle.getString("long");

 //       setSpinnerDetail1();

        mRequestType = IConstant_WebService.WSR_Filter_Request;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            if (mRequestType == IConstant_WebService.WSR_Filter_Request) {
                requestCommon = new RequestCommon();
                requestCommon.catergory_id = Id;
                RequestManager.Filter(this, null, requestCommon, mRequestType);
            } else if (mRequestType == IConstant_WebService.WSR_Filter_Post) {
                filterSearchRequest = new FilterSearchRequest();
                filterSearchRequest.location = strLocation;
                filterSearchRequest.language = strLanguage;
                filterSearchRequest.appointment = strAppAvailable;
                filterSearchRequest.docFees = strDoctorFees;
                filterSearchRequest.education = strEducation;
                filterSearchRequest.speciality = strSpeciality;
                filterSearchRequest.experience = strExperience;
                filterSearchRequest.latitude = latitude;
                filterSearchRequest.longitude = longitude;
                RequestManager.FilterSearch(this, null, filterSearchRequest, mRequestType);
            }
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_Filter_Request) {
            if (responseObj != null) {
                setData(requestType, responseObj);
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        } else if (requestType == IConstant_WebService.WSR_Filter_Post) {
            if (responseObj != null) {
                setData(requestType, responseObj);
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(int requestType, Object responseObj) {
        String msg = "";
        if (requestType == IConstant_WebService.WSR_Filter_Request) {
            FilterResponse res = (FilterResponse) responseObj;
            if (res != null) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    setSpinnerDetail(res);
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    msg = res.msg;
                    AlertDialogFinish.Show(this, msg, true);
                }
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        }else if (requestType == IConstant_WebService.WSR_Filter_Post) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                MLog.e("res", "" + res.toString());
                if (res.status != null) {
                    if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        if (res.doctorsList != null) {
              //              String filter_id = detail.filter_id;
                            finish();
                            refreshDoctorListObserver.onRefreshReceived(res.doctorsList, "", strSpeciality);
                        }else {
                            AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                        }
                    }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        msg = res.msg;
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        }
    }

    private void setSpinnerDetail(FilterResponse detail) {
        if (detail.language!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.language.size(); i++) {
                list.add(detail.language.get(i).language);
            }
            ArrayAdapter<String> dataAdapter_Location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter_Location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_Language.setAdapter(dataAdapter_Location);
        }
        if (detail.location!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.location.size(); i++) {
                list.add(detail.location.get(i).city);
            }
            ArrayAdapter<String> dataAdapter_Language = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
            dataAdapter_Language.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_Location.setAdapter(dataAdapter_Language);
        }
        if (detail.education!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.education.size(); i++) {
                list.add(detail.education.get(i).education);
            }
            ArrayAdapter<String> dataAdapter_Education = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter_Education.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_Education.setAdapter(dataAdapter_Education);
        }
        if (detail.doctorfees!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.doctorfees.size(); i++) {
                list.add(detail.doctorfees.get(i).fee);
            }
            ArrayAdapter<String> dataAdapter_DocFees = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter_DocFees.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_DocFees.setAdapter(dataAdapter_DocFees);
        }
        if (detail.categories!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.categories.size(); i++) {
                list.add(detail.categories.get(i).category);
            }
            ArrayAdapter<String> dataAdapter_Speciality = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter_Speciality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_Speciality.setAdapter(dataAdapter_Speciality);
        }
/*        if (detail.appointment!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.appointment.size(); i++) {
                list.add(detail.appointment.get(i).appointment);
            }
            ArrayAdapter<String> dataAdapter_AppAvailable = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    list);
            dataAdapter_AppAvailable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_AppAvailable.setAdapter(dataAdapter_AppAvailable);
        }*/
        if (detail.experience!=null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i =0; i<detail.experience.size(); i++) {
                list.add(detail.experience.get(i).experience);
            }
            ArrayAdapter<String> dataAdapter_Exp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter_Exp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_Exp.setAdapter(dataAdapter_Exp);
        }
    }
}
