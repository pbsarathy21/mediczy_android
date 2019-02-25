package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Prithivi on 13-01-2018.
 */

public class NetworkPartner_Merchant_Offers implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public String id;
    @SerializedName("merchant_id")
    public String merchant_id;
    @SerializedName("description")
    public String description;
    @SerializedName("discount")
    public String discount;
    @SerializedName("status")
    public String status;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
