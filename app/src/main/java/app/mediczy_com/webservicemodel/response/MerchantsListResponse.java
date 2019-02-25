package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 13-01-2018.
 */

public class MerchantsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("merchant_id")
    public String merchant_id;
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;


    @SerializedName("offer")
    public String offer;
    @SerializedName("image")
    public String image;
    @SerializedName("average_ratings")
    public String average_ratings;
    @SerializedName("kilometer")
    public String kilometer;
}