package Utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Administrator on 2016/12/16.
 */

public class Utils {

    private static long fastClickTime = 0;

    public static boolean hasInternet(Context context) {
        boolean flag;
        if (((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }
    /**
     * 是否是双击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        long deltaTime = currentTimeMillis - fastClickTime;
        if (deltaTime > 0 && deltaTime < 1000) {
            return true;
        }
        fastClickTime = currentTimeMillis;
        return false;
    }
}
