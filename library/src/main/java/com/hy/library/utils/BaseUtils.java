package com.hy.library.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

/**
 * Created time : 2019-10-19 10:10.
 *
 * @author HY
 */
public class BaseUtils {
    /**
     * 判断颜色是不是亮色
     *
     * @param color 颜色
     * @return https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    public static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    /**
     * get attrs color
     */
    public static int getTypeValueColor(Context mContext, int attr) {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{attr};
        TypedArray array = mContext.obtainStyledAttributes(typedValue.resourceId, attribute);
        int color = array.getColor(0, -1);
        array.recycle();
        return color;
    }

    public static boolean isMarshmallow(){
        return Build.VERSION.SDK_INT<=Build.VERSION_CODES.M;
    }

}
