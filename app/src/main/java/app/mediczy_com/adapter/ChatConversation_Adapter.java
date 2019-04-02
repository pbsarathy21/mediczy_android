package app.mediczy_com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import app.mediczy_com.R;
import app.mediczy_com.chat.chat_new.ConversationDeleteObserver;
import app.mediczy_com.chat.chat_new.ConversationListActivity;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservicemodel.response.ChatDetailModel;

/**
 * Created by Prithivi Raj on 08-06-2017.
 */

public class ChatConversation_Adapter extends RecyclerView.Adapter<ChatConversation_Adapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private ArrayList<ChatDetailModel> arrayList;
    public ConversationDeleteObserver observer;

    public ChatConversation_Adapter(Context ctx, ArrayList<ChatDetailModel> itemsData) {
        this.arrayList = itemsData;
        this.context = ctx;
        imageLoader=new ImageLoader(context);
        observer = (ConversationDeleteObserver) ConversationListActivity.conversationListActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatConversation_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_conversation_list_adapter, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvMessage.setText(arrayList.get(position).getMessage());
        viewHolder.tvTime.setText(arrayList.get(position).getTime());
        viewHolder.tvName.setText(arrayList.get(position).getName());
        viewHolder.tvCategoryType.setText(arrayList.get(position).getCategory_type());

//        String image = IConstant_WebService.imageUrl_Doctors+arrayList.get(position).getImage();
//        MLog.e("image-->", "" + image);

        if (arrayList.get(position).getRead_status()!=null) {
            if (arrayList.get(position).getRead_status().equalsIgnoreCase("0")) {
                viewHolder.iVMsgStatus.setVisibility(View.VISIBLE);
            } if (arrayList.get(position).getRead_status().equalsIgnoreCase("1")) {
                viewHolder.iVMsgStatus.setVisibility(View.GONE);
            }
        }else {
            Utility.getInstance().showToast("Read Status Null", context);
        }

        if (arrayList.get(position).getUser_type().equalsIgnoreCase("patient")) {
            viewHolder.iVProfile.setVisibility(View.GONE);
            viewHolder.tvNameFirstLetter.setVisibility(View.VISIBLE);
            viewHolder.tvCategoryType.setVisibility(View.VISIBLE);
            String nameFirstLetter = arrayList.get(position).getName();
            nameFirstLetter = String.valueOf(nameFirstLetter.charAt(0));
            MLog.e("Name_FirstLetter", "" + nameFirstLetter);
            viewHolder.tvNameFirstLetter.setText(nameFirstLetter);
        } else if (arrayList.get(position).getUser_type().equalsIgnoreCase("doctor")) {
            viewHolder.tvCategoryType.setVisibility(View.GONE);
            viewHolder.iVProfile.setVisibility(View.VISIBLE);
            viewHolder.tvNameFirstLetter.setVisibility(View.GONE);
            String Profile_image = arrayList.get(position).getProfile_image();
            MLog.e("Profile_image-->", "" + Profile_image);
            imageLoader.DisplayImage(Profile_image, viewHolder.iVProfile, 4);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(position).getUser_type().equalsIgnoreCase("patient")) {
                    observer.onConversationClicked(position, null);
                }else if (arrayList.get(position).getUser_type().equalsIgnoreCase("doctor")) {
                    observer.onConversationClicked(position, viewHolder.iVProfile);
                }
            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                observer.onConversationLongClicked(position);
                return true;
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMessage;
        public TextView tvTime;
        public TextView tvName;
        public TextView tvCategoryType;
        public CircularImageView iVProfile;
        public ImageView iV;
        public ImageView iVMsgStatus;
        public RelativeLayout cardView;
        public TextView tvNameFirstLetter;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvMessage = (TextView) itemLayoutView.findViewById(R.id.tv_left_chat_con_list_msg);
            tvTime = (TextView) itemLayoutView.findViewById(R.id.tv_left_chat_con_list_time);
            tvName = (TextView) itemLayoutView.findViewById(R.id.tv_left_chat_con_list_name);
            tvCategoryType = (TextView) itemLayoutView.findViewById(R.id.tv_left_chat_con_list_cat_value);
            tvNameFirstLetter = (TextView) itemLayoutView.findViewById(R.id.tv_logo_chat_name);
            iVProfile = (CircularImageView) itemLayoutView.findViewById(R.id.iv_left_chat_con_msg);
            iV = (ImageView) itemLayoutView.findViewById(R.id.iv_left_chat_con_list_msg);
            iVMsgStatus = (ImageView) itemLayoutView.findViewById(R.id.iv_left_chat_con_list_msg_un_readed);
            cardView = (RelativeLayout) itemLayoutView.findViewById(R.id.rl_left_chat_con_list);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}