package app.mediczy_com.network_partners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.mediczy_com.R;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservicemodel.response.MerchantsListResponse;
import app.mediczy_com.webservicemodel.response.NetworkPartner_Merchant_Offers;

/**
 * Created by Prithivi Raj on 26-12-2017.
 */
public class MerchantsDetailOffersList_Adapter extends RecyclerView.Adapter<MerchantsDetailOffersList_Adapter.ViewHolder> {

    private Context context;
    private ImageLoader imageLoader;
    private List<NetworkPartner_Merchant_Offers> arrayList;

    public MerchantsDetailOffersList_Adapter(MerchantDetail ctx, List<NetworkPartner_Merchant_Offers> merchant_offers) {
        this.arrayList = merchant_offers;
        this.context = ctx;
        imageLoader=new ImageLoader(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MerchantsDetailOffersList_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.network_partner_detail_offers, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.tv_desc.setText(""+arrayList.get(position).description);
        viewHolder.tv_discount.setText(""+arrayList.get(position).discount +" %");
        viewHolder.tv_discountHeading.setText("Offer "+ ++position);
        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_desc;
        public TextView tv_discount;
        public TextView tv_discountHeading;
        public RelativeLayout rl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tv_desc = (TextView) itemLayoutView.findViewById(R.id.tv_np_detail_desc);
            tv_discount = (TextView) itemLayoutView.findViewById(R.id.tv_np_detail_discount);
            tv_discountHeading = (TextView) itemLayoutView.findViewById(R.id.tv_np_detail_cus_des);
            rl = (RelativeLayout) itemLayoutView.findViewById(R.id.ll_np_detail_cus_all);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}