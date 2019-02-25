package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class DoctorScheduleListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("timeslot")
    public String timeslot;
    @SerializedName("status")
    public String status;
}
