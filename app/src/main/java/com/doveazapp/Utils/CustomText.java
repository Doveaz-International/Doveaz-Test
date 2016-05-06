package com.doveazapp.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Karthik on 2016/04/11.
 */
public class CustomText extends TextView {
    public CustomText(Context context) {
        super(context);
        setFont();
    }
    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/monotype_corsiva.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
