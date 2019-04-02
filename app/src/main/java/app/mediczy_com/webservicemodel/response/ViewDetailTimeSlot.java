package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class ViewDetailTimeSlot implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("date")
    public String date;

    @SerializedName("time")
    public String time;

    @SerializedName("doctor_schedule_id")
    public String doctor_schedule_id;
}
