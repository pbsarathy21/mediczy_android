package app.mediczy_com.appointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.adapter.YourAppointmentDoctor_Adapter;
import app.mediczy_com.adapter.YourAppointmentPatient_Adapter;
import app.mediczy_com.bean.BeanYourAppointment;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.RecyclerItemClickListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi on 01-11-2016.
 */

public class YourAppointmentFragment extends Fragment implements ResponseListener, DoctorAcceptObserver {

    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String ReqType="", device_id, redId, Type, id, number, responseStatus="",
            responseMsg="", appointment_id="", status="";
    private int position;
    YourAppointmentDoctor_Adapter doctorAdapter;
    YourAppointmentPatient_Adapter patientAdapter;
    ArrayList<BeanYourAppointment> arrayList = new ArrayList<BeanYourAppointment>();

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_your_appointment, container, false);
        context = getActivity();
        init(rootView);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position1) {
/*                        String id = arrayList.get(position1).getId();
                        Intent i = new Intent(context, PatientFormDetail.class);
                        i.putExtra("appointment_id", id);
                        context.startActivity(i);
                        ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);*/
                    }
                })
        );

        return rootView;
    }

    private void init(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_your_appointment_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        device_id = Utility.getInstance().getDeviceID(context);
        redId = LocalStore.getInstance().getGcmId(context);
        Type = LocalStore.getInstance().getType(context);
        id = LocalStore.getInstance().getUserID(context);
        number = LocalStore.getInstance().getPhoneNumber(context);

        mRequestType = IConstant_WebService.WSR_YourAppointmentFragment;
        if (Type.equalsIgnoreCase("Doctor")) {
            ReqType="doctor";
            onRequest(ReqType);
        }else {
            ReqType="patient";
            onRequest(ReqType);
        }
    }

    private void onRequest(String reqType) {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            requestCommon.patient_id = id;
            requestCommon.patient_phone_number = number;
            requestCommon.doctor_id = id;
            requestCommon.status = status;
            requestCommon.appointment_id = appointment_id;
            String requestURL;
            if (reqType.equals("patient")) {
                requestURL = IConstant_WebService.patient_appointments;
                RequestManager.YourAppointmentFragment(context, this, requestCommon, mRequestType, requestURL);
            }if (reqType.equals("doctor")) {
                requestURL = IConstant_WebService.doctor_appointments;
                RequestManager.YourAppointmentFragment(context, this, requestCommon, mRequestType, requestURL);
            }if (reqType.equals("accept_decline")) {
                requestURL = IConstant_WebService.doctor_appointment_decision;
                RequestManager.YourAppointmentFragment(context, this, requestCommon, mRequestType, requestURL);
            }
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    private void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoctorAccept(int position, ArrayList<BeanYourAppointment> arrayList, boolean status) {
        appointment_id = arrayList.get(position).getId();
        if (status) {
            this.status = "ACCEPTED";
        }else {
            this.status = "REJECTED";
        }
        this.position = position;
        ReqType="accept_decline";
        onRequest(ReqType);
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_YourAppointmentFragment) {
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
                if (ReqType.equalsIgnoreCase("patient")) {
                    arrayList.clear();
                    if (res.patient_appointment_list != null) {
                        if (res.patient_appointment_list.size() >0) {
                            for (int i = 0; i < res.patient_appointment_list.size(); i++) {
                                BeanYourAppointment bean = new BeanYourAppointment();
                                bean.setDoctorId(res.patient_appointment_list.get(i).doctor_id);
                                bean.setId(res.patient_appointment_list.get(i).appointment_id);
                                bean.setName(res.patient_appointment_list.get(i).doctor_name);
                                bean.setStatus(res.patient_appointment_list.get(i).status);
                                bean.setBookedDate(res.patient_appointment_list.get(i).booked_date);
                                bean.setImage(res.patient_appointment_list.get(i).doctor_image);
                                bean.setExperience(res.patient_appointment_list.get(i).experience +" Exp");
                                bean.setSpecialist("Specialist : "+res.patient_appointment_list.get(i).specialist);
                                bean.setDate(res.patient_appointment_list.get(i).date);
                                bean.setTime(res.patient_appointment_list.get(i).time);
                                arrayList.add(bean);
                            }
                            patientAdapter = new YourAppointmentPatient_Adapter(context, arrayList);
                            mRecyclerView.setAdapter(patientAdapter);
                            Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
                        } else {
                            showToast("Appointment List is Empty");
                        }
                    } else {
                        AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                    }
                } else if (ReqType.equalsIgnoreCase("doctor")) {
                    if (res.patient_appointment_list.size() >0) {
                        arrayList.clear();
                        for (int i = 0; i < res.patient_appointment_list.size(); i++) {
                            BeanYourAppointment bean = new BeanYourAppointment();
                            bean.setId(res.patient_appointment_list.get(i).appointment_id);
                            bean.setDoctorId(res.patient_appointment_list.get(i).doctor_id);
                            bean.setName(res.patient_appointment_list.get(i).user_name);
                            bean.setStatus(res.patient_appointment_list.get(i).doctors_reply);
                            bean.setImage(res.patient_appointment_list.get(i).patient_profileimage);
                            bean.setAge(res.patient_appointment_list.get(i).age);
                            bean.setDate(res.patient_appointment_list.get(i).date);
                            bean.setTime(res.patient_appointment_list.get(i).time);
                            bean.setDate_time(res.patient_appointment_list.get(i).date_time);
                            bean.setBookedDate(res.patient_appointment_list.get(i).booked_date);
                            arrayList.add(bean);
                        }
                        doctorAdapter = new YourAppointmentDoctor_Adapter(context, this, arrayList);
                        mRecyclerView.setAdapter(doctorAdapter);
                        Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
                    } else {
                        showToast("Appointment List is Empty");
                    }
                } else if (ReqType.equalsIgnoreCase("accept_decline")) {
                    responseMsg = res.msg;
                    if (responseMsg.equalsIgnoreCase("ACCEPTED")) {
                        arrayList.get(position).setStatus("ACCEPTED");
                        doctorAdapter.notifyItemChanged(position);
                    }else  if (responseMsg.equalsIgnoreCase("REJECTED") ||
                            responseMsg.equalsIgnoreCase("DECLINED")) {
                        arrayList.get(position).setStatus("REJECTED");
                        doctorAdapter.notifyItemChanged(position);
                    }
                    showToast(responseMsg);
                }
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
