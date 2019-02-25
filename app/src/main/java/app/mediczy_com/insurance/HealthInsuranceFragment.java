package app.mediczy_com.insurance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.adapter.Insurance_Adapter;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.settings_help.Help_Activity;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi on 01-10-2017.
 */

public class HealthInsuranceFragment extends Fragment implements View.OnClickListener,
        ResponseListener {

    private Context context;
    private Animation animationShake;
    private Spinner sp_InsuranceCompany, sp_Adult, sp_Child, sp_City;
    private Button btnSubmit, btnInsuranceFaq;
    private TextView tvDateStart, tvDateEnd, sp_Dependants;
    private EditText Ed_Policy_Number, Ed_Sum_Assured, Ed_Employer_Name, Ed_ID_Proof;
    private RelativeLayout rlFormView, rlListView;
    private CardView cardView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    Insurance_Adapter insurance_adapter;

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;
    public String ReqType = "", startDate="", endDate="", str_InsuranceCompany,
            str_Adult, str_Child, str_Dependants, str_City;
    String latitude="", longitude="";
    private int insuranceCompPosition, adultPosition;
    private int childPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_insurance, container, false);
        context = getActivity();
        init(rootView);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateField();
            }
        });

        btnInsuranceFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Help_Activity.class);
         //       Intent i = new Intent(context, DeliveryFormActivity.class);
                i.putExtra(Constant.isFrom, "HealthInsuranceFragment");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        sp_InsuranceCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_InsuranceCompany = parent.getItemAtPosition(position).toString();
                insuranceCompPosition = position;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_Adult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_Adult = parent.getItemAtPosition(position).toString();
                adultPosition = position;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_Child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_Child = parent.getItemAtPosition(position).toString();
                childPosition = position;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/*        sp_Dependants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_Dependants = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        sp_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_City = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    private void validateField() {
        if (str_Adult.equalsIgnoreCase("Adult")) {
            str_Adult ="";
        } if (str_Child.equalsIgnoreCase("Child")) {
            str_Child = "";
        }
        if (Ed_Policy_Number.getText().toString().equals("")) {
            Ed_Policy_Number.startAnimation(animationShake);
            Ed_Policy_Number.setError("Invalid Policy Number");
        }else if(Ed_Sum_Assured.getText().toString().equals("")) {
            Ed_Policy_Number.startAnimation(animationShake);
            Ed_Policy_Number.setError("Invalid Sum Assured");
        }else if (Ed_Employer_Name.getText().toString().equals("")) {
            Ed_Policy_Number.startAnimation(animationShake);
            Ed_Policy_Number.setError("Invalid Employer Name");
        } else {
            mRequestType = IConstant_WebService.WSR_InsuranceSubmitReq;
            ReqType="InsuranceSubmit";
            onRequest(ReqType);
        }
    }

    private void init(View rootView) {
        animationShake = AnimationUtils.loadAnimation(context, R.anim.shake);
        rlFormView = (RelativeLayout)rootView.findViewById(R.id.insurance_from_view);
        rlListView = (RelativeLayout)rootView.findViewById(R.id.insurance_list_view);
        cardView = (CardView)rootView.findViewById(R.id.insurance_card_list);
        sp_City = (Spinner) rlListView.findViewById(R.id.spinner_insurance_city);
        sp_InsuranceCompany = (Spinner) rlFormView.findViewById(R.id.spinner_insurance_comp);
        btnSubmit = (Button) rlFormView.findViewById(R.id.submit_insurance);
        btnInsuranceFaq = (Button) rlFormView.findViewById(R.id.insurance_faq);
        tvDateStart = (TextView) rlFormView.findViewById(R.id.tv_date_start);
        tvDateEnd = (TextView) rlFormView.findViewById(R.id.tv_date_end);
        sp_Dependants = (TextView) rlFormView.findViewById(R.id.tv_dept);
        sp_Adult = (Spinner) rlFormView.findViewById(R.id.textView_inc_adult);
        sp_Child = (Spinner) rlFormView.findViewById(R.id.tv_child);
        Ed_Policy_Number = (EditText) rlFormView.findViewById(R.id.editText_policy_num);
        Ed_Sum_Assured = (EditText) rlFormView.findViewById(R.id.editText_sum_assured);
        Ed_Employer_Name = (EditText) rlFormView.findViewById(R.id.editText_emp_name);
        Ed_ID_Proof = (EditText) rlFormView.findViewById(R.id.editText_emp_id);

        mRecyclerView = (RecyclerView) rlListView.findViewById(R.id.recycler_view_insurance_list);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        tvDateStart.setOnClickListener(this);
        tvDateEnd.setOnClickListener(this);

        ArrayAdapter<String> dataAdapter_city = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.city_array));
        dataAdapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_City.setAdapter(dataAdapter_city);

        ArrayAdapter<String> dataAdapter_adult = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.adult_list));
        dataAdapter_adult.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Adult.setAdapter(dataAdapter_adult);

        ArrayAdapter<String> dataAdapter_child = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.child_list));
        dataAdapter_child.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Child.setAdapter(dataAdapter_child);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            latitude = bundle.getString("lat");
            longitude = bundle.getString("long");
        }

        String latLongString = LocalStore.getInstance().getLatitude_Longitude(context);
        MLog.e("latLong :", "" + latLongString);
        String[] LatList = latLongString.split(",");
        latitude = LatList[0];
        longitude = LatList[1];

        setLocalData();
        String isInsuranceSubmitted = LocalStore.getInstance().isInsuranceFormSubmitted(context);
        MLog.e("isInsuranceSubmitted :", "" + isInsuranceSubmitted);
        if (isInsuranceSubmitted.equalsIgnoreCase("Yes")) {
            rlListView.setVisibility(View.VISIBLE);
            rlFormView.setVisibility(View.GONE);
            showInsuranceList();
        } else {
            rlListView.setVisibility(View.GONE);
            rlFormView.setVisibility(View.VISIBLE);
            mRequestType = IConstant_WebService.WSR_InsuranceCompanyReq;
            ReqType="InsuranceCompany";
            onRequest(ReqType);
        }
//        showFrame();
    }

    private void setLocalData() {
        Gson gson = new Gson();
        String json = LocalStore.getInstance().getInsuranceSubmitted(context);
        if (json!=null && !json.equals("0")) {
            RequestCommon requestCommon = gson.fromJson(json, RequestCommon.class);
            if (requestCommon!=null) {
                Ed_Policy_Number.setText(requestCommon.Policy_Number);
                Ed_Sum_Assured.setText(requestCommon.Sum_Assured);
                tvDateStart.setText(requestCommon.start_date);
                tvDateEnd.setText(requestCommon.end_date);
                Ed_Employer_Name.setText(requestCommon.Employer_Name);
                Ed_ID_Proof.setText(requestCommon.ID_Proof);
                sp_Adult.setSelection(requestCommon.adultPosition);
                sp_Child.setSelection(requestCommon.childPosition);
                sp_InsuranceCompany.setSelection(requestCommon.insuranceCompPosition);
                startDate = requestCommon.start_date;
                endDate= requestCommon.end_date;
                str_InsuranceCompany = requestCommon.insurance_company;
            }
        }
    }

    private void onRequest(String reqType) {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            if (reqType.equals("InsuranceCompany")) {
                RequestManager.InsuranceCompany(context, this, requestCommon, mRequestType);
            }if (reqType.equals("network_partners")) {
                requestCommon.latitude = latitude;
                requestCommon.longitude = longitude;
                requestCommon.tpa_name = str_InsuranceCompany;
                RequestManager.NetworkPartners(context, this, requestCommon, mRequestType);
            }if (reqType.equals("InsuranceSubmit")) {
                requestCommon.insuranceCompPosition = insuranceCompPosition;
                requestCommon.childPosition = childPosition;
                requestCommon.adultPosition = adultPosition;

                requestCommon.insurance_company = str_InsuranceCompany;
                requestCommon.Policy_Number = Ed_Policy_Number.getText().toString();
                requestCommon.Sum_Assured = Ed_Sum_Assured.getText().toString();
                requestCommon.start_date = startDate;
                requestCommon.end_date = endDate;
                requestCommon.dependants = str_Dependants;
                requestCommon.child = str_Child;
                requestCommon.adult = str_Adult;
                requestCommon.Employer_Name = Ed_Employer_Name.getText().toString();
                requestCommon.ID_Proof = Ed_ID_Proof.getText().toString();
                RequestManager.SubmitInsurance(context, this, requestCommon, mRequestType);
            }
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        CommonResponse res = (CommonResponse) responseObj;
        MLog.e("CommonResponse :", "" + res);
        if (res != null) {
            setData(res, requestType);
        }else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
        }
    }

    private void setData(CommonResponse res, int requestType) {
        try {
            if (requestType == IConstant_WebService.WSR_InsuranceCompanyReq) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.insuranceCompanyList != null) {
                        if (res.insuranceCompanyList.size() >0) {
                            ArrayList<String> arrayInsuranceComp = new ArrayList<>();
                            for (int i = 0; i < res.insuranceCompanyList.size(); i++) {
                                InsuranceCompanyModel companyModel = new InsuranceCompanyModel();
                                companyModel.setName(res.insuranceCompanyList.get(i).name);
                                arrayInsuranceComp.add(res.insuranceCompanyList.get(i).name);
                            }
                            ArrayAdapter<String> dataAdapter_gender = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayInsuranceComp);
                            dataAdapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_InsuranceCompany.setAdapter(dataAdapter_gender);
                            setLocalData();
                        }
                        else {
                            Utility.getInstance().showToast("Insurance Company List is Empty", context);
                        }
                    } else {
                        AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                    }
                }else if(res.status.equalsIgnoreCase(Constant.Response_Status_Error)){
                    AlertDialogFinish.Show((Activity) context, res.msg, true);
                }

            }else if (requestType == IConstant_WebService.WSR_InsuranceSubmitReq) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    Utility.getInstance().showToast(res.msg, context);
                    LocalStore.getInstance().setInsuranceFormSubmitted(context, "Yes");
                    Gson gson = new Gson();
                    String json = gson.toJson(requestCommon);
                    LocalStore.getInstance().setInsuranceForm(context, json);
                    showInsuranceList();
                }else if(res.status.equalsIgnoreCase(Constant.Response_Status_Error)){
                    AlertDialogFinish.Show((Activity) context, res.msg, true);
                }
            } else if (requestType == IConstant_WebService.WSR_NetworkPartnersReq) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.networkPartnerListResponses != null) {
       //                 res.networkPartnerListResponses.clear();
                        if (res.networkPartnerListResponses.size() > 0) {
                            insurance_adapter = new Insurance_Adapter(context, res.networkPartnerListResponses);
                            mRecyclerView.setAdapter(insurance_adapter);
                            Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
                            showListView();
                        } else {
                            rlFormView.setVisibility(View.GONE);
                            rlListView.setVisibility(View.VISIBLE);
                            Utility.getInstance().showToast("Network Partner List is Empty", context);
                        }
                    } else {
                        showListView();
                        Utility.getInstance().showToast(res.msg, context);
                    }
                }else if(res.status.equalsIgnoreCase(Constant.Response_Status_Error)){
                    AlertDialogFinish.Show((Activity) context, res.msg, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show((Activity) context, e.toString(), true);
        }
    }

    private void showListView() {
        rlFormView.setVisibility(View.GONE);
        rlListView.setVisibility(View.VISIBLE);
        try {
            HomeNavigation.homeNavigation.showEditInsuranceIconToolbar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_date_start:
                datePicker(tvDateStart, "startDate");
                break;
            case R.id.tv_date_end:
                datePicker(tvDateEnd, "endDate");
                break;
        }
    }

    public void showInsuranceForm() {
        rlFormView.setVisibility(View.VISIBLE);
        rlListView.setVisibility(View.GONE);
        mRequestType = IConstant_WebService.WSR_InsuranceCompanyReq;
        ReqType="InsuranceCompany";
        onRequest(ReqType);
    }

    public void showInsuranceList() {
        mRequestType = IConstant_WebService.WSR_NetworkPartnersReq;
        ReqType="network_partners";
        onRequest(ReqType);
    }

    public void datePicker(final TextView textView, final String date) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        if (date.equalsIgnoreCase("startDate")) {
                            startDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            textView.setText(startDate);
                        }else if(date.equalsIgnoreCase("endDate")) {
                            if (startDate.equals("")) {
                                Utility.getInstance().showToast("Select start date first", context);
                                return;
                            }
                            endDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            boolean isDateAfter = Utility.getInstance().isDateAfter(startDate, endDate);
                            if (isDateAfter) {
                                textView.setText(endDate);
                            }else {
                                Utility.getInstance().showToast("End date must be greater than start date", context);
                            }
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
