package app.mediczy_com.ChatBot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import app.mediczy_com.R;
import app.mediczy_com.utility.Consts;
import app.mediczy_com.utility.MLog;


public class PaymentGateway extends AppCompatActivity {

    private WebView webView;
    private ProgressBar bar;
    private TextView title;
    private String Id, Url = "", MobileNumber, USerId;
    public Activity activity = PaymentGateway.this;
    ImageView back_arrow, notifiction_icon, search_icon;
    Context context;
    public static final String TAG = PaymentGateway.class.getSimpleName();


    ImageView img_cross;
    TextView text_title, text;
    private static int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bot_activity_payment_gateway);

        context = PaymentGateway.this;

        Initialization();

        //GET THE URL DATA
        Intent intent = getIntent();
        Url = intent.getStringExtra("URL");
        MLog.e(TAG, "url==>" + Url);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent objIntent = new Intent(PaymentGateway.this, ChatBotActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);


            }
        });


        init();

    }

    private void init() {
        // TODO Auto-generated method stub
        webView = (WebView) findViewById(R.id.webView_payment);
        bar = (ProgressBar) findViewById(R.id.loading_progress_payment);
        bar.setVisibility(View.GONE);

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

        if (Build.VERSION.SDK_INT >= 19)
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        else if (Build.VERSION.SDK_INT >= 11)
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

	/*	Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("Id");*/
/*

		MobileNumber = LocalStore.getPhoneNumber(this);
		USerId = LocalStore.getUserID(this);
*/
        //Url = IConstant_WebService.payment + Consts.for_mobile_no + "&amount=" + Consts.amount;
        //Url = "http://ehagroups.com/admin/mobile/membership_payment?";
        MLog.e("", "Url---->" + Url);


        if (InternetStatus.InternetStatus(PaymentGateway.this)) {
            if (!Url.equals("")) {
                webView.loadUrl(Url);
            } else {
                MLog.e("", "Url is Null---->");
            }
        } else {
            // Util.makeToast(PaymentGateway.this, "Internet Connection Failed !!");
          /*  if(!Consts.check_internet_page_open){
                Intent objIntent = new Intent(PaymentGateway.this, InternetConnectionsActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.enter,R.anim.exit);

            }else
                MLog.e("", "Internet Page is Already Open");
*/
        }

/*

        } else {
            String Url = "http://ehagroups.com/admin/mobile/booking_payment?mobile=" + Consts.for_mobile_no + "&amount=" + Consts.total + "&property_name=" + Consts.cat_name;
            Log.e("Url---->", "" + Url);
            webView.loadUrl(Url);
        }
*/


    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            bar.setVisibility(View.VISIBLE);
            Log.e("shouldOverrideUr--->", "" + url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            bar.setVisibility(View.VISIBLE);
            Log.e("onPageStarted---->", "" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
            Log.e("onPageFinished---->", "" + url);
            Log.e("urrrrrrrrl", url);
            if (url.contains(Consts.Payment_Success)) {
                // LocalStore money = new LocalStore();
                //  money.setmoneypaid(Payments.this, "moneypaidtodoctor");
//				Toast.makeText(getApplicationContext(), "Pay Completed", Toast.LENGTH_SHORT).show();
                //  Intent myIntent = new Intent(ViewDetail.ACTION_CLOSE);
                // sendBroadcast(myIntent);
               /* rthhjhyg

                Util.makeToast(PaymentGateway.this, "Pay Completed");
                Intent intent = new Intent(PaymentGateway.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);*/


                //Update_Dialog();

                Intent intent = new Intent(PaymentGateway.this, ChatBotActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();


            /*    new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Toast.makeText(EditActivity.this, "Update Ads Successfully !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaymentGateway.this, HomeFragment.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
*/

            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            bar.setVisibility(View.GONE);
            Log.e("onReceivedError---->", "" + failingUrl);
        }

        //Show loader on url load
        public void onLoadResource(WebView view, String url) {
            bar.setVisibility(View.VISIBLE);
            Log.e("onLoadResource---->", "" + url);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.Check);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private void custom_dialog_cancel_payment() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(PaymentGateway.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_cancel_payment);
        final TextView Tv_Cancel = (TextView) dialog.findViewById(R.id.textViewpop_up_cancel);
        final TextView ok = (TextView) dialog.findViewById(R.id.textView_pop_up_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(PaymentGateway.this, ChatBotActivity.class);
                startActivity(intent);
                PaymentGateway.this.finish();
                PaymentGateway.this.overridePendingTransition(R.anim.enter, R.anim.exit);
                Toast.makeText(context, "Your Payment Cancelled Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        Tv_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            custom_dialog_cancel_payment();
            return true;
        }
        return false;
    }

    public void Initialization() {

        //textview
        title = (TextView) findViewById(R.id.title);
        title.setText("Payment Gateway");
      //  Util.setFont(3, context, title, "Payment Gateway");

        //imageview

        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        notifiction_icon = (ImageView) findViewById(R.id.notifiction_icon);
        search_icon = (ImageView) findViewById(R.id.search_icon);


    }


/*
    public void Update_Dialog() {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        //  builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View content = inflater.inflate(R.layout.edit_update_dialog, null);
        builder1.setView(content);

        //  AlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        text_title = (TextView) content.findViewById(R.id.text_title);
        text = (TextView) content.findViewById(R.id.text);
        Util.setFont(3, activity, text_title, "Congratulations");
        Util.setFont(2, activity, text, "Payment Completed");


        img_cross = (ImageView) content.findViewById(R.id.img_cross);
        img_cross.setBackground(ContextCompat.getDrawable(activity, R.mipmap.good));

        final AlertDialog alertDialog = builder1.create();
        alertDialog.show();
        //  alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Transparent_white)));

        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view));

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        img_cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MLog.e("fvfdv", "cross");
                alertDialog.dismiss();

            }
        });


    }
*/

/*
    public void Payment_Cancel() {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        //  builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View content = inflater.inflate(R.layout.edit_update_dialog, null);
        builder1.setView(content);

        //  AlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        text_title = (TextView) content.findViewById(R.id.text_title);
        text = (TextView) content.findViewById(R.id.text);
        Util.setFont(3, activity, text_title, "Congratulations");
        Util.setFont(2, activity, text, "Payment Cancel");


        img_cross = (ImageView) content.findViewById(R.id.img_cross);

        img_cross.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bad));


        final AlertDialog alertDialog = builder1.create();
        alertDialog.show();
        //  alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Transparent_white)));

        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view));

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        img_cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MLog.e("fvfdv", "cross");
                alertDialog.dismiss();

            }
        });


    }*/


}
