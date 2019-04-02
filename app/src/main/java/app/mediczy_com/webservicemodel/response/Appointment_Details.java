package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class Appointment_Details implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("image")
    public String image;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("patient_name")
    public String patient_name;
    @SerializedName("catergory")
    public String catergory;
    @SerializedName("patient_mobile_number")
    public String patient_mobile_number;
    @SerializedName("date_time")
    public String date_time;
    @SerializedName("appointment_for")
    public String appointment_for;
    @SerializedName("issue")
    public String issue;
    @SerializedName("gender")
    public String gender;
    @SerializedName("age")
    public String age;
    @SerializedName("height")
    public String height;
    @SerializedName("weight")
    public String weight;
    @SerializedName("city")
    public String city;
    @SerializedName("previous_diagnosed")
    public String previous_diagnosed;
    @SerializedName("medications")
    public String medications;
    @SerializedName("allergies")
    public String allergies;
    @SerializedName("transaction_status")
    public String transaction_status;
    @SerializedName("doctor_reply")
    public String doctor_reply;
    @SerializedName("doctor_id")
    public String doctor_id;
}
