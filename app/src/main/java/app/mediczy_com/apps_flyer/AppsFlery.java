package app.mediczy_com.apps_flyer;

import android.app.Application;
import android.content.Context;

import app.mediczy_com.HomeNavigation;

/**
 * Created by Prithivi on 08-12-2016.
 */

public class AppsFlery {

    Context context;
    Application application;

    public AppsFlery(HomeNavigation homeNavigation) {
        context = homeNavigation;
 //       registerAppsFlery();
    }

    public AppsFlery(Context ctx, Application application1) {
        context = ctx;
        application = application1;
    }

   /* private void registerAppsFlery() {

        *//**
         #AppsFlyer: Optional methods- Use according to your specific needs. Please refer to the SDK integration documentation for more information

         #AppsFlyer: Please pay special attention to the collection of device IDs

         AppsFlyerLib.getInstance().setImeiData("GET_DEVICE_IMEI");
         AppsFlyerLib.getInstance().setAndroidIdData("GET_DEVICE_ANDROID_ID");
         AppsFlyerLib.getInstance().setCustomerUserId("myId");
         AppsFlyerLib.getInstance().setCurrencyCode("GBP");
         *//*

        *//**
         #AppsFlyer: the startTracking method must be called after any optional 'Set' methods, except for setGCMProjectID()
         You can get your AppsFlyer Dev Key from the "SDK Integration" section in your dashboard
         *//*

        AppsFlyerLib.getInstance().startTracking(application, Constant.APPS_FLERY_KEY);

        *//**
         #AppsFlyer: collecting your GCM project ID by setGCMProjectID allows you to track uninstall data in your dashboard
         AppsFlyerLib.getInstance().setGCMProjectNumber("YOUR_PROJECT_ID_NUMBER");

         Please refer to this documentation for more information:
         https://support.appsflyer.com/hc/en-us/articles/208004986
         *//*

        *//**
         #AppsFlyer: registerConversionListener implements the collection of attribution (conversion) data
         Please refer to this documentation to view all the available attribution parameters:
         https://support.appsflyer.com/hc/en-us/articles/207032096-Accessing-AppsFlyer-Attribution-Conversion-Data-from-the-SDK-Deferred-Deeplinking
         *//*
        AppsFlyerLib.getInstance().registerConversionListener(context, new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    MLog.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " +
                            conversionData.get(attrName));
                }
                //SCREEN VALUES//
                final String install_type = "Install Type: " + conversionData.get("af_status");
                final String media_source = "Media Source: " + conversionData.get("media_source");
                final String install_time = "Install Time(GMT): " + conversionData.get("install_time");
                final String click_time = "Click Time(GMT): " + conversionData.get("click_time");
              *//*  runOnUiThread(new Runnable() {
                    public void run() {
                        ((TextView) findViewById(R.id.logView)).setText(install_type + "\n" + media_source + "\n" + click_time + "\n" + install_time);
                    }


                });
*//*
            }

            @Override
            public void onInstallConversionFailure(String errorMessage) {
                MLog.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
                MLog.d(AppsFlyerLib.LOG_TAG, "DEEP LINK WORKING");
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                MLog.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
            }
        });
    }*/
}
