package app.mediczy_com.health_card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinaygaba.creditcardview.CreditCardView;
import java.util.ArrayList;
import app.mediczy_com.R;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservicemodel.response.Health_Card_ListResponse;

/**
 * Created by Prithivi Raj on 26-12-2017.
 */
public class HealthCard_Adapter extends RecyclerView.Adapter<HealthCard_Adapter.ViewHolder> {

    Context context;
    private ArrayList<Health_Card_ListResponse> arrayList;
    private ImageLoader imageLoader;
    PaymentCardBuyObserver caller;

    public HealthCard_Adapter(Context ctx, ArrayList<Health_Card_ListResponse> itemsData) {
        this.arrayList = itemsData;
        this.context = ctx;
        imageLoader=new ImageLoader(ctx);
        caller = (PaymentCardBuyObserver) HealthCardListActivity.healthCardListActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HealthCard_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_health_card_list, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final Health_Card_ListResponse object = arrayList.get(position);
        String imagePath = arrayList.get(position).image_path;
        MLog.e("imagePath", "" + imagePath);
//        imageLoader.DisplayImage(imagePath, viewHolder.healthCard, 4);

        viewHolder.healthCard.setBackgroundResource(object.cardImage);
        viewHolder.healthCard.setCardName(object.name);
        viewHolder.healthCard.setCardNumber(object.amount+" Rs");
        viewHolder.healthCard.setExpiryDate("");
//        viewHolder.healthCard.setValid(object.benefits);
        viewHolder.healthCard.setValid("");

        viewHolder.healthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardId = object.membership_card_id;
                Utility.getInstance().showToast("Selected : "+arrayList.get(position).name, context);
                caller.onCardSelected(object);
            }
        });

        viewHolder.btnBenefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller.onCardBenefitsSelected(object);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CreditCardView healthCard;
        public TextView btnBenefit;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            healthCard = (CreditCardView) itemLayoutView.findViewById(R.id.adapter_health_card);
            btnBenefit = (TextView) itemLayoutView.findViewById(R.id.button_adapter_health_card_benefit);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}