package app.mediczy_com;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;

/**
 * Created by Prithivi on 05-Mar-17.
 */

@ReportsCrashes(
        formUri = "https://medo.cloudant.com/acra-example/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "tubtakedstinumenterences",
        formUriBasicAuthPassword = "igqMFFMatvtMXVCKgy7u6a5W",
        formKey = "", // This is required for backward compatibility but not used
        mailTo = "mailatprithiviraj@gmail.com", // my email here
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PACKAGE_NAME, ReportField.REPORT_ID, ReportField.BUILD, ReportField.STACK_TRACE},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.toast_crash)

    public class AppState extends MultiDexApplication {

    public AppState() {
        super();
        MLog.e("AppState", "App State");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
         MultiDex.install(this);
 //        ACRA.init(this);
 //       Utility.getInstance().showDialogRepeat(this);
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        MLog.e("onLowMemory", "Low Memory");
    }
}



