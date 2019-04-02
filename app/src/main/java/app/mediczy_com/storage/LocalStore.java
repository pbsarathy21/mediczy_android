package app.mediczy_com.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import app.mediczy_com.iconstant.Constant;

public class LocalStore {

	private static LocalStore objLocalStore;
	private LocalStore(){
		//ToDo here
	}

	public static LocalStore getInstance() {
		if (objLocalStore == null) {
			objLocalStore = new LocalStore();
		}
		return objLocalStore;
	}
	
	public  void ClearData(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
	}

	public  void Clear(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		settings.edit().remove(Constant.DoctorDetailNumber).commit();
	}

	public void setUserId(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.USERID_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getUserID(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.USERID_SESSION_KEY, "A");
	}

	public void setDoctorImage(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.DOCTORIMAGE_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getDoctorImage(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.DOCTORIMAGE_SESSION_KEY, "0");
	}

	public void setFirstName(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.FIRSTNAME_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getFirstName(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.FIRSTNAME_SESSION_KEY, "0");
	}

	public void setLastName(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.LASTNAME_SESSION_KEY, "" + count);
		editor.commit();
	}

	public void setPhoneNumber(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.NUMBER_SESSION_KEY, "" + "+91"+count);
		editor.commit();
	}

	public  String getPhoneNumber(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.NUMBER_SESSION_KEY, "0");
	}

	public  String getDob(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.DOB_SESSION_KEY, "0");
	}

	public void setDob(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.DOB_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getGender(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.GENDER_SESSION_KEY, "0");
	}

	public void setGender(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.GENDER_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getDoctorBalance(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.BAL_SESSION_KEY, "0");
	}

	public void setDoctorBalance(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.BAL_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getRefId(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.REF_ID_SESSION_KEY, "0");
	}

	public void setRefId(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.REF_ID_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getsetHealthCard_Available(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.HEALTH_CARD_ID_SESSION_KEY, "A");
	}

	public void setHealthCard_Available(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.HEALTH_CARD_ID_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getType(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.TYPE_SESSION_KEY, "0");
	}

	public void setType(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.TYPE_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getOtp(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.OTP_SESSION_KEY, "0");
	}

	public void setOtp(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.OTP_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getCountryCode(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.COUNTRYCODE_SESSION_KEY, "0");
	}

	public void setCountryCode(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.COUNTRYCODE_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getGcmId(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.GCMID_SESSION_KEY, "0");
	}

	public void setGcmId(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.GCMID_SESSION_KEY, "" + count);
		editor.commit();
	}

	public void setNotificationTone(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.NOTIFICATION_TONE_KEY, "" + count);
		editor.commit();
	}

	public  String getNotificationTone(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.NOTIFICATION_TONE_KEY, "1");
	}

	public void setCount(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.COUNT_SESSION_KEY, "" + count);
		editor.commit();
	}

	public  String getCount(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.COUNT_SESSION_KEY, "0");
	}

	public void set_doctorSessionTime(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.DOCTOR_ONLINE_TIME, "" + count);
		editor.commit();
	}
	public  String get_doctorSessionTime(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.DOCTOR_ONLINE_TIME, "0");
	}

	public void setradiochecked(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.RADIOISCHECK_ID_KEY , "" + count);
		editor.commit();
	}

	public  String getradiochecked(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.RADIOISCHECK_ID_KEY , "norecord");
	}

	public  String get_DoctorFreeCode(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.DOCTORFREECODE, "0");
	}

	public void set_DoctorFreeCode(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.DOCTORFREECODE, "" + count);
		editor.commit();
	}

	public  String get_paymentok(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.PAYMENTOK, "0");
	}

	public void set_paymentok(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.PAYMENTOK, "" + count);
		editor.commit();
	}

	public void setmoneypaid(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.MONEY_ID_KEY , "" + count);
		editor.commit();
	}


	public  String getmoneypaid(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.MONEY_ID_KEY , "notpaid");
	}

	public  String getCategory(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.CATEGORY, "0");
	}

	public  void setCategory(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.CATEGORY, "" + count);
		editor.commit();
	}

	public  String getDoctorDetailNumber(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.DoctorDetailNumber, "0");
	}

	public  void setDoctorDetailNumber(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.DoctorDetailNumber, "" + count);
		editor.commit();
	}

	public  String getDoctorDetailId(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.DoctorDetailId, "0");
	}

	public  void setDoctorDetailId(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.DoctorDetailId, "" + count);
		editor.commit();
	}

	/*public  String getPayStatus(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.PAY_STATUS, "0");
	}

	public  void setPayStatus(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.PAY_STATUS, "" + count);
		editor.commit();
	}*/

	public  String getPatientVideoCallId(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.PatientVideoCallId, "0");
	}

	public  void setPatientVideoCallId(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.PatientVideoCallId, "" + count);
		editor.commit();
	}

	public  String getLatitude_Longitude(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.lat_long, "0");
	}

	public  void setLatitude_Longitude(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.lat_long, "" + count);
		editor.commit();
	}

	public String isInsuranceFormSubmitted(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.isInsuFromSubmitted, "0");
	}

	public void setInsuranceFormSubmitted(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.isInsuFromSubmitted, "" + count);
		editor.commit();
	}

	public String getInsuranceSubmitted(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getString(Constant.InsuFromSubmitted, "0");
	}

	public void setInsuranceForm(Context context, String count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(Constant.InsuFromSubmitted, "" + count);
		editor.commit();
	}

	public boolean getAppShortcut(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPref.getBoolean(Constant.AppShortCut, false);
	}

	public void setAppShortcut(Context context, boolean count) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putBoolean(Constant.AppShortCut, count);
		editor.commit();
	}

	public  void ClearSelectedCard(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Constant.PREFE_FILE_NAME, Context.MODE_PRIVATE);
		settings.edit().remove(Constant.SELECTED_CARD_ID_KEY).commit();
	}

	/*        Gson gson = new Gson();
        String json = gson.toJson(object);
        LocalStore.getInstance().set_SelectedCard(this, json);*/


/*        Gson gson = new Gson();
        String json = LocalStore.getInstance().getSelectedCard(this);
        if (json!=null && !json.equalsIgnoreCase("0")) {
            Health_Card_ListResponse object = gson.fromJson(json, Health_Card_ListResponse.class);
        }*/
}
