package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 08-10-2017.
 */

public class DeliveryFormReq implements Serializable {
    private static final long serialVersionUID = 1L;

    public String Name;
    public String Address;
    public String Phone;
    public String Zip;
    public String City;
    public String state;
    public String type;
    public String country;
    public String date;
    public String time;
    public String adsId;
}
