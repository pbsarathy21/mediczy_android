package app.mediczy_com.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private final String TAG = LocationService.class.getSimpleName();

    private Intent intent;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        mGoogleApiClient = new GoogleApiClient.Builder(this) // com.google.android.gms.location.LocationServices
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() " + intent);
        this.intent = intent;

        /**
         * com.google.android.gms.common.GooglePlayServicesUtil
         */
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            Log.d(TAG, "handleActionGPS(): Connect to GooglePlayServices");
            mGoogleApiClient.connect();
        } else {
            Log.e(TAG, "GooglePlayService is not available");
            stopSelf();
        }

        return (START_REDELIVER_INTENT);
    }

    /**
     * com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: Connected");
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // nterval 10 seconds
        mLocationRequest.setNumUpdates(1); // number of location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
        stopSelf();
    }

    /**
     * com.google.android.gms.location.LocationListener
     *
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged()");

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();

        stopSelf();
    }


    // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "GoogleApiClient connection has failed: " + connectionResult.getErrorCode());
        stopSelf();
    }

    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

}