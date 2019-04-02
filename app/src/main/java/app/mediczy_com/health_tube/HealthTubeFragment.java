package app.mediczy_com.health_tube;

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

import app.mediczy_com.R;
import app.mediczy_com.adapter.Notification_Adapter;
import app.mediczy_com.bean.Bean_Doctor_Prescribed;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi on 17-11-2016.
 */

public class HealthTubeFragment extends Fragment implements ResponseListener {

    Context context;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Bean_Doctor_Prescribed> arrayList = new ArrayList<Bean_Doctor_Prescribed>();
    Notification_Adapter adapter;

    private String ReqType="", device_id, redId, Type;
    private String MobileNumber, USerId;

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification, container, false);
        context = getActivity();
        init(rootView);

       /* mRecyclerView.setOnScrollListener(new MyScrollListener(context) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });*/

        return rootView;
    }

    private void init(View rootView) {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_doctor_notification_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MobileNumber = LocalStore.getInstance().getPhoneNumber(context);
        USerId = LocalStore.getInstance().getUserID(context);
        device_id = Utility.getInstance().getDeviceID(context);
        redId = LocalStore.getInstance().getGcmId(context);
        Type = LocalStore.getInstance().getType(context);

        mRequestType = IConstant_WebService.WSR_NotificationFragment;
        ReqType="notification_List";
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
            requestCommon.mobile_number = MobileNumber;
            requestCommon.userId = USerId;
            requestCommon.type = Type;
            RequestManager.NotificationFragment(context, this, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    private void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_NotificationFragment) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                String responseStatus = res.status;
                if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.notificationlist != null) {
                        setData(res);
                    }else {
                        AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                    }
                }else if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    String responseMsg = res.msg;
                    AlertDialogFinish.Show(getActivity(), responseMsg, true);
                }
            }else {
                AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(CommonResponse res) {
        arrayList.clear();
        if (res.notificationlist.size() >0) {
            for (int i = 0; i < res.notificationlist.size(); i++) {
                Bean_Doctor_Prescribed bean = new Bean_Doctor_Prescribed();
                bean.setTitle(res.notificationlist.get(i).notification_title);
                bean.setDesc(res.notificationlist.get(i).notification_msg);
                bean.setDate(res.notificationlist.get(i).notification_time);
                bean.setImage(res.notificationlist.get(i).notification_image);
                bean.setType(res.notificationlist.get(i).type);

 //               bean.setImage("http://www.ebookfrenzy.com/android_book/movie.mp4");
//                bean.setType("video");
                arrayList.add(bean);
            }
            adapter = new Notification_Adapter(context, arrayList);
            mRecyclerView.setAdapter(adapter);
            Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
        }
        else {
            showToast("HealthTube_List List is Empty");
        }
    }
}
