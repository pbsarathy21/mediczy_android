package app.mediczy_com;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.sinch.android.rtc.SinchError;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.mediczy_com.ChatBot.Report_ListActivity;
import app.mediczy_com.adapter.HomeMenuListAdapter;
import app.mediczy_com.ambulance_list.AmbulanceListFragment;
import app.mediczy_com.appointment.DoctorAppointmentFragment;
import app.mediczy_com.appointment.YourAppointmentFragment;
import app.mediczy_com.bean.BeanHomeNavigation;
import app.mediczy_com.chat.FragmentChat;
import app.mediczy_com.chat.chat_new.ConversationListActivity;
import app.mediczy_com.chat.chat_new.NotificationObserver;
import app.mediczy_com.dashboard.HomeFragment;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.doctor_prescribed.Doctor_PrescribedFragment;
import app.mediczy_com.gps.GPSTracker;
import app.mediczy_com.health_card.HealthCard;
import app.mediczy_com.health_card.HealthCardListActivity;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.insurance.HealthInsuranceFragment;
import app.mediczy_com.health_tube.HealthTubeFragment;
import app.mediczy_com.profile.ProfileImageObserver;
import app.mediczy_com.profile.Profile_Edit;
import app.mediczy_com.reports_fragment.Reports_Fragment;
import app.mediczy_com.service_broadcast.ServiceOnlineStatus;
import app.mediczy_com.settings_help.Setting_Fragment;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.TextViewFont;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.video_call.updated.SinchService;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by prithivi on 21-Jan-16.
 */

