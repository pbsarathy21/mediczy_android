package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 13-01-2018.
 */

public class HospitalsMerchantsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("hospital_id")
    public String hospital_id;
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("kilometer")
    public String kilometer;
    @SerializedName("image")
    public String image;
}