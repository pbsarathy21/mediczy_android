package app.mediczy_com.gps;

/**
 * Created by Prithivi on 08-12-2016.
 */

public interface GpsObserver {

    public void onGPSLocationReceived(GPSTracker gpsTracker, int type);
}
