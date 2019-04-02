package app.mediczy_com.payment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.mediczy_com.R;
import app.mediczy_com.dashboard.Register;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 04-01-2016.
 */
public class PaymentByDoctor extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar bar;
    private String Id, Url;
    private String MobileNumber, USerId;
    PaymentFreeCodeObserver caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
    }


    private void init() {
        // TODO Auto-generated method stub
        caller = (PaymentFreeCodeObserver) Register.register;
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
        if (Build.VERSION.SDK_INT >= 19)
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        else if (Build.VERSION.SDK_INT >= 11)
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("Id");

//        LocalStore.setDoctorDetailId(this, Id);
        MobileNumber = LocalStore.getInstance().getPhoneNumber(this);
        USerId = LocalStore.getInstance().getUserID(this);
        Url = IConstant_WebService.doctor_pay_u_money;
        MLog.e("Url---->", "" + Url);
        webView.loadUrl(Url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            bar.setVisibility(View.VISIBLE);
            MLog.e("shouldOverrideUrlLoading---->", "" + url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            bar.setVisibility(View.VISIBLE);
            MLog.e("onPageStarted---->", "" + url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //	handler.proceed(); // Ignore SSL certificate errors
   //         alertSecurity(error, handler);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
            MLog.e("onPageFinished---->", "" + url);
            String chk = url.substring(url.lastIndexOf("/") + 1);
            MLog.e("chk", chk);
            if (url.contains(Constant.payment_finished)) {
                LocalStore.getInstance().set_paymentok(PaymentByDoctor.this, "paid");
                caller.onPaymentReceived(true);
                finish();
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
//            LocalStore money = new LocalStore();
            LocalStore.getInstance().setmoneypaid(PaymentByDoctor.this, "moneynotpaidtodoctor");
            bar.setVisibility(View.GONE);
            MLog.e("onReceivedError---->", "" + failingUrl);
        }

        //Show loader on url load
        public void onLoadResource(WebView view, String url) {
            bar.setVisibility(View.VISIBLE);
            MLog.e("onLoadResource---->", "" + url);
        }
    }

    private void alertSecurity(SslError error, final SslErrorHandler handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        String message = "SSL Certificate error.";
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = "The certificate authority is not trusted.";
                break;
            case SslError.SSL_EXPIRED:
                message = "The certificate has expired.";
                break;
            case SslError.SSL_IDMISMATCH:
                message = "The certificate Hostname mismatch.";
                break;
            case SslError.SSL_NOTYETVALID:
                message = "The certificate is not yet valid.";
                break;
        }

        message += " Do you want to continue anyway?";
        alertDialog.setTitle("SSL Certificate Error");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ignore SSL certificate errors
                handler.proceed();
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                handler.cancel();
            }
        });
        alertDialog.show();
    }

    private void custom_dialog_cancel_payment() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(PaymentByDoctor.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_cancel_payment);
        final TextView Tv_Cancel = (TextView) dialog.findViewById(R.id.textViewpop_up_cancel);
        final TextView ok = (TextView) dialog.findViewById(R.id.textView_pop_up_ok);

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PaymentByDoctor.this.finish();
                PaymentByDoctor.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
        });

        Tv_Cancel.setOnClickListener(new OnClickListener() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflate = getSupportMenuInflater();
//		inflate.inflate(R.menu.add_item_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                custom_dialog_cancel_payment();
                break;
        }
        return false;
    }
}
