package developers.weightmanagement.Water.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Date;

import developers.weightmanagement.Water.Database.DrinkDataSource;
import developers.weightmanagement.Water.MainWindow.AlarmHelper;
import developers.weightmanagement.Water.MainWindow.DateHandler;
import developers.weightmanagement.Water.Settings.PreferenceKey;
import developers.weightmanagement.Water.Settings.PrefsHelper;

public class DateLogBroadcastReceiver extends BroadcastReceiver {

    private DrinkDataSource db;
    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext =context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }



    private void insertDateLog() {

        int waterNeed= PrefsHelper.getWaterNeedPrefs(mContext);
        db= new DrinkDataSource(mContext);
        db.open();
         db.createDateLog(0,waterNeed, DateHandler.getCurrentDate());
  //     db.createMissingDateLog(0,waterNeed);

        System.out.println("db alarm fired "+ DateHandler.getCurrentDate());
        System.out.println("new record inserted: "+new Date()+" water need: "+waterNeed+" Water drank: "+0);

        Intent local = new Intent();
        local.setAction("com.update.view.action");
        mContext.sendBroadcast(local);

    }

}
