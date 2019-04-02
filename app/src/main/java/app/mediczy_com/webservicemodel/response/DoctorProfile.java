package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 24-02-2018.
 */

public class DoctorProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("degree")
    public String degree;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("experience")
    public String experience;
    @SerializedName("price")
    public String price;
    @SerializedName("image_path")
    public String image_path;
    @SerializedName("address")
    public String address;
    @SerializedName("services")
    public String services;
    @SerializedName("languages")
    public String languages;
    @SerializedName("licence")
    public String licence;
}
