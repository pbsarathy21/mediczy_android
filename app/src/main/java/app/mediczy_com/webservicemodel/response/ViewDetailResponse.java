package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class ViewDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("doctor_details")
    public Doctors_detail detail;

    @SerializedName("timeslots")
    public List<ViewDetailTimeSlot> timeslots;
}
