package app.mediczy_com.doctor_prescribed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import app.mediczy_com.R;
import app.mediczy_com.adapter.Doctor_Prescribed_Adapter;
import app.mediczy_com.bean.Bean_Doctor_Prescribed;
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
import app.mediczy_com.webservicemodel.response.PrescribedListResponse;

/**
 * Created by Prithivi on 23-10-2016.
 */

public class Doctor_PrescribedFragment extends Fragment implements ResponseListener {

    Context context;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Bean_Doctor_Prescribed> arrayList = new ArrayList<Bean_Doctor_Prescribed>();
    Doctor_Prescribed_Adapter adapter;

    private String ReqType="", device_id, redId, MobileNumber, USerId, Type;
    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;
    String responseStatus="", responseMsg="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doctor__prescrided, container, false);
        context = getActivity();
        init(rootView);

        mRequestType = IConstant_WebService.WSR_Doctor_Prescribed_List;
        onRequest();
        return rootView;
    }

    private void init(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_doctor_prescribed_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MobileNumber = LocalStore.getInstance().getPhoneNumber(context);
        USerId = LocalStore.getInstance().getUserID(context);
        device_id = Utility.getInstance().getDeviceID(context);
        redId = LocalStore.getInstance().getGcmId(context);
        Type = LocalStore.getInstance().getType(context);
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            requestCommon.mobile_number = MobileNumber;
            requestCommon.id = USerId;
            requestCommon.type = Type;
            RequestManager.Doctor_PrescribedFragment(context, this, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_Doctor_Prescribed_List) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                MLog.e("Prescription", ""+res);
                responseStatus = res.status;
                if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.prescribedListResponses != null) {
                        setData(res.prescribedListResponses);
                    }else {
                        AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                    }
                }else if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    responseMsg = res.msg;
                    AlertDialogFinish.Show(getActivity(), responseMsg, true);
                } else {
                    AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                }
            }else {
                AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(List<PrescribedListResponse> res) {
        try {
            arrayList.clear();
            if (res.size() >0) {
                for (int i = 0; i < res.size(); i++) {
                    Bean_Doctor_Prescribed bean = new Bean_Doctor_Prescribed();
                    bean.setTitle(res.get(i).name);
                    bean.setSubject(res.get(i).doctor_subject);
                    bean.setId(res.get(i).prescription_id);
                    bean.setDesc(res.get(i).description_detail);
                    bean.setDate(res.get(i).prescription_date);
                    bean.setImage(res.get(i).doctor_image);
                    bean.setType(res.get(i).type);
                    arrayList.add(bean);
                }
                adapter = new Doctor_Prescribed_Adapter(context, arrayList);
                mRecyclerView.setAdapter(adapter);
                Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
            }
            else {
               Utility.getInstance().showToast("Prescription List is Empty", context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(getActivity(), e.toString(), true);
        }
    }
}
