
package net.impjq.ftclient;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class Utils {
    public static final boolean DEBUG_ENABLE = true;

    public static void log(String TAG, String msg) {
        if (DEBUG_ENABLE && null != msg) {
            Log.i(TAG, msg);
        }
    }

    public static String getTime() {
        Date date = new Date();
        String time = date.toLocaleString();
        return time;
    }

    /**
     * Check the string is empty
     * 
     * @param string
     * @return true if the string is null or "".
     */
    public static boolean isEmpty(String string) {
        boolean empty = false;

        if (null == string || (null != string && string.equals(""))) {
            empty = true;
        }

        return empty;
    }

    public static String createPhotoName() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float temp = ((float) height) / ((float) width);
        int newHeight = (int) ((newWidth) * temp);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix(); // resize the bit map

        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

}
