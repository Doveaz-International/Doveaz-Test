package com.doveazapp.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Karthik on 7/12/2016.
 */
public class CustomDoveazFont extends TextView {
    public CustomDoveazFont(Context context) {
        super(context);
        setFont();
    }

    public CustomDoveazFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomDoveazFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/viga.otf");
        setTypeface(font, Typeface.NORMAL);
    }
}

