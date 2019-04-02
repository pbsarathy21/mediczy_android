package app.mediczy_com.dashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.mediczy_com.ChatBot.ChatBotActivity;
import app.mediczy_com.CustomTextView;
import app.mediczy_com.DoctorsList;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.Session.Session;
import app.mediczy_com.adapter.Home_CategoryAdapter;
import app.mediczy_com.bean.Home_Type_Bean;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.gps.GPSTracker;
import app.mediczy_com.health_tube.HealthTubeDetail;
import app.mediczy_com.home.OnClickListenerObserver;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.network_partners.AdsMerchantsListActivity;
import app.mediczy_com.network_partners.HospitalMerchantsListActivity;
import app.mediczy_com.network_partners.MerchantsListActivity;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.Merchant_CategoriesListResponse;

/**
 * Created by Prithivi on 23-10-2016.
 */

public class HomeFragment extends Fragment implements ResponseListener,
        OnClickListenerObserver, SelectedNetworkPartnerObserver {

    private Context context;
    private RecyclerView mRecyclerView, mRecyclerView_networkPartner;
    private CardView cardView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private Animation animAlpha2;
    public static HomeFragment homeFragment;
    private Home_CategoryAdapter adapter_list;
    private Network_Partners_Category_Adapter adapter;
    private ArrayList<Merchant_CategoriesListResponse> arrayListCategory = new ArrayList<Merchant_CategoriesListResponse>();
    private Merchant_CategoriesListResponse object;
    double latitude = 13.0827, longitude = 80.2707;
    private ArrayList<Home_Type_Bean> arrayList = new ArrayList<Home_Type_Bean>();
    private String device_id, redId, Ph_Number, ID, ReqType = "", category_id,
            Name, gethour, getminute, version_code, logout_status, doctor_online_status;
    int year, month, day, hour, minute, second;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0;

    Session session;
    ImageView fab;
    CustomTextView askMe;

    private String UserType;


    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_simple_coordinator, container, false);
        context = getActivity();
  //      Actionbar_Menu(rootView);
        homeFragment = this;
        session = new Session(getActivity());

        askMe = rootView.findViewById(R.id.ask_me);

        askMe.setSelected(true);

        init(rootView);

        UserType = LocalStore.getInstance().getType(context);
        MLog.e("UserType", "" + UserType);
        if (UserType.equalsIgnoreCase("Patient")) {
            fab.setVisibility(View.VISIBLE);
            askMe.setVisibility(View.VISIBLE);

        } else if (UserType.equalsIgnoreCase("Doctor")) {
            fab.setVisibility(View.GONE);
            askMe.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatBotActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
//                getActivity().finish();
            }
        });

        askMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatBotActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
