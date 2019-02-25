package app.mediczy_com.iconstant;

/**
 * Created by Prithivi Raj on 18-07-2015.
 */
public interface IConstant_WebService {
/*    String BaseUrl_Image = "http://immanuvel.com/selva/app/images/";
    String BaseUrl = "http://immanuvel.com/selva/app/mobile/";*/

    String BaseUrl_Image = "http://mediczy.com/app_n/images/";
    //   String BaseUrl_Image = "http://mediczy.com/app/images/";
//    String BaseUrl = "http://mediczy.com/app/mobile/";
//    String BaseUrl = "http://mediczy.com/app_n/mobile/";
//    String BaseUrl_New = "http://beginnersparadise.com/mediczy/public/api/mobile/";

//    String BaseUrl = "http://beginnersparadise.com/mediczy-laravel/api/mobile/";
    String BaseUrl = "http://mediczy.com/mediczy-api/api/mobile/";

    public static String next_available = BaseUrl + "next_available";
    public static String online_status = BaseUrl + "online_status";
    public static String payment_status = BaseUrl + "payment_status";
    public static String register = BaseUrl + "register";
    public static String doctor_login = BaseUrl + "doctor_login";
    public static String check_user_otp = BaseUrl + "check_user_otp";
    public static String send_user_otp = BaseUrl + "send_user_otp";
    public static String home_page = BaseUrl + "home_page";
    public static String ads = BaseUrl + "ads";
    public static String get_hospital_categories = BaseUrl + "temp_get_hospital_categories";
    public static String get_hospital_category_doctors = BaseUrl + "temp_get_hospital_category_doctors?id=";
    public static String doctors_list = BaseUrl + "doctors_list";
    public static String ambulance_list = BaseUrl + "ambulance_list";
    public static String network_partners = BaseUrl + "network_partners";
    public static String network_partnersHome = BaseUrl + "merchants";
    public static String merchant_get_hospitals = BaseUrl + "get_hospitals";
    public static String card_buy = BaseUrl + "user_health_card";
    public static String delivery_form = BaseUrl + "delivery_form";
    public static String check_user_health_card = BaseUrl + "check_user_health_card";
    public static String user_health_card_pay = BaseUrl + "user_health_card_pay/";
    public static String health_card_faq = BaseUrl + "health_card_faq";
    public static String insurance_faq = BaseUrl + "insurance_faq";
    public static String filtered_doctors_list = BaseUrl + "filtered_doctors_list";
    public static String filters = BaseUrl + "filters";
    public static String doctor_admin_balance = BaseUrl + "doctor_admin_balance";
    public static String patient_appointments = BaseUrl + "patient_appointments";
    public static String doctor_appointments = BaseUrl + "doctor_appointments";
    public static String doctor_appointment_decision = BaseUrl + "doctor_appointment_decision";
    public static String appointment_details = BaseUrl + "appointment_details";
    public static String doctor_session_update = BaseUrl + "doctor_session_update";
    public static String voice_click_to_call = BaseUrl + "voice_click_to_call";
    public static String video_call_log = BaseUrl + "video_call_log";
    public static String add_prescription = BaseUrl + "add_prescription";
    public static String call_completed = BaseUrl + "call_completed";
    public static String user_doctor_detail = BaseUrl + "user_doctor_detail";
    public static String user_free_code = BaseUrl + "user_free_code";
    public static String network_partner_Detail = BaseUrl + "merchant";
    public static String network_partner_invoice_amount = BaseUrl + "merchant_code";
    public static String merchant_rating = BaseUrl + "user_merchant_rating";
    public static String health_card_list = BaseUrl + "health_card_details";
    public static String free_code = BaseUrl + "free_code?";
    public static String doctor_profile = BaseUrl + "doctor_profile";
    public static String doctor_profile_update = BaseUrl + "doctor_profile_update";
    public static String doctor_banner_update = BaseUrl + "doctor_banner_update";
    public static String doctor_pay_u_money = BaseUrl + "doctor_pay_u_money";
    public static String user_pay_u_money = BaseUrl + "user_pay_u_money/";
    public static String user_appointment_pay_u_money = BaseUrl + "user_appointment_pay_u_money/";
    public static String prescription_list = BaseUrl + "prescription_list";
    public static String health_tube_notifications = BaseUrl + "health_tube_notifications";
    public static String prescription_detail = BaseUrl + "prescription";
    public static String doctor_notification = BaseUrl + "doctor_notification";
    public static String check_free_code = BaseUrl + "check_free_code";
    public static String faq = BaseUrl + "faq";

    public static String get_time_slots = BaseUrl + "get_time_slots";
    public static String add_doctor_timeslot = BaseUrl + "add_doctor_timeslot";
    public static String insurances = BaseUrl + "insurances";
    public static String add_user_insurance = BaseUrl + "add_user_insurance";

