package app.mediczy_com.ambulance_list;

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
import java.util.ArrayList;
import java.util.List;
import app.mediczy_com.R;
import app.mediczy_com.adapter.Ambulance_Adapter;
import app.mediczy_com.bean.Bean_Ambulance;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.RecyclerItemClickListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.AmbulanceListResponse;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi on 23-10-2016.
 */

public class AmbulanceListFragment extends Fragment implements ResponseListener {

    Context context;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Bean_Ambulance> arrayList = new ArrayList<Bean_Ambulance>();
    Ambulance_Adapter adapter;
    public static AmbulanceListFragment list_activity;
    String ReqType="", device_id, redId;
    String latitude="", longitude="";

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ambulance_list, container, false);
        list_activity = this;
        context = getActivity();
        init(rootView);

/*        mRecyclerView.setOnScrollListener(new MyScrollListener(context) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });*/

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                })
        );

        return rootView;
    }

    private void init(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_ambulance_list);
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

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

        device_id = Utility.getInstance().getDeviceID(context);
        redId = LocalStore.getInstance().getGcmId(context);

        ReqType="Ambulance_List";
        mRequestType = IConstant_WebService.WSR_AmbulanceFragment;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            requestCommon.latitude = latitude;
            requestCommon.longitude = longitude;
            RequestManager.AmbulanceFragment(context, this, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_AmbulanceFragment) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.ambulanceList != null) {
                        setData(res.ambulanceList);
                    }else {
                        AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                    }
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(getActivity(), res.msg, true);
                }
            }else {
                AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(List<AmbulanceListResponse> res) {
        try {
            arrayList.clear();
            if (res.size() >0) {
                for (int i = 0; i < res.size(); i++) {
                    Bean_Ambulance bean = new Bean_Ambulance();
                    bean.setHospital_Name(res.get(i).hospital_name);
                    bean.setLocation(res.get(i).address);
                    bean.setNumber(res.get(i).phonenumber);
                    bean.setKm(res.get(i).kilometer+" Km");
                    bean.setOffer(res.get(i).offer);
                    arrayList.add(bean);
                }
                adapter = new Ambulance_Adapter(context, arrayList);
                mRecyclerView.setAdapter(adapter);
                Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
            }
            else {
                Utility.getInstance().showToast("Offers List is Empty", context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(getActivity(), e.toString(), true);
        }
    }
}
