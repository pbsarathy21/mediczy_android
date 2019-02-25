package app.mediczy_com.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import app.mediczy_com.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservice.request.CustomVolleyRequestQueue;
import app.mediczy_com.webservicemodel.response.Merchant_CategoriesListResponse;

/**
 * Created by Prithivi Raj on 26-12-2017.
 */
public class Network_Partners_Category_Adapter extends RecyclerView.Adapter<Network_Partners_Category_Adapter.ViewHolder> {

    public SelectedNetworkPartnerObserver observer;
    Context context;
    private app.mediczy_com.imageloader.ImageLoader imageLoader;
    private ArrayList<Merchant_CategoriesListResponse> arrayList;
    private com.android.volley.toolbox.ImageLoader mImageLoader;

    public Network_Partners_Category_Adapter(Context ctx, ArrayList<Merchant_CategoriesListResponse> itemsData) {
        this.arrayList = itemsData;
        this.context = ctx;
     //   imageLoader=new ImageLoader(context);
        mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
        observer = (SelectedNetworkPartnerObserver) HomeFragment.homeFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Network_Partners_Category_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_np_category_list, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        final Merchant_CategoriesListResponse object = arrayList.get(position);
        viewHolder.tv_title.setText(object.name);
        String imagePath = arrayList.get(position).icon;
        MLog.e("imagePath->", "" + imagePath);
//        imageLoader.DisplayImage(imagePath, viewHolder.iv, 4);

/*        Picasso.with(context).load(imagePath).
                into(viewHolder.iv, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
                        viewHolder.iv.setImageResource(R.drawable.no_image_plain);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });*/

//        com.android.volley.toolbox.NetworkImageView
        //Image URL - This can point to any image file supported by Android
//        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView, R.mipmap.truiton_short, android.R.drawable.ic_dialog_alert));
        viewHolder.mNetworkImageView.setImageUrl(imagePath, mImageLoader);

        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observer.onSelectedTopCategory(object);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title;
        public ImageView iv;
        public RelativeLayout rl;
        public NetworkImageView mNetworkImageView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tv_title = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_np_title);
 //           iv = (ImageView) itemLayoutView.findViewById(R.id.imageView_adapter_cat_image);
            rl = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view_cat_adapter);
            mNetworkImageView = (NetworkImageView) itemLayoutView.findViewById(R.id.imageView_adapter_cat_image);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}