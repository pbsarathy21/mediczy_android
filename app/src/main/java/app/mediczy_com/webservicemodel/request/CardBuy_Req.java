package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 26-03-2017.
 */

public class CardBuy_Req implements Serializable {
    private static final long serialVersionUID = 1L;

    public String first_name;
    public String last_name;
    public String email;
    public String street_address;
    public String street_address1;
    public String city;
    public String state;
    public String zip_code;
    public String country;
    public String phone;
    public String selectedCard;
    public String selectedCardAmount;
}
