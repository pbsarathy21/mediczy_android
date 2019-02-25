package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class ViewDetailAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("address")
    public String address;

    @SerializedName("city")
    public String city;

    @SerializedName("state")
    public String state;

    @SerializedName("country")
    public String country;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lng")
    public String lng;
}
