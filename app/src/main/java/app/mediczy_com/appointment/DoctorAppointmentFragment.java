package app.mediczy_com.appointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import app.mediczy_com.R;
import app.mediczy_com.adapter.DoctorAppointmentDateSlot_Adapter;
import app.mediczy_com.adapter.DoctorAppointmentTimeSlot_Adapter;
import app.mediczy_com.bean.BeanDateSlot;
import app.mediczy_com.bean.BeanTimeSlot;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.MyScrollListener;
import app.mediczy_com.utility.RecyclerItemClickListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi on 27-10-2016.
 */

public class DoctorAppointmentFragment extends Fragment implements ResponseListener,
        TimeObserver {

    private Context context;
    private Button btnSubmit;
    private TextView tvDateSelected;
    private ArrayList<BeanDateSlot> arrayDateSlot = new ArrayList<>();
    private ArrayList<BeanTimeSlot> arrayTimeSlot = new ArrayList<>();
    private ArrayList<String> arrayTimeSlotSelected = new ArrayList<>();
    private RecyclerView mRecyclerView, mRecyclerViewTimeSlot;
    private RecyclerView.LayoutManager mLayoutManager;
    private DoctorAppointmentDateSlot_Adapter adapter;
    private DoctorAppointmentTimeSlot_Adapter timeSlot_adapter;
    private String ReqType="", responseStatus="", responseMsg="", id, selectedDate, selectedTime;
    public boolean isClicked=false;
    private int position;

    private String[] timings = new String[]{"08.00Am:09.00Am","09.00Am:10.00Am","10.00Am:11.00Am",
            "11.00Am:12.00Pm", "12.00Pm:01.00Pm","01.00Pm:02.00Pm","02.00Pm:03.00Pm","03.00Pm:04.00Pm","04.00Pm:05.00Pm"
            ,"05.00Pm:06.00Pm","06.00Pm:07.00Pm","07.00Pm:08.00Pm","08.00Pm:09.00Pm","09.00Pm:10.00Pm",
            "10.00Pm:11.00PM","11.00PM:12.00AM"};

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doctor_appointment_fragment, container, false);
        context = getActivity();
        init(rootView);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        mRecyclerView.setOnScrollListener(new MyScrollListener(context) {
            @Override
            public void onMoved(int distance) {
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position1) {
                        position = position1;
                        MLog.e("isClicked", "" + isClicked);
                        if (isClicked) {
                            conformSubmit();
                        }else {
                            timeSelected();
                        }
                    }
                })
        );

        return rootView;
    }

    private void timeSelected() {
        arrayTimeSlotSelected.clear();
        selectedTime="";
        isClicked=false;
        selectedDate = arrayDateSlot.get(position).getDateNumeric()+"-"+
                arrayDateSlot.get(position).getMonth()+"-"+arrayDateSlot.get(position).getYear();
        MLog.e("selectedDate", "" + selectedDate);
        tvDateSelected.setText(arrayDateSlot.get(position).getMonth()+" "+
                arrayDateSlot.get(position).getDateNumeric()+" "+arrayDateSlot.get(position).getYear());
        ReqType="doctor_schedule_list";
        onRequest(ReqType);
    }

    private void submit() {
        isClicked=false;
        if (arrayTimeSlot.size()>0) {
            selectedTime="";
            for (int i=0; i<arrayTimeSlot.size(); i++) {
                if (arrayTimeSlot.get(i).getAvailable().equalsIgnoreCase("Available")) {
                    String selectedTime = arrayTimeSlot.get(i).getTime().toString();
                    arrayTimeSlotSelected.add(selectedTime);
                }
            }
            if (arrayTimeSlotSelected.size()>0) {
                selectedTime = arrayTimeSlotSelected.toString();
                selectedTime = selectedTime.substring(1, selectedTime.length()-1);
                MLog.e("selectedTime", "" + selectedTime);
                ReqType="add_doctor_timeslot";
                onRequest(ReqType);
            }else {
                selectedTime="";
                MLog.e("selectedTime", "" + selectedTime);
                ReqType="add_doctor_timeslot";
                onRequest(ReqType);
            }
        } else {
            Utility.getInstance().showToast("Time slot is empty",context);
        }
    }

    private void init(View rootView) {
        btnSubmit = (Button) rootView.findViewById(R.id.button_doctor_appoin_submit);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_time_slot_list);
        mRecyclerViewTimeSlot = (RecyclerView) rootView.findViewById(R.id.recycler_view_time_slot_list_1);
        tvDateSelected  = (TextView) rootView.findViewById(R.id.textview_doctor_date_selected);
        mRecyclerViewTimeSlot.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerViewTimeSlot.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        id = LocalStore.getInstance().getUserID(context);
        clearArray();
        getNextSevenDays();
