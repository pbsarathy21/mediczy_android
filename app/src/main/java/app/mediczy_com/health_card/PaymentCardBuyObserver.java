package app.mediczy_com.health_card;

import app.mediczy_com.webservicemodel.response.Health_Card_ListResponse;

/**
 * Created by Prithivi on 26-03-2017.
 */

public interface PaymentCardBuyObserver {

    void onPaymentReceived(boolean paymentStatus);

    void onCardSelected(Health_Card_ListResponse object);

    void onCardBenefitsSelected(Health_Card_ListResponse object);
}
