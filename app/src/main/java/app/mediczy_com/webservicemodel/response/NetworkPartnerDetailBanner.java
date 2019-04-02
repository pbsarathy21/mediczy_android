package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 13-01-2018.
 */

public class NetworkPartnerDetailBanner implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("image_path")
    public String image_path;
}
