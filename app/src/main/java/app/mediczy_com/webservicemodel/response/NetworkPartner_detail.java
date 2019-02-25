package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 13-1-2018.
 */

public class NetworkPartner_detail implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;
    @SerializedName("status")
    public String status;
    @SerializedName("merchant_code")
    public String merchant_code;
    @SerializedName("merchant_id")
    public String merchant_id;
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("offer_count")
    public String offer_count;
    @SerializedName("offers")
    public String offers;
    @SerializedName("open_timing")
    public String open_timing;
    @SerializedName("close_timing")
    public String close_timing;
    @SerializedName("address")
    public String address;
    @SerializedName("contact_no")
    public String contact_no;
    @SerializedName("email")
    public String email;
    @SerializedName("website")
    public String website;
    @SerializedName("rating_count")
    public String rating_count;
    @SerializedName("average_ratings")
    public String average_ratings;
    @SerializedName("lat")
    public String lat;
    @SerializedName("long")
    public String llong;
    @SerializedName("image")
    public String image;
}
