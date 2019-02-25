package app.mediczy_com;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import app.mediczy_com.Retrofit.ListDetails;
import app.mediczy_com.Retrofit.RetrofitInterface;
import app.mediczy_com.Session.Session;
import app.mediczy_com.adapter.DoctorsList_Adapter;
import app.mediczy_com.adapter.Spa_List_Adapter;
import app.mediczy_com.bean.Bean_Doctor_List;
import app.mediczy_com.bean.Bean_Spa_Salon;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.filter.FilterActivity;
import app.mediczy_com.filter.FilterRefreshDoctorListObserver;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.request.CommonRequest;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.MyScrollListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.DoctorListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class DoctorsList extends BaseActivity implements ResponseListener, FilterRefreshDoctorListObserver {

    public static DoctorsList doctorsList;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    DoctorsList_Adapter adapter;
    Spa_List_Adapter spaListAdapter;
    ArrayList<Bean_Doctor_List> arrayList = new ArrayList<Bean_Doctor_List>();
    ArrayList<Bean_Spa_Salon> arrayList_spa = new ArrayList<Bean_Spa_Salon>();
    String device_id, redId, category_id = "", hospital_id = "", Name = "",
            latitude = "", longitude = "", MobileNumber, user_Id,
            date = "", filter_id = "", isFrom = "";
    int year, month, day;

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;
    private boolean mIsRefreshing = true;

    RetrofitInterface apiInterface;
    Context context;
    Session session;
    public List<DoctorListResponse> docList;
    private String userId;
    private String UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_list);

        session = new Session(this);
        doctorsList = this;
        init();


        mRecyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });

        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_doctor_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (session.getDoc().equalsIgnoreCase("1")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitInterface.url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInterface = retrofit.create(RetrofitInterface.class);
            userId = LocalStore.getInstance().getUserID(this);
            MLog.e("userId==>", "" + userId);
            Doctor_List();
        } else {
            Bundle bundle = getIntent().getExtras();
            category_id = bundle.getString("id");
            hospital_id = bundle.getString("hospital_id");
            Name = bundle.getString("name");
            latitude = bundle.getString("lat");
            longitude = bundle.getString("long");
            isFrom = bundle.getString(Constant.isFrom);

            MobileNumber = LocalStore.getInstance().getPhoneNumber(this);
            user_Id = LocalStore.getInstance().getUserID(this);
            MLog.e("isFrom", "" + isFrom);
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            date = String.valueOf(day) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);
            MLog.e("Doctor_Date", "" + date);
            MLog.e("hospital_id, category_id", "" + hospital_id + ", " + category_id);
            getSupportActionBar().setTitle(Name);
            device_id = Utility.getInstance().getDeviceID(DoctorsList.this);
            redId = LocalStore.getInstance().getGcmId(DoctorsList.this);
            filter_id = "0";
            mRequestType = IConstant_WebService.WSR_DoctorsList;
            onRequest();

        }
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.catergory_id = category_id;
            requestCommon.hospital_id = hospital_id;
            requestCommon.phone_number = MobileNumber;
            requestCommon.longitude = latitude;
            requestCommon.latitude = longitude;
            requestCommon.date = date;
            requestCommon.filter_id = filter_id;
            requestCommon.user_id = user_Id;
            requestCommon.refId = LocalStore.getInstance().getRefId(DoctorsList.this);
            String url;
            if (isFrom.equals("MerchantsCategoryActivity")) {
                url = IConstant_WebService.get_hospital_category_doctors;
            } else {
                url = IConstant_WebService.doctors_list;
            }
            RequestManager.DoctorsList(this, null, requestCommon, mRequestType, url);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (session.getDoc().equalsIgnoreCase("1")) {

        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_search, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    if (!Name.equalsIgnoreCase(Constant.spa_salon)) {
                        if (arrayList != null && !arrayList.isEmpty()) {
                            adapter.filter(newText);
                        }
                    } else {
                        if (arrayList_spa != null && !arrayList_spa.isEmpty()) {
                            spaListAdapter.filter(newText);
                        }
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    return false;
                    //Here u can get the value "query" which is entered in the search box.
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }



      /*  MenuItem m =  menu.getItem(0);
        m.setIcon(R.drawable.common_google_signin_btn_icon_dark_normal);
*/
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
//                showToast("Coming Soon");
                moveToFilterActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveToFilterActivity() {
        Intent i = new Intent(DoctorsList.this, FilterActivity.class);
        i.putExtra("id", category_id);
        i.putExtra("name", Name);
        i.putExtra("lat", String.valueOf(latitude));
        i.putExtra("long", String.valueOf(longitude));
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        try {
            if (requestType == IConstant_WebService.WSR_DoctorsList) {
                CommonResponse res = (CommonResponse) responseObj;
                if (res != null) {
                    MLog.e("res", "" + res.toString());
                    if (isFrom.equals("MerchantsCategoryActivity")) {
                    }
                    if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        if (res.doctorsList != null) {
                            setData(res.doctorsList);
                        } else {
                            AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                        }
                    } else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, res.msg, true);
                    }
                } else {
                    AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }

    private void setData(List<DoctorListResponse> doctorsList) {
        try {
            arrayList.clear();
            if (doctorsList.size() > 0) {
                for (int i = 0; i < doctorsList.size(); i++) {
                    Bean_Doctor_List bean = new Bean_Doctor_List();
                    bean.setId(doctorsList.get(i).doctor_id);
                    bean.setName(doctorsList.get(i).name);
                    bean.setStudy(doctorsList.get(i).degree);
                    bean.setDoctor_Type(doctorsList.get(i).doctor_type);
                    bean.setAvailable(doctorsList.get(i).online_status);
                    bean.setExperience(doctorsList.get(i).expyear);
                    bean.setRate(doctorsList.get(i).price);
                    bean.setNextAvailable(doctorsList.get(i).next_avab);
                    bean.setImage(doctorsList.get(i).image);
                    arrayList.add(bean);
                }
                mIsRefreshing = true;
                mRecyclerView.setVisibility(View.VISIBLE);
                adapter = new DoctorsList_Adapter(DoctorsList.this, arrayList, Name);
                mRecyclerView.setAdapter(adapter);
                Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
            } else {
                mIsRefreshing = false;
                mRecyclerView.setVisibility(View.GONE);
                Utility.getInstance().showToast("Doctor List is Empty", this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }

    @Override
    public void onRefreshReceived(List<DoctorListResponse> doctorsList, String filter_id, String Speciality) {
        try {
            getSupportActionBar().setTitle(Speciality);
            setData(doctorsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        this.filter_id = filter_id;
        mRequestType = IConstant_WebService.WSR_DoctorsList;
        onRequest();*/
    }


    public void Doctor_List() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.id = userId;
        commonRequest.type = "patient";
        commonRequest.lat = session.getLat();
        commonRequest.longitude = session.getLong();
        Call<ListDetails> Doc_List = apiInterface.Act_Doctor(commonRequest);
        Doc_List.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                MLog.e("", "Doctor_List_Status:==>" + response.body());
                MLog.e("", "Doctor_List_Status:==>" + response.body().doctors);
                if (response.body().status.equalsIgnoreCase("success")) {
                    docList = response.body().doctors;
                    Doc_Set(docList);
                } else {
                    Toast.makeText(DoctorsList.this, "No Doctors ..!!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {
                Toast.makeText(DoctorsList.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void Doc_Set(List<DoctorListResponse> docList) {
        try {
            arrayList.clear();
            if (docList.size() > 0) {
                for (int i = 0; i < docList.size(); i++) {
                    Bean_Doctor_List bean = new Bean_Doctor_List();
                    bean.setId(docList.get(i).doctor_id);
                    bean.setName(docList.get(i).name);
                    bean.setStudy(docList.get(i).degree);
                    bean.setDoctor_Type(docList.get(i).doctor_type);
                    bean.setAvailable(docList.get(i).online_status);
                    bean.setExperience(docList.get(i).expyear);
                    bean.setRate(docList.get(i).price);
                    bean.setNextAvailable(docList.get(i).next_avab);
                    bean.setImage(docList.get(i).image);
                    arrayList.add(bean);
                }
                mIsRefreshing = true;
                mRecyclerView.setVisibility(View.VISIBLE);
                adapter = new DoctorsList_Adapter(DoctorsList.this, arrayList, Name);
                mRecyclerView.setAdapter(adapter);
                Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
            } else {
                mIsRefreshing = false;
                mRecyclerView.setVisibility(View.GONE);
                Utility.getInstance().showToast("Doctor List is Empty", this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }
}
