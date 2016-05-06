package com.doveazapp.Activities;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Karthik on 2015/12/23.
 */
public class VolleyProvider {
    private static RequestQueue queue = null;

    private VolleyProvider() {
    }

    public static synchronized RequestQueue getQueue(Context ctx) {
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return queue;
    }
}
