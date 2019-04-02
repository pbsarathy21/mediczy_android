package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 13-1-2018.
 */

public class Health_Card_ListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("membership_card_id")
    public String membership_card_id;
    @SerializedName("name")
    public String name;
    @SerializedName("amount")
    public String amount;
    @SerializedName("benefits")
    public String benefits;
    @SerializedName("image_path")
    public String image_path;

    @SerializedName("cardImage")
    public Integer cardImage;
}
