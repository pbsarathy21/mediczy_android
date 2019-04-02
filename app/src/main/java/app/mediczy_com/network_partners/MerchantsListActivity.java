package app.mediczy_com.network_partners;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.MyScrollListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.MerchantsListResponse;

public class MerchantsListActivity extends BaseActivity implements ResponseListener {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog pDialog = null;
    private MerchantsList_Adapter adapter;
    private RequestCommon requestCommon;
    private Bundle bundle;
    private int mRequestType = 0;
    public String Id, latitude="", longitude="", name="", type="";

    ArrayList<MerchantsListResponse> arrayList = new ArrayList<MerchantsListResponse>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_partner_activity);
        init();

        mRecyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_network_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        bundle = getIntent().getExtras();
        Id = bundle.getString("id");
        name = bundle.getString("name");
        latitude = bundle.getString("lat");
        longitude = bundle.getString("long");
        getSupportActionBar().setTitle(name);
        onRequest();
    }

    private void onRequest() {
        mRequestType = IConstant_WebService.WSR_NP_NetworkPartner_List_Req;
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.id = Id;
            requestCommon.latitude = latitude;
            requestCommon.longitude = longitude;
            RequestManager.NetworkPartnersHome(this, null, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_NP_NetworkPartner_List_Req) {
            CommonResponse res = (CommonResponse) responseObj;
            MLog.e("MerchantsListActivity :", "" + res);
            if (res != null) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.merchantsListResponse != null) {
                        setData(res.merchantsListResponse);
                    }else {
                        AlertDialogFinish.Show(this, "merchantsListResponse null", true);
                    }
                } else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(this, res.msg, true);
                }
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(List<MerchantsListResponse> res) {
        for (int i = 0; i < res.size(); i++) {
            MerchantsListResponse response = new MerchantsListResponse();
            response.title = res.get(i).title;
            response.description = res.get(i).description;
            response.merchant_id = res.get(i).merchant_id;
            response.offer = res.get(i).offer;
            response.kilometer = res.get(i).kilometer+" KM";
            response.average_ratings = res.get(i).average_ratings;
            response.image = res.get(i).image;
            arrayList.add(response);
            adapter = new MerchantsList_Adapter(MerchantsListActivity.this, arrayList, bundle);
            mRecyclerView.setAdapter(adapter);
            Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
        }
    }
}


/*
<FrameLayout
        android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:id="@+id/contact_new_fl"
                android:layout_marginTop="@dimen/Margin_3"
                android:paddingTop="?attr/actionBarSize">
</FrameLayout>

        ContactLayout = (FrameLayout) findViewById(R.id.contact_new_fl);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentView = new Fragment_ContactUs();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
        response = (BuilderOverview_Response) bundle.getSerializable("BUILDER_RESPONSE");

        }
        Bundle bundle_contactUs = new Bundle();
        bundle_contactUs.putBoolean("ISVIEWDETAIL",false);
        fragmentView.setArguments(bundle);
        fragmentManager.beginTransaction()
        .replace(R.id.contact_new_fl, fragmentView, "")
        .commit();*/
