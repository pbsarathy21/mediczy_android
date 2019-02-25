package app.mediczy_com.webservice.request;

import android.content.Context;
import android.support.v4.app.Fragment;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservicemodel.request.CardBuy_Req;
import app.mediczy_com.webservicemodel.request.DeliveryFormReq;
import app.mediczy_com.webservicemodel.request.DoctorProfileReq;
import app.mediczy_com.webservicemodel.request.FilterSearchRequest;
import app.mediczy_com.webservicemodel.request.PatientFromDetail_Req;
import app.mediczy_com.webservicemodel.request.RegisterRequest;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.request.ViewDetailRequest;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class RequestManager {

   public static boolean fillCommonParams(RestClient client, Context activity) {

       String localVersion = null;
       try {
           localVersion = activity.getResources().getString(R.string.versionName);
       } catch (Exception e) {
           e.printStackTrace();
       }
       String device_id = Utility.getInstance().getDeviceID(activity);
       String gcm_id = LocalStore.getInstance().getGcmId(activity);
       String USerId = LocalStore.getInstance().getUserID(activity);
       String Type = LocalStore.getInstance().getType(activity);
       String PhoneNumber = LocalStore.getInstance().getPhoneNumber(activity);
       client.addParam("device_id", device_id);
       client.addParam("fcm_id", gcm_id);
       client.addParam("type", Type);
       client.addParam("id", USerId);
       client.addParam("user_id", USerId);
       client.addParam("doctor_id", USerId);
       client.addParam("phone_number", PhoneNumber);
       client.addParam("version_code", localVersion);
        return true;
    }

    public static boolean PatientRequest(Context activity, Fragment fragment,
                                         PatientFromDetail_Req req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.appointment_details, reqtype);
            fillCommonParams(client, activity);
//            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("user_appointment_id", String.valueOf(req.appointment_id));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at PatientFromDetail_Req in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean PatientProblemForm(Context activity, Fragment fragment,
                                             RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.add_patient_appointment, reqtype);
            fillCommonParams(client, activity);
            client.addParam("appointment_type", String.valueOf(req.appointment_for));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("catergory_id", String.valueOf(req.catergory_id));
            client.addParam("catergory_name", String.valueOf(req.categoryName));
            client.addParam("patient_mobile_number", String.valueOf(req.patient_mobile_number));
            client.addParam("issue", String.valueOf(req.issue));
            client.addParam("gender", String.valueOf(req.gender));
            client.addParam("age", String.valueOf(req.age));
            client.addParam("weight", String.valueOf(req.weight));
            client.addParam("height", String.valueOf(req.height));
            client.addParam("city", String.valueOf(req.city));
            client.addParam("previous_diagnosed", String.valueOf(req.previous_diagnosed));
            client.addParam("medications", String.valueOf(req.medications));
            client.addParam("allergies", String.valueOf(req.allergies));
            client.addParam("doctor_schedule_id", String.valueOf(req.timeslot_id));
            client.addParam("patient_id", String.valueOf(req.patient_id));
            client.addParam("image", req.image);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at PatientProblemForm in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ViewDetailRequest(Context activity, Fragment fragment,
                                           ViewDetailRequest req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.user_doctor_detail, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_id", String.valueOf(req.id));
            client.addParam("mobile_number", String.valueOf(req.mobile_number));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ViewDetailRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ViewDetailCodeRequest(Context activity, Fragment fragment,
                                            ViewDetailRequest req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.user_free_code, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("freecode", String.valueOf(req.free_code));
            client.addParam("phone_number", String.valueOf(req.mobile_number));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ViewDetailCodeRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Doctor_Prescribed_Detail(Context activity, Fragment fragment,
                                                   RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.prescription_detail, reqtype);
            fillCommonParams(client, activity);
            client.addParam("prescription_id", String.valueOf(req.prescription_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Doctor_Prescribed_Detail in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Doctor_PrescribedFragment(Context activity, Fragment fragment,
                                                   RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.prescription_list, reqtype);
            fillCommonParams(client, activity);
            client.addParam("id", String.valueOf(req.id));
            client.addParam("type", String.valueOf(req.type));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Doctor_PrescribedFragment in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean HomeFragment(Context activity, Fragment fragment,
                                                    RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.home_page, reqtype);
            fillCommonParams(client, activity);
  //          client.addParam("mobile_number", String.valueOf(req.mobile_number));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at HomeFragment in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean YourAppointmentFragment(Context activity, Fragment fragment,
                                       RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            fillCommonParams(client, activity);
            client.addParam("patient_id", String.valueOf(req.patient_id));
            client.addParam("patient_phone_number", String.valueOf(req.patient_phone_number));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("doctor_phone_number", String.valueOf(req.doctor_phone_number));
            client.addParam("status", String.valueOf(req.status));
            client.addParam("user_appointment_id", String.valueOf(req.appointment_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at YourAppointmentFragment in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DoctorAdminBalance(Context activity, Fragment fragment,
                                                  RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at DoctorAdminBalance in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DoctorAppointmentFragment(Context activity, Fragment fragment,
                                                  RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("date", String.valueOf(req.date));
            client.addParam("time_slots", String.valueOf(req.time));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at DoctorAppointmentFragment in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NotificationFragment(Context activity, Fragment fragment,
                                               RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.health_tube_notifications, reqtype);
            fillCommonParams(client, activity);
            client.addParam("type", String.valueOf(req.type));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at HealthTubeFragment in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DoctorsList(Context activity, Fragment fragment,
                                               RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            fillCommonParams(client, activity);
            client.addParam("id", String.valueOf(req.user_id));
            client.addParam("category_id", String.valueOf(req.catergory_id));
            client.addParam("hospital_id", String.valueOf(req.hospital_id));
            client.addParam("phone_number", String.valueOf(req.phone_number));
            client.addParam("long", String.valueOf(req.longitude));
            client.addParam("lat", String.valueOf(req.latitude));
            client.addParam("date", String.valueOf(req.date));
            client.addParam("filter_id", String.valueOf(req.filter_id));
            client.addParam("doc_ref_code", String.valueOf(req.refId));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at DoctorsList in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean AmbulanceFragment(Context activity, Fragment fragment,
                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.ambulance_list, reqtype);
            fillCommonParams(client, activity);
            client.addParam("long", String.valueOf(req.longitude));
            client.addParam("lat", String.valueOf(req.latitude));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at MyOffersListFragment in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean HomeNavigation_SessionTime(Context activity, Fragment fragment,
                                            RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.doctor_session_update, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("status", String.valueOf(req.status));
            client.addParam("duration", String.valueOf(req.session_time));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at HomeNavigation_SessionTime in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean HomeNavigation_OnlineStatus(Context activity, Fragment fragment,
                                                     RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.online_status, reqtype);
            fillCommonParams(client, activity);
            client.addParam("number", String.valueOf(req.mobile_number));
            client.addParam("status", String.valueOf(req.onlineStatus));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at HomeNavigation_OnlineStatus in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DoctorLogin(Context activity, Fragment fragment,
                                   RegisterRequest req, int reqType) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.doctor_login, reqType);
            client.addParam("type", String.valueOf(req.type));
            client.addParam("fcm_id", String.valueOf(req.fcm_id));
            client.addParam("device_id", String.valueOf(req.device_id));
            client.addParam("username", String.valueOf(req.username));
            client.addParam("password", String.valueOf(req.password));
            client.addParam("freecode", String.valueOf(req.free_code));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at DoctorLogin in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean RegisterFreeCode(Context activity, Fragment fragment,
                                      RegisterRequest req, int reqType) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.check_free_code, reqType);
            client.addParam("type", String.valueOf(req.type));
            client.addParam("fcm_id", String.valueOf(req.fcm_id));
            client.addParam("device_id", String.valueOf(req.device_id));
            client.addParam("freecode", String.valueOf(req.free_code));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at RegisterFreeCode in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Register(Context activity, Fragment fragment,
                                   RegisterRequest req, int reqType) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.register, reqType);
            client.addParam("type", String.valueOf(req.type));
            client.addParam("fcm_id", String.valueOf(req.fcm_id));
            client.addParam("device_id", String.valueOf(req.device_id));
            client.addParam("firstname", String.valueOf(req.firstname));
            client.addParam("lastname", String.valueOf(req.lastname));
            client.addParam("number", String.valueOf(req.number));
            client.addParam("country_code", String.valueOf(req.country_code));
            client.addParam("gender", String.valueOf(req.gender));
            client.addParam("dob", String.valueOf(req.dob));
            client.addParam("ref_id", String.valueOf(req.ref_id));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Register in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Otp_Screen(Context activity, Fragment fragment,
                                                     RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
 //           fillCommonParams(client, activity);
            client.addParam("number", String.valueOf(req.mobile_number));
            client.addParam("user_id", String.valueOf(req.user_id));
            client.addParam("otp", String.valueOf(req.otp));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Otp_Screen in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Otp_Resend(Context activity, Fragment fragment,
                                     RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            //           fillCommonParams(client, activity);
            client.addParam("number", String.valueOf(req.mobile_number));
            client.addParam("user_id", String.valueOf(req.user_id));
            client.addParam("otp", String.valueOf(req.otp));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Otp_Screen in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Doctor_Prescribed(Context activity, Fragment fragment,
                                                    RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.add_prescription, reqtype);
//            fillCommonParams(client, activity);
            client.addParam("user_id", String.valueOf(req.patient_id));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.addParam("prescription", String.valueOf(req.prescription_Msg));
            client.addParam("doctor_subject", String.valueOf(req.prescription_Subject));
            client.addParam("code_status", String.valueOf(req.prescription_CodeStatus));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Doctor_Prescribed in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Doctor_Call_Completed(Context activity, Fragment fragment,
                                            RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.call_completed, reqtype);
            fillCommonParams(client, activity);
            client.addParam("user_id", String.valueOf(req.patient_id));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Doctor_Call_Completed in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ViewDetail_PhoneCallRequest(Context activity, Fragment fragment,
                                            RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.voice_click_to_call, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_mobilenumber", String.valueOf(req.doctor_phone_number));
            client.addParam("patient_mobilenumber", String.valueOf(req.patient_phone_number));
            client.addParam("patient_id", String.valueOf(req.patient_id));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ViewDetail_PhoneCallRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ViewDetail_videoCallRequest(Context activity, Fragment fragment,
                                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.video_call_log, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_mobilenumber", String.valueOf(req.doctor_phone_number));
            client.addParam("patient_mobilenumber", String.valueOf(req.patient_phone_number));
            client.addParam("user_id", String.valueOf(req.patient_id));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ViewDetail_videoCallRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Filter(Context activity, Fragment fragment,
                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.filters, reqtype);
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.GET, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Filter in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean FilterSearch(Context activity, Fragment fragment,
                                       FilterSearchRequest req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.filtered_doctors_list, reqtype);
            fillCommonParams(client, activity);
            client.addParam("location", String.valueOf(req.location));
            client.addParam("language", String.valueOf(req.language));
            client.addParam("appointment", String.valueOf(req.appointment));
            client.addParam("fees", String.valueOf(req.docFees));
            client.addParam("education", String.valueOf(req.education));
            client.addParam("exp_years", String.valueOf(req.experience));
            client.addParam("category", String.valueOf(req.speciality));
            client.addParam("lat", String.valueOf(req.latitude));
            client.addParam("long", String.valueOf(req.longitude));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at FilterSearch in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean CardBuyRequest(Context activity, Fragment fragment,
                                         CardBuy_Req req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.card_buy, reqtype);
            fillCommonParams(client, activity);
            client.addParam("first_name", String.valueOf(req.first_name));
            client.addParam("last_name", String.valueOf(req.last_name));
            client.addParam("email", String.valueOf(req.email));
            client.addParam("street_address", String.valueOf(req.street_address));
            client.addParam("street_address1", String.valueOf(req.street_address1));
            client.addParam("city", String.valueOf(req.city));
            client.addParam("state", String.valueOf(req.state));
            client.addParam("zip_code", String.valueOf(req.zip_code));
            client.addParam("country", String.valueOf(req.country));
            client.addParam("phone", String.valueOf(req.phone));
            client.addParam("membership_card_id", String.valueOf(req.selectedCard));
            client.addParam("amount", String.valueOf(req.selectedCardAmount));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at CardBuyRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean HealthCardRequest(Context activity, Fragment fragment, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.check_user_health_card, reqtype);
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at HealthCardRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ConversationList(Context activity, Fragment fragment,
                                     RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.user_conversations, reqtype);
            fillCommonParams(client, activity);
            client.addParam("type", String.valueOf(req.type));
            client.addParam("id", String.valueOf(req.id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ConversationList in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ConversationDelete(Context activity, Fragment fragment,
                                           RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.delete_conversation, reqtype);
            fillCommonParams(client, activity);
            client.addParam("conversation_id", String.valueOf(req.conversation_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ConversationDelete in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ChatList(Context activity, Fragment fragment,
                                           RequestCommon req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            fillCommonParams(client, activity);
            client.addParam("conversation_id", String.valueOf(req.conversation_id));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ChatList in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ChatSendMessage(Context activity, Fragment fragment,
                                   RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.add_message, reqtype);
            fillCommonParams(client, activity);
            client.addParam("conversation_id", String.valueOf(req.conversation_id));
            client.addParam("message", String.valueOf(req.message));
            client.addParam("message_type", String.valueOf(req.message_type));
            client.addParam("current_time", String.valueOf(req.current_time));
            client.addParam("image", String.valueOf(req.image));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ChatSendMessage in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ChatBlock(Context activity, Fragment fragment,
                                   RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.chat_block_unblock_user, reqtype);
            fillCommonParams(client, activity);
            client.addParam("conversation_id", String.valueOf(req.conversation_id));
            client.addParam("status", String.valueOf(req.status));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ChatBlock in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ChatUn_Block(Context activity, Fragment fragment,
                                    RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.unblock_user, reqtype);
            fillCommonParams(client, activity);
            client.addParam("log_userid", String.valueOf(req.userId));
            client.addParam("log_usertype", String.valueOf(req.type));
            client.addParam("block_id", String.valueOf(req.receiver_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ChatUn_Block in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean ChatClear(Context activity, Fragment fragment,
                                       RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.clear_conversation, reqtype);
            fillCommonParams(client, activity);
            client.addParam("conversation_id", String.valueOf(req.conversation_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at ChatClear in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean InsuranceCompany(Context activity, Fragment fragment,
                                    RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.insurances, reqtype);
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.GET, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at InsuranceCompany in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DoctorEditPhoto(Context activity, Fragment fragment,
                                          DoctorProfileReq req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.doctor_banner_update, reqtype);
            client.addParam("doctor_banner_id", String.valueOf(req.doctor_banner_id));
            client.addParam("image", String.valueOf(req.imagePath));
            client.addParam("doctor_id", String.valueOf(req.doctor_id));
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at InsuranceCompany in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean SubmitInsurance(Context activity, Fragment fragment,
                                           RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.add_user_insurance, reqtype);
            fillCommonParams(client, activity);
            client.addParam("insurance_company", String.valueOf(req.insurance_company));
            client.addParam("policy_number", String.valueOf(req.Policy_Number));
            client.addParam("sum_insured", String.valueOf(req.Sum_Assured));
            client.addParam("start_date", String.valueOf(req.start_date));
            client.addParam("end_date", String.valueOf(req.end_date));
            client.addParam("dependants", String.valueOf(req.dependants));
            client.addParam("child", String.valueOf(req.child));
            client.addParam("adult", String.valueOf(req.adult));
            client.addParam("emp_name", String.valueOf(req.Employer_Name));
            client.addParam("id_proof", String.valueOf(req.ID_Proof));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at SubmitInsurance in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DeliveryFormRequest(Context activity, Fragment fragment,
                                              DeliveryFormReq req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.delivery_form, reqtype);
            fillCommonParams(client, activity);
            client.addParam("ad_id", req.adsId);
            client.addParam("type", String.valueOf(req.type));
            client.addParam("address", String.valueOf(req.Address));
            client.addParam("phone", String.valueOf(req.Phone));
            client.addParam("zip", String.valueOf(req.Zip));
            client.addParam("state", String.valueOf(req.state));
            client.addParam("country", String.valueOf(req.country));
            client.addParam("date", String.valueOf(req.date));
            client.addParam("time", String.valueOf(req.time));
            client.addParam("city", String.valueOf(req.City));

            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at DeliveryFormRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DoctorProfileRequest(Context activity, Fragment fragment,
                                               DoctorProfileReq req, int reqtype, String url) {
        try {
            // Generate req
            RestClient client = new RestClient(url, reqtype);
            fillCommonParams(client, activity);
            client.addParam("doctor_id", req.doctor_id);
            client.addParam("firstname", req.firstname);
            client.addParam("lastname", req.lastname);
            client.addParam("degree", req.degree);
            client.addParam("experience", req.experience);
            client.addParam("address", req.address);
            client.addParam("price", req.price);
            client.addParam("licence", req.licence);
            client.addParam("languages", req.languages);
            client.addParam("services", req.services);
            client.addParam("image", req.imagePath);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at DoctorProfileRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartners(Context activity, Fragment fragment,
                                            RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.network_partners, reqtype);
            fillCommonParams(client, activity);
            client.addParam("long", String.valueOf(req.longitude));
            client.addParam("lat", String.valueOf(req.latitude));
            client.addParam("tpa_name", String.valueOf(req.tpa_name));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartners in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartnersHome(Context activity, Fragment fragment,
                                          RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.network_partnersHome, reqtype);
            client.addParam("long", String.valueOf(req.longitude));
            client.addParam("lat", String.valueOf(req.latitude));
            client.addParam("merchant_category_id", String.valueOf(req.id));
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartnersHome in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartnersHospitalHome(Context activity, Fragment fragment,
                                              RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.merchant_get_hospitals, reqtype);
            client.addParam("long", String.valueOf(req.longitude));
            client.addParam("lat", String.valueOf(req.latitude));
            client.addParam("merchant_category_id", String.valueOf(req.id));
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartnersHome in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartnersAdsHome(Context activity, Fragment fragment,
                                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.ads, reqtype);
            client.addParam("long", String.valueOf(req.longitude));
            client.addParam("lat", String.valueOf(req.latitude));
            client.addParam("merchant_category_id", String.valueOf(req.id));
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartnersAdsHome in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartnerDetailRequest(Context activity, Fragment fragment,
                                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.network_partner_Detail, reqtype);
            fillCommonParams(client, activity);
            client.addParam("merchant_id", String.valueOf(req.id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartnerDetailRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartnerInvoiceRequest(Context activity, Fragment fragment,
                                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.network_partner_invoice_amount, reqtype);
            fillCommonParams(client, activity);
            client.addParam("merchant_id", String.valueOf(req.id));
            client.addParam("amount", String.valueOf(req.amount));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartnerInvoiceRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean NetworkPartnerRatingRequest(Context activity, Fragment fragment,
                                                       RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.merchant_rating, reqtype);
            fillCommonParams(client, activity);
            client.addParam("merchant_id", String.valueOf(req.id));
            client.addParam("rating", String.valueOf(req.rating));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at NetworkPartnerRatingRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean CardBuyListRequest(Context activity, Fragment fragment,
                                                      RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.health_card_list, reqtype);
            fillCommonParams(client, activity);
            client.execute(RestClient.RequestMethod.GET, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at CardBuyListRequest in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean Merchant_Category(Context activity, Fragment fragment,
                                       RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.get_hospital_categories, reqtype);
            fillCommonParams(client, activity);
            client.addParam("hospital_id", String.valueOf(req.hospital_id));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at Merchant_Category in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean CheckHealthCardAvailable(Context activity, Fragment fragment,
                                            RequestCommon req, int reqtype) {
        try {
            // Generate req
            RestClient client = new RestClient(IConstant_WebService.check_health_card_available, reqtype);
            fillCommonParams(client, activity);
            client.addParam("health_card_number", String.valueOf(req.health_card_number));
            client.execute(RestClient.RequestMethod.POST, activity, fragment);
        } catch (Exception e) {
            MLog.e("RequestManager", "exception at CheckHealthCardAvailable in RequestManager and error is " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
