package app.mediczy_com.webservice.request;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class RestClient {

	private ArrayList<NameValuePair> headers;
	private ArrayList<NameValuePair> params;

	public enum RequestMethod {
		POST, GET
	}
	private String url;
	private int requestType;
	private static RequestQueue queue;

	public void addParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public RestClient(String url, int requestType) {
		this.url = url;
		this.requestType = requestType;
		headers = new ArrayList<NameValuePair>();
		params = new ArrayList<NameValuePair>();
	}

	public void executeLog() {
		MLog.e("", "Request Api " + url);
		// For debugging
		for (NameValuePair p : params) {
			MLog.e("", "Request param :" + p.getName() + " = " + p.getValue());
		}
	}

	public void execute(RequestMethod method, Context activity, Fragment fragment) throws Exception {
		executeLog();
		if (fragment!=null)
			postData(url, method, activity, (ResponseListener) fragment);
		else
			postData(url, method, activity, (ResponseListener) activity);
	}

	private void postData(String url, RequestMethod method, final Context activity, final ResponseListener resplist) {
		try {
			//queue = Volley.newRequestQueue(activity);
			if (queue == null) {
				queue = Volley.newRequestQueue(activity);
			}
			int timeout = 60000; // 60 seconds - time out
			int requestMethod = 0;
			if (method.name().equals(RequestMethod.POST.name())) {
				requestMethod = Method.POST;
			}else if (method.name().equals(RequestMethod.GET.name())) {
				requestMethod = Method.GET;
			} MLog.e("requestMethod: ", method.name()+" : "+String.valueOf(requestMethod));
			StringRequest postRequest = new StringRequest(requestMethod, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								Object object = invokeParser(response, requestType);
								MLog.e("onResponse :", response.toString());
								resplist.onResponseReceived(object, requestType);
							} catch (Exception e) {
								e.printStackTrace();
								MLog.e("onResponse Exception:", e.toString());
							}
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// error
					MLog.e("Error.Response", "" + error.hashCode());
					resplist.onResponseReceived(null, requestType);
				}
			}) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<String, String>();
					headers.put("Content-Type","application/x-www-form-urlencoded");
//					headers.put("Authorization", basicAuth);
//					headers.put("Accept-Encoding", "gzip");
					return headers;
				}

				@Override
				protected Map<String, String> getParams() {

					Map<String, String> pvalues = new HashMap<String, String>();
					for (NameValuePair p : params) {
						pvalues.put("" + p.getName(), "" + p.getValue());
					}
					return pvalues;
				}
			};
			// Request Time out
			postRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			queue.add(postRequest);
		} catch (Exception e) {
			MLog.e("requestException: ", e.toString());
		}
	}

	private Object invokeParser(String response, int req_code) {
		switch (req_code) {
			case IConstant_WebService.WSR_ViewDetail:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_PatientProblemForm:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Doctor_Prescribed_Detail:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Doctor_Prescribed_List:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Home_List:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_YourAppointmentFragment:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DoctorAppointmentFragment:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_PatientFormDetail:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NotificationFragment:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DoctorsList:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_AmbulanceFragment:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_HomeNavigation_Session:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Otp_Screen:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Doctor_Prescribed:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_ViewDetail_PhoneCall:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_ViewDetail_VideoCall:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_CardBuyReq:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_HealthCardReq:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Filter_Request:
				return Parse.FilterResponse(response);
			case IConstant_WebService.WSR_Filter_Post:
				return Parse.CommonParse(response);

			case IConstant_WebService.WSR_ConversationListReq:
				return Parse.ChatResponse(response);
			case IConstant_WebService.WSR_ConversationDeleteReq:
				return Parse.ChatResponse(response);
			case IConstant_WebService.WSR_ChatBlockReq:
				return Parse.ChatResponse(response);
			case IConstant_WebService.WSR_ChatClearReq:
				return Parse.ChatResponse(response);
			case IConstant_WebService.WSR_ChatListReq:
				return Parse.ChatResponse(response);
			case IConstant_WebService.WSR_ChatSendMessage:
				return Parse.ChatResponse(response);
			case IConstant_WebService.WSR_InsuranceCompanyReq:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_InsuranceSubmitReq:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DeliveryFromReq:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NetworkPartnersReq:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NP_NetworkPartnerDoctor_List_Req:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NP_NetworkPartnerAds_List_Req:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NP_Detail_Req:
				return Parse.NetworkPartnerDetailResponse(response);
			case IConstant_WebService.WSR_NP_NetworkPartner_List_Req:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NP_health_card_list_Req:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NP_Detail_Get_Code_Req:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_NP_Detail_Rating_Req:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_Call_Completed:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_RegisterPatient:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_RegisterDoctor:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_RegisterFreeCode:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DoctorProfile:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DoctorProfileEdit:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DoctorBannerEdit:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_DoctorBalance:
				return Parse.CommonParse(response);
			case IConstant_WebService.WSR_HealthCardCheckAvailable:
				return Parse.CommonParse(response);
		}
		return null;
	}
}
