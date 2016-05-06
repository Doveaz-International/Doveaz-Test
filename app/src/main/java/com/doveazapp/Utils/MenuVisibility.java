package com.doveazapp.Utils;

import android.app.Activity;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by Karthik on 11/28/2015.
 */
public class MenuVisibility {

    public static void menuVisible(final Activity activity) {
        try {
            ViewConfiguration config = ViewConfiguration.get(activity);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
}