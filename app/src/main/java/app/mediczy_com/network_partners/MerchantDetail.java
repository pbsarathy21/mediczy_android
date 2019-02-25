package app.mediczy_com.network_partners;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.NetworkPartnerDetailResponse;

public class MerchantDetail extends BaseActivity implements ResponseListener, View.OnClickListener {

    private Context context;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private Toolbar toolBar;
    private Animation animationShake;
    private RelativeLayout CoordinateScrollView, Rl_Chat, prevButton, nextButton;
    private LinearLayout Ll_View;
    private FloatingActionButton Fab_call;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView Iv_NoImage;
    private ViewPager mViewPager;
    private CardView cardView;
    private ImageLoader imageLoader;
    private RequestCommon requestCommon;
    private ProgressDialog pDialog = null;
    private TextView tvTitle, tvRatingCount, tvRatingTotal, tvOffers, tvDesc, tvTiming, tvWebSite,
            tvAddress, tvViewOffers;
    private RatingBar ratingBar;
    private Button btnCall, btnDirection, btnMessage, btnGetOfferCode;
    private EditText edInvoiceAmount;
    private NetworkPartnerDetailResponse res;
    private ArrayList<String> arrayListImage = new ArrayList<String>();
    private int pagerPosition = 0;
    private int mRequestType = 0;
    private String ReqType, Id, name, latitude, longitude, rating;
    private boolean rating_bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_partner_detail_activity);
        context = this;
        setToolBar();
        init();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating1, boolean b) {
                if (rating_bool) {
                    ratingBar.setRating(rating1);
                    rating = String.valueOf(rating1);
                    ReqType = "RATING_REQ";
                    onRequest(ReqType);
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void setToolBar() {
        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        CoordinateScrollView = (RelativeLayout) findViewById(R.id.rlCollapseScroll_network_partner_detail);
        toolBar = (Toolbar) findViewById(R.id.toolbar_network_partner_detail);
        setSupportActionBar(toolBar);
        Rl_Chat = (RelativeLayout)toolBar.findViewById(R.id.rl_toolbar_np_chat);
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

        Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("id");
        name = bundle.getString("name");
        latitude = bundle.getString("lat");
        longitude = bundle.getString("long");
    }

    private void init() {
        imageLoader=new ImageLoader(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView_np_detail);
//        mapFragment.getMapAsync(this);

        Fab_call = (FloatingActionButton) findViewById(R.id.fabBtn_network_partner_call);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_network_partner_detail);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextAppearance(View.SCROLLBAR_POSITION_DEFAULT);

        Ll_View = (LinearLayout) findViewById(R.id.inc_gallery_network_partner_detail);
        mViewPager = (ViewPager) Ll_View.findViewById(R.id.vd_image_view_pager_image);
        prevButton = (RelativeLayout) Ll_View.findViewById(R.id.vd_image__Left_image);
        nextButton = (RelativeLayout) Ll_View.findViewById(R.id.vd_image__Right_image);
        Iv_NoImage = (ImageView)Ll_View.findViewById(R.id.vd_image_no_image);

/*        mViewPager.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        Iv_NoImage.setVisibility(View.VISIBLE);*/
        cardView = (CardView) findViewById(R.id.include_network_partner_detail);

        tvTitle = (TextView) cardView.findViewById(R.id.tv_np_detail_company_name);
        tvRatingCount = (TextView) cardView.findViewById(R.id.tv_np_detail_rating_count1_value);
        tvRatingTotal = (TextView) cardView.findViewById(R.id.tv_np_detail_rating_count);
        tvDesc = (TextView) cardView.findViewById(R.id.tv_np_detail_offer);
        tvTiming = (TextView) cardView.findViewById(R.id.tv_np_detail_timing_value);
        tvAddress = (TextView) cardView.findViewById(R.id.tv_np_detail_address);
        tvWebSite = (TextView) cardView.findViewById(R.id.tv_np_detail_website);
        tvWebSite.setOnClickListener(this);
        tvOffers = (TextView) cardView.findViewById(R.id.tv_np_detail_desc);
        tvViewOffers = (TextView) cardView.findViewById(R.id.tv_np_detail_desc_view_more);
        tvViewOffers.setOnClickListener(this);
        ratingBar = (RatingBar) cardView.findViewById(R.id.ratingBar_np_detail_rating);
        btnCall = (Button) cardView.findViewById(R.id.btn_np_detail_card_call);
        btnCall.setOnClickListener(this);
        btnDirection = (Button) cardView.findViewById(R.id.btn_np_detail_card_direction);
        btnDirection.setOnClickListener(this);
        btnMessage = (Button) cardView.findViewById(R.id.btn_np_detail_card_message);
        btnMessage.setOnClickListener(this);
        btnMessage.setVisibility(View.GONE);
        btnGetOfferCode = (Button) cardView.findViewById(R.id.button_np_detail_invoice_amount);
        btnGetOfferCode.setOnClickListener(this);
        edInvoiceAmount = (EditText) cardView.findViewById(R.id.editText_np_detail_invoice_amount);

        ReqType = "NP_DETAIL";
        onRequest(ReqType);
    }

    private void onRequest(String ReqType) {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.id = Id;
            if (ReqType.equals("NP_DETAIL")) {
                mRequestType = IConstant_WebService.WSR_NP_Detail_Req;
                RequestManager.NetworkPartnerDetailRequest(this, null, requestCommon, mRequestType);
            }else if (ReqType.equals("INVOICE_AMOUNT_REQ")) {
                mRequestType = IConstant_WebService.WSR_NP_Detail_Get_Code_Req;
                String InvoiceAmount = edInvoiceAmount.getText().toString();
                requestCommon.amount = InvoiceAmount;
                RequestManager.NetworkPartnerInvoiceRequest(this, null, requestCommon, mRequestType);
            }else if (ReqType.equals("RATING_REQ")) {
                mRequestType = IConstant_WebService.WSR_NP_Detail_Rating_Req;
                requestCommon.rating = rating;
                RequestManager.NetworkPartnerRatingRequest(this, null, requestCommon, mRequestType);
            }
        }
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        try {
            final LatLng mMapLocation = new LatLng(Double.parseDouble(res.detail.lat), Double.parseDouble(res.detail.llong));
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.addMarker(new MarkerOptions()
                    .position(mMapLocation)
                    .title(""+res.detail.address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMapLocation, 10));
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, context);
        if (requestType == IConstant_WebService.WSR_NP_Detail_Req) {
            NetworkPartnerDetailResponse res = (NetworkPartnerDetailResponse) responseObj;
            if (res != null) {
                MLog.e("MerchantDetail :", "" + res.toString());
                setData(res);
            }
        }else if (requestType == IConstant_WebService.WSR_NP_Detail_Get_Code_Req) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                MLog.e("MerchantDetail :", "" + res.toString());
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    AlertDialogFinish.Show(this, res.merchant_code, true);
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(this, res.msg, true);
                }
            }
        }else if (requestType == IConstant_WebService.WSR_NP_Detail_Rating_Req) {
            CommonResponse res = (CommonResponse) responseObj;
            if (res != null) {
                MLog.e("MerchantDetail :", "" + res.toString());
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    setRatingValues(res.average_ratings, res.rating_count);
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(this, res.msg, true);
                }
            }
        }
    }

    private void setData(NetworkPartnerDetailResponse res) {
        try {
            this.res = res;
            if (res.detail!= null) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    collapsingToolbar.setTitle("" + res.detail.title);
                    tvDesc.setText("" +res.detail.description);
                    tvAddress.setText("" +res.detail.address);
                    tvTiming.setText("" +res.detail.open_timing +" - "+res.detail.close_timing);
                    tvTitle.setText("" +res.detail.title);
                    tvWebSite.setText("" +res.detail.website);
                    setRatingValues(res.detail.average_ratings, res.detail.rating_count);
                    setMap();
                    setOffers();
                    setWebsite();

                    if (res.banner.size() > 0) {
                        arrayListImage.clear();
                        for (int i = 0; i < res.banner.size(); i++) {
                            arrayListImage.add(res.banner.get(i).image_path);
                        }
                    }
                    if (arrayListImage.size() <= 0) {
                        arrayListImage.add(res.detail.image);
                    }
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(this, res.detail.msg, true);
                }
                ImageGallery_Large();
            }else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    private void setWebsite() {
        if (res.detail.website!=null) {
            SpannableString content = new SpannableString(res.detail.website);
            content.setSpan(new UnderlineSpan(), 0, res.detail.website.length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
            tvWebSite.setText(content);
        }else {
            tvWebSite.setText("No Website Available");
        }
    }

    private void setOffers() {
        if (res.merchant_offers.size() > 0) {
            String description = res.merchant_offers.get(0).description;
            String discount = res.merchant_offers.get(0).discount;
            tvOffers.setText(""+description+"." +"  "+discount+"% Discount");
            if (res.merchant_offers.size() > 2) {
                tvViewOffers.setVisibility(View.VISIBLE);
            }else {
                tvViewOffers.setVisibility(View.GONE);
            }
        }else {
            tvOffers.setText("No Offer Available");
            tvViewOffers.setVisibility(View.GONE);
        }
    }

    private void setRatingValues(String average_ratings, String rating_count) {
        rating_bool = false;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1= new SpannableString(average_ratings+" ");
        str1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_1)), 0, str1.length(), 0);
        builder.append(str1);
        SpannableString str2= new SpannableString(rating_count +" Rating");
        str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_1)), 0, str2.length(), 0);
        builder.append(str2);
        tvRatingTotal.setText(builder, TextView.BufferType.SPANNABLE);
        if (average_ratings!=null) {
            float roundValue = Utility.getInstance().roundToDecimal(Double.valueOf(average_ratings));
            ratingBar.setRating(Float.valueOf(roundValue));
            rating_bool = true;
        }
    }

    private void setMap() {
        mapFragment.getView().setClickable(false);
        final LatLng mMapLocation = new LatLng(Double.parseDouble(res.detail.lat), Double.parseDouble(res.detail.llong));
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                googleMap.addMarker(new MarkerOptions()
                        .position(mMapLocation)
                        .title(""+res.detail.address)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMapLocation, 10));
                googleMap.getUiSettings().setAllGesturesEnabled(false);
            }
        });
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_np_detail_desc_view_more:
                viewOffers();
                break;
            case R.id.tv_np_detail_website:
                loadWebLink();
                break;
            case R.id.btn_np_detail_card_call:
                callButtonClicked();
                break;
            case R.id.btn_np_detail_card_direction:
                direction();
                break;
            case R.id.btn_np_detail_card_message:
                try {
                    showConversationActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.getInstance().showToast( e.toString(), this);
                }
                break;
            case R.id.button_np_detail_invoice_amount:
                InvoiceValidate();
                break;
        }
    }

    private void viewOffers() {
        if (res.merchant_offers.size() > 0) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.network_partner_detail_offers_dialog);
            RecyclerView.LayoutManager mLayoutManager;
            RecyclerView mRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_network_detail_offers);
            TextView tvTotalCount = (TextView) dialog.findViewById(R.id.textView_m_offers_total_discount_value);
            Button btnCancel = (Button) dialog.findViewById(R.id.button__network_detail_offers_cancel);
            RelativeLayout rlCancel = (RelativeLayout) dialog.findViewById(R.id.rl_m_offers_close);
            btnCancel.setVisibility(View.VISIBLE);
            rlCancel.setVisibility(View.GONE);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            dialog.show();
            MerchantsDetailOffersList_Adapter adapter = new MerchantsDetailOffersList_Adapter(this,  res.merchant_offers);
            mRecyclerView.setAdapter(adapter);
            Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            rlCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            if (res.detail.offer_count!=null) {
                tvTotalCount.setText(res.detail.offer_count);
            }
        }else {
            tvOffers.setText("No Offer Available");
        }
    }

    private void direction() {
  /*   Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + ""));
        startActivity(intent);*/
        try {
            String url1 = "http://maps.google.com/maps?" + "saddr="+ "13.082680" + "," + "80.270718" +
                    "&daddr=" + "13.082680" + "," + "80.270718";

            String url = "http://maps.google.com/maps?" + "saddr="+ latitude + "," + longitude +
                    "&daddr=" + res.detail.lat + "," + res.detail.llong;
            Intent intent1 = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            intent1.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }

    private void loadWebLink() {
        String webUrl = res.detail.website;
        try {
            if (webUrl!=null) {
    /*            Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(webUrl));
                startActivity(i);*/
                Uri uri = Uri.parse("http://"+webUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                Utility.getInstance().showToast("No Website Available", this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }

    private void InvoiceValidate() {
        String inVoiceAmount = edInvoiceAmount.getText().toString().trim();
        if (!inVoiceAmount.equals("")) {
        //    AlertDialogFinish.Show(this, "Discount Code is MD12345", true);
            ReqType = "INVOICE_AMOUNT_REQ";
            onRequest(ReqType);
        }else {
            Utility.getInstance().showToast("Invalid Amount", this);
        }
    }

    private void callButtonClicked() {
        if (Build.VERSION.SDK_INT >= 23) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + res.detail.contact_no));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(callIntent);
            }else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + res.detail.contact_no));
            context.startActivity(callIntent);
        }
    }

    private void showConversationActivity() {
/*
        Intent i = new Intent(MerchantDetail.this, ConversationListActivity.class);
        i.putExtra("Id", res.detail.merchant_id);
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);*/

        try {
            if (res.detail.contact_no!=null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
                {
                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, ""+res.detail.title);

                    if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                    // any app that support this intent.
                    {
                        sendIntent.setPackage(defaultSmsPackageName);
                    }
                    startActivity(sendIntent);

                }
                else // For early versions, do what worked for you before.
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", res.detail.contact_no);
                    smsIntent.putExtra("sms_body",res.detail.title);
                    startActivity(smsIntent);
/*
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.setData(Uri.parse("sms:" + res.detail.contact_no); */
                }

/*                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(res.detail.contact_no, null, res.detail.title,
                        null, null);*/
            }else {
                Utility.getInstance().showToast("Number is Empty", this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast( e.toString(), this);
        }
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
            Picasso.with(MerchantDetail.this).load(imageUrls.get(position)).into(imgDisplay, new Callback.EmptyCallback() {
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
}
