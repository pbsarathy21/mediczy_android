package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class NetworkPartnerListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("network_partner_id")
    public String network_partner_id;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("offers")
    public String offers;
    @SerializedName("image")
    public String image;
    @SerializedName("number")
    public String number;
    @SerializedName("lat")
    public String lat;
    @SerializedName("lang")
    public String lang;
    @SerializedName("verify_status")
    public String verify_status;
    @SerializedName("kilometer")
    public String kilometer;
}