//        setTimeSlot();
        mRequestType = IConstant_WebService.WSR_DoctorAppointmentFragment;
        ReqType="doctor_schedule_list";
        onRequest(ReqType);
    }

    private void onRequest(String reqType) {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            requestCommon.doctor_id = id;
            requestCommon.date = selectedDate;
            requestCommon.time = selectedTime;
            String requestURL;
            if (reqType.equals("doctor_schedule_list")) {
                requestURL = IConstant_WebService.get_time_slots;
                RequestManager.DoctorAppointmentFragment(context, this, requestCommon, mRequestType, requestURL);
            }if (reqType.equals("add_doctor_timeslot")) {
                requestURL = IConstant_WebService.add_doctor_timeslot;
                RequestManager.DoctorAppointmentFragment(context, this, requestCommon, mRequestType, requestURL);
            }
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    private void clearArray() {
        arrayDateSlot.clear();
        arrayTimeSlot.clear();
        arrayTimeSlotSelected.clear();
    }

    private void getNextSevenDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
        for (int i = 0; i < 7; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            String day = sdf.format(calendar.getTime());
            day = day.replace(" ", "-");

            String [] parts = day.split("-", 4);
//            MLog.e("day_after", "" + day);
            BeanDateSlot bean1 = new BeanDateSlot();
            bean1.setWeekDay(parts[0] = parts[0].substring(0, parts[0].length()-parts[0].length()+1));
            bean1.setDateNumeric(parts[1]);
            bean1.setMonth(parts[2]);
            bean1.setYear(parts[3]);
            if (i==0) {
                tvDateSelected.setText(parts[2]+" "+parts[1]+" "+parts[3]);
                selectedDate = parts[1]+"-"+parts[2]+"-"+parts[3];
                MLog.e("selectedDate", "" + selectedDate);
                bean1.setSelected(true);
            } else {
                bean1.setSelected(false);
            }
            arrayDateSlot.add(bean1);
            //         showToast(strDays[i]);
        }
        adapter = new DoctorAppointmentDateSlot_Adapter(context, this, arrayDateSlot);
        mRecyclerView.setAdapter(adapter);
        Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
    }

    private void setTimeSlot() {
        arrayTimeSlot.clear();
        arrayTimeSlotSelected.clear();

        for (int i=0; i<timings.length; i++) {
            BeanTimeSlot beanTimeSlot = new BeanTimeSlot();
            beanTimeSlot.setTime(timings[i]);
            beanTimeSlot.setAvailable("Busy");
            arrayTimeSlot.add(beanTimeSlot);
        }
        timeSlot_adapter = new DoctorAppointmentTimeSlot_Adapter(context, this, arrayTimeSlot);
        mRecyclerViewTimeSlot.setAdapter(timeSlot_adapter);
        Utility.getInstance().runLayoutAnimation(mRecyclerViewTimeSlot, context);
    }

    @Override
    public void onSelectedTime(int po, ArrayList<BeanTimeSlot> arrayList, boolean status) {
        isClicked=true;
        if (status) {
            String timeSelected = arrayList.get(po).getTime();
 //           arrayTimeSlotSelected.add(timeSelected);
        }else {
            String time = arrayList.get(po).getTime();
 //           arrayTimeSlotSelected.remove(time);
        }
    }

    private void conformSubmit() {
        new AlertDialog.Builder(context)
                .setTitle(Constant.Alart_AppName_Internet)
                .setMessage("Do you want to save ?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        submit();
                        isClicked=true;
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                timeSelected();
            }
        }).show();
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_DoctorAppointmentFragment) {
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
            if (ReqType.equalsIgnoreCase("doctor_schedule_list")) {
                arrayTimeSlot.clear();
                if (res.time_slots != null) {
                    if (res.time_slots.size() >0) {
                        for (int i = 0; i < res.time_slots.size(); i++) {
                            BeanTimeSlot beanTimeSlot = new BeanTimeSlot();
                            beanTimeSlot.setTime(res.time_slots.get(i).timeslot);
                            beanTimeSlot.setAvailable(res.time_slots.get(i).status);
                            arrayTimeSlot.add(beanTimeSlot);
                        }
                        timeSlot_adapter = new DoctorAppointmentTimeSlot_Adapter(context, this, arrayTimeSlot);
                        mRecyclerViewTimeSlot.setAdapter(timeSlot_adapter);
                        Utility.getInstance().runLayoutAnimation(mRecyclerViewTimeSlot, context);
                    }
                    else {
                        Utility.getInstance().showToast("TimeSlot List is Empty", context);
                    }
                } else {
                    AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                }
            } else if (ReqType.equalsIgnoreCase("add_doctor_timeslot")){
                responseStatus = res.status;
                if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                    responseMsg = res.msg;
                    Utility.getInstance().showToast(responseMsg,context);
                    if (isClicked)
                        timeSelected();
                }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                    responseMsg = res.msg;
                    isClicked=false;
                    AlertDialogFinish.Show((Activity) context, responseMsg, true);
                } else {
                    isClicked=false;
                    AlertDialogFinish.Show((Activity) context, Constant.Alart_Status500, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(getActivity(), e.toString(), true);
        }
    }
}
