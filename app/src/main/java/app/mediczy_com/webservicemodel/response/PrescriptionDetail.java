package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class PrescriptionDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;
    @SerializedName("status")
    public String status;
    @SerializedName("doctor_name")
    public String doctor_name;
    @SerializedName("doctor_degree")
    public String doctor_degree;
    @SerializedName("doctor_regno")
    public String doctor_regno;
    @SerializedName("prescription_date")
    public String prescription_date;
    @SerializedName("description_detail")
    public String description_detail;
    @SerializedName("user_name")
    public String user_name;
    @SerializedName("user_phone")
    public String user_phone;
    @SerializedName("user_dob")
    public String user_dob;
    @SerializedName("signature")
    public String signature;

    @SerializedName("subject")
    public String subject;
    @SerializedName("prescription")
    public String prescription;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("doctor")
    public DoctorRegisterResponse doctor;

    @SerializedName("user")
    public PatientRegisterResponse user;
}
