package app.mediczy_com.reports_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import app.mediczy_com.R;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi on 01-07-2018.
 */

public class Reports_Fragment extends Fragment {

    Context context;
    private WebView webView;
    private ProgressBar progress;
    private String url="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reports_fragment, container, false);
        context = getActivity();

        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        webView = (WebView) rootView.findViewById(R.id.webView_report);
        progress = (ProgressBar) rootView.findViewById(R.id.progressBar_report);
        progress.setMax(100);
        progress.setVisibility(View.GONE);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
/*
        url = getArguments().getString("url");
        if (url.equals("reports")) {
        } else if (url.equals("my_opinions")) {
        } else if (url.equals("user_profile")) {
        }
 */
        url = getArguments().getString("url");
        MLog.e("url->", "" + url);
        webView.loadUrl(url);
        progress.setProgress(0);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            MLog.e("shouldOverrideUrlLoading->","" + url);
            return true;
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }
}
