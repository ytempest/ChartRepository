package com.ytempest.chart.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author ytempest
 * @date 2019/7/3
 */
public class ViewUtils {
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
