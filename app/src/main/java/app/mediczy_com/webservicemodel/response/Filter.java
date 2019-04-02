package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 05-03-2017.
 */

public class Filter implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("city")
    public String city;
    @SerializedName("language")
    public String language;
    @SerializedName("education")
    public String education;
    @SerializedName("fee")
    public String fee;
    @SerializedName("category")
    public String category;
    @SerializedName("appointment")
    public String appointment;
    @SerializedName("experience")
    public String experience;
}
