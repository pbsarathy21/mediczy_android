package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class BaseResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	@SerializedName("status")
	public String status;

	@SerializedName("error")
	public String error;

	@SerializedName("msg")
	public String msg;

	@SerializedName("image")
	public String image;

	@SerializedName("error_code")
	public String error_code;

	@SerializedName("merchant_code")
	public String merchant_code;

	@SerializedName("call_log_id")
	public String call_log_id;

	@SerializedName("empty")
	public String empty;

	@SerializedName("type")
	public String type;

	@SerializedName("health_card_avail")
	public String health_card_avail;

	@SerializedName("pending_amount")
	public String pending_amount;

	@SerializedName("otp")
	public String otp;

	@SerializedName("payment_status")
	public String payment_status;

	@SerializedName("countrycode")
	public String countrycode;

	@SerializedName("online_status")
	public String online_status;

	@SerializedName("next_available")
	public String next_available;

	@SerializedName("callid")
	public String callid;

	@SerializedName("call_status")
	public String call_status;

	@SerializedName("version_code")
	public String version_code;

	@SerializedName("logout_status")
	public String logout_status;

	@SerializedName("doctor_online_status")
	public String doctor_online_status;

	@SerializedName("id")
	public String id;

	@SerializedName("membership_id")
	public String membership_id;

	@SerializedName("rating_count")
	public String rating_count;

	@SerializedName("average_ratings")
	public String average_ratings;

	@SerializedName("freecode")
	public String freecode;

	@SerializedName("card_id")
	public String card_id;

	@SerializedName("card_validity")
	public String card_validity;

	@SerializedName("card_name")
	public String card_name;

	@SerializedName("health_card_name")
	public String health_card_name;
}
