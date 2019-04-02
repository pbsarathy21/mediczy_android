package app.mediczy_com.filter;

import java.util.List;
import app.mediczy_com.webservicemodel.response.DoctorListResponse;

/**
 * Created by Prithivi on 05-03-2017.
 */

public interface FilterRefreshDoctorListObserver {

    void onRefreshReceived(List<DoctorListResponse> doctorsList, String filter_id, String Speciality);
}
