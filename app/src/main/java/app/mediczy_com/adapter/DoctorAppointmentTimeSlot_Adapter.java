package app.mediczy_com.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.appointment.TimeObserver;
import app.mediczy_com.bean.BeanTimeSlot;

/**
 * Created by Prithivi Raj on 27-10-2016.
 */
public class DoctorAppointmentTimeSlot_Adapter extends RecyclerView.Adapter<DoctorAppointmentTimeSlot_Adapter.ViewHolder> {

    Context context;
    Fragment fragment;
    public TimeObserver observer;
    private ArrayList<BeanTimeSlot> arrayList;

    public DoctorAppointmentTimeSlot_Adapter(Context context, Fragment doctorsList, ArrayList<BeanTimeSlot> itemsData) {
        this.arrayList = itemsData;
        this.context = context;
        this.fragment = doctorsList;
        if (fragment!=null)
            observer = (TimeObserver) fragment;
        else
            observer = (TimeObserver) context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DoctorAppointmentTimeSlot_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_appo_time_slot, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.Tv_Name.setText(arrayList.get(position).getTime());
        String type = arrayList.get(position).getAvailable();
        if (type.equalsIgnoreCase("Available")) {
            viewHolder.rl.setBackgroundColor(context.getResources().getColor(R.color.white));
            viewHolder.BtnAvaButton.setBackgroundResource(R.drawable.btn_border_app_color);
            viewHolder.BtnAvaButton.setText(type);
            viewHolder.BtnAvaButton.setEnabled(true);
        } if (type.equalsIgnoreCase("Busy")) {
            viewHolder.rl.setBackgroundColor(context.getResources().getColor(R.color.white));
            viewHolder.BtnAvaButton.setBackgroundResource(R.drawable.btn_border_red);
            viewHolder.BtnAvaButton.setText(type);
            viewHolder.BtnAvaButton.setEnabled(true);
        }if (type.equalsIgnoreCase("Booked")) {
            viewHolder.rl.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
            viewHolder.BtnAvaButton.setBackgroundResource(R.drawable.btn_border_gray);
            viewHolder.BtnAvaButton.setText(type);
            viewHolder.BtnAvaButton.setEnabled(false);
        }
        viewHolder.BtnAvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = arrayList.get(position).getAvailable();
                if (type.equalsIgnoreCase("Available")) {
                    arrayList.get(position).setAvailable("Busy");
                    viewHolder.rl.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
                    viewHolder.BtnAvaButton.setBackgroundResource(R.drawable.btn_border_gray);
                    viewHolder.BtnAvaButton.setText("Busy");
                    observer.onSelectedTime(position, arrayList, false);
                } if (type.equalsIgnoreCase("Busy")) {
                    arrayList.get(position).setAvailable("Available");
                    viewHolder.BtnAvaButton.setBackgroundResource(R.drawable.btn_border_app_color);
                    viewHolder.BtnAvaButton.setText("Available");
                    observer.onSelectedTime(position, arrayList, true);
                }
                notifyItemChanged(position);
//                notifyDataSetChanged();
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Name;
        public Button BtnAvaButton;
        public RelativeLayout rl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textview_doctor_time_slot);
            BtnAvaButton = (Button) itemLayoutView.findViewById(R.id.btn_doctor_appo_time_slot);
            rl = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view_doctor_time_slot);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}