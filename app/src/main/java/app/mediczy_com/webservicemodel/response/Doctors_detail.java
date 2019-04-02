package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class Doctors_detail implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public String id;
    @SerializedName("mediczy_id")
    public String mediczy_id;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("username")
    public String username;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("email_id")
    public String email_id;
    @SerializedName("degree")
    public String degree;
    @SerializedName("experience")
    public String experience;
    @SerializedName("languages")
    public String languages;
    @SerializedName("registration_licence")
    public String registration_licence;
    @SerializedName("gender")
    public String gender;
    @SerializedName("dob")
    public String dob;
    @SerializedName("online_time")
    public String online_time;
    @SerializedName("offline_time")
    public String offline_time;
    @SerializedName("image_path")
    public String image_path;
    @SerializedName("default_banner")
    public String default_banner;

    @SerializedName("msg")
    public String msg;
    @SerializedName("status")
    public String status;
    @SerializedName("doctor_name")
    public String doctor_name;
    @SerializedName("doctor_id")
    public String doctor_id;
    @SerializedName("doctor_degree")
    public String doctor_degree;
    @SerializedName("expyear")
    public String expyear;
    @SerializedName("image")
    public String image;
    @SerializedName("price")
    public String price;
    @SerializedName("address")
    public ViewDetailAddress address;
    @SerializedName("doctor_type")
    public String doctor_type;
    @SerializedName("pay_status")
    public String pay_status;
    @SerializedName("online_status")
    public String online_status;
    @SerializedName("doctor_number")
    public String doctor_number;
    @SerializedName("free_code")
    public String free_code;
    @SerializedName("next_avb")
    public String next_avb;
    @SerializedName("category_id")
    public String category_id;
    @SerializedName("services")
    public String services;
    @SerializedName("category_type")
    public String category_type;
    @SerializedName("languageknown")
    public String languageknown;
    @SerializedName("registrationlicense")
    public String registrationlicense;

    @SerializedName("banners")
    public List<ViewDetailBanner> banner;
}
