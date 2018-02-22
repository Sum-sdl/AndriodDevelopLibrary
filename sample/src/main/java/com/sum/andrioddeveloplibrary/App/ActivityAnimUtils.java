package com.sum.andrioddeveloplibrary.App;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by sdl on 2018/1/23.
 */

public class ActivityAnimUtils {

    public static String url = "http://img31.house365.com/M02/01/72/rBEBYFTTb52AKGnpAAGRhUbP6bI584.jpg";

    public static final String TAG_image_url = "image_url";

    public static void startActivity(Activity activity, Class target, View click) {
        Intent intent = new Intent(activity, target);
        Rect rect = new Rect();
        click.getGlobalVisibleRect(rect);
        intent.setSourceBounds(rect);
        intent.putExtra(TAG_image_url, url);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

}
