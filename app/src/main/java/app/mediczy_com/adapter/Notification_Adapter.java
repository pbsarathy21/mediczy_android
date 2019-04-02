package app.mediczy_com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.Bean_Doctor_Prescribed;
import app.mediczy_com.health_tube.HealthTubeDetail;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.ViewHolder>
{
    Context context;
    private ImageLoader imageLoader;
    private ArrayList<Bean_Doctor_Prescribed> arrayList;

    public Notification_Adapter(Context doctorsList, ArrayList<Bean_Doctor_Prescribed> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        imageLoader=new ImageLoader(doctorsList);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Notification_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.Tv_Title.setText(arrayList.get(position).getTitle());
        viewHolder.Tv_Desc.setText(arrayList.get(position).getDesc());
        viewHolder.Tv_Date.setText(arrayList.get(position).getDate());
        final String imagePath = arrayList.get(position).getImage();
        MLog.e("imagePath", "" + imagePath);
        final String type = arrayList.get(position).getType();
        if (type.equalsIgnoreCase("image")) {
            viewHolder.Iv_Image.setVisibility(View.VISIBLE);
            viewHolder.videoView.setVisibility(View.GONE);
            viewHolder.webView.setVisibility(View.GONE);

            Picasso.with(context).load(imagePath).
                    into(viewHolder.Iv_Image, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            // TODO Auto-generated method stub
                            super.onError();
//              Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                            viewHolder.Iv_Image.setImageResource(R.drawable.no_image_plain);
                            MLog.e("ImageError", "" + "ImageError");
                        }
                    });
        }else if(type.equalsIgnoreCase("video")) {
            viewHolder.Iv_Image.setVisibility(View.GONE);
            viewHolder.videoView.setVisibility(View.VISIBLE);
            viewHolder.webView.setVisibility(View.GONE);

            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(viewHolder.videoView);
            viewHolder.videoView.setMediaController(mediaController);
            viewHolder.videoView.requestFocus();

        }else if(type.equalsIgnoreCase("Youtube")) {
            String youTubeUrl = arrayList.get(position).getImage();
            MLog.e("youTubeUrl", "" + youTubeUrl);

            viewHolder.Iv_Image.setVisibility(View.GONE);
            viewHolder.videoView.setVisibility(View.GONE);
            viewHolder.webView.setVisibility(View.VISIBLE);

            viewHolder.webView.getSettings().setJavaScriptEnabled(true);
            viewHolder.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            viewHolder.webView.loadUrl(youTubeUrl + "?autoplay=1&vq=small");
            viewHolder.webView.setWebChromeClient(new WebChromeClient());
        }

/*        viewHolder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.videoView.start();
            }
        });

        viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // This is just to show image when loaded
                mp.start();
                mp.pause();
            }
        });

        viewHolder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // setLooping(true) didn't work, thats why this workaround
                String imagePath = null;
                if(type.equalsIgnoreCase("video")) {
                    imagePath = IConstant_WebService.imageUrl_Notification+arrayList.get(position).getImage();
                }else if(type.equalsIgnoreCase("Youtube")) {
                    imagePath = arrayList.get(position).getImage();
                }
                viewHolder.videoView.setVideoURI(Uri.parse(imagePath));
                viewHolder.videoView.setMediaController(new MediaController(context));
                viewHolder.videoView.requestFocus();
                viewHolder.videoView.start();
            }
        });*/

/*        viewHolder.Iv_ImagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imagePath = IConstant_WebService.imageUrl_Notification+arrayList.get(position).getImage();
                Intent i = new Intent(context, HealthTube_VideoView.class);
                i.putExtra("image", imagePath);
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });*/

        viewHolder.Rl_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.webView.stopLoading();
                viewHolder.webView.onPause();
                String title = arrayList.get(position).getTitle();
                String desc = arrayList.get(position).getDesc();
                String date = arrayList.get(position).getDate();
                String type = arrayList.get(position).getType();
                String imagePath = arrayList.get(position).getImage();
                MLog.e("imagePath", "" + imagePath);

                Intent i = new Intent(context, HealthTubeDetail.class);
                i.putExtra("title", title);
                i.putExtra("desc", desc);
                i.putExtra("date", date);
                i.putExtra("type", type);
                i.putExtra("image", imagePath);
                i.putExtra(Constant.isFrom, "HealthTubeFragment");
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Title;
        public TextView Tv_Desc;
        public TextView Tv_Date;
        public ImageView Iv_Image;
        public ImageView Iv_ImagePlay;
        public VideoView videoView;
        public WebView webView;
        public RelativeLayout Rl_Click;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_Title = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_prescribed_name_noti);
            Tv_Desc = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_prescribed_desc_noti);
            Tv_Date = (TextView) itemLayoutView.findViewById(R.id.textview_adapter_doctor_prescribed_date_noti);
            Iv_Image = (ImageView) itemLayoutView.findViewById(R.id.noti_image);
            Iv_ImagePlay = (ImageView) itemLayoutView.findViewById(R.id.noti_image_play);
            videoView = (VideoView) itemLayoutView.findViewById(R.id.notification_video_viewdapter_doctor);
            webView = (WebView) itemLayoutView.findViewById(R.id.notification_webView_doctor);
            Rl_Click = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view_doctor_prescribed_adapter_noti);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}