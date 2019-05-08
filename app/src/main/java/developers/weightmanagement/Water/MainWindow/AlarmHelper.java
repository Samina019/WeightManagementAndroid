package developers.weightmanagement.Water.MainWindow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import developers.weightmanagement.Water.BroadcastReceivers.DateLogBroadcastReceiver;
import static android.content.Context.ALARM_SERVICE;

public class AlarmHelper {


    public static void setDBAlarm(Context context) {
        boolean alarmDown =(PendingIntent.getBroadcast(context, 90,
                new Intent(context,DateLogBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) == null);
        if(alarmDown) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Intent mIntent = new Intent(context,DateLogBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.
                    getBroadcast(context,90,mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent);

    }}

}

