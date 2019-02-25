package app.mediczy_com.ChatBot;


import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;

public class Repot_Adapter extends RecyclerView.Adapter<Repot_Adapter.ViewHolder> {


    ArrayList<Present_User_Bot> obj_All = new ArrayList<>();
    ArrayList<Absent_User_Bot> obj_absent = new ArrayList<>();
    ArrayList<Unknow_User_Bot> obj_unknow = new ArrayList<>();
    ArrayList<Integer> count = new ArrayList<>();


    Activity mActivity;

    public Repot_Adapter(Activity activity, ArrayList<Present_User_Bot> obj_All, ArrayList<Integer> count) {

        this.mActivity = activity;
        this.obj_All = obj_All;
        this.count = count;

    }


    @Override
    public Repot_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.chat_bot_report_adapter, viewGroup, false);
        // create ViewHolder
        Repot_Adapter.ViewHolder viewHolder = new Repot_Adapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {


        try {


            viewHolder.present_linear.setVisibility(View.VISIBLE);
            viewHolder.abscent_linear.setVisibility(View.GONE);
            viewHolder.unknown_linear.setVisibility(View.GONE);
            viewHolder.text_present.setVisibility(View.GONE);

            if (obj_All.get(position).choice.equalsIgnoreCase("present")) {

                int pos = count.get(0);

                if (pos == position) {

                    viewHolder.text_present.setVisibility(View.VISIBLE);
                    viewHolder.text_present.setText("Present");

                } else {
                    viewHolder.text_present.setVisibility(View.GONE);

                }
                viewHolder.text_present_value.setText(obj_All.get(position).name);
                viewHolder.img_pre.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.op));


            } else if (obj_All.get(position).choice.equalsIgnoreCase("absent")) {

                int pos = count.get(1);

                if (pos == position) {

                    viewHolder.text_present.setVisibility(View.VISIBLE);
                    viewHolder.text_present.setText("Absent");


                } else {
                    viewHolder.text_present.setVisibility(View.GONE);

                }

                viewHolder.text_present_value.setText(obj_All.get(position).name);
                viewHolder.img_pre.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.yt));


            } else if (obj_All.get(position).choice.equalsIgnoreCase("unknown")) {

                int pos = count.get(2);

                if (pos == position) {

                    viewHolder.text_present.setVisibility(View.VISIBLE);
                    viewHolder.text_present.setText("Unknown");


                } else {
                    viewHolder.text_present.setVisibility(View.GONE);

                }

                viewHolder.text_present_value.setText(obj_All.get(position).name);
                viewHolder.img_pre.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.yt));


            }


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        if (!obj_All.isEmpty()) {
            return obj_All.size();
        }
        if (count.isEmpty()) {
            return count.size();
        }

        return 2;

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout unknown_linear, abscent_linear, present_linear;

        TextView text_present, text_present_value, text_absent, text_absent_value, text_unknow, text_unknow_value;
        ImageView img_pre, img_abs, img_unknow;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            text_present = (TextView) itemView.findViewById(R.id.text_present);
            text_present_value = (TextView) itemView.findViewById(R.id.text_present_value);
            text_absent = (TextView) itemView.findViewById(R.id.text_absent);
            text_absent_value = (TextView) itemView.findViewById(R.id.text_absent_value);
            text_unknow = (TextView) itemView.findViewById(R.id.text_unknow);
            text_unknow_value = (TextView) itemView.findViewById(R.id.text_unknow_value);

            present_linear = (LinearLayout) itemView.findViewById(R.id.present_linear);
            abscent_linear = (LinearLayout) itemView.findViewById(R.id.abscent_linear);
            unknown_linear = (LinearLayout) itemView.findViewById(R.id.unknown_linear);


            img_pre = (ImageView) itemView.findViewById(R.id.img_pre);
            img_abs = (ImageView) itemView.findViewById(R.id.img_abs);
            img_unknow = (ImageView) itemView.findViewById(R.id.img_unknow);


            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

    }

}

