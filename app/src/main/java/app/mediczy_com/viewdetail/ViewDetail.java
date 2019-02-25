package app.mediczy_com.viewdetail;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import app.mediczy_com.R;
import app.mediczy_com.adapter.DetailDateTime_Adapter;
import app.mediczy_com.bean.BeanDetailTimeSlot;
import app.mediczy_com.chat.chat_new.ChatListActivity;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.patient_problem_form.PatientProblemForm;
import app.mediczy_com.payment.Payment;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.RecyclerItemClickListener;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.video_call.updated.CallScreenActivityVideoCall;
import app.mediczy_com.video_call.updated.SinchService;
import app.mediczy_com.video_call.updated.VideoCallBaseActivity_ViewDetail;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.request.ViewDetailRequest;
import app.mediczy_com.webservicemodel.response.CommonResponse;
/**
 * Created by Prithivi Raj on 04-01-2016.
 */
public class ViewDetail extends VideoCallBaseActivity_ViewDetail implements ResponseListener, SinchService.StartFailedListener {

    public static final String ACTION_CLOSE = "app.mediczy.VIEWDETAIL";
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
    private final int REQUEST_PHONE_PERMISSIONS = 1;

    public static ViewDetail viewDetail;
    private FirstReceiver firstReceiver;
    private Animation animationShake;
    private Toolbar toolBar;
    private FloatingActionButton Fab_call, FabCoupon;
    private Button btnAppointment, btnPay;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageLoader imageLoader;
    private ViewPager mViewPager;
    private TextView Tv_Degree, Tv_doctor_Name, Tv_transaction_fees, Tv_Price, Tv_Exp,
            Tv_Loc, Tv_Date, Tv_Time, Tv_Spec, Tv_language, Tv_licence, Tv_service;
    private CircularImageView Iv_Doctor_Image;
    private ImageView Iv_NoImage, Iv_CouponCode;
    private EditText Ed_Coupon;
    private CardView Rl_View;
    private LinearLayout Ll_View;
    private RelativeLayout Rl_DateTime, prevButton, nextButton, CoordinateScrollView,
            Rl_CouponCode, Rl_Chat;
    private Bitmap Doctor_ImageBitmap;
    private ArrayList<String> arrayListImage = new ArrayList<String>();
    private ArrayList<BeanDetailTimeSlot> arrayListTimeSlot = new ArrayList<BeanDetailTimeSlot>();
    private String device_id, redId, doctor_Id="", Name="", Date="", ReqType, Type, Number, isFrom, userId;
    private String responseMsg, resDoctor_Degree, resDoctorName="", resDoctorSpec, resPayStatus, resOnlineStatus,
            resDoctorExp, resDoctorImage, resDoctorPrice, resDoctorAddress, resServices, time_date_id,
    resFreeCode, resNumber="", resLanguageknown, resRegistrationlicense, categoryName="";

