package app.mediczy_com.doctor_prescribed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.adapter.Doctor_Prescribed_Adapter;
import app.mediczy_com.bean.Bean_Doctor_Prescribed;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.MyScrollListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.AsyncRequest;

public class Doctor_Prescribed_List extends AppCompatActivity implements  AsyncRequest.OnAsyncRequestComplete{

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Bean_Doctor_Prescribed> arrayList = new ArrayList<Bean_Doctor_Prescribed>();
    Doctor_Prescribed_Adapter adapter;

    private String ReqType="", device_id, redId;
    private String MobileNumber, USerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor__prescrided);
        init();

        mRecyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });
        if (Utility.getInstance().isConnectingToInternet(this)) {
            ReqType="Doctor_Prescribed_List";
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
                JSONArray jSonArray = jSon.getJSONArray("prescription_list");
                if (jSonArray.length() >0) {
                    for (int i = 0; i < jSonArray.length(); i++) {
                        JSONObject object = jSonArray.getJSONObject(i);
                        Bean_Doctor_Prescribed bean = new Bean_Doctor_Prescribed();
                        bean.setTitle(object.getString("doctor_name"));
                        bean.setSubject(object.getString("doctor_subject"));
                        bean.setId(object.getString("prescription_id"));
                        bean.setDesc(object.getString("description_detail"));
                        bean.setDate(object.getString("prescription_date"));
                        bean.setImage(object.getString("doctor_image"));
                        arrayList.add(bean);
                    }
                    adapter = new Doctor_Prescribed_Adapter(Doctor_Prescribed_List.this, arrayList);
                    mRecyclerView.setAdapter(adapter);
                }
                else {
                    showToast("Prescription List is Empty");
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
            if (reqType1.equals("Doctor_Prescribed_List")) {
                ArrayList<NameValuePair> params = getParams(reqType1);
                String requestURL = IConstant_WebService.prescription_list;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar_doctor_prescribed);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Doctor Prescribed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_doctor_prescribed_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MobileNumber = LocalStore.getInstance().getPhoneNumber(this);
        USerId = LocalStore.getInstance().getUserID(this);
        device_id = Utility.getInstance().getDeviceID(Doctor_Prescribed_List.this);
        redId = LocalStore.getInstance().getGcmId(Doctor_Prescribed_List.this);

/*        Bundle bundle = getIntent().getExtras();
        for (int i = 0; i < 20; i++) {
            Bean_Doctor_Prescribed bean = new Bean_Doctor_Prescribed();
            bean.setTitle("Brush this weekend ?");
            bean.setSubject("Ali Conner");
            bean.setDesc("I'll be in your neighborhood doing errands this weekends. Do you want");
            bean.setDate("Jan 9, 2016");
            bean.setImage("");
            arrayList.add(bean);
        }
        adapter = new Doctor_Prescribed_Adapter(Doctor_Prescribed_List.this, arrayList);
        mRecyclerView.setAdapter(adapter);*/
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
