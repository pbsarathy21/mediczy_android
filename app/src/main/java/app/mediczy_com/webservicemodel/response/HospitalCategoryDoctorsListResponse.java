package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 16-1-2018.
 */

public class HospitalCategoryDoctorsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("doctor_id")
    public String doctor_id;
    @SerializedName("name")
    public String name;
    @SerializedName("degree")
    public String degree;
    @SerializedName("doctor_type")
    public String doctor_type;
    @SerializedName("online_status")
    public String online_status;
    @SerializedName("expyear")
    public String expyear;
    @SerializedName("price")
    public String price;
    @SerializedName("image")
    public String image;
    @SerializedName("next_avab")
    public String next_avab;
}
