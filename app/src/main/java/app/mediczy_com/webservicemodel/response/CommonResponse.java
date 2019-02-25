package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class CommonResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("prescription")
    public PrescriptionDetail prescription_details;

    @SerializedName("appointment_details")
    public Appointment_Details appointment_details;

    @SerializedName("prescription_list")
    public List<PrescribedListResponse>  prescribedListResponses;

    @SerializedName("categories")
    public List<HomeCategoryResponse>  categoryResponses;

    @SerializedName("cat_values")
    public List<HomeCategoryResponse>  hospital_categoriesResponses;

    @SerializedName("patient_appointment_list")
    public List<YourAppointmentResponse>  patient_appointment_list;

    @SerializedName("time_slots")
    public List<DoctorScheduleListResponse>  time_slots;

    @SerializedName("push_notifications")
    public List<NotificationListResponse>  notificationlist;

    @SerializedName("doctors")
    public List<DoctorListResponse>  doctorsList;

    @SerializedName("hospital_category_doctors")
    public List<HospitalCategoryDoctorsListResponse>  hospital_category_doctorsList;

    @SerializedName("ambulance_list")
    public List<AmbulanceListResponse>  ambulanceList;

    @SerializedName("health_card_detail")
    public HealthCardDetail healthCardDetail;

    @SerializedName("insurance_list")
    public List<InsuranceCompanyListResponse>  insuranceCompanyList;

    @SerializedName("network_partners")
    public List<NetworkPartnerListResponse>  networkPartnerListResponses;

    @SerializedName("ad_list")
    public List<NotificationListResponse>  adList;

    @SerializedName("hospitals")
    public List<HospitalsMerchantsListResponse>  hospitals_categories_list;

    @SerializedName("merchant_categories")
    public List<Merchant_CategoriesListResponse>  merchant_categories_list;

    @SerializedName("merchants")
    public List<MerchantsListResponse>  merchantsListResponse;

    @SerializedName("health_card_details")
    public List<Health_Card_ListResponse>  health_card_list;

    @SerializedName("doctor")
    public DoctorRegisterResponse doctorRegisterResponse;

    @SerializedName("user")
    public PatientRegisterResponse patientRegisterResponse;

    @SerializedName("doctor_details")
    public Doctors_detail detail;

    @SerializedName("timeslots")
    public List<ViewDetailTimeSlot> timeslots;

    @SerializedName("doctor_profile")
    public DoctorProfile doctorProfile;

    @SerializedName("doctor_banners")
    public List<ViewDetailBanner> doctor_banners;
}
