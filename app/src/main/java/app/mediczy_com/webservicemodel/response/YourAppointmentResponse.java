package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class YourAppointmentResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("doctor_id")
    public String doctor_id;
    @SerializedName("user_appointment_id")
    public String appointment_id;
    @SerializedName("doctor_name")
    public String doctor_name;
    @SerializedName("status")
    public String status;
    @SerializedName("doctor_image")
    public String doctor_image;
    @SerializedName("experience")
    public String experience;
    @SerializedName("specialist")
    public String specialist;
    @SerializedName("date")
    public String date;
    @SerializedName("time")
    public String time;
    @SerializedName("booked_date")
    public String booked_date;
    @SerializedName("date_time")
    public String date_time;
    @SerializedName("user_name")
    public String user_name;

    @SerializedName("patient_name")
    public String patient_name;
    @SerializedName("patient_profileimage")
    public String patient_profileimage;
    @SerializedName("age")
    public String age;
    @SerializedName("doctors_reply")
    public String doctors_reply;
}
