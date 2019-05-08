package developers.weightmanagement.Sleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    private static final String TAG = ScreenReceiver.class.getSimpleName();

    // For Receiver implemented in Activity
    public static boolean wasScreenOn = true;
    public static boolean isScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(isScreenOn==true) {
            wasScreenOn = true;
        }
        else {
            wasScreenOn = false;
        }
        //Log.d(TAG,"Pre-receive: isScreenOn="+isScreenOn+" wasScreenOn="+wasScreenOn);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // ** Do nothing **
            isScreenOn = false;
            Log.e(TAG,"Receiver - Screen is: OFF. isScreenOn="+isScreenOn+" wasScreenOn="+wasScreenOn);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // ** Do nothing **

            isScreenOn = true;
            Log.e(TAG,"Receiver - Screen is: ON. isScreenOn="+isScreenOn+" wasScreenOn="+wasScreenOn);
        }
    }

}
