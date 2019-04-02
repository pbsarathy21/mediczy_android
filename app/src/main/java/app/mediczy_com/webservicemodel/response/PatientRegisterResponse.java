package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class PatientRegisterResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("type")
    public String type;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("id")
    public String id;
    @SerializedName("health_card_id")
    public String health_card_id;
    @SerializedName("firstname")
    public String firstname;
    @SerializedName("lastname")
    public String lastname;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("countrycode")
    public String countrycode;

    @SerializedName("gender")
    public String gender;
    @SerializedName("dob")
    public String dob;
    @SerializedName("fcm_id")
    public String fcm_id;
    @SerializedName("device_id")
    public String device_id;
    @SerializedName("otp")
    public String otp;
    @SerializedName("referral_code")
    public String referral_code;
    @SerializedName("otp_status")
    public String otp_status;
}
