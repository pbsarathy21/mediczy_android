package app.mediczy_com.ambulance_list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.adapter.Ambulance_Adapter;
import app.mediczy_com.bean.Bean_Ambulance;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MyScrollListener;
import app.mediczy_com.utility.RecyclerItemClickListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.AsyncRequest;

/**
 * Created by Prithivi Raj on 08-01-2016.
 */
public class Ambulance_List_Activity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete{

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Bean_Ambulance> arrayList = new ArrayList<Bean_Ambulance>();
    Ambulance_Adapter adapter;
    public static Ambulance_List_Activity list_activity;
    String ReqType="", device_id, redId;
    String latitude="", longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambulance_list);
        list_activity = this;
        init();

        mRecyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Ambulance_List_Activity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                })
        );
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_ambulance);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ambulance List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_ambulance_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getString("lat");
        longitude = bundle.getString("long");

        /*for (int i = 0; i < 20; i++) {
            Bean_Ambulance bean = new Bean_Ambulance();
            bean.setHospital_Name("Bharadwaj Hospital");
            bean.setLocation("Muthu sami mudali street sainathapuram, vellore-1");
            bean.setNumber("9790545214");
            bean.setKm("1.5 Km");
            arrayList.add(bean);
        }
        adapter = new Ambulance_Adapter(Ambulance_List_Activity.this, arrayList);
        mRecyclerView.setAdapter(adapter);*/

        device_id = Utility.getInstance().getDeviceID(Ambulance_List_Activity.this);
        redId = LocalStore.getInstance().getGcmId(Ambulance_List_Activity.this);

        if (Utility.getInstance().isConnectingToInternet(this)) {
            ReqType="Ambulance_List";
            RequestWebService(ReqType);
        } else {
            AlertDialogFinish.Show(Ambulance_List_Activity.this, Constant.Alart_Internet, false);
        }
    }

    @Override
    public void asyncResponse(String response) {
        if (response!=null && !response.isEmpty()) {
            try {
                JSONObject jSon = new JSONObject(response);
                JSONArray jSonArray = jSon.getJSONArray("ambulance_list");
                if (jSonArray.length() >0) {
                    for (int i = 0; i < jSonArray.length(); i++) {
                        JSONObject object = jSonArray.getJSONObject(i);
                        Bean_Ambulance bean = new Bean_Ambulance();
                        bean.setHospital_Name(object.getString("hospital_name"));
                        bean.setLocation(object.getString("address"));
                        bean.setNumber(object.getString("number"));
                        bean.setKm(object.getString("kilometer")+" Km");
                        arrayList.add(bean);
                    }
                    adapter = new Ambulance_Adapter(Ambulance_List_Activity.this, arrayList);
                    mRecyclerView.setAdapter(adapter);
                }
                else {
                    showToast("Ambulance List is Empty");
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogFinish.Show(Ambulance_List_Activity.this, e.toString(), true);
            }
        } else {
            AlertDialogFinish.Show(Ambulance_List_Activity.this, Constant.Alart_Status500, false);
        }
    }

    private void RequestWebService(String reqType1) {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            if (reqType1.equals("Ambulance_List")) {
                ArrayList<NameValuePair> params = getParams(reqType1);
                String requestURL = IConstant_WebService.ambulance_list+"lat="+latitude+"&lang="+longitude;;
                AsyncRequest getPosts = new AsyncRequest(Ambulance_List_Activity.this, null, Constant.GET, params, "Loading data...");
                getPosts.execute(requestURL);
            }
        } else {
            AlertDialogFinish.Show(Ambulance_List_Activity.this, Constant.Alart_Internet, false);
        }
    }

    private ArrayList<NameValuePair> getParams(String reqType) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gcmid", redId));
        params.add(new BasicNameValuePair("device_id", device_id));
        params.add(new BasicNameValuePair("number", ""));
        return params;
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finish();
            overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
            return true;
        }
        return false;
    }
}