//                getActivity().finish();
            }
        });

        return rootView;
    }

    private void moveToDoctorActivity() {
        Intent i = new Intent(context, DoctorsList.class);
        i.putExtra("id", category_id);
        i.putExtra("name", Name);
        i.putExtra("lat", String.valueOf(latitude));
        i.putExtra("long", String.valueOf(longitude));
        i.putExtra(Constant.isFrom, "HomeFragment");
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void moveToTopCategory() {
        if (object.name.equalsIgnoreCase("Hospital") ||
                object.name.equalsIgnoreCase("Hospitals")) {
            Intent i = new Intent(context, HospitalMerchantsListActivity.class);
            i.putExtra("id", object.merchant_category_id);
            i.putExtra("name", object.name);
            i.putExtra("lat", String.valueOf(latitude));
            i.putExtra("long", String.valueOf(longitude));
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (object.name.equalsIgnoreCase("Ads") ||
                object.name.equalsIgnoreCase("Products")||
                object.name.equalsIgnoreCase("health tips")||
                object.name.equalsIgnoreCase("Offers")) {
            Intent i = new Intent(context, AdsMerchantsListActivity.class);
            i.putExtra("id", object.merchant_category_id);
            i.putExtra("name", object.name);
            i.putExtra("lat", String.valueOf(latitude));
            i.putExtra("long", String.valueOf(longitude));
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            Intent i = new Intent(context, MerchantsListActivity.class);
            i.putExtra("id", object.merchant_category_id);
            i.putExtra("name", object.name);
            i.putExtra("lat", String.valueOf(latitude));
            i.putExtra("long", String.valueOf(longitude));
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void init(View rootView) {
        //new 26/12/2017
        mRecyclerView_networkPartner = (RecyclerView) rootView.findViewById(R.id.rv_network_partner_home);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_home_list);
        cardView = (CardView) rootView.findViewById(R.id.card_network_partner_home);
        animAlpha2 = AnimationUtils.loadAnimation(context, R.anim.on_click_no_repeate);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLinearLayoutManager = (LinearLayoutManager) mRecyclerView_networkPartner.getLayoutManager();
        try {
            fab = (ImageView) rootView.findViewById(R.id.fab);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(fab);
            Glide.with(this).load(R.raw.chat_bot_rob).into(imageViewTarget);
        } catch (Exception e) {
            MLog.e("", "Debug from onCreate Exception" + e.toString());

        }

        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView_networkPartner.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        mRecyclerView_networkPartner.setLayoutManager(mLayoutManager);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        gethour = String.valueOf(hour);
        getminute = String.valueOf(minute);

        device_id = Utility.getInstance().getDeviceID(context);
        redId = LocalStore.getInstance().getGcmId(context);
        Ph_Number = LocalStore.getInstance().getPhoneNumber(context);
        ID = LocalStore.getInstance().getUserID(context);

        mRequestType = IConstant_WebService.WSR_Home_List;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(getActivity())) {
            Utility.getInstance().showLoading(pDialog, context);
            requestCommon = new RequestCommon();
 //           requestCommon.mobile_number = MobileNumber;
            RequestManager.HomeFragment(context, this, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(getActivity(), Constant.Alart_Internet, true);
        }
    }

    public void showRecycleViewView() {
        adapter_list = new Home_CategoryAdapter(context, arrayList);
        mRecyclerView.setAdapter(adapter_list);
        Utility.getInstance().runLayoutAnimation(mRecyclerView, context);
        checkVersion();
        checkLogOutUser();
    }

    private void checkVersion() {
        String localVersion = getResources().getString(R.string.versionName);
        MLog.e("localVersion----->", "" + localVersion);
        MLog.e("latestVersion----->", "" + version_code);
        if (version_code!=null && !version_code.equals("")) {
            if (!version_code.equals(localVersion)){
                AlertDialogFinish.Update(getActivity(), Constant.Alert_Update_ReqMsg, true);
            }
        }
    }

    private void checkLogOutUser() {
        MLog.e("logout_status----->", "" + logout_status);
        if (logout_status!=null && logout_status.equals("1")) {
            logOut();
        }
    }

    private void logOut() {
        LocalStore.getInstance().ClearData(context);
        Intent i = new Intent(context, Register.class);
        startActivity(i);
        HomeNavigation.homeNavigation.finish();
        getActivity().overridePendingTransition(R.anim.fade_in_1, R.anim.fade_out_1);
    }

   /* public boolean isLocationPermissionGranted(int type) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getLatitude_Longitude(type);
                return true;
            } else {
                //   Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            getLatitude_Longitude(type);
            return true;
        }*/

        public boolean isLocationPermissionGranted(int type) {
            getLatitude_Longitude(type);
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if( perms.get(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("record audio permission denied", context);
                    getActivity().finish();
                } else if( perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("camera permission denied", context);
                    getActivity().finish();
                } else if( perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    getActivity().finish();
                } /*else if( perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("phone call permission denied android", context);
                    getActivity().finish();
                }*/else {
       //             showRecycleViewView();
                }
                if( perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("external storage permission denied", context);
                    getActivity().finish();
                }else {
                    showRecycleViewView();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestAndroidPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("ACCESS_COARSE_LOCATION");
            if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
                permissionsNeeded.add("RECORD_AUDIO");
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("CAMERA");
            /*if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
                permissionsNeeded.add("CALL_PHONE");*/
            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }else{
                showRecycleViewView();
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            showRecycleViewView();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        try {
            if (requestType == IConstant_WebService.WSR_Home_List) {
                CommonResponse res = (CommonResponse) responseObj;
                if (res != null) {
                    MLog.e("res", "" + res.toString());
                    if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setData(res);
                    } else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(getActivity(), res.msg, true);
                    }
                }else {
                    AlertDialogFinish.Show(getActivity(), Constant.Alart_Status500, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), context);
        }
    }

    private void setData(CommonResponse res) {
        try {
            version_code = res.version_code;
            logout_status = res.logout_status;
            doctor_online_status = res.doctor_online_status;
            if (res.categoryResponses != null) {
                arrayList.clear();
                if (res.categoryResponses.size() > 0) {
                    for (int i = 0; i < res.categoryResponses.size(); i++) {
                        Home_Type_Bean bean = new Home_Type_Bean();
                        bean.setCategory_id(res.categoryResponses.get(i).category_id);
                        bean.setHospital_id(res.categoryResponses.get(i).hospital_id);
                        bean.setCount(res.categoryResponses.get(i).doctors_online_count);
                        bean.setTypeName(res.categoryResponses.get(i).name);
                        bean.setImage(res.categoryResponses.get(i).image);
                        bean.setType(Home_Type_Bean.TYPE_CATEGORY);
                        arrayList.add(bean);
                    }
                    requestAndroidPermissions();
                } else {
                    Utility.getInstance().showToast("Category List is Empty", context);
                }
            }
            if (res.merchant_categories_list != null) {
                arrayListCategory.clear();
                if (res.merchant_categories_list.size() > 0) {
                    for (int i = 0; i < res.merchant_categories_list.size(); i++) {
                        Merchant_CategoriesListResponse response = new Merchant_CategoriesListResponse();
                        response.name = res.merchant_categories_list.get(i).name;
                        response.icon = res.merchant_categories_list.get(i).icon;
                        response.merchant_category_id = res.merchant_categories_list.get(i).merchant_category_id;
                        arrayListCategory.add(response);
                    }
                    adapter = new Network_Partners_Category_Adapter(context, arrayListCategory);
                    mRecyclerView_networkPartner.setAdapter(adapter);
                    Utility.getInstance().runLayoutAnimation(mRecyclerView_networkPartner, context);
                } else {
                    Utility.getInstance().showToast("Merchant categories List is Empty", context);
                }
            }
            //           setCategory();
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(getActivity(), e.toString(), true);
        }
    }

    @Override
    public void onCategoryClicked(Home_Type_Bean object) {
        category_id = object.getCategory_id();
        Name = object.getTypeName();
        LocalStore.getInstance().setCategory(context, Name);
        MLog.e("Name", "" + Name);
 //       isLocationPermissionGranted(2);
        moveToDoctorActivity();
    }

 /*   private boolean getLatitude_Longitude(int type) {
        boolean status = false;
        GPSTracker gpsTracker = new GPSTracker(context, getActivity());
        gpsTracker.getLocation();
        if (gpsTracker.canGetLocation()) {
            latitude =  gpsTracker.getLatitude();
            longitude =  gpsTracker.getLongitude();
            if (latitude != 0.0) {
                String latLongString = String.valueOf(latitude) +","+ String.valueOf(longitude);
                MLog.e("current_latLongString:", "" + latLongString);
                if (type==2) {
                    moveToDoctorActivity();
                } else if (type == 1) {
                    moveToTopCategory();
                }
                status = true;
            } else {
                Utility.getInstance().showToast("Can't get your location try again !!!", context);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
        return status;
    }*/

    private boolean getLatitude_Longitude(int type) {
        boolean status = false;
        GPSTracker gpsTracker = new GPSTracker(context, getActivity());
        gpsTracker.getLocation();
        latitude =  13.0827;
        longitude =  80.2707;
        if (gpsTracker.canGetLocation()) {
            latitude =  13.0827;
            longitude =  80.2707;
            if (latitude != 0.0) {
                String latLongString = String.valueOf(latitude) +","+ String.valueOf(longitude);
                MLog.e("current_latLongString:", "" + latLongString);
                if (type==2) {
                    moveToDoctorActivity();
                } else if (type == 1) {
                    moveToTopCategory();
                }
                status = true;
            } else {
                String latLongString = String.valueOf(latitude) +","+ String.valueOf(longitude);
                MLog.e("current_latLongString:", "" + latLongString);
                if (type==2) {
                    moveToDoctorActivity();
                } else if (type == 1) {
                    moveToTopCategory();
                }
                status = true;
            }
        } else {
            String latLongString = String.valueOf(latitude) +","+ String.valueOf(longitude);
            MLog.e("current_latLongString:", "" + latLongString);
            if (type==2) {
                moveToDoctorActivity();
            } else if (type == 1) {
                moveToTopCategory();
            }
            status = true;
        }
        return status;
    }

    @Override
    public void onAdsClicked(Home_Type_Bean object) {
        String title = object.getTitleAds();
        String desc = object.getDescAds();
        String date = object.getDateAds();
        String type = object.getTypeAds();
        String imagePath = object.getImageAds();
        MLog.e("imagePath", "" + imagePath);
        Intent i = new Intent(context, HealthTubeDetail.class);
        i.putExtra("title", title);
        i.putExtra("desc", desc);
        i.putExtra("date", date);
        i.putExtra("type", type);
        i.putExtra("image", imagePath);
        i.putExtra(Constant.isFrom, "HomeFragment");
        context.startActivity(i);
        ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onSelectedTopCategory(Merchant_CategoriesListResponse object) {
        this.object = object;
        isLocationPermissionGranted(1);
    }
}
