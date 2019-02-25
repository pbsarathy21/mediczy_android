package app.mediczy_com.settings_help;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;

/**
 * Created by Prithivi Raj on 02-02-2016.
 */
public class Help_Activity extends BaseActivity
{
    Toolbar toolBar;
    private WebView webView;
    private ProgressBar progress;
    private String isFrom="", url="";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        setToolbar();
        init();
    }

    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("FAQ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init()
    {
        webView = (WebView) findViewById(R.id.webView_help);
        progress = (ProgressBar) findViewById(R.id.progressBar_help);
        progress.setMax(100);
        webView.setWebChromeClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
            isFrom = bundle.getString(Constant.isFrom);

        if (isFrom.equals("CardBuyActivity")) {
            url = IConstant_WebService.health_card_faq;
        }else if (isFrom.equals("HealthInsuranceFragment")) {
            url = IConstant_WebService.insurance_faq;
        } else {
            url = IConstant_WebService.faq;
        }

        webView.loadUrl(url);
        Help_Activity.this.progress.setProgress(0);
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Help_Activity.this.setValue(newProgress);
            if (newProgress==100) {
                progress.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }
}
