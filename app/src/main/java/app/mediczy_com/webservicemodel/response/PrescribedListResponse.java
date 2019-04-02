package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class PrescribedListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;
    @SerializedName("status")
    public String status;
    @SerializedName("name")
    public String name;
    @SerializedName("doctor_subject")
    public String doctor_subject;
    @SerializedName("prescription_id")
    public String prescription_id;
    @SerializedName("description_detail")
    public String description_detail;
    @SerializedName("prescription_date")
    public String prescription_date;
    @SerializedName("doctor_image")
    public String doctor_image;
    @SerializedName("type")
    public String type;
}