    public static String imageUrl_Cat = BaseUrl_Image + "category/";
    public static String imageUrl_Doctors = BaseUrl_Image + "doctors/";
    public static String imageUrl_chat_images = BaseUrl_Image + "chat_images/";
    public static String imageUrl_Notification = BaseUrl_Image + "pushnotification/";
    public static String imageUrl_spa_image = BaseUrl_Image + "spa/";
    public static String doctor_banner_image = BaseUrl_Image + "doctors/banner/";
    public static String imageUrl_Doctor = BaseUrl_Image + "doctors/";
    public static String imageUrl_Signature = BaseUrl_Image + "doctors/signature/";
    public static String check_health_card_available = BaseUrl + "check_health_card_available";
    public static String add_patient_appointment = BaseUrl + "add_patient_appointment";

    public static String user_conversations = BaseUrl + "user_conversations";
    public static String chat_list = BaseUrl + "chats";
    public static String initiate_conversation = BaseUrl + "initiate_conversation";
    public static String chat_block_unblock_user = BaseUrl + "chat_block_unblock_user";
    public static String unblock_user = BaseUrl + "unblock_user";
    public static String delete_conversation = BaseUrl + "delete_conversation";
    public static String add_message = BaseUrl + "add_message";
    public static String clear_conversation = BaseUrl + "clear_conversation";

    //Reports Url
    public static String reports = BaseUrl + "reports";
    public static String my_opinions = BaseUrl + "my-opinions";
    public static String user_profile = BaseUrl + "user_profile/";

    // Push HealthTube_List
//    static final String GOOGLE_PROJECT_ID = "277874654158";
    static final String GOOGLE_PROJECT_ID = "mediczy-1150";
    static final String MESSAGE_KEY = "k";
    static final String MESSAGE_KEY_Title = "notification_title";
    static final String MESSAGE_KEY_MSg = "notification_msg";
    static final String MESSAGE_KEY_Time = "notification_time";
    static final String MESSAGE_KEY_Image = "notification_image";

//    "&lat="2123"&lang="12121""

    /*AIzaSyCLaMysFZgjso6IhEYTa3NyeJg28rb-m-Y*/

/*    registration ID=APA91bHuPhoz73oxderjFIVV_SFkIY9VYnksks0ltjb4nKp
    Zskr6QdGpzxLBsWQqgRGK7U2xpNvgfMf-bwGxcXvUCzmVda5tlYh-a7ImwYht6AaRyGw8ZOA*/


    //	web request constant id
    public static int WSR_PatientProblemForm = 1;
    public static int WSR_ViewDetail = 2;
    public static int WSR_Doctor_Prescribed_Detail = 3;
    public static int WSR_Doctor_Prescribed_List = 4;
    public static int WSR_Home_List = 5;
    public static int WSR_YourAppointmentFragment = 6;
    public static int WSR_DoctorAppointmentFragment = 7;
    public static int WSR_PatientFormDetail = 8;
    public static int WSR_NotificationFragment = 9;
    public static int WSR_DoctorsList = 10;
    public static int WSR_AmbulanceFragment = 11;
    public static int WSR_HomeNavigation_Session = 12;
    public static int WSR_Otp_Screen = 13;
    public static int WSR_Doctor_Prescribed = 14;
    public static int WSR_ViewDetail_PhoneCall = 15;
    public static int WSR_Filter_Request = 16;
    public static int WSR_Filter_Post = 17;
    public static int WSR_CardBuyReq = 18;
    public static int WSR_HealthCardReq = 19;
    public static int WSR_ConversationListReq = 20;
    public static int WSR_ChatListReq = 21;
    public static int WSR_ChatSendMessage = 22;
    public static int WSR_ConversationDeleteReq = 23;
    public static int WSR_ChatBlockReq = 24;
    public static int WSR_ChatClearReq = 25;
    public static int WSR_InsuranceCompanyReq = 26;
    public static int WSR_InsuranceSubmitReq = 27;
    public static int WSR_ViewDetail_VideoCall = 28;
    public static int WSR_DeliveryFromReq = 29;
    public static int WSR_NetworkPartnersReq = 30;

    public static int WSR_NP_Detail_Req = 31;
    public static int WSR_NP_Detail_Get_Code_Req = 32;
    public static int WSR_NP_NetworkPartner_List_Req = 33;
    public static int WSR_NP_Detail_Rating_Req = 34;
    public static int WSR_NP_health_card_list_Req = 35;
    public static int WSR_NP_NetworkPartnerDoctor_List_Req = 36;
    public static int WSR_NP_NetworkPartnerAds_List_Req = 37;

    public static int WSR_RegisterPatient = 38;
    public static int WSR_RegisterDoctor = 39;
    public static int WSR_RegisterFreeCode = 40;
    public static int WSR_DoctorProfile = 41;
    public static int WSR_DoctorProfileEdit = 42;
    public static int WSR_DoctorBannerEdit = 43;
    public static int WSR_DoctorBalance = 44;
    public static int WSR_HealthCardCheckAvailable = 45;
    public static int WSR_Call_Completed = 46;
}
