package app.mediczy_com.service_broadcast;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import java.io.IOException;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 02-01-2016.
 */
public class ServiceVersionCheck extends IntentService {

    public static final String SERVICE_ServiceVersionCheck = "msg";

    public ServiceVersionCheck() {
        super("ServiceVersionCheck");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
        //        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String requestString = intent.getStringExtra(SERVICE_ServiceVersionCheck);
        MLog.e("Service_requestString--->:", "" + requestString);

        VersionChecker versionChecker = new VersionChecker();
        versionChecker.execute();
    }

    //version check
    public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Constant.Package_Service + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String latestVersion) {
            super.onPostExecute(latestVersion);

            Constant.latestVersion = latestVersion;
            String localVersion = getResources().getString(R.string.versionName);
            MLog.e("localVersion----->", "" + localVersion);
            MLog.e("latestVersion----->", "" + latestVersion);
        }
    }
}
