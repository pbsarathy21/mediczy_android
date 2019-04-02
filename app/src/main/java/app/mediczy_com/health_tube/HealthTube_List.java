package app.mediczy_com.health_tube;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.adapter.Notification_Adapter;
import app.mediczy_com.bean.Bean_Doctor_Prescribed;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.MyScrollListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.AsyncRequest;

public class HealthTube_List extends BaseActivity implements  AsyncRequest.OnAsyncRequestComplete{

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Bean_Doctor_Prescribed> arrayList = new ArrayList<Bean_Doctor_Prescribed>();
    Notification_Adapter adapter;

    private String ReqType="", device_id, redId;
    private String MobileNumber, USerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        init();

        mRecyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });

        if (Utility.getInstance().isConnectingToInternet(this)) {
            ReqType="notification_List";
            RequestWebService(ReqType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, false);
        }
    }

    @Override
    public void asyncResponse(String response) {
        MLog.e("response", "" + response);
        if (response!=null && !response.isEmpty()) {
            try {
                JSONObject jSon = new JSONObject(response);
                MLog.e("jSon", "" + jSon);
                JSONArray jSonArray = jSon.getJSONArray("notification");
                if (jSonArray.length() >0) {
                    for (int i = 0; i < jSonArray.length(); i++) {
                        JSONObject object = jSonArray.getJSONObject(i);
                        Bean_Doctor_Prescribed bean = new Bean_Doctor_Prescribed();
                        bean.setTitle(object.getString("notification_title"));
                        bean.setDesc(object.getString("notification_msg"));
                        bean.setDate(object.getString("notification_time"));
                        bean.setImage(object.getString("notification_image"));
                        arrayList.add(bean);
                    }
                    adapter = new Notification_Adapter(HealthTube_List.this, arrayList);
                    mRecyclerView.setAdapter(adapter);
/*                    LocalStore localStore = new LocalStore();
                    localStore.setCount(HealthTube_List.this, Constant.NewCount);
                    Intent myIntent = new Intent(HomePage.ACTION_CLOSE);
                    sendBroadcast(myIntent);*/
                }
                else {
                    showToast("HealthTube_List List is Empty");
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogFinish.Show(this, e.toString(), true);
            }
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Status500, false);
        }
    }

    private void RequestWebService(String reqType1) {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            if (reqType1.equals("notification_List")) {
                ArrayList<NameValuePair> params = getParams(reqType1);
                String requestURL = IConstant_WebService.health_tube_notifications;
                AsyncRequest getPosts = new AsyncRequest(this, null, Constant.POST, params, "Loading data...");
                getPosts.execute(requestURL);
            }
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, false);
        }
    }

    private ArrayList<NameValuePair> getParams(String reqType) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gcmid", redId));
        params.add(new BasicNameValuePair("device_id", device_id));
        params.add(new BasicNameValuePair("mobile_number", MobileNumber));
        return params;
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_doctor_notification);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_doctor_notification_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MobileNumber = LocalStore.getInstance().getPhoneNumber(this);
        USerId = LocalStore.getInstance().getUserID(this);
        device_id = Utility.getInstance().getDeviceID(HealthTube_List.this);
        redId = LocalStore.getInstance().getGcmId(HealthTube_List.this);
/*
        for (int i = 0; i < 20; i++) {
            Bean_Doctor_Prescribed bean = new Bean_Doctor_Prescribed();
            bean.setTitle("Brush this weekend ?");
            bean.setSubject("Ali Conner");
            bean.setDesc("I'll be in your neighborhood doing errands this weekends. Do you want");
            bean.setDate("Jan 9, 2016");
            bean.setImage("");
            arrayList.add(bean);
        }
        adapter = new Notification_Adapter(HealthTube_List.this, arrayList);
        mRecyclerView.setAdapter(adapter);*/
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
