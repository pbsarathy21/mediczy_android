package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 13-1-2018.
 */

public class NetworkPartnerDetailResponse  extends BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("merchant_details")
    public NetworkPartner_detail detail;

    @SerializedName("merchant_images")
    public List<NetworkPartnerDetailBanner> banner;

    @SerializedName("merchant_offers")
    public List<NetworkPartner_Merchant_Offers> merchant_offers;
}