    private int pagerPosition = 0;
    private int mRequestType = 0;
    private ViewDetailRequest detailRequest;
    private RequestCommon requestCommon;
    private ProgressDialog pDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail);
        viewDetail = this;
        init();

        Iv_Doctor_Image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    Utility.getInstance().showImage(Iv_Doctor_Image, ViewDetail.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        Rl_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveChatListActivity();
            }
        });

        Fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog_callSelection();
 //               placeVideoCall();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resPayStatus.equalsIgnoreCase("Unpaid")) {
                    Intent i = new Intent(ViewDetail.this, Payment.class);
                    i.putExtra("Id", doctor_Id);
                    i.putExtra(Constant.isFrom, "ViewDetail");
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    Utility.getInstance().showToast("Already Paid", ViewDetail.this);
                }
            }
        });

        FabCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.e("resFreeCode", "" + resFreeCode);
                String edCode = Ed_Coupon.getText().toString();
                if (!edCode.equals("")) {
                    ReqType = "freeCode";
                    onRequest(ReqType);
                } else {
                    Utility.getInstance().showToast("Invalid Code", ViewDetail.this);
                }
            }
        });

        Iv_CouponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog_Coupon();
            }
        });

        Ed_Coupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    FabCoupon.setVisibility(View.GONE);
                } else {
                    FabCoupon.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsPopUp(2);
            }
        });
    }

    private void moveChatListActivity() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Doctor_ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream .toByteArray();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ViewDetail.this, ChatListActivity.class);
                i.putExtra("conversation_id", doctor_Id);
                i.putExtra("doctor_id", doctor_Id);
                i.putExtra("name", resDoctorName);
                i.putExtra("image", resDoctorImage);
                i.putExtra("user_type", "doctor");
  //              i.putExtra("BitmapImage", Doctor_ImageBitmap);
                i.putExtra("ImageByteArray", byteArray);
                i.putExtra(Constant.isFrom, "ViewDetail");
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(firstReceiver);
    }

    @Override
    protected void onPause() {
//        dismissDialog();
        super.onPause();
    }

    private void init() {
        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        CoordinateScrollView = (RelativeLayout) findViewById(R.id.rlCollapseScroll);
        toolBar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolBar);
 //       final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        try {
            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btnPay = (Button) findViewById(R.id.button_vd_pay);
        btnAppointment = (Button) findViewById(R.id.submit_patient_appointment);
        Fab_call = (FloatingActionButton) findViewById(R.id.fabBtn_vd_call);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextAppearance(View.SCROLLBAR_POSITION_DEFAULT);
        imageLoader=new ImageLoader(this);
        Rl_View = (CardView) findViewById(R.id.vd_logo);
        Ll_View = (LinearLayout) findViewById(R.id.inc_gallery);
        Rl_Chat = (RelativeLayout)toolBar.findViewById(R.id.rl_toolbar1_chat);

        mViewPager = (ViewPager) Ll_View.findViewById(R.id.vd_image_view_pager_image);
        prevButton = (RelativeLayout) Ll_View.findViewById(R.id.vd_image__Left_image);
        nextButton = (RelativeLayout) Ll_View.findViewById(R.id.vd_image__Right_image);
        Iv_NoImage = (ImageView)Ll_View.findViewById(R.id.vd_image_no_image);
        Tv_Degree = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_edu);
        Tv_Loc = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_loc);
        Tv_doctor_Name = (TextView)Rl_View.findViewById(R.id.textview_vd_logo_doctor_name);
        Tv_Price = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_price);
        Tv_transaction_fees = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_price_transaction_fees);
        Tv_Spec = (TextView)Rl_View.findViewById(R.id.textview_vd_logo_educa);
        Tv_Exp = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_exp);
        Tv_Date = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_time);
        Tv_Time = (TextView)Rl_View.findViewById(R.id.textview_vd_full_data_time_hours);
        Tv_language = (TextView) Rl_View.findViewById(R.id.textview_vd_full_data_language);
        Tv_licence = (TextView) Rl_View.findViewById(R.id.textview_vd_full_data_licence);
        Tv_service = (TextView) Rl_View.findViewById(R.id.textview_vd_full_data_service);
        Rl_DateTime = (RelativeLayout) Rl_View.findViewById(R.id.rl_vd_full_data_time);

        Iv_Doctor_Image = (CircularImageView)Rl_View.findViewById(R.id.imageView_vd_doctor_photo);
        Ed_Coupon = (EditText)Rl_View.findViewById(R.id.editText_view_detail_coupon_code);
        FabCoupon = (FloatingActionButton) findViewById(R.id.fab_coupon_code);
        Iv_CouponCode = (ImageView)Rl_View.findViewById(R.id.imageView1_vd_full_data_coupon_help);
        Rl_CouponCode = (RelativeLayout)Rl_View.findViewById(R.id.rl_view_detail_copon_code);
        FabCoupon.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        Name = bundle.getString("name");
        Date = bundle.getString("date");
        categoryName = bundle.getString("categoryName");
        isFrom = bundle.getString("isFrom");
        device_id = Utility.getInstance().getDeviceID(this);
        redId = LocalStore.getInstance().getGcmId(this);
        Type = LocalStore.getInstance().getType(this);
        doctor_Id = bundle.getString("id");
        Number = LocalStore.getInstance().getPhoneNumber(this);
        userId = LocalStore.getInstance().getUserID(this);
        MLog.e("Doctor_Id", "" + doctor_Id);
        MLog.e("userId", "" + userId);

