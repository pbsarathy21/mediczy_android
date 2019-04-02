package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class HealthCardDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;
    @SerializedName("status")
    public String status;
    @SerializedName("card_id")
    public String card_id;
    @SerializedName("card_validity")
    public String card_validity;
    @SerializedName("card_name")
    public String card_name;
}
