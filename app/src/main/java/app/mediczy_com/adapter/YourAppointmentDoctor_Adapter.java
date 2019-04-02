package app.mediczy_com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import app.mediczy_com.appointment.DoctorAcceptObserver;
import app.mediczy_com.bean.BeanYourAppointment;
import app.mediczy_com.patient_problem_form.PatientFormDetail;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class YourAppointmentDoctor_Adapter extends RecyclerView.Adapter<YourAppointmentDoctor_Adapter.ViewHolder> {
    Context context;
    Fragment fragment;
    public DoctorAcceptObserver observer;
    private ArrayList<BeanYourAppointment> arrayList;

    public YourAppointmentDoctor_Adapter(Context doctorsList, Fragment fragment, ArrayList<BeanYourAppointment> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        this.fragment = fragment;
        if (fragment!=null)
            observer = (DoctorAcceptObserver) fragment;
        else
            observer = (DoctorAcceptObserver) context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public YourAppointmentDoctor_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_your_appointment_doctor, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.TvName.setText(arrayList.get(position).getName());
        viewHolder.TvAge.setText(arrayList.get(position).getAge());
        viewHolder.TvDate.setText(arrayList.get(position).getDate_time());
        viewHolder.TvBookedDate.setText(arrayList.get(position).getBookedDate());
        String name = arrayList.get(position).getName();
        name = String.valueOf(name.charAt(0));
        MLog.e("Name_FirstLetter", "" + name);

        viewHolder.IvImage.setText(name);

        String status = arrayList.get(position).getStatus();
        if (status.equalsIgnoreCase("PENDING")) {
            viewHolder.rlBoth.setVisibility(View.VISIBLE);
            viewHolder.btnAccept_Decline.setVisibility(View.GONE);
        }if (status.equalsIgnoreCase("ACCEPTED")) {
            viewHolder.rlBoth.setVisibility(View.GONE);
            viewHolder.btnAccept_Decline.setText("ACCEPTED");
            viewHolder.btnAccept_Decline.setVisibility(View.VISIBLE);
            viewHolder.btnAccept_Decline.setBackgroundResource(R.drawable.btn_border_app_color);
        }if (status.equalsIgnoreCase("REJECTED")) {
            viewHolder.rlBoth.setVisibility(View.GONE);
            viewHolder.btnAccept_Decline.setText("REJECTED");
            viewHolder.btnAccept_Decline.setVisibility(View.VISIBLE);
            viewHolder.btnAccept_Decline.setBackgroundResource(R.drawable.btn_border_red);
        }

        viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                arrayList.get(position).setStatus("accepted");
//                notifyItemChanged(position);
                observer.onDoctorAccept(position, arrayList, true);
            }
        });

        viewHolder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  //              arrayList.get(position).setStatus("declined");
 //               notifyItemChanged(position);
                observer.onDoctorAccept(position, arrayList, false);
            }
        });

        viewHolder.btnAccept_Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = arrayList.get(position).getId();
                Intent i = new Intent(context, PatientFormDetail.class);
                i.putExtra("appointment_id", id);
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView TvName;
        public TextView TvAge;
        public TextView TvDate;
        public Button btnAccept, btnDecline, btnAccept_Decline;
        public TextView IvImage;
        public TextView TvBookedDate;
        public RelativeLayout rlBoth, rlClick;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            TvName = (TextView) itemLayoutView.findViewById(R.id.textView1_appointment_name);
            TvAge = (TextView) itemLayoutView.findViewById(R.id.textView1_appointment_age_value);
            TvDate = (TextView) itemLayoutView.findViewById(R.id.textView1_appointment_date);
            btnAccept = (Button) itemLayoutView.findViewById(R.id.button1_appointment_Accept);
            btnDecline = (Button) itemLayoutView.findViewById(R.id.button1_appointment__decline);
            btnAccept_Decline = (Button) itemLayoutView.findViewById(R.id.button1_appointment_accept_decline);
            IvImage = (TextView) itemLayoutView.findViewById(R.id.imageView_appointment_image);
            TvBookedDate = (TextView) itemLayoutView.findViewById(R.id.textView_appointment_title_value);
            rlBoth = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_layout_appointment_confrom_btn);
            rlClick = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_layout_appointment_conform);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}