//        btnPay.setVisibility(View.GONE);
        Fab_call.setVisibility(View.GONE);

        if (Type.equalsIgnoreCase("Doctor")){
            Rl_Chat.setVisibility(View.GONE);
            btnPay.setVisibility(View.GONE);
            Fab_call.setVisibility(View.GONE);
            Rl_CouponCode.setVisibility(View.GONE);
            btnAppointment.setVisibility(View.GONE);
        }else if(Type.equalsIgnoreCase("Patient")) {
            Rl_Chat.setVisibility(View.VISIBLE);
        }

        IntentFilter filter = new IntentFilter(ACTION_CLOSE);
        firstReceiver = new FirstReceiver();
        registerReceiver(firstReceiver, filter);

        ReqType="View_Detail";
        onRequest(ReqType);
    }

    private void onRequest(String ReqType) {
        mRequestType = IConstant_WebService.WSR_ViewDetail;
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            detailRequest = new ViewDetailRequest();
            detailRequest.id = doctor_Id;
            detailRequest.mobile_number = Number;
            if (ReqType.equals("View_Detail")) {
                RequestManager.ViewDetailRequest(this, null, detailRequest, mRequestType);
            }if (ReqType.equals("freeCode")) {
                String edCode = Ed_Coupon.getText().toString();
                detailRequest.free_code = edCode;
                detailRequest.doctor_id = doctor_Id;
                RequestManager.ViewDetailCodeRequest(this, null, detailRequest, mRequestType);
            }if (ReqType.equals("phoneCall")) {
                mRequestType = IConstant_WebService.WSR_ViewDetail_PhoneCall;
/*                resNumber = resNumber.replace(" ","").trim();
                Number = Number.replace(" ","").trim();*/
                requestCommon = new RequestCommon();
                requestCommon.patient_id = userId;
                requestCommon.doctor_id = doctor_Id;
                requestCommon.doctor_phone_number = resNumber;
                requestCommon.patient_phone_number = Number;
                RequestManager.ViewDetail_PhoneCallRequest(this, null, requestCommon, mRequestType);
            }if (ReqType.equals("videoCall")) {
                mRequestType = IConstant_WebService.WSR_ViewDetail_VideoCall;
                requestCommon = new RequestCommon();
                requestCommon.patient_id = userId;
                requestCommon.doctor_id = doctor_Id;
                requestCommon.doctor_phone_number = resNumber;
                requestCommon.patient_phone_number = Number;
                RequestManager.ViewDetail_videoCallRequest(this, null, requestCommon, mRequestType);
            }
        }else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    private void ImageGallery_Large() {
        MLog.e("imageUrls", "" + arrayListImage);
        try {
            if (arrayListImage.size() > 0) {
                // view pager set adapter
                mViewPager.setAdapter(new ImagePagerAdapter_Large(arrayListImage));
                mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    // private  float MIN_SCALE = 0.75f;
                    private float MIN_SCALE = 0.85f;
                    private float MIN_ALPHA = 0.5f;

                    @Override
                    public void transformPage(View page, float position) {
                        // do transformation here
                        int pageWidth = page.getWidth();
                        int pageHeight = page.getHeight();
                        if (position < -1) { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            page.setAlpha(0);
                        } else if (position <= 1) { // [-1,1]
                            // Modify the default slide transition to shrink the page as well
                            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                            if (position < 0) {
                                page.setTranslationX(horzMargin - vertMargin / 2);
                            } else {
                                page.setTranslationX(-horzMargin + vertMargin / 2);
                            }
                            // Scale the page down (between MIN_SCALE and 1)
                            page.setScaleX(scaleFactor);
                            page.setScaleY(scaleFactor);
                            // Fade the page relative to its size.
                            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                        } else { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            page.setAlpha(0);
                        }

                    }
                });
                mViewPager.setCurrentItem(pagerPosition);
            }else{
                Iv_NoImage.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
            }
            PageListener pageListener = new PageListener();
            mViewPager.setOnPageChangeListener(pageListener);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
            });
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            });
            if (arrayListImage.size() <= 1) {
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            MLog.e("onSetImageGallery", "" + e);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        MLog.e("ViewDetailResponse :", "" + responseObj);
        if (requestType == IConstant_WebService.WSR_ViewDetail) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    setData(res);
                } else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(this, res.msg, true);
                }
            }
        }else if (requestType == IConstant_WebService.WSR_ViewDetail_PhoneCall) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                String responseSuccess = req.status;
                String call_log_id = req.call_log_id;
                String msg = req.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("call_log_id", "" + call_log_id);
                if (responseSuccess != null) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setDataClickToCall(req);
                    }else if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }
        }else if (requestType == IConstant_WebService.WSR_ViewDetail_VideoCall) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                String responseSuccess = req.status;
                String msg = req.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("msg", "" + msg);
                if (responseSuccess != null) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        startSinchService();
                    }else if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }
        }
    }

    private void startSinchService() {
/*        final ProgressDialog pDialog1 = new ProgressDialog(this);
        pDialog1.setCancelable(false);
        pDialog1.show();
        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                pDialog1.setMessage("Connecting call in "+String.valueOf(millisUntilFinished / 1000 +" second.."));
            }
            public void onFinish() {
                sinchService();
                pDialog1.dismiss();
            }
        }.start();*/
        sinchService();
    }

    private void setDataClickToCall(CommonResponse req) {
        try {
            Utility.getInstance().showToast(req.call_log_id, this);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    private void setData(CommonResponse res) {
        if (ReqType.equals("View_Detail")) {
            resDoctor_Degree = res.detail.degree;
            resDoctorName = res.detail.firstname+" "+res.detail.lastname;
            resDoctorExp = res.detail.experience;
            resDoctorPrice = res.detail.price;
            resDoctorImage = res.detail.image_path;
            String address = res.detail.address.address+", "+res.detail.address.city+", "+res.detail.address.country;
            resDoctorAddress = address;
            resDoctorSpec = res.detail.category_type;
            resPayStatus = res.payment_status;
            resOnlineStatus = res.detail.online_status;
            resFreeCode = res.detail.free_code;
 //           resNumber = res.countrycode+res.detail.mobile;
            resNumber = res.detail.mobile;
            resLanguageknown = res.detail.languages;
            resRegistrationlicense = res.detail.registration_licence;
            resServices = res.detail.services;

            if (res.timeslots.size() > 0) {
                arrayListTimeSlot.clear();
                for (int i = 0; i < res.timeslots.size(); i++) {
                    String date = res.timeslots.get(i).date;
                    String time = res.timeslots.get(i).time;
                    String time_slot_id = res.timeslots.get(i).doctor_schedule_id;
                    BeanDetailTimeSlot slot = new BeanDetailTimeSlot();
                    slot.setTime(time);
                    slot.setDate(date);
                    slot.setId(time_slot_id);
                    arrayListTimeSlot.add(slot);
                }
            }else {
                btnAppointment.setVisibility(View.GONE);
            }

            if (res.detail.banner.size() > 0) {
                arrayListImage.clear();
                for (int i = 0; i < res.detail.banner.size(); i++) {
                    arrayListImage.add(res.detail.banner.get(i).image_path);
                }
            } if (arrayListImage.size() <= 0) {
                arrayListImage.add(res.detail.default_banner);
            }

            String imageDoctor = resDoctorImage;
            MLog.e("imageDoctor", "" + imageDoctor);

            imageLoader.DisplayImage(imageDoctor, Iv_Doctor_Image, 4);
            MLog.e("Doctor_ImageBitmap", "" + Doctor_ImageBitmap);
            ImageGallery_Large();

            collapsingToolbar.setTitle("" + resDoctorName);
            Tv_Degree.setText("" + resDoctor_Degree);
            Tv_doctor_Name.setText("" + resDoctorName);
            Tv_Exp.setText("" + resDoctorExp + " Year Experience");
            Tv_Price.setText("INR " + resDoctorPrice);
            //         Tv_transaction_fees.setText("+ 30% (Transaction fees)");
            Tv_Loc.setText("" + resDoctorAddress);
            Tv_Spec.setText("" + resDoctorSpec);
            Tv_licence.setText("" + resRegistrationlicense);
            Tv_language.setText("" + resLanguageknown);
            Tv_service.setText("" + resServices);
            MLog.e("resPayStatus", "" + resPayStatus);
            if (Type.equalsIgnoreCase("Patient")) {
                if (resPayStatus.equalsIgnoreCase("Unpaid")) {
                    btnPay.setVisibility(View.VISIBLE);
                    Fab_call.setVisibility(View.GONE);
                }
                if (resPayStatus.equalsIgnoreCase("Paid")) {
                    btnPay.setVisibility(View.GONE);
                    Rl_CouponCode.setVisibility(View.GONE);
//                        btnAppointment.setVisibility(View.GONE);
                    Fab_call.setVisibility(View.VISIBLE);
                }
            }

            if (resFreeCode!=null && !resFreeCode.isEmpty()){
                if (resFreeCode.equalsIgnoreCase("available")){
                    btnPay.setVisibility(View.GONE);
                    Fab_call.setVisibility(View.VISIBLE);
                    Ed_Coupon.setText("");
                    Rl_CouponCode.setVisibility(View.GONE);
                }
            }

            if (resOnlineStatus.equalsIgnoreCase("NO")) {
                btnPay.setVisibility(View.GONE);
                Fab_call.setVisibility(View.GONE);
                Rl_CouponCode.setVisibility(View.GONE);
                Tv_Date.setText("Not Available Now");
            }if (resOnlineStatus.equalsIgnoreCase("YES")) {
                Tv_Date.setText("Available Now");
            }
        }else if (ReqType.equals("freeCode")) {
            if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                btnPay.setVisibility(View.GONE);
                btnAppointment.setVisibility(View.GONE);
                Rl_CouponCode.setVisibility(View.GONE);
                Fab_call.setVisibility(View.VISIBLE);
                Ed_Coupon.setText("");
            }
            else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                responseMsg = res.detail.msg;
                AlertDialogFinish.Show(ViewDetail.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(ViewDetail.this, Constant.Alart_Status500, false);
            }
        }else if (isFrom.equalsIgnoreCase("YourAppointment")) {
            btnPay.setVisibility(View.GONE);
            btnAppointment.setVisibility(View.GONE);
            Rl_CouponCode.setVisibility(View.GONE);
            Fab_call.setVisibility(View.GONE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Doctor_ImageBitmap = Utility.getInstance().getBitmap(Iv_Doctor_Image);
                Doctor_ImageBitmap = Utility.getInstance().getResizedBitmap(Doctor_ImageBitmap, 200);
            }
        }, 300);
//        Fab_call.setVisibility(View.VISIBLE);
    }

    private class ImagePagerAdapter_Large extends PagerAdapter {
        ArrayList<String> imageUrls;
        public ImagePagerAdapter_Large(ArrayList<String> imageUrls1) {
            this.imageUrls = imageUrls1;
            MLog.e("imageUrls_adapter_large", "" + imageUrls);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final ImageView imgDisplay;
            final ViewAnimator animator;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.viewpager_image_swipe_activity, container, false);
            imgDisplay = (ImageView) v.findViewById(R.id.image);
            imgDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                }
            });
            imgDisplay.setTag("" + position);
            animator = (ViewAnimator) v.findViewById(R.id.animator);
            animator.setDisplayedChild(1);
            Picasso.with(ViewDetail.this).load(imageUrls.get(position)).into(imgDisplay, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                    // Index 0 is the image view.
                    animator.setDisplayedChild(0);
                }

                @Override
                public void onError() {
                    // TODO Auto-generated method stub
                    super.onError();
                    animator.setDisplayedChild(0);
//                    Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                    imgDisplay.setImageResource(R.drawable.no_image);
                    MLog.e("ImageError", "" + "ImageError");
                }
            });
            ((ViewPager) container).addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
