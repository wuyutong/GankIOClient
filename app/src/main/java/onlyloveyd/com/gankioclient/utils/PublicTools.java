package onlyloveyd.com.gankioclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telecom.ConnectionService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lisa on 2016/12/22.
 * Email: 457420045@qq.com
 */

public class PublicTools {



    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;


    public static final String KEY_BUNDLE_CATEGORY="CATEGORY";
    public static final String KEY_BUNDLE_URL="URL";
    public static final String BONUS = "福利";
    public static final String ABOUT = "关于";
    public static final String SDK_APP_KEY = "5592uD0UYy7V7kEWFbxElOEzFpnkvhZD";

    /**
     * whether network is available
     * @param context
     * @return true network is available
     *         false network is not available
     */
    public static boolean isNetWorkAvailable(Context context) {
        if(context!= null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date date
     * @return String
     */
    public static String getTimestampString(Date date) {
        Date curDate = new Date();
        long splitTime = curDate.getTime() - date.getTime();
        if (splitTime < (30 * ONE_DAY)) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚";
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE);
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR);
            }

            return String.format("%d天前", splitTime / ONE_DAY);
        }
        String result;
        result = "M月d日 HH:mm";
        return (new SimpleDateFormat(result, Locale.CHINA)).format(date);
    }


}
