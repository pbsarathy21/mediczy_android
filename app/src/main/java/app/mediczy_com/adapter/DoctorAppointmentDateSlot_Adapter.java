package app.mediczy_com.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.BeanDateSlot;

/**
 * Created by Prithivi Raj on 27-10-2016.
 */
public class DoctorAppointmentDateSlot_Adapter extends RecyclerView.Adapter<DoctorAppointmentDateSlot_Adapter.ViewHolder> {
    Context context;
    Fragment fragment;
    private ArrayList<BeanDateSlot> arrayList;

    public DoctorAppointmentDateSlot_Adapter(Context context, Fragment doctorsList, ArrayList<BeanDateSlot> itemsData) {
        this.arrayList = itemsData;
        this.context = context;
        this.fragment = doctorsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DoctorAppointmentDateSlot_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_appo_date_slot, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.Tv_Name.setText(arrayList.get(position).getWeekDay());
        viewHolder.Tv_dateNumber.setText(arrayList.get(position).getDateNumeric());

        if (arrayList.get(position).isSelected()) {
            viewHolder.Tv_dateNumber.setTextColor(context.getResources().getColor(R.color.color_1));
            viewHolder.Tv_dateNumber.setBackgroundResource(R.drawable.rounded_corner_date_white);
  //          viewHolder.Rl.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.Tv_dateNumber.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.Tv_dateNumber.setBackgroundResource(R.drawable.rounded_corner_date_app_color);
        }

        viewHolder.Rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0; i<arrayList.size(); i++) {
                    viewHolder.Tv_dateNumber.setTextColor(context.getResources().getColor(R.color.black));
                    viewHolder.Tv_dateNumber.setBackgroundResource(R.drawable.rounded_corner_date_app_color);
                    arrayList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }
    //            viewHolder.Rl.setBackgroundResource(R.drawable.btn_border_app_color);
                viewHolder.Tv_dateNumber.setTextColor(context.getResources().getColor(R.color.color_1));
                viewHolder.Tv_dateNumber.setBackgroundResource(R.drawable.rounded_corner_date_white);
                arrayList.get(position).setSelected(true);
                notifyItemChanged(position);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Name, Tv_dateNumber;
        private CardView Rl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textview_doctor_date_slot);
            Tv_dateNumber = (TextView) itemLayoutView.findViewById(R.id.textview_doctor_date_slot_number);
            Rl = (CardView) itemLayoutView.findViewById(R.id.card_relative_layout_doctor_date_slot);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}