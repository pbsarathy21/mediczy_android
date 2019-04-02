package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-2-2018.
 */

public class DoctorRegisterResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("category_id")
    public String category_id;
    @SerializedName("mediczy_id")
    public String mediczy_id;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("username")
    public String username;

    @SerializedName("pms_password")
    public String pms_password;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("countrycode")
    public String countrycode;
    @SerializedName("email_id")
    public String email_id;
    @SerializedName("degree")
    public String degree;
    @SerializedName("experience")
    public String experience;
    @SerializedName("services")
    public String services;
    @SerializedName("type")
    public String type;
    @SerializedName("languages")
    public String languages;
    @SerializedName("registration_licence")
    public String registration_licence;
    @SerializedName("image")
    public String image;
    @SerializedName("signature")
    public String signature;
    @SerializedName("price")
    public String price;
    @SerializedName("gender")
    public String gender;
    @SerializedName("dob")
    public String dob;
    @SerializedName("fcm_id")
    public String fcm_id;
    @SerializedName("device_id")
    public String device_id;
    @SerializedName("online_time")
    public String online_time;
    @SerializedName("offline_time")
    public String offline_time;
    @SerializedName("last_logged_on")
    public String last_logged_on;
    @SerializedName("online_status")
    public String online_status;
    @SerializedName("pms_status")
    public String pms_status;
    @SerializedName("doctor_id")
    public String doctor_id;
    @SerializedName("image_path")
    public String image_path;

}