//            showImageCount(position);
        }
    }

    public void imagePicasso(String imagePath) {
        Picasso.with(this).load(imagePath).
                into(Iv_Doctor_Image, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
//              Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                        Iv_Doctor_Image.setImageResource(R.drawable.no_image_1);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });
    }

    private void custom_dialog_time_slot() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_time_slot);
        dialog.setCancelable(true);
        RecyclerView mRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_time_slot_list_popup);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DetailDateTime_Adapter adapter = new DetailDateTime_Adapter(ViewDetail.this, arrayListTimeSlot);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ViewDetail.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position1) {
                        time_date_id = arrayListTimeSlot.get(position1).getId();
                        moveToPatientFormActivity();
                        dialog.dismiss();
                    }
                })
        );
        dialog.show();
    }

    private void moveToPatientFormActivity() {
        Intent profile = new Intent(ViewDetail.this, PatientProblemForm.class);
        profile.putExtra("id", doctor_Id);
        profile.putExtra("time_date_id", time_date_id);
        profile.putExtra("categoryName", categoryName);
        startActivity(profile);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void custom_dialog_Coupon() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(ViewDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_coupon);
        TextView tvHeading = (TextView) dialog.findViewById(R.id.textView_terms_value_help);
        dialog.show();
    }

    class FirstReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MLog.e("FirstReceiver", "FirstReceiver");
            if (intent.getAction().equals(ACTION_CLOSE)) {
                ReqType="View_Detail";
                onRequest(ReqType);
            }
        }
    }

    private void custom_dialog_prescription() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_prescription);
        dialog.setCancelable(false);
        final EditText Ed_PopUp = (EditText) dialog.findViewById(R.id.editText_popup__add_prescription);
        final TextView tvSend = (TextView) dialog.findViewById(R.id.textView_pres_send);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.textView_pres_cancel);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Ed_PopUp, InputMethodManager.SHOW_IMPLICIT);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String str_ed = Ed_PopUp.getText().toString();
                if (!str_ed.equals("")) {
                    dialog.dismiss();
                } else {
                    Ed_PopUp.startAnimation(animationShake);
                    Utility.getInstance().showToast("Enter prescription !!!", ViewDetail.this);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void phoneCall() {
        if (resNumber != null && !resNumber.isEmpty()) {
/*            LocalStore.setDoctorDetailNumber(this, resNumber);
            LocalStore.setDoctorDetailId(this, doctor_Id);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + resNumber));
            startActivity(callIntent);*/
            ReqType="phoneCall";
            onRequest(ReqType);
        } else {
            Utility.getInstance().showToast("Invalid Number !!!", ViewDetail.this);
        }
    }

    // Video call
    private void custom_dialog_callSelection() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_call_selection);
        dialog.setCancelable(true);
        final Button phoneCall = (Button) dialog.findViewById(R.id.button_popup_phone_call);
        final Button videoCall = (Button) dialog.findViewById(R.id.button_popup_video_call);
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                termsPopUp(1);
            }
        });
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                termsPopUp(0);
            }
        });
        dialog.show();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Utility.getInstance().showToast( error.toString(), ViewDetail.this);
        Utility.getInstance().dismissDialog(pDialog, this);
    }

    @Override
    public void onStarted() {
        Utility.getInstance().dismissDialog(pDialog, this);
        openPlaceCallActivity();
    }

    private void openPlaceCallActivity() {
        /*Intent mainActivity = new Intent(this, PlaceCallActivityVideoCall.class);
        mainActivity.putExtra("id", doctor_Id);
        startActivity(mainActivity);*/

        LocalStore.getInstance().setDoctorDetailNumber(this, resNumber);
        LocalStore.getInstance().setDoctorDetailId(this, doctor_Id);

        Call call = getSinchServiceInterface().callUserVideo(doctor_Id.toString());
//        Call call = getSinchServiceInterface().callUserVideo("raj");
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivityVideoCall.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        callScreen.putExtra(SinchService.CALL_NAME, resDoctorName);
        startActivity(callScreen);
    }

    private void placePhoneCall() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(ViewDetail.this, Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED)) {
                phoneCall();
            }else {
                ActivityCompat.requestPermissions(ViewDetail.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSIONS);
            }
        }else {
            phoneCall();
        }
    }

    private void placeVideoCall() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
                permissionsNeeded.add("RECORD_AUDIO");
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("CAMERA");
            if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
                permissionsNeeded.add("CALL_PHONE");
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
//                ReqType="videoCall";
//                onRequest(ReqType);
                sinchService();
            }
        } else {
//            ReqType="videoCall";
//            onRequest(ReqType);
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

    private void sinchService() {
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userId);
//            getSinchServiceInterface().startClient("test");
//            showLoading();
            Utility.getInstance().showToast("click again to call", ViewDetail.this);
        } else {
            openPlaceCallActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(android.Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if( perms.get(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("record audio permission denied", ViewDetail.this);
                }else if( perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("camera permission denied", ViewDetail.this);
                }else {
                    sinchService();
                }
                break;

            case REQUEST_PHONE_PERMISSIONS:
                Map<String, Integer> perms_phone = new HashMap<String, Integer>();
                perms_phone.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms_phone.put(permissions[i], grantResults[i]);
                if( perms_phone.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                    Utility.getInstance().showToast("phone call permission denied", ViewDetail.this);
                } else {
                    phoneCall();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void termsPopUp(final int termsPopUp) {
        String msg = "";
        if (termsPopUp == 0) {
            msg = Constant.Alert_Video;
        } else if (termsPopUp == 1) {
            msg = Constant.Alert_Audio;
        } else if (termsPopUp == 2) {
            msg = Constant.Alert_BookApp;
        }
        new AlertDialog.Builder(this)
                .setTitle(Constant.Alart_AppName_Internet)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(Constant.Alert_Accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (termsPopUp == 0) {
                            placeVideoCall();
                        } else if (termsPopUp == 1) {
                            placePhoneCall();
                        } else if (termsPopUp == 2) {
                            custom_dialog_time_slot();
   //                         moveToPatientFormActivity();
                        }
                    }
                }).show();
    }
}
