package app.mediczy_com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.BeanDetailTimeSlot;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class DetailDateTime_Adapter extends RecyclerView.Adapter<DetailDateTime_Adapter.ViewHolder> {
    Context context;
    private ArrayList<BeanDetailTimeSlot> arrayList;

    public DetailDateTime_Adapter(Context doctorsList, ArrayList<BeanDetailTimeSlot> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DetailDateTime_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_time_slot_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.tvTime.setText(arrayList.get(position).getTime());
        viewHolder.tvDate.setText(arrayList.get(position).getDate());
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTime;
        public TextView tvDate;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvTime = (TextView) itemLayoutView.findViewById(R.id.textview_vd_time_slot);
            tvDate = (TextView) itemLayoutView.findViewById(R.id.textview_vd_date_slot);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}