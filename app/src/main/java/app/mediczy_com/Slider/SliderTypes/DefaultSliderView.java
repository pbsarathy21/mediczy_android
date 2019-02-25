package app.mediczy_com.Slider.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import app.mediczy_com.R;


public class DefaultSliderView extends BaseSliderView
{

    public DefaultSliderView(Context context)
    {
        super(context);
    }

    @Override
    public View getView()
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.chat_bot_render_type_default,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }
}