public class HomeNavigation extends AppCompatActivity implements ResponseListener, ProfileImageObserver,
        NotificationObserver, SinchService.StartFailedListener, ServiceConnection {

    private static final int REQUEST_LOCATION1 = 1;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawer, Rl_logo_top, viewLogo, rlChat, rlInsuranceEdit;
    private TextView TvName, TvStartLetter, imageButton_title, tvNotification;
    private TextViewFont tvEditInsurance, tvInsuranceList;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private HomeMenuListAdapter mMenuAdapter;
    public static HomeNavigation homeNavigation;
    private ArrayList<BeanHomeNavigation> array_title = new ArrayList<>();
    private CircleImageView IvLogoDoctor;
    private RadioButton toggleButton;
    private ImageLoader imageLoader;
    private boolean isOnline = true;
    private String device_id, redId, Ph_Number, ID, ReqType = "", responseStatus, latLongString,
            responseMsg, newNotification="", UserType, type="", doctorProfileImage, gethour,
            getminute, session_select_time, current_time, health_card_number;
    private int year, month, day, hour, minute, second;

    private Fragment home_fragment = new HomeFragment();
    private Fragment ambulance_fragment = new AmbulanceListFragment();
    private Fragment doctor_prescribedFragment = new Doctor_PrescribedFragment();
    private Fragment settingsFragment = new Setting_Fragment();
    private Fragment doctorAppointmentFragment = new DoctorAppointmentFragment();
    private Fragment yourAppointmentFragment = new YourAppointmentFragment();
//    private Fragment notificationFragment = new HealthTubeFragment();
    private Fragment chatFragment = new FragmentChat();
    private HealthInsuranceFragment healthInsuranceFragment = new HealthInsuranceFragment();

    private FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    final private int REQUEST_LOCATION_PERMISSIONS=10;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 11;

    private ProgressDialog pDialog = null;
    private RequestCommon requestCommon;
    private int mRequestType = 0, position, availableStatus=0;
    private SinchService.SinchServiceInterface mSinchServiceInterface;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation);
        this.savedInstanceState = savedInstanceState;
        homeNavigation = this;
        init();
        requestAndroidPermissions();
        Rl_logo_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileActivity();
            }
        });
        if (newNotification!=null && !newNotification.equals(""))
            showConversationActivity();
        setNavigationData();
        defaultNavigationSelectedItem();
        setSessionTime(false);
    }

    private void init() {
        setToolBar();
        Rl_logo_top = (RelativeLayout) findViewById(R.id.logo_view_top);
        TvStartLetter = (TextView) Rl_logo_top.findViewById(R.id.tv_logo_top_photo1);
        IvLogoDoctor = (CircleImageView) Rl_logo_top.findViewById(R.id.imageView_logo_top_photo);
        TvName = (TextView) Rl_logo_top.findViewById(R.id.textview1_name);
//        ImageView IvEdit = (ImageView) Rl_logo_top.findViewById(R.id.imageView1_edit_logo_top);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawer = (RelativeLayout) findViewById(R.id.drawer);
        viewLogo = (RelativeLayout) findViewById(R.id.logo_view_bottom);
        viewLogo.setVisibility(View.GONE);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        gethour = String.valueOf(hour);
        getminute = String.valueOf(minute);

        TvName.setText(LocalStore.getInstance().getFirstName(this));
        device_id = Utility.getInstance().getDeviceID(this);
        redId = LocalStore.getInstance().getGcmId(this);
        Ph_Number = LocalStore.getInstance().getPhoneNumber(this);
        ID = LocalStore.getInstance().getUserID(this);
        type = LocalStore.getInstance().getType(this);
        setDoctorLogo();

        String name = LocalStore.getInstance().getFirstName(this);
        name = String.valueOf(name.charAt(0));
        MLog.e("Name_FirstLetter", "" + name);
        TvStartLetter.setText(name);

        if (type.equalsIgnoreCase("Doctor")) {
            Rl_logo_top.setEnabled(true);
            IvLogoDoctor.setVisibility(View.VISIBLE);
            TvStartLetter.setVisibility(View.GONE);
        } else{
            Rl_logo_top.setEnabled(false);
            IvLogoDoctor.setVisibility(View.GONE);
            TvStartLetter.setVisibility(View.VISIBLE);
        }
    }

    private void defaultNavigationSelectedItem() {
        if (savedInstanceState == null) {
            if (type.equalsIgnoreCase("Doctor")) {
                selectItemDoctor(0);
            }if (type.equalsIgnoreCase("Patient")) {
                selectItemPatient(0);
            }
        }
    }

    private void showConversationActivity() {
        Intent i = new Intent(HomeNavigation.this, ConversationListActivity.class);
        i.putExtra("Id", UserType);
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        tvNotification.setVisibility(View.GONE);
    }

    private void showEditProfileActivity() {
        Intent profile = new Intent(HomeNavigation.this, Profile_Edit.class);
        startActivity(profile);
        overridePendingTransition(R.anim.fade_in_1,R.anim.fade_out_1);
    }

    private void showChatIconToolbar() {
        rlInsuranceEdit.setVisibility(View.GONE);
        rlChat.setVisibility(View.VISIBLE);
    }

    public void showEditInsuranceIconToolbar() {
/*        new Thread(new Runnable() {
            public void run() {
                rlChat.setVisibility(View.GONE);
                rlInsuranceEdit.setVisibility(View.VISIBLE);
            }
        }).start();*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlChat.setVisibility(View.GONE);
                rlInsuranceEdit.setVisibility(View.VISIBLE);
                String isInsuranceSubmitted = LocalStore.getInstance().isInsuranceFormSubmitted(HomeNavigation.this);
                MLog.e("isInsuranceSubmitted :", "" + isInsuranceSubmitted);
                if (isInsuranceSubmitted.equalsIgnoreCase("Yes")) {
                    tvEditInsurance.setVisibility(View.GONE);
                    tvInsuranceList.setVisibility(View.VISIBLE);
                }else {
                    tvInsuranceList.setVisibility(View.GONE);
                    tvEditInsurance.setVisibility(View.VISIBLE);
                }
            }
        }, 200);
    }

    private void setToolBar() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
            newNotification = bundle.getString("newNotification");
        toolbar = (Toolbar) findViewById(R.id.toolbar_home_navigation);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        RelativeLayout mCustomView = (RelativeLayout) findViewById(R.id.home_tool_action);
        rlInsuranceEdit = (RelativeLayout) findViewById(R.id.relative_custom_action_bar_edit_insurance);
        rlChat = (RelativeLayout) findViewById(R.id.relative_custom_action_bar_chat_menu);
        TextViewFont tvCardBuy = (TextViewFont) mCustomView.findViewById(R.id.imageButton__custom_action_bar_back_card_buy);
        tvEditInsurance = (TextViewFont) mCustomView.findViewById(R.id.tv_actionBar_edit_insurance);
        tvInsuranceList = (TextViewFont) mCustomView.findViewById(R.id.tv_actionBar_insurance_list);
        ImageView tvChat = (ImageView) mCustomView.findViewById(R.id.imageButton__custom_action_bar_back_chat);
        imageButton_title = (TextView) mCustomView.findViewById(R.id.textView__custom_action_bar);
        tvNotification = (TextView) mCustomView.findViewById(R.id.tv_left_chat_notification);
        toggleButton = (RadioButton) mCustomView.findViewById(R.id.toggleButton_online_offline);
        tvNotification.setVisibility(View.GONE);
        rlInsuranceEdit.setVisibility(View.GONE);
        UserType = LocalStore.getInstance().getType(HomeNavigation.this);
        MLog.e("UserType", "" + UserType);
        if (UserType.equalsIgnoreCase("Patient")) {
            toggleButton.setVisibility(View.GONE);
            tvCardBuy.setVisibility(View.VISIBLE);
        } else if (UserType.equalsIgnoreCase("Doctor")) {
            toggleButton.setVisibility(View.VISIBLE);
            tvCardBuy.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tvChat.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvChat.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)tvNotification.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvNotification.setLayoutParams(params1);
        }

        String Type = LocalStore.getInstance().getType(this);
        if (Type.equalsIgnoreCase("Patient")) {
            String Count = LocalStore.getInstance().getCount(this);
            MLog.e("Count", "" + Count);
        }
        tvChat.setVisibility(View.VISIBLE);
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConversationActivity();
            }
        });

        tvCardBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToHealthCardActivity();
            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalStore.getInstance().set_doctorSessionTime(HomeNavigation.this, "0");
                MLog.e("isOnline", "" + isOnline);
                if (isOnline) {
                    availableStatus=0;
                    ReqType = "OnlineStatus";
                    mRequestType = IConstant_WebService.WSR_HomeNavigation_Session;
                    onRequest(ReqType, "offline");
                } else {
                    setSessionTime(false);
                }
            }
        });

        tvInsuranceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInsuranceList.setVisibility(View.GONE);
                tvEditInsurance.setVisibility(View.VISIBLE);
                healthInsuranceFragment.showInsuranceForm();
            }
        });
        tvEditInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInsuranceList.setVisibility(View.VISIBLE);
                tvEditInsurance.setVisibility(View.GONE);
                healthInsuranceFragment.showInsuranceList();
            }
        });
    }

    private void onRequest(String ReqType, String online_status) {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.doctor_id = ID;
            requestCommon.login_time = current_time;
            requestCommon.status = String.valueOf(availableStatus);
            requestCommon.session_time = session_select_time;
            requestCommon.mobile_number = Ph_Number;
            requestCommon.onlineStatus = online_status;
            requestCommon.health_card_number = online_status;
            if (ReqType.equalsIgnoreCase("session_time"))
                RequestManager.HomeNavigation_SessionTime(this, null, requestCommon, mRequestType);
            else if (ReqType.equalsIgnoreCase("OnlineStatus"))
                RequestManager.HomeNavigation_SessionTime(this, null, requestCommon, mRequestType);
            else if (ReqType.equalsIgnoreCase("HealthCardCheckAvailable"))
                RequestManager.CheckHealthCardAvailable(this, null, requestCommon, mRequestType);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    private void startOnlineService() {
        if (type.equalsIgnoreCase("Doctor"))
        {
            if (Utility.getInstance().isConnectingToInternet(this)) {
                Intent msgIntent = new Intent(this, ServiceOnlineStatus.class);
                msgIntent.putExtra(ServiceOnlineStatus.SERVICE_IN_MSG, "Show_Toast");
                startService(msgIntent);
            }
        }
    }

    private void setSessionTime(boolean dialogCancel) {
        if (type.equalsIgnoreCase("Doctor")) {
            String savedDate = LocalStore.getInstance().get_doctorSessionTime(this);
            MLog.e("savedDate", "" + savedDate);
            if (!savedDate.equalsIgnoreCase("0")) {
                try {
                    if (Utility.getInstance().isExpire(savedDate)) {
                        custom_dialog_session_time(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogFinish.Show(this, e.toString(), true);
                }
            }else{
                custom_dialog_session_time(dialogCancel);
            }
        }
    }

    private void setDoctorLogo() {
        String imagePath = LocalStore.getInstance().getDoctorImage(this);
        doctorProfileImage = imagePath;
        MLog.e("doctorProfileImage->", "" + doctorProfileImage);
        imageLoader=new ImageLoader(this);
        imageLoader.DisplayImage(doctorProfileImage, IvLogoDoctor, 4);
    }

    private void moveToHealthCardActivity() {
        String sessionHealthCard_Available = LocalStore.getInstance().getsetHealthCard_Available(this);
        MLog.e("sessionHealthCard_Available->", "" + sessionHealthCard_Available);
        if (sessionHealthCard_Available.equalsIgnoreCase("yes")) {
            Intent i = new Intent(HomeNavigation.this, HealthCard.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else {
            Intent intent = new Intent(HomeNavigation.this, HealthCardListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void moveToNetworkPartners() {
        if (Utility.getInstance().isConnectingToInternet(HomeNavigation.this)) {
            if (isLocationPermissionGranted()) {
                if (isLatitudeReceived()) {
                    LocalStore.getInstance().setLatitude_Longitude(HomeNavigation.this, latLongString);
                    fragmentTransaction.replace(R.id.content_frame, healthInsuranceFragment);
                    showEditInsuranceIconToolbar();
                }
            }
        } else {
            AlertDialogFinish.Show(HomeNavigation.this, Constant.Alart_Internet, true);
        }
    }

    private void moveToReportListActivity() {
        Intent intent = new Intent(HomeNavigation.this, Report_ListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void moveToAmbulanceList() {
        if (Utility.getInstance().isConnectingToInternet(HomeNavigation.this)) {
            if (isLocationPermissionGranted()) {
                if (isLatitudeReceived()) {
                    LocalStore.getInstance().setLatitude_Longitude(HomeNavigation.this, latLongString);
                    fragmentTransaction.replace(R.id.content_frame, ambulance_fragment);
                }
            }
        } else {
            AlertDialogFinish.Show(HomeNavigation.this, Constant.Alart_Internet, true);
        }
    }

    private void moveReportsFragment(String url, FragmentTransaction ft) {
        Fragment reports_fragment = new Reports_Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("url", url);
        reports_fragment.setArguments(bundle);
 //       ft.replace(R.id.content_frame, reports_fragment);
        fragmentTransaction.replace(R.id.content_frame, reports_fragment);
    }

    private boolean isLatitudeReceived() {
        boolean status = false;
        GPSTracker gpsTracker = new GPSTracker(HomeNavigation.this, HomeNavigation.this);
        gpsTracker.getLocation();
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            if (latitude != 0.0) {
                latLongString = String.valueOf(latitude) +","+ String.valueOf(longitude);
                MLog.e("current_latLongString:", "" + latLongString);
                status = true;
            } else {
                Utility.getInstance().showToast("Can't get your location try again !!!", getApplicationContext());
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
        return status;
    }

    private void setNavigationData() {
        setMenuItemsName_Icon();
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mMenuAdapter = new HomeMenuListAdapter(this, array_title, "HomeNavigation");
        mDrawerList.setAdapter(mMenuAdapter);
        mMenuAdapter.notifyDataSetChanged();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
//                getSupportActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };
        setDrawerAction();
    }

    /*private void setMenuItemsName_Icon() {
        String[] menuTitle = null;
        String[] menuIcon = null;
        if (type.equalsIgnoreCase("Doctor")) {
            menuTitle = new String[] {getResources().getString(R.string.Category_Doctor), getResources().getString(R.string.Network_Partners_Doctor),
                    getResources().getString(R.string.Doctor_Prescribed_Patient), getResources().getString(R.string.My_Report_Doctor),
                    getResources().getString(R.string.My_Calender_Doctor), getResources().getString(R.string.My_Appointment_Doctor),
                    getResources().getString(R.string.My_Insurance_Doctor), getResources().getString(R.string.Settings_Doctor)};

            menuIcon = new String[] {getResources().getString(R.string.home_menu_Category), getResources().getString(R.string.home_menu_Hospital),
                    getResources().getString(R.string.home_menu_Prescription), getResources().getString(R.string.card_buy),
                    getResources().getString(R.string.home_menu_Appointment), getResources().getString(R.string.home_menu_Your_Appointment),
                    getResources().getString(R.string.home_menu_MyInsurance), getResources().getString(R.string.home_menu_setting)};
        }if (type.equalsIgnoreCase("Patient")) {
            menuTitle = new String[] {getResources().getString(R.string.Category_Patient), getResources().getString(R.string.sys_lis), getResources().getString(R.string.Network_Partners_Patient),
                    getResources().getString(R.string.Doctor_Prescribed_Patient), getResources().getString(R.string.My_Report_Doctor),
                    getResources().getString(R.string.My_Appointment_Patient), getResources().getString(R.string.My_Insurance_Patient),
                    getResources().getString(R.string.Settings_Patient), getResources().getString(R.string.Share_Patient)};

            menuIcon = new String[] {getResources().getString(R.string.home_menu_Category), getResources().getString(R.string.sim_li), getResources().getString(R.string.home_menu_Hospital),
                    getResources().getString(R.string.home_menu_Prescription) ,getResources().getString(R.string.Award),
                    getResources().getString(R.string.home_menu_Your_Appointment), getResources().getString(R.string.home_menu_MyInsurance),
                    getResources().getString(R.string.home_menu_setting), getResources().getString(R.string.home_menu_share)};
        }
        for (int i=0; i<menuTitle.length; i++) {
            BeanHomeNavigation beanHOME = new BeanHomeNavigation();
            beanHOME.setTitle(menuTitle[i]);
            beanHOME.setIcon(menuIcon[i]);
            array_title.add(beanHOME);
        }
    }*/

    private void setMenuItemsName_Icon() {
        String[] menuTitle = null;
        String[] menuIcon = null;
        if (type.equalsIgnoreCase("Doctor")) {
            menuTitle = new String[] {getResources().getString(R.string.Category_Doctor), getResources().getString(R.string.Network_Partners_Doctor),
                    getResources().getString(R.string.Doctor_Prescribed_Patient),
                    getResources().getString(R.string.My_Calender_Doctor), getResources().getString(R.string.My_Appointment_Doctor),
                    getResources().getString(R.string.My_Insurance_Doctor), getResources().getString(R.string.Settings_Doctor)};

            menuIcon = new String[] {getResources().getString(R.string.home_menu_Category), getResources().getString(R.string.home_menu_Hospital),
                    getResources().getString(R.string.home_menu_Prescription),
                    getResources().getString(R.string.home_menu_Appointment), getResources().getString(R.string.home_menu_Your_Appointment),
                    getResources().getString(R.string.home_menu_MyInsurance), getResources().getString(R.string.home_menu_setting)};
        }if (type.equalsIgnoreCase("Patient")) {
            menuTitle = new String[] {getResources().getString(R.string.Category_Patient), getResources().getString(R.string.sys_lis), getResources().getString(R.string.Network_Partners_Patient),
                    getResources().getString(R.string.Doctor_Prescribed_Patient),
                    getResources().getString(R.string.My_Appointment_Patient), getResources().getString(R.string.My_Insurance_Patient),
                    getResources().getString(R.string.Settings_Patient), getResources().getString(R.string.Share_Patient)};

            menuIcon = new String[] {getResources().getString(R.string.home_menu_Category), getResources().getString(R.string.sim_li), getResources().getString(R.string.home_menu_Hospital),
                    getResources().getString(R.string.home_menu_Prescription) ,
                    getResources().getString(R.string.home_menu_Your_Appointment), getResources().getString(R.string.home_menu_MyInsurance),
                    getResources().getString(R.string.home_menu_setting), getResources().getString(R.string.home_menu_share)};
        }
        for (int i=0; i<menuTitle.length; i++) {
            BeanHomeNavigation beanHOME = new BeanHomeNavigation();
            beanHOME.setTitle(menuTitle[i]);
            beanHOME.setIcon(menuIcon[i]);
            array_title.add(beanHOME);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SliderControl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SliderControl() {
        if (mDrawerLayout.isDrawerOpen(mDrawer)) {
            mDrawerLayout.closeDrawer(mDrawer);
        } else {
            mDrawerLayout.openDrawer(mDrawer);
        }
    }

    @Override
    public void onImageUpdated(boolean status) {
        setDoctorLogo();
    }

    @Override
    public void onNotificationReceived(Boolean status) {
        MLog.e("onNotificationReceived", "Received: " + status);
        runOnUiThread(new Runnable() {
            public void run() {
                tvNotification.setVisibility(View.VISIBLE);
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerLayout.closeDrawer(mDrawer);
            mMenuAdapter.onCurrentParent = position;
            mMenuAdapter.notifyDataSetChanged();
            if (type.equalsIgnoreCase("Doctor")) {
                selectItemDoctor(position);
            }if (type.equalsIgnoreCase("Patient")) {
                selectItemPatient(position);
            }
        }
    }

  /*  private void selectItemPatient(int position) {
        showChatIconToolbar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentTransaction = ft;
        this.position = position;
        switch (position) {
            case 0:
                ft.replace(R.id.content_frame, home_fragment);
                break;
            case 1:
                moveToReportListActivity();
                break;
            case 2:
                moveToAmbulanceList();
                break;
            case 3:
                ft.replace(R.id.content_frame, doctor_prescribedFragment);
                break;
            case 4:
                String reports = IConstant_WebService.reports;
                moveReportsFragment(reports, ft);
                break;
            case 5:
                ft.replace(R.id.content_frame, yourAppointmentFragment);
                break;
            case 6:
                moveToNetworkPartners();
                break;
            case 7:
                ft.replace(R.id.content_frame, settingsFragment);
                break;
            case 8:
                share();
                break;
        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);
        setTitle(array_title.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawer);
    }*/

    private void selectItemPatient(int position) {
        showChatIconToolbar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentTransaction = ft;
        this.position = position;
        switch (position) {
            case 0:
                ft.replace(R.id.content_frame, home_fragment);
                break;
            case 1:
                moveToReportListActivity();
                break;
            case 2:
                moveToAmbulanceList();
                break;
            case 3:
                ft.replace(R.id.content_frame, doctor_prescribedFragment);
                break;
            case 4:
                ft.replace(R.id.content_frame, yourAppointmentFragment);
                break;
            case 5:
                moveToNetworkPartners();
                break;
            case 6:
                ft.replace(R.id.content_frame, settingsFragment);
                break;
            case 7:
                share();
                break;
            case 8:

                break;
        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);
        setTitle(array_title.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawer);
    }

   /* private void selectItemDoctor(int position) {
        showChatIconToolbar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentTransaction=ft;
        switch (position) {
            case 0:
                ft.replace(R.id.content_frame, home_fragment);
                break;
            case 1:
                moveToAmbulanceList();
                break;
            case 2:
                ft.replace(R.id.content_frame, doctor_prescribedFragment);
                break;
            case 3:
                String reports = IConstant_WebService.reports;
                moveReportsFragment(reports, ft);
                break;
            case 4:
                ft.replace(R.id.content_frame, doctorAppointmentFragment);
                break;
            case 5:
                ft.replace(R.id.content_frame, yourAppointmentFragment);
                break;
            case 6:
                moveToNetworkPartners();
                break;
            case 7:
                ft.replace(R.id.content_frame, settingsFragment);
                break;
        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);
        setTitle(array_title.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawer);
    }
*/

    private void selectItemDoctor(int position) {
        showChatIconToolbar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentTransaction=ft;
        switch (position) {
            case 0:
                ft.replace(R.id.content_frame, home_fragment);
                break;
            case 1:
                moveToAmbulanceList();
                break;
            case 2:
                ft.replace(R.id.content_frame, doctor_prescribedFragment);
                break;
            case 3:
                ft.replace(R.id.content_frame, doctorAppointmentFragment);
                break;
            case 4:
                ft.replace(R.id.content_frame, yourAppointmentFragment);
                break;
            case 5:
                moveToNetworkPartners();
                break;
            case 6:
                ft.replace(R.id.content_frame, settingsFragment);
                break;
            case 7:

                break;
        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);
        setTitle(array_title.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawer);
    }

    public void navigateToMyAppointment() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectItemPatient(5);
            }
        }, 100);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle!=null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
        imageButton_title.setText(title);
    }

    private void setDrawerAction() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                //               setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (toolbar != null) {
//            toolbar.setTitle(getString(R.string.app_name));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_HomeNavigation_Session ||
                requestType == IConstant_WebService.WSR_HealthCardCheckAvailable) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                setData(req);
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                if (requestType == IConstant_WebService.WSR_HomeNavigation_Session) {
                    custom_dialog_session_time(false);
                }
            }
        }
    }

    private void setData(CommonResponse req) {
        String responseSuccess = req.status;
        responseMsg = req.msg;
        MLog.e("responseSuccess", "" + responseSuccess);
        MLog.e("msg", "" + responseMsg);
        if (ReqType.equalsIgnoreCase("session_time")) {
            if (responseSuccess != null) {
                if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    LocalStore.getInstance().set_doctorSessionTime(HomeNavigation.this, session_select_time);
                    availableStatus=1;
                    isOnline = true;
                    toggleButton.setText("Online");
                    toggleButton.setTextColor(getResources().getColor(R.color.white));
                    Utility.getInstance().showToast(responseMsg, getApplicationContext());
                }
                if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    Utility.getInstance().showToast(responseMsg, getApplicationContext());
                    custom_dialog_session_time(false);
                }
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
                custom_dialog_session_time(false);
            }
        }if (ReqType.equalsIgnoreCase("OnlineStatus")) {
            if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                availableStatus=0;
                isOnline = false;
                toggleButton.setText("Offline");
                toggleButton.setTextColor(getResources().getColor(R.color.red));
            } if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                AlertDialogFinish.Show(this, responseMsg, true);
            }
        }if (ReqType.equalsIgnoreCase("HealthCardCheckAvailable")) {
            if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                LocalStore.getInstance().setHealthCard_Available(this, "yes");
                Intent i = new Intent(HomeNavigation.this, HealthCard.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            } if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                AlertDialogFinish.Show(this, responseMsg, true);
            }
        }
    }

    private void custom_dialog_session_time(boolean dialogCancel) {
        final Dialog dialog = new Dialog(HomeNavigation.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_onlinetime);
        dialog.setCancelable(dialogCancel);
        final Button btn_add = (Button) dialog.findViewById(R.id.button_time);
        Spinner spTime = (Spinner) dialog.findViewById(R.id.spinner_time);
        // str_time_select= (String) spinnertime.getSelectedItem();
        ArrayAdapter<String> time_select = new ArrayAdapter<String>(HomeNavigation.this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.time));
        time_select.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(time_select);
        spTime.setSelection(0);

        toggleButton.setText("Offline");
        toggleButton.setTextColor(getResources().getColor(R.color.red));

        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition() == 0) {
                    session_select_time = Utility.getInstance().getTimeMinAfter(30);
                    MLog.e("server_select_time", session_select_time);
                } else if (parent.getSelectedItemPosition() == 1) {
                    session_select_time = Utility.getInstance().getTimeHoursAfter(1);
                    MLog.e("server_select_time", session_select_time);
                } else if (parent.getSelectedItemPosition() == 2) {
                    session_select_time = Utility.getInstance().getTimeHoursAfter(2);
                    MLog.e("server_select_time", session_select_time);
                } else if (parent.getSelectedItemPosition() == 3) {
                    session_select_time = Utility.getInstance().getTimeHoursAfter(3);
                    MLog.e("server_select_time", session_select_time);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                String today = Utility.getInstance().getToday("MMM-dd-yyyy hh:mm:ss a");
                current_time = today;
                availableStatus=1;
                ReqType = "session_time";
                mRequestType = IConstant_WebService.WSR_HomeNavigation_Session;
                onRequest(ReqType, "");
            }
        });
        dialog.show();
    }

    private void share() {
        String refId = LocalStore.getInstance().getRefId(HomeNavigation.this);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Mediczy Reference Code : "+refId);
/*
        sharingIntent.setType("image/png");
//             sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        sharingIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(ViewDetailNavigation.this, bitmap));
*/
        sharingIntent.putExtra(Intent.EXTRA_TEXT, ("" + getResources().getString(R.string.invite_message)
                +" My Ref ID: "+ refId + " : "+ Constant.PlayStorePath));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestAndroidPermissions() {
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
            if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
                permissionsNeeded.add("CALL_PHONE");
            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
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
            }else {
                sinchService();
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            sinchService();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                Map<String, Integer> permsCapture = new HashMap<String, Integer>();
                permsCapture.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    permsCapture.put(permissions[i], grantResults[i]);
                if (permsCapture.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(position == 1)
                        moveToAmbulanceList();
                    else if (position == 6 || position == 5) {
                        moveToNetworkPartners();
                    }
                }else{
                    Utility.getInstance().showToast("Location permission denied", getApplicationContext());
                }
                break;
           case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(android.Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if( perms.get(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("record audio permission denied", getApplicationContext());
                    finish();
                    return;
                } else if( perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("phone call permission denied", getApplicationContext());
                    finish();
                    return;
                } else if( perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("camera storage permission denied", getApplicationContext());
                    finish();
                    return;
                } else if( perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("external storage permission denied", getApplicationContext());
                    finish();
                    return;
                }else {
                   sinchService();
                }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            exitPopUp();
            return true;
        }
        return false;
    }

    private void exitPopUp() {
        new AlertDialog.Builder(this)
                .setTitle(Constant.Alart_AppName_Internet)
                .setMessage("Are you sure want to exit the app")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HomeNavigation.homeNavigation = null;
                       finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void sinchService() {
        getApplicationContext().bindService(new Intent(HomeNavigation.this, SinchService.class), HomeNavigation.this,
                BIND_AUTO_CREATE);
        try {
            final String userId = LocalStore.getInstance().getUserID(HomeNavigation.this);
            MLog.e("sinchService_userId", "" + userId);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getSinchServiceInterface()!=null) {
                        if (!getSinchServiceInterface().isStarted()) {
                            Utility.getInstance().showToast("Sinch Started", getApplicationContext());
                            getSinchServiceInterface().startClient(userId);
                        }
                    }
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

   @Override
    public void onStartFailed(SinchError error) {
       Utility.getInstance().showToast(error.toString(), getApplicationContext());
 //       dismissDialog();
    }

    @Override
    public void onStarted() {
//        dismissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION1:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
 //                       showToast("RESULT_CANCELED");
                        break;
                    }
                    case Activity.RESULT_OK: {
                        // The user was asked to change settings, but chose not to
                        GPSTracker gpsTracker = new GPSTracker(HomeNavigation.this, HomeNavigation.this);
                        gpsTracker.getLocation();
                        double latitude = 0, longitude=0;
                        if (gpsTracker.canGetLocation()) {
                            latitude = gpsTracker.getLatitude();
                            longitude = gpsTracker.getLongitude();
                        }
                        String latLongString = "Lat:" + latitude + "\nLong:" + longitude;
                        MLog.e("current_latLongString:", "" + latLongString);
                        Utility.getInstance().showToast("Wait a while to get your current location", getApplicationContext());
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }
}
