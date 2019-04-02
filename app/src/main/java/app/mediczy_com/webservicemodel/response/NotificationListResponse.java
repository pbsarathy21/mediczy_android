package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class NotificationListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("notification_title")
    public String notification_title;
    @SerializedName("notification_msg")
    public String notification_msg;
    @SerializedName("notification_time")
    public String notification_time;
    @SerializedName("notification_image")
    public String notification_image;
    @SerializedName("type")
    public String type;
    @SerializedName("view_type")
    public String view_type;
    @SerializedName("ad_id")
    public String ads_id;
    @SerializedName("company_name")
    public String company_name;
    @SerializedName("company_image")
    public String company_image;
    @SerializedName("company_view_count")
    public String company_view_count;
}
