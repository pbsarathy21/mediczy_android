package app.mediczy_com.network_partners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import app.mediczy_com.bean.Home_Type_Bean;
import app.mediczy_com.delivery_form.DeliveryFormActivity;
import app.mediczy_com.health_tube.HealthTubeDetail;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
/**
 * Created by Prithivi Raj on 13-1-2018.
 */
public class Ads_MerchantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context ctx;
    private ArrayList<Home_Type_Bean> arrayList;
    private ImageLoader imageLoader;

    public Ads_MerchantsAdapter(Context context, ArrayList<Home_Type_Bean> arrayList) {
        this.ctx = context;
        this.arrayList = arrayList;
        imageLoader=new ImageLoader(ctx);
    }
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ads_list1, null);
        // create ViewHolder
        ViewHolderAds viewHolder = new ViewHolderAds(itemLayoutView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        Home_Type_Bean object = arrayList.get(listPosition);
        if (object != null) {
            setAdsData(((ViewHolderAds) holder), object);
        }
    }
    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // ads item of RecyclerView
    public static class ViewHolderAds extends RecyclerView.ViewHolder {
        public TextView Tv_Title;
        public TextView Tv_Desc;
        public TextView Tv_Date;
        public TextView Tv_CompName;
        public ImageView Iv_Image;
        public VideoView videoView;
        public WebView webView;
        public RelativeLayout Rl_Click;
        public CardView cardView;
        public CircularImageView ivCompanyImage;
        public TextView tvViewCount;
        public RelativeLayout btnEnquiry;
        public RelativeLayout btnGetSample;
        public RelativeLayout btnViewCount;
        public RelativeLayout btnBottomView;

        public ViewHolderAds(View itemLayoutView) {
            super(itemLayoutView);
            Tv_CompName = (TextView) itemLayoutView.findViewById(R.id.textview_ads_adapter_name);
            Tv_Desc = (TextView) itemLayoutView.findViewById(R.id.textView_ads_adapter_desc);
            Tv_Date = (TextView) itemLayoutView.findViewById(R.id.textview_ads_adapter_date);
            Tv_Title = (TextView) itemLayoutView.findViewById(R.id.textview_ads_adapter_tit);
            tvViewCount = (TextView) itemLayoutView.findViewById(R.id.textView_ads_adapter_view);
            btnBottomView = (RelativeLayout) itemLayoutView.findViewById(R.id.rl_ads_bottom_view);
            btnViewCount = (RelativeLayout) itemLayoutView.findViewById(R.id.rl_ads_bottom_view_view);
            btnEnquiry = (RelativeLayout) itemLayoutView.findViewById(R.id.rl_ads_bottom_view_enguiry);
            btnGetSample = (RelativeLayout) itemLayoutView.findViewById(R.id.rl_ads_bottom_view_get_sample);
            Iv_Image = (ImageView) itemLayoutView.findViewById(R.id.ads_image);
            ivCompanyImage = (CircularImageView) itemLayoutView.findViewById(R.id.iv_ads_company_logo);
            videoView = (VideoView) itemLayoutView.findViewById(R.id.videoview_ads_adapter);
            webView = (WebView) itemLayoutView.findViewById(R.id.webview_ads_adapter);
            Rl_Click = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view_ads_adapter_top);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_ads_adapter_top);
        }
    }
    private void setAdsData(final ViewHolderAds viewHolder, final Home_Type_Bean object) {
        viewHolder.Tv_Title.setText(object.getTitleAds());
        viewHolder.Tv_CompName.setText(object.getCompanyName());
        viewHolder.Tv_Desc.setText(object.getDescAds());
        viewHolder.Tv_Date.setText(object.getDateAds());
        viewHolder.tvViewCount.setText(object.getViewCount());
        final String imagePath = object.getImageAds();
        MLog.e("imagePath", "" + imagePath);
        final String imagePathCompanyLogo = object.getCompanyLogo();
        MLog.e("imagePathCompanyLogo", "" + imagePathCompanyLogo);
        imageLoader.DisplayImage(imagePathCompanyLogo, viewHolder.ivCompanyImage, 4);
        MLog.e("view_type->", "" + object.getView_type());
        if (object.getView_type()!=null) {
            if (object.getView_type().equalsIgnoreCase("offer")) {
                viewHolder.btnBottomView.setVisibility(View.VISIBLE);
            } else if (object.getView_type().equalsIgnoreCase("health_tube")) {
                viewHolder.btnBottomView.setVisibility(View.GONE);
            }
        }
        final String type = object.getTypeAds();
        if (type.equalsIgnoreCase("image")) {
            viewHolder.Iv_Image.setVisibility(View.VISIBLE);
            viewHolder.videoView.setVisibility(View.GONE);
            viewHolder.webView.setVisibility(View.GONE);
            Picasso.with(ctx).load(imagePath).into(viewHolder.Iv_Image, new Callback.EmptyCallback() {
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
        } else if(type.equalsIgnoreCase("video")) {
            viewHolder.Iv_Image.setVisibility(View.GONE);
            viewHolder.videoView.setVisibility(View.VISIBLE);
            viewHolder.webView.setVisibility(View.GONE);
            MediaController mediaController = new MediaController(ctx);
            mediaController.setAnchorView(viewHolder.videoView);
            viewHolder.videoView.setMediaController(mediaController);
            viewHolder.videoView.requestFocus();
        } else if(type.equalsIgnoreCase("Youtube")) {
            String youTubeUrl = object.getImageAds();
            MLog.e("youTubeUrl", "" + youTubeUrl);
            viewHolder.Iv_Image.setVisibility(View.GONE);
            viewHolder.videoView.setVisibility(View.GONE);
            viewHolder.webView.setVisibility(View.VISIBLE);
            viewHolder.webView.getSettings().setJavaScriptEnabled(true);
            viewHolder.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            viewHolder.webView.loadUrl(youTubeUrl + "?autoplay=1&vq=small");
            viewHolder.webView.setWebChromeClient(new WebChromeClient());
        }
        viewHolder.Rl_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.webView.stopLoading();
                viewHolder.webView.onPause();
                Intent i = new Intent(ctx, HealthTubeDetail.class);
                i.putExtra("title", object.getTitleAds());
                i.putExtra("desc", object.getDescAds());
                i.putExtra("date", object.getDateAds());
                i.putExtra("type", object.getTypeAds());
                i.putExtra("image", object.getImageAds());
                i.putExtra(Constant.isFrom, "HomeFragment");
                ctx.startActivity(i);
                ((Activity)ctx).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        viewHolder.btnEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 //               Utility.getInstance().showToast("Enquiry Clicked", ctx);
                Intent i = new Intent(ctx, DeliveryFormActivity.class);
                i.putExtra("type", "enquiry");
                i.putExtra("id", object.getIdAds());
                ctx.startActivity(i);
                ((Activity)ctx).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        viewHolder.btnGetSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utility.getInstance().showToast("Submit Form Clicked", ctx);
                Intent i = new Intent(ctx, DeliveryFormActivity.class);
                i.putExtra("type", "sample");
                i.putExtra("id", object.getIdAds());
                ctx.startActivity(i);
                ((Activity)ctx).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }
}