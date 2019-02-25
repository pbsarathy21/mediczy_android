package app.mediczy_com.ChatBot;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.Session.Session;
import app.mediczy_com.utility.MLog;


/**
 * Created by bala on 9/8/2018.
 */

public class List_Report_Adapter extends RecyclerView.Adapter<List_Report_Adapter.ViewHolder> {

    Activity mActivity;

    public ArrayList<User_Re_List> obj_list;
    int percentage;
    double perc = 0.0;
    public static final String TAG = List_Report_Adapter.class.getSimpleName();
    Session session;

    public List_Report_Adapter(Activity activity, ArrayList<User_Re_List> obj_list) {
        this.mActivity = activity;
        this.obj_list = obj_list;
        session = new Session(mActivity);

    }

    @Override
    public List_Report_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.chat_bot_rep_list, viewGroup, false);
        // create ViewHolder
        List_Report_Adapter.ViewHolder viewHolder = new List_Report_Adapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final List_Report_Adapter.ViewHolder viewHolder, final int position) {


        try {


            if (obj_list.size() > 0) {

                viewHolder.text_app.setText(obj_list.get(position).condition_name);
                viewHolder.text_date.setText(obj_list.get(position).created_at);
                // viewHolder.text_con.setText(obj_list.get(position).severity);


                //set progressbar

                if (!obj_list.get(position).probability.isEmpty()) {
                    if (!obj_list.get(position).severity.isEmpty()) {

                        if (obj_list.get(position).severity.equalsIgnoreCase("mild")) {


                            viewHolder.text_con.setText(obj_list.get(position).severity);
                            viewHolder.text_con.setTextColor(Color.parseColor("#008000"));
                            perc = Double.parseDouble(obj_list.get(position).probability);
                            MLog.e(TAG, "per==>" + perc);
                            double value = perc * 100;
                            MLog.e(TAG, "value==>" + value);
                            percentage = (int) Math.round(value);
                            MLog.e(TAG, "PERCENTAGE==>" + percentage);
                            Resources res = mActivity.getResources();
                            Rect bounds = viewHolder.progressbar.getProgressDrawable().getBounds();

                            viewHolder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress));

                            viewHolder.progressbar.getProgressDrawable().setBounds(bounds);
                            viewHolder.progressbar.setProgress(percentage);

                        } else if (obj_list.get(position).severity.equalsIgnoreCase("moderate")) {

                            viewHolder.text_con.setText(obj_list.get(position).severity);
                            viewHolder.text_con.setTextColor(Color.parseColor("#ffa500"));

                            perc = Double.parseDouble(obj_list.get(position).probability);
                            MLog.e(TAG, "per==>" + perc);
                            double value = perc * 100;
                            MLog.e(TAG, "value==>" + value);
                            percentage = (int) Math.round(value);
                            MLog.e(TAG, "PERCENTAGE==>" + percentage);
                            Resources res = mActivity.getResources();
                            Rect bounds = viewHolder.progressbar.getProgressDrawable().getBounds();

                            viewHolder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_yellow));

                            viewHolder.progressbar.getProgressDrawable().setBounds(bounds);
                            viewHolder.progressbar.setProgress(percentage);

                        } else if (obj_list.get(position).severity.equalsIgnoreCase("severe")) {


                            viewHolder.text_con.setText(obj_list.get(position).severity);
                            viewHolder.text_con.setTextColor(Color.parseColor("#00CA93"));


                            perc = Double.parseDouble(obj_list.get(position).probability);
                            MLog.e(TAG, "per==>" + perc);
                            double value = perc * 100;
                            MLog.e(TAG, "value==>" + value);
                            percentage = (int) Math.round(value);
                            MLog.e(TAG, "PERCENTAGE==>" + percentage);
                            Resources res = mActivity.getResources();
                            Rect bounds = viewHolder.progressbar.getProgressDrawable().getBounds();

                            viewHolder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_severe));

                            viewHolder.progressbar.getProgressDrawable().setBounds(bounds);
                            viewHolder.progressbar.setProgress(percentage);

                        }

                    }

                }
            }


        } catch (
                IndexOutOfBoundsException e)

        {
            e.printStackTrace();
        }


        viewHolder.lin_ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    session.setView_id(obj_list.get(position).user_bot_report_id);
                    Intent intent = new Intent(mActivity, Report_DetailActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }



            }
        });

    }

    @Override
    public int getItemCount() {


        if (!obj_list.isEmpty()) {

            return obj_list.size();
        }


        return 1;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_app, text_date, text_con;
        ProgressBar progressbar;
        LinearLayout lin_ba;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            progressbar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            text_app = (TextView) itemView.findViewById(R.id.text_app);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            text_con = (TextView) itemView.findViewById(R.id.text_con);
            lin_ba = (LinearLayout) itemView.findViewById(R.id.lin_ba);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

    }

}





