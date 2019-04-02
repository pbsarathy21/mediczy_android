package app.mediczy_com.dashboard;

import app.mediczy_com.webservicemodel.response.Merchant_CategoriesListResponse;

/**
 * Created by Prithivi on 29-12-2017.
 */

public interface SelectedNetworkPartnerObserver {

    void onSelectedTopCategory(Merchant_CategoriesListResponse object);
}
