package app.mediczy_com.ChatBot;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.mediczy_com.R;
import app.mediczy_com.Session.Session;
import app.mediczy_com.utility.Bluetooth;
import app.mediczy_com.utility.Consts;
import app.mediczy_com.utility.Dbhelper;

/**
 * Created by bala on 8/29/2018.
 */


public class C_BotAdapter extends RecyclerView.Adapter<C_BotAdapter.ViewHolder> {

    Activity mActivity;
    private static final String TAG = C_BotAdapter.class.getSimpleName();

    List<Mentions> A_mention;
    ArrayList<Right_Chat> Ch_RI = new ArrayList<>();
    List<Bluetooth> chat_message = new ArrayList<>();
    ArrayList<Static_Value> qus = new ArrayList<>();

    ArrayList<Left_Robert> Rob = new ArrayList<>();
   // public final int SPLASH_DISPLAY_LENGTH = 500;
    // ArrayList<Mentions> Robert;
    Dbhelper dbhelper;
    Session session;

    public C_BotAdapter(Activity activity, ArrayList<Left_Robert> Rob) {

        this.A_mention = A_mention;
        this.mActivity = activity;
        this.Rob = Rob;
        this.qus = qus;
        this.Ch_RI = Ch_RI;
        session = new Session(mActivity);
        dbhelper = new Dbhelper(activity);

    }


    @Override
    public C_BotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bot_reply_chat_bot, parent, false);
        C_BotAdapter.ViewHolder holder = new C_BotAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String dates = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        holder.date.setText(dates);
        final String time = new SimpleDateFormat("hh:mm a").format(new Date());


        try {

            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.rob);
            Glide.with(mActivity).load(R.raw.chat_bot_rob).into(imageViewTarget);
        } catch (Exception e) {

            Log.e(TAG, "Debug from onCreate Exception" + e.toString());

        }


        if (Consts.Commom_Flag.equalsIgnoreCase("0")) {

           // holder.line_rob.setVisibility(View.GONE);
            holder.linear_all.setVisibility(View.GONE);
            holder.rob.setVisibility(View.VISIBLE);
            holder.linear_left.setVisibility(View.VISIBLE);
            holder.text_time_left.setText(time);
            holder.replyMsg_Txt_left.setText(Rob.get(position).Robert_String);


        } else if (Consts.Commom_Flag.equalsIgnoreCase("1")) {

           // holder.line_rob.setVisibility(View.GONE);
            holder.rob.setVisibility(View.VISIBLE);
            holder.linear_left.setVisibility(View.VISIBLE);
            holder.text_time_left.setText(time);
            holder.replyMsg_Txt_left.setText(Rob.get(position).Robert_String);
            if (Rob.get(position).Check.equalsIgnoreCase("")) {
                holder.linear_all.setVisibility(View.GONE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);

            } else {
                holder.linear_all.setVisibility(View.VISIBLE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);
                holder.text_time.setText(time);

            }

        } else if (Consts.Commom_Flag.equalsIgnoreCase("2")) {



          //  holder.line_rob.setVisibility(View.GONE);
            holder.rob.setVisibility(View.VISIBLE);
            holder.linear_left.setVisibility(View.VISIBLE);
            holder.text_time_left.setText(time);
            holder.replyMsg_Txt_left.setText(Rob.get(position).Robert_String);
            holder.date.setVisibility(View.GONE);
            if (Rob.get(position).Check.equalsIgnoreCase("")) {
                holder.linear_all.setVisibility(View.GONE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);


            } else {
                holder.linear_all.setVisibility(View.VISIBLE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);
                holder.text_time.setText(time);

            }
          /*  holder.line_rob.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                }


            }, SPLASH_DISPLAY_LENGTH);
*/

        } else if (Consts.Commom_Flag.equalsIgnoreCase("3")) {
           // holder.line_rob.setVisibility(View.GONE);
            holder.rob.setVisibility(View.VISIBLE);
            holder.linear_left.setVisibility(View.VISIBLE);
            holder.text_time_left.setText(time);
            holder.replyMsg_Txt_left.setText(Rob.get(position).Robert_String);
            holder.date.setVisibility(View.GONE);
            if (Rob.get(position).Check.equalsIgnoreCase("")) {
                holder.linear_all.setVisibility(View.GONE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);

            } else {
                holder.linear_all.setVisibility(View.VISIBLE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);
                holder.text_time.setText(time);
            }

          /*  holder.line_rob.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                }


            }, SPLASH_DISPLAY_LENGTH);
*/

        } else if (Consts.Commom_Flag.equalsIgnoreCase("4")) {

            holder.rob.setVisibility(View.VISIBLE);
            holder.linear_left.setVisibility(View.VISIBLE);
            holder.text_time_left.setText(time);
            holder.replyMsg_Txt_left.setText(Rob.get(position).Robert_String);
            holder.date.setVisibility(View.GONE);
            if (Rob.get(position).Check.equalsIgnoreCase("")) {
                holder.linear_all.setVisibility(View.GONE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);

            } else {
                holder.linear_all.setVisibility(View.VISIBLE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);
                holder.text_time.setText(time);
            }

          /*  holder.line_rob.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.line_rob.setVisibility(View.GONE);


                }


            }, SPLASH_DISPLAY_LENGTH);*/


        } else if (Consts.Commom_Flag.equalsIgnoreCase("5")) {


           // holder.line_rob.setVisibility(View.GONE);
            holder.rob.setVisibility(View.VISIBLE);
            holder.linear_left.setVisibility(View.VISIBLE);
            holder.text_time_left.setText(time);
            holder.replyMsg_Txt_left.setText(Rob.get(position).Robert_String);
            holder.date.setVisibility(View.GONE);
            if (Rob.get(position).Check.equalsIgnoreCase("")) {
                holder.linear_all.setVisibility(View.GONE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);

            } else {
                holder.linear_all.setVisibility(View.VISIBLE);
                holder.replyMsg_Txt.setText(Rob.get(position).Check);
                holder.text_time.setText(time);

            }

         /*   holder.line_rob.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }


            }, SPLASH_DISPLAY_LENGTH);*/


        }


    }

    @Override
    public int getItemCount() {


        if (!Rob.isEmpty()) {
            return Rob.size();
        }

        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linear_mess, linear_mess_left, linear_all, line_rob;
        RelativeLayout linear_left;
        TextView replyMsg_Txt, text_time, replyMsg_Txt_left, text_time_left, date;

        ImageView rob;
        DotProgressBar dotProgressBar;


        public ViewHolder(View itemView) {
            super(itemView);
            linear_all = (LinearLayout) itemView.findViewById(R.id.linear_all);
            linear_left = (RelativeLayout) itemView.findViewById(R.id.linear_left);
            linear_mess = (LinearLayout) itemView.findViewById(R.id.linear_mess);
            linear_mess_left = (LinearLayout) itemView.findViewById(R.id.linear_mess_left);

            line_rob = (LinearLayout) itemView.findViewById(R.id.line_rob);
            dotProgressBar = (DotProgressBar) itemView.findViewById(R.id.dot_progress_bar);

            replyMsg_Txt = (TextView) itemView.findViewById(R.id.replyMsg_Txt);
            text_time = (TextView) itemView.findViewById(R.id.text_time);

            replyMsg_Txt_left = (TextView) itemView.findViewById(R.id.replyMsg_Txt_left);
            text_time_left = (TextView) itemView.findViewById(R.id.text_time_left);
            rob = (ImageView) itemView.findViewById(R.id.rob);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }
}

