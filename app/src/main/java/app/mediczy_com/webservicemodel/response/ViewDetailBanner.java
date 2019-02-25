package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class ViewDetailBanner implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("image_path")
    public String image_path;

    @SerializedName("doctor_banner_id")
    public String doctor_banner_id;
}
