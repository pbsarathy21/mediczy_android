package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class AmbulanceListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("ambulance_id")
    public String ambulance_id;
    @SerializedName("hospital_name")
    public String hospital_name;
    @SerializedName("address")
    public String address;
    @SerializedName("phonenumber")
    public String phonenumber;
    @SerializedName("kilometer")
    public String kilometer;
    @SerializedName("offer")
    public String offer;
}
