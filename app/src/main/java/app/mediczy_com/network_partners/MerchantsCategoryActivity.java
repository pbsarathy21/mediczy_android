package app.mediczy_com.network_partners;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.bean.Home_Type_Bean;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

public class MerchantsCategoryActivity extends BaseActivity implements ResponseListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle bundle;
    private MerchantsCategoryAdapter adapter_list;
    private ArrayList<Home_Type_Bean> arrayList = new ArrayList<Home_Type_Bean>();
    private ProgressDialog pDialog = null;
    private RequestCommon requestCommon;
    private int mRequestType = 0;

    public String Id, latitude="", longitude="", name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_partner_category_activity);
        toolBar();
        init();
    }

    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bundle = getIntent().getExtras();
        Id = bundle.getString("id");
        name = bundle.getString("name");
        latitude = bundle.getString("lat");
        longitude = bundle.getString("long");
        getSupportActionBar().setTitle(name);
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_network_cat_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRequestType = IConstant_WebService.WSR_Home_List;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.hospital_id = Id;
            RequestManager.Merchant_Category(this, null, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }
    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_Home_List) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (res.hospital_categoriesResponses != null) {
                        setData(res);
                    }else {
                        AlertDialogFinish.Show(this, "CategoryResponse null", true);
                    }
                } else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(this, res.msg, true);
                }
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(CommonResponse res) {
        try {
            if (res.hospital_categoriesResponses != null) {
                arrayList.clear();
                if (res.hospital_categoriesResponses.size() >0) {
                    for (int i = 0; i < res.hospital_categoriesResponses.size(); i++) {
                        Home_Type_Bean bean = new Home_Type_Bean();
                        bean.setCategory_id(res.hospital_categoriesResponses.get(i).id);
                        bean.setHospital_id(res.hospital_categoriesResponses.get(i).hospital_id);
                        bean.setCount(res.hospital_categoriesResponses.get(i).online_count);
                        bean.setTypeName(res.hospital_categoriesResponses.get(i).name);
                        bean.setImage(res.hospital_categoriesResponses.get(i).image);
                        bean.setType(Home_Type_Bean.TYPE_CATEGORY);
                        arrayList.add(bean);
                    }
                    adapter_list = new MerchantsCategoryAdapter(this, arrayList, bundle);
                    mRecyclerView.setAdapter(adapter_list);
                    Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
                } else {
                    Utility.getInstance().showToast("Category List is Empty", this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }
}
