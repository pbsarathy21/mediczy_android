package app.mediczy_com.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Prithivi on 26-11-2016.
 */

public class TextViewFont extends TextView {

    static Typeface typeface;

    public TextViewFont(Context context) {
        super(context);
        if(typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/MaterialIcons-Regular.ttf");
        this.setTypeface(typeface);
    }

    public TextViewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/MaterialIcons-Regular.ttf");
        this.setTypeface(typeface);
    }

    public TextViewFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/MaterialIcons-Regular.ttf");
        this.setTypeface(typeface);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/MaterialIcons-Regular.ttf");
        this.setTypeface(typeface);
    }
}