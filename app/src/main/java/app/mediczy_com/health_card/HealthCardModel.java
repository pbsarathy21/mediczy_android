package app.mediczy_com.health_card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi Raj on 08-06-2017.
 */

public class HealthCardModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("cardImage")
    public Integer cardImage;

    @SerializedName("cardName")
    public String cardName;

    @SerializedName("cardNumber")
    public String cardNumber;

    @SerializedName("cardExpiryDate")
    public String cardExpiryDate;

    @SerializedName("cardCVVNumber")
    public String cardCVVNumber;

    @SerializedName("cardId")
    public String cardId;
}
