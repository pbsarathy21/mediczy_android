package app.mediczy_com.payment;

/**
 * Created by Prithivi on 22-10-2016.
 */

public interface PaymentFreeCodeObserver {

    void onPaymentReceived(boolean paymentStatus);
}
