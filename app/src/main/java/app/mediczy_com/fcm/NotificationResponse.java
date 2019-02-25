package app.mediczy_com.fcm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 04-11-2017.
 */

public class NotificationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;

    @SerializedName("id")
    public String id;

    @SerializedName("status")
    public String status;
}